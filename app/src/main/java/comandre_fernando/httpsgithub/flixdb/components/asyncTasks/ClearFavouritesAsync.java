package comandre_fernando.httpsgithub.flixdb.components.asyncTasks;

import android.os.AsyncTask;

import comandre_fernando.httpsgithub.flixdb.roomdb.FavDB;

public class ClearFavouritesAsync extends AsyncTask<Void,Void,Void> {
    private final FavDB db;

    public ClearFavouritesAsync(FavDB instance){
        this.db=instance;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        db.favMoviesDao().deleteAll();
        return null;
    }
}
