package edu.udacity.kondeg.movies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import edu.udacity.kondeg.movies.MovieTitle;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM MovieTitle")
    LiveData<List<MovieTitle>> getAll();

    @Query("SELECT * FROM MovieTitle WHERE id = :id")
    MovieTitle getMovieById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MovieTitle movie);

    @Delete
    void delete(MovieTitle movie);
}
