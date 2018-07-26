package comandre_fernando.httpsgithub.flixdb.components.asyncTasks;

import android.os.AsyncTask;

import comandre_fernando.httpsgithub.flixdb.data_objects.MoviesObject;
import comandre_fernando.httpsgithub.flixdb.roomdb.FavDB;
import comandre_fernando.httpsgithub.flixdb.roomdb.FavMovies;

public class AddMovieToFavouritesAsync extends AsyncTask<Void,Void,Void> {
    private final FavDB db;
    private final MoviesObject movie;

    public AddMovieToFavouritesAsync(FavDB instance, MoviesObject movie){
        this.db = instance;
        this.movie = movie;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        FavMovies movie = new FavMovies();
        movie.ID = this.movie.getID();
        movie.Title = this.movie.getTitle();
        movie.ReleaseDate = this.movie.getReleaseDate();
        movie.AvgRating = this.movie.getAvgRating();
        movie.Overview = this.movie.getOverview();
        movie.PosterImageUri = this.movie.getPosterImageUri().toString();
        db.favMoviesDao().insertMovie(movie);
        return null;
    }
}
