package comandre_fernando.httpsgithub.flixdb.roomdb;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import comandre_fernando.httpsgithub.flixdb.data_objects.MoviesObject;
import comandre_fernando.httpsgithub.flixdb.components.asyncTasks.AddMovieToFavouritesAsync;
import comandre_fernando.httpsgithub.flixdb.components.asyncTasks.ClearFavouritesAsync;
import comandre_fernando.httpsgithub.flixdb.components.asyncTasks.RemoveFavouriteMovieAsync;

/**
 * NOTE: I have left this just for feedback as it is not allowed for this project.
 *
 * Main database file that creates, migrates(Not implemented) or recovers the db
 * References Google codelabs and sample projects
 */
@Database(entities = {FavMovies.class} , version = 1, exportSchema = false)
public abstract class FavDB extends RoomDatabase {
    private static final String DB_NAME = "Favourites.db";

    private static FavDB INSTANCE;

    public abstract FavMoviesDao favMoviesDao();

    /**
     * Creates the db or recovers an instance of it.
     * @param context Context
     * @return An instance of the db.
     */
    public static FavDB getInstance(Context context){
        if (INSTANCE==null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), FavDB.class , DB_NAME).build();
        }
        return INSTANCE;
    }


    @Ignore
    public void removeMovieAtId(String id){
        new RemoveFavouriteMovieAsync(INSTANCE,id).execute();
    }

    @Ignore
    public void clearFavourites(){
        new ClearFavouritesAsync(INSTANCE).execute();
    }

    @Ignore
    public void addToFavourites(MoviesObject moviesParcel){
        new AddMovieToFavouritesAsync(INSTANCE, moviesParcel).execute();
    }


}
