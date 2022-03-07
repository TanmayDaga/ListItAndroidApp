package com.example.listit.MainActivity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.listit.AppExecutors;
import com.example.listit.Database.AppDataBase;
import com.example.listit.Database.CategoryEntry;
import com.example.listit.Database.ListItDao;
import com.example.listit.Database.ListItEntry;
import com.example.listit.R;

import java.util.HashMap;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private final AppDataBase mDb;
    private final LiveData<List<String>> categories;
    private HashMap<String, LiveData<List<ListItEntry>>> mEntriesOfFragments;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        mDb = AppDataBase.getInstance(application);
        categories = mDb.categoryDao().loadCategoriesName();
        setmEntriesOfFragments();
    }

    public void AddMyTasks() {
        AppExecutors.getsInstance().diskIo().execute(new Runnable() {
            @Override
            public void run() {
                mDb.categoryDao().insertCategory(new CategoryEntry(getApplication().
                        getString(R.string.category_my_tasks), 1));
            }
        });

    }

    public LiveData<List<String>> getCategories() {
        return categories;
    }

    public LiveData<List<ListItEntry>> getFragmentData(String category) {
        if(mEntriesOfFragments == null){
            return null;// Null is being handled in fragement;
        }
        return mEntriesOfFragments.get(category);
    }

    private void setmEntriesOfFragments() {

//        Only ran in constructor so no executor

        categories.observe(, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> stringList) {
                ListItDao dao = mDb.listItDao();
                for (String categoryName : stringList) {
                    mEntriesOfFragments.put(categoryName,
                            dao.loadListItsOfCategoryUnCompleted(categoryName));

                }
            }

        });
    }

    public void deleteAllByCategory(String category){
        AppExecutors.getsInstance().diskIo().execute(new Runnable() {
            @Override
            public void run() {
                mDb.listItDao().deleteAllByCategory(category);
            }
        });
    }
    public void deleteListIts(ListItEntry listItEntry) {
        AppExecutors.getsInstance().diskIo().execute(new Runnable() {
            @Override
            public void run() {
                mDb.listItDao().deleteListIt(listItEntry);
            }
        });

    }

    public void setCompleted(int id) {
        AppExecutors.getsInstance().diskIo().execute(new Runnable() {
            @Override
            public void run() {
                mDb.listItDao().setCompletedById(true, id);
            }
        });

    }
}
