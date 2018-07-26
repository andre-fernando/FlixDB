package comandre_fernando.httpsgithub.flixdb.roomdb;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.List;

/**
 * NOTE: I have left this just for feedback as it is not allowed for this project.
 *
 * Data Access Object for the FavMovies Table.
 * Contains the method to edit the db.
 * Reference Google Codelabs and Samples
 */
@Dao
public interface FavMoviesDao {

    @Query("select * from favourite_movies")
    List<FavMovies> getFavMovies();

    @Query("select * from favourite_movies")
    LiveData<List<FavMovies>> getFavouritesLiveData();

    @Query("select * from favourite_movies where ID = :id")
    FavMovies getFavMovieWithId(String id);

    @Delete
    void deleteMovie(FavMovies movie);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(FavMovies movie);

    @Query("delete from favourite_movies")
    void deleteAll();

    // Methods starting with cp are used in the content provider

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    Long cp_insert(FavMovies favMovies);

    @Delete
    int cp_delete(FavMovies movie);

    @Query("delete from favourite_movies")
    int cp_deleteAll();

    @Query("select * from favourite_movies")
    Cursor cp_get_favourites();

    @Query("select title from favourite_movies")
    Cursor cp_get_titles();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    Long[] cp_insert_all(FavMovies[] movies);

}
