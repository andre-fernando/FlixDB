package comandre_fernando.httpsgithub.flixdb.components.viewmodels;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import comandre_fernando.httpsgithub.flixdb.data_objects.MoviesObject;
import comandre_fernando.httpsgithub.flixdb.components.asyncTasks.FetchFavouritesLiveDataAsync;
import comandre_fernando.httpsgithub.flixdb.roomdb.FavDB;
import comandre_fernando.httpsgithub.flixdb.roomdb.FavMovies;

/**
 * This is the viewmodel class for the
 * Favourites Activity. It also holds the LiveData
 * of the Favourites chosen by the user
 *
 * Reference Google code labs
 */
public class FavouritesViewModel extends AndroidViewModel {
    private LiveData<List<FavMovies>> observableList;

    public FavouritesViewModel(@NonNull Application application) {
        super(application);
        try {
            FavDB db = FavDB.getInstance(getApplication().getApplicationContext());
            observableList = new FetchFavouritesLiveDataAsync(db).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            observableList = new MutableLiveData<>();
        }
    }

    public ArrayList<MoviesObject> getFavouriteMovies(){
        List<FavMovies> tempList = observableList.getValue();
        if (tempList != null) {
            ArrayList<MoviesObject> toReturn = new ArrayList<>();
            for (FavMovies fm: tempList){
                toReturn.add(MoviesObject.parseMovie(fm));
            }
            return toReturn;
        }else {
            return new ArrayList<>();
        }
    }

    public LiveData<List<FavMovies>> getFavouriteMoviesObservable(){
        return observableList;
    }

}
