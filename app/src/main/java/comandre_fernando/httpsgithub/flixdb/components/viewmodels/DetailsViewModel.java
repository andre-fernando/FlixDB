package comandre_fernando.httpsgithub.flixdb.components.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import comandre_fernando.httpsgithub.flixdb.R;
import comandre_fernando.httpsgithub.flixdb.components.asyncTasks.FetchJsonAsync;
import comandre_fernando.httpsgithub.flixdb.components.constants.BaseUrl;
import comandre_fernando.httpsgithub.flixdb.components.constants.JsonTags;
import comandre_fernando.httpsgithub.flixdb.components.constants.Query;
import comandre_fernando.httpsgithub.flixdb.components.constants.UrlPath;
import comandre_fernando.httpsgithub.flixdb.data_objects.MoviesObject;
import comandre_fernando.httpsgithub.flixdb.data_objects.ReviewObject;
import comandre_fernando.httpsgithub.flixdb.data_objects.TrailerObject;

public class DetailsViewModel extends AndroidViewModel {
    private MoviesObject movie;
    private ArrayList<TrailerObject> trailerList;
    private ArrayList<ReviewObject> reviewList,nextPage;
    private boolean isFavourite;
    private Uri currentReviewUri;
    private int totalReviewPages;
    private final String defaultLanguage;


    public DetailsViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        defaultLanguage = sharedPreferences.getString(
                context.getString(R.string.pref_default_lang_key)
                ,"en-US"

        );
        totalReviewPages=0;
    }

    //region Getter's and Setter's

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public int getTotalReviewPages(){
        return totalReviewPages;
    }

    public MoviesObject getMovie() {
        return movie;
    }

    public void setMovie(MoviesObject selectedMovie){
        this.movie = selectedMovie;
    }

    public ArrayList<TrailerObject> getTrailerList() {
        return trailerList;
    }

    public ArrayList<ReviewObject> getReviewList() {
        return reviewList;
    }

    public ArrayList<ReviewObject> getNextPage() {
        return nextPage;
    }

    //endregion Getter's & Setter's

    //region Methods for Reviews

    /**
     * Final wrapper method that returns the reviews in a
     * Arrraylist of object ReviewParcel.
     * @return ArrayList of ReviewParcel object
     */
    public boolean loadReviews(){

        Uri theUri = Uri.parse(BaseUrl.API).buildUpon()
                .appendPath(UrlPath.MOVIE)
                .appendPath(movie.getID())
                .appendPath(UrlPath.REVIEW)
                .appendQueryParameter(Query.API_KEY,BaseUrl.API_KEY)
                .appendQueryParameter(Query.LANGUAGE, defaultLanguage)
                .build();
        currentReviewUri =theUri;
        totalReviewPages =0;
        String RecievedString = ParseUrl(theUri);
        if (RecievedString!=null){
            reviewList = ParseReviewListJsonString(RecievedString);
            return reviewList!=null;
        }else return false;
    }

    /**
     * Gets next page of reviews, used in scroll listener
     * @param page page number
     * @return ArrayList of ReviewParcel object
     */
    public boolean loadNextReviewPage(int page){
        if (nextPage!=null && page >2) reviewList.addAll(nextPage);
        Uri theUri = currentReviewUri.buildUpon()
                .appendQueryParameter(Query.PAGE, Integer.toString(page))
                .build();
        String RecievedString = ParseUrl(theUri);
        if (RecievedString!=null){
            nextPage = ParseReviewListJsonString(RecievedString);
            return nextPage!=null;
        }else return false;
    }


    /**
     * Converts a JSON string int an ArrayList<ReviewParcel>
     * @param jsonstr JSON String data
     * @return ArrayList<ReviewParcel>
     */
    private ArrayList<ReviewObject> ParseReviewListJsonString(String jsonstr){
        if (jsonstr.isEmpty()) {return null;}
        try {
            JSONObject ReviewObject = new JSONObject(jsonstr);
            if (totalReviewPages ==0){
                totalReviewPages = ReviewObject.getInt(JsonTags.TOTAL_PAGES);
            }
            JSONArray ReviewObjectArray = ReviewObject.getJSONArray(JsonTags.RESULTS);
            ArrayList<ReviewObject> ParsedReviewData = new ArrayList<>();
            String id, author, content , url;
            for (int i =0; i<ReviewObjectArray.length();i++){
                JSONObject obj = ReviewObjectArray.getJSONObject(i);
                id=obj.getString(JsonTags.ID);
                author=obj.getString(JsonTags.AUTHOR);
                content = obj.getString(JsonTags.REVIEW_CONTENT);
                url = obj.getString(JsonTags.URL);
                if (!author.isEmpty() && !content.isEmpty()){
                    ParsedReviewData.add(new ReviewObject(id,author,content,url));
                }
            }
            return ParsedReviewData;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    //endregion Methods for Reviews

    //region Methods for Trailers


    public boolean loadTrailers(){
        Uri theUri = Uri.parse(BaseUrl.API)
                .buildUpon()
                .appendPath(UrlPath.MOVIE)
                .appendPath(movie.getID())
                .appendPath(UrlPath.VIDEO)
                .appendQueryParameter(Query.API_KEY, BaseUrl.API_KEY)
                .appendQueryParameter(Query.LANGUAGE, defaultLanguage)
                .build();
        String RecievedString =ParseUrl(theUri);
        if (RecievedString!=null){
            trailerList = ParseTrailerJsonString(RecievedString);
            return trailerList!=null;
        }else return false;
    }


    /**
     * Converts a JSON String into TrailerParcel objects
     * @param jsonstr JSON String for movie trailers
     * @return Arraylist of TrailerParcel objects
     */
    private ArrayList<TrailerObject> ParseTrailerJsonString(String jsonstr){
        if (jsonstr.isEmpty()) {return null;}

        try {
            JSONObject TrailerObject = new JSONObject(jsonstr);
            JSONArray TrailerObjectArray = TrailerObject.getJSONArray(JsonTags.RESULTS);
            ArrayList<comandre_fernando.httpsgithub.flixdb.data_objects.TrailerObject> ParsedTrailerData = new ArrayList<>();
            String ID,Key,Name,Site,Type;
            for (int i=0 ; i<TrailerObjectArray.length();i++){
                JSONObject obj = TrailerObjectArray.getJSONObject(i);
                ID=obj.getString(JsonTags.ID);
                Key=obj.getString(JsonTags.KEY);
                Name = obj.getString(JsonTags.NAME);
                if (Name.isEmpty()){
                    Name="Trailer "+i;
                }
                Site = obj.getString(JsonTags.SITE);
                Type = obj.getString(JsonTags.TYPE);
                if (Site.equals("YouTube") && Type.equals("Trailer") && !Key.isEmpty()){
                    ParsedTrailerData.add(new TrailerObject(ID,Key,Name,Site,Type));
                }
            }
            return  ParsedTrailerData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //endregion

    /**
     * This method gets a raw JSON string from the url
     * @param UriRequest The url which corresponds to the desired data required.
     * @return  JSON String
     */
    private String ParseUrl(Uri UriRequest) {
        try {
            URL theUrl = new URL(UriRequest.toString());
            return new FetchJsonAsync().execute(theUrl).get();
        } catch (InterruptedException | ExecutionException | MalformedURLException e ) {
            e.printStackTrace();
            return null;
        }
    }

}
