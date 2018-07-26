package comandre_fernando.httpsgithub.flixdb.components.asyncTasks;

import android.os.AsyncTask;

import java.util.List;

import comandre_fernando.httpsgithub.flixdb.roomdb.FavDB;
import comandre_fernando.httpsgithub.flixdb.roomdb.FavMovies;

public class FetchFavouriteMoviesListAsync extends
        AsyncTask<Void, Void, List<FavMovies>> {
    private final FavDB db;

    public FetchFavouriteMoviesListAsync(FavDB instance){
        this.db = instance;
    }

    @Override
    protected List<FavMovies> doInBackground(Void... voids) {
        return db.favMoviesDao().getFavMovies();
    }
}