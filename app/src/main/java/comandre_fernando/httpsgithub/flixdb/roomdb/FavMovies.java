package comandre_fernando.httpsgithub.flixdb.roomdb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import comandre_fernando.httpsgithub.flixdb.data_objects.MoviesObject;

/**
 * NOTE: I have left this just for feedback as it is not allowed for this project.
 *
 * Table for favourite movies
 */
@Entity (tableName = FavMovies.TABLE_NAME)
public class FavMovies {
    @SuppressWarnings("NullableProblems")
    @PrimaryKey
    @NonNull
    @ColumnInfo (name = COLUMN_ID)
    public String ID;
    @ColumnInfo (name = COLUMN_TITLE)
    public String Title;
    @ColumnInfo (name = COLUMN_RELEASE_DATE)
    public String ReleaseDate;
    @ColumnInfo (name = COLUMN_AVG_RATING)
    public String AvgRating;
    @ColumnInfo (name = COLUMN_OVERVIEW)
    public String Overview;
    @ColumnInfo (name = COLUMN_POSTER_IMAGE_URI)
    public String PosterImageUri;

    //Constants for the table and column names

    @Ignore
    public static final String TABLE_NAME ="favourite_movies";
    @Ignore
    public static final String COLUMN_ID = "id";
    @Ignore
    public static final String COLUMN_TITLE = "title";
    @Ignore
    public static final String COLUMN_RELEASE_DATE = "release_date";
    @Ignore
    public static final String COLUMN_AVG_RATING = "avg_rating";
    @Ignore
    public static final String COLUMN_OVERVIEW = "overview";
    @Ignore
    public static final String COLUMN_POSTER_IMAGE_URI = "poster_image_uri";

    /**
     * Converts ContentValues to FavMovies object
     * @param cv ContentValues recieved from content resolver
     * @return A FavMovies object
     */
    @Ignore
    public static FavMovies fromContentValues(ContentValues cv){
        FavMovies fm = new FavMovies();
        if ((cv.containsKey(COLUMN_ID)) && (cv.containsKey(COLUMN_TITLE)) &&
                (cv.containsKey(COLUMN_RELEASE_DATE)) && (cv.containsKey(COLUMN_AVG_RATING)) &&
                (cv.containsKey(COLUMN_OVERVIEW)) && (cv.containsKey(COLUMN_POSTER_IMAGE_URI))){
            fm.ID = cv.getAsString(COLUMN_ID);
            fm.Title = cv.getAsString(COLUMN_TITLE);
            fm.ReleaseDate = cv.getAsString(COLUMN_RELEASE_DATE);
            fm.AvgRating = cv.getAsString(COLUMN_AVG_RATING);
            fm.Overview = cv.getAsString(COLUMN_OVERVIEW);
            fm.PosterImageUri = cv.getAsString(COLUMN_POSTER_IMAGE_URI);
            return fm;
        }
        else return null;
    }

    @Ignore
    public static ArrayList<MoviesObject> toMoviesObjectList(List<FavMovies> list){
        ArrayList<MoviesObject> toReturn = new ArrayList<>();
        for (FavMovies movie : list){
            toReturn.add(MoviesObject.parseMovie(movie));
        }
        return toReturn;
    }

}
