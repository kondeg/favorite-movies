package edu.udacity.kondeg.movies.database;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

public class MovieExecutor implements Executor {

    @Override
    public void execute(@NonNull Runnable runnable) {
        new Thread(runnable).start();
    }

}
