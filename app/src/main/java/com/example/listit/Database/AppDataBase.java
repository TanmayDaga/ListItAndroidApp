package com.example.listit.Database;


import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;




@Database(entities = {ListItEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDataBase extends RoomDatabase {

    private static final String LOG_TAG = AppDataBase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "listIt";
    private static AppDataBase sInstance;

    public static AppDataBase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {

                Log.d(LOG_TAG, "Creating room database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class,
                        AppDataBase.DATABASE_NAME).build();

            }
        }

        Log.d(LOG_TAG, "Getting database instance");
        return sInstance;
    }


    public static AppDataBase getInstance(){
        return sInstance;
    }
    public abstract ListItDao listItDao();


}