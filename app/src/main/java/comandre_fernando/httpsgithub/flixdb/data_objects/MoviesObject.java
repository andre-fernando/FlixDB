package comandre_fernando.httpsgithub.flixdb.data_objects;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import comandre_fernando.httpsgithub.flixdb.components.constants.BaseUrl;
import comandre_fernando.httpsgithub.flixdb.components.constants.UrlPath;
import comandre_fernando.httpsgithub.flixdb.roomdb.FavMovies;

/**
 * This Class allows us to create objects of Movie data.
 * It implements Parcelable
 */

@SuppressWarnings("unused")
public class MoviesObject implements Parcelable {
    private String ID;
    private String Title;
    private String ReleaseDate;
    private String AvgRating;
    private String Overview;
    private Uri PosterImageUri;

    /**
     *  The Constructor accepts all strins from the JSON file
     * @param id The id of the movie
     * @param title The Title of the movie
     * @param releasedate The release date
     * @param avgrating The average rating
     * @param overview The Summary of the movie
     * @param ImageUrlSuffix The suffix of the url that identifies the Movie Poster.
     */
    public MoviesObject(String id, String title, String releasedate, String avgrating, String overview, String ImageUrlSuffix){
        ID = id;
        Title = title;
        ReleaseDate = releasedate;
        AvgRating = avgrating;
        Overview = overview;
        // To Build the uri from the string
        PosterImageUri = Uri.parse(BaseUrl.API_IMAGE).buildUpon()
                            .appendPath(UrlPath.IMAGESIZE)
                            .appendEncodedPath(ImageUrlSuffix)
                            .build();
    }
    public MoviesObject(String id, String title, String releasedate, String avgrating, String overview, Uri imageUri) {
        ID = id;
        Title = title;
        ReleaseDate = releasedate;
        AvgRating = avgrating;
        Overview = overview;
        PosterImageUri = imageUri;
    }

    private MoviesObject(Parcel in) {
        ID = in.readString();
        Title = in.readString();
        ReleaseDate = in.readString();
        AvgRating = in.readString();
        Overview = in.readString();
        PosterImageUri = (Uri) in.readValue(MoviesObject.class.getClassLoader());
    }


    /** Getters */
    public String getID(){ return ID;  }

    public String getTitle(){ return Title;  }

    public String getReleaseDate(){ return ReleaseDate;  }

    public String getAvgRating(){ return AvgRating;  }

    public String getOverview(){ return Overview;  }

    public  Uri getPosterImageUri() { return PosterImageUri;  }


    /** Setters */
    public  void setID(String a){ ID = a; }

    public  void setTitle(String a){ Title = a; }

    public  void setReleaseDate(String a){ ReleaseDate = a; }

    public  void setAvgRating(String a){ AvgRating = a; }

    public  void setOverview(String a){ Overview = a; }

    public  void setPosterImageUri(Uri a){ PosterImageUri = a; }


    /**
     * Converts a FavMovies object from the db to a
     * MoviesParcel object.
     * @param fm FavMovies object to be converted
     * @return A MoviesParcel object
     */
    public static MoviesObject parseMovie(FavMovies fm){
        return new MoviesObject(fm.ID,fm.Title,fm.ReleaseDate,fm.AvgRating,fm.Overview, Uri.parse(fm.PosterImageUri));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ID);
        parcel.writeString(Title);
        parcel.writeString(ReleaseDate);
        parcel.writeString(AvgRating);
        parcel.writeString(Overview);
        parcel.writeValue(PosterImageUri);
    }

    public static final Creator<MoviesObject> CREATOR = new Creator<MoviesObject>() {
        @Override
        public MoviesObject createFromParcel(Parcel in) {
            return new MoviesObject(in);
        }

        @Override
        public MoviesObject[] newArray(int size) {
            return new MoviesObject[size];
        }
    };
}
