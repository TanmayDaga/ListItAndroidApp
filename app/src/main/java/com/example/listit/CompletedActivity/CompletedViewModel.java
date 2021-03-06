package com.example.listit.CompletedActivity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.listit.AppExecutors;
import com.example.listit.Database.AppDataBase;
import com.example.listit.Database.ListItEntry;

import java.util.List;

public class CompletedViewModel extends AndroidViewModel {

    AppDataBase mDb;
    LiveData<List<ListItEntry>> entries;

    public CompletedViewModel(@NonNull Application application) {
        super(application);
        mDb = AppDataBase.getInstance(application);
        entries = mDb.listItDao().loadAllCompletedLists();
    }

    public LiveData<List<ListItEntry>> getCompletedListIts() {
        return entries;
    }

    public void deleteListIts(ListItEntry listItEntry) {
        AppExecutors.getsInstance().diskIo().execute(new Runnable() {
            @Override
            public void run() {
                mDb.listItDao().deleteListIt(listItEntry);
            }
        });

    }

    public void setUnCompleted(int id) {
        AppExecutors.getsInstance().diskIo().execute(new Runnable() {
            @Override
            public void run() {
                mDb.listItDao().setCompletedById(false, id);
            }
        });

    }
}
