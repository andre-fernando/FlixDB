package comandre_fernando.httpsgithub.flixdb.components.asyncTasks;

import android.os.AsyncTask;

import comandre_fernando.httpsgithub.flixdb.roomdb.FavDB;
import comandre_fernando.httpsgithub.flixdb.roomdb.FavMovies;

public class RemoveFavouriteMovieAsync extends AsyncTask<Void,Void,Void> {
    private final FavDB db;
    private final String id;

    public RemoveFavouriteMovieAsync(FavDB instance, String id){
        this.db = instance;
        this.id = id;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        FavMovies favMovie = db.favMoviesDao().getFavMovieWithId(id);
        db.favMoviesDao().deleteMovie(favMovie);
        return null;
    }
}
