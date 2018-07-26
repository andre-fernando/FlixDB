package comandre_fernando.httpsgithub.flixdb.components.asyncTasks;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import comandre_fernando.httpsgithub.flixdb.roomdb.FavDB;
import comandre_fernando.httpsgithub.flixdb.roomdb.FavMovies;

public class FetchFavouritesLiveDataAsync extends
        AsyncTask<Void, Void, LiveData<List<FavMovies>>> {
    private final FavDB db;

    public FetchFavouritesLiveDataAsync(FavDB instance){
        this.db = instance;
    }

    @Override
    protected LiveData<List<FavMovies>> doInBackground(Void... voids) {
        return db.favMoviesDao().getFavouritesLiveData();
    }
}
