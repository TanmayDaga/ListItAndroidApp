package com.example.listit;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class AppExecutors {


    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;
    private final Executor diskIo;
    private final Executor mainThread;
    private final Executor networkIo;

    private AppExecutors(Executor diskIo, Executor networkIo, Executor mainThread) {

        this.diskIo = diskIo;
        this.networkIo = networkIo;
        this.mainThread = mainThread;

    }

    public static AppExecutors getsInstance() {
        if (sInstance == null) {
            synchronized ((LOCK)) {

                sInstance = new AppExecutors(Executors.newSingleThreadExecutor(),//Ek thread ek baar me run karega//disk io me ek hi thread
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());

            }
        }
        return sInstance;
    }

    public Executor diskIo() {
        return diskIo;
    }

    public Executor mainThread() {
        return mainThread;
    }

    public Executor networkIO() {
        return networkIo;
    }

    private static class MainThreadExecutor implements Executor {

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            mainThreadHandler.post(runnable);
        }

    }
}

