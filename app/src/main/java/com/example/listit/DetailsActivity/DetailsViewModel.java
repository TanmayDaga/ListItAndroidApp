package com.example.listit.DetailsActivity;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.listit.AppExecutors;
import com.example.listit.Database.AppDataBase;
import com.example.listit.Database.CategoryEntry;
import com.example.listit.Database.ListItEntry;

import java.util.Date;
import java.util.List;

public class DetailsViewModel extends AndroidViewModel {

    AppDataBase mDb;
    LiveData<List<String>> categoriesLiveData;

    public DetailsViewModel(@NonNull Application application) {
        super(application);

        mDb = AppDataBase.getInstance(application);
        categoriesLiveData = mDb.categoryDao().loadCategoriesName();

    }

    public LiveData<List<String>> getCategoriesLiveData() {

        return categoriesLiveData;
    }

    public void insertListIt(String title, String description, Date dueDate, String category, int priority){

        AppExecutors.getsInstance().diskIo().execute(new Runnable() {
            @Override
            public void run() {
                mDb.listItDao().insertListIt(new ListItEntry(
                        title,description,dueDate,category,priority
                ));
            }
        });



    }
}
