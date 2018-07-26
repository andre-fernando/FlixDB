package comandre_fernando.httpsgithub.flixdb.components.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

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

public class MainViewModel extends AndroidViewModel {
    private ArrayList<MoviesObject> movieList;
    private ArrayList<MoviesObject> nextPage;


    private int totalMoviePages;
    private Uri currentUri;
    private boolean showAdultContent;
    private String defaultLanguage;


    public MainViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        defaultLanguage = sharedPreferences.getString(
                context.getString(R.string.pref_default_lang_key)
                ,"en-US"

        );
        showAdultContent = sharedPreferences.getBoolean(
                context.getString(R.string.pref_allow_adult_key),
                context.getResources().getBoolean(R.bool.pref_allow_adult_default)
        );

        totalMoviePages = 0;
    }

    //region Getters and Setters

    public ArrayList<MoviesObject> getMovieList(){ return movieList;}

    public ArrayList<MoviesObject> getNextPage() { return nextPage;}

    public int getTotalMoviePages(){ return totalMoviePages;}

    public void setShowAdultContent(boolean showAdultContent) {
        this.showAdultContent = showAdultContent;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }
    //endregion Getters and Setters


    public boolean loadTopRatedMovies() {
        Uri theUri = Uri.parse(BaseUrl.API).buildUpon()
                .appendPath(UrlPath.MOVIE)
                .appendPath(UrlPath.TOP_RATED)
                .appendQueryParameter(Query.API_KEY,BaseUrl.API_KEY)
                .appendQueryParameter(Query.LANGUAGE, defaultLanguage)
                .appendQueryParameter(Query.INCLUDE_ADULT,Boolean.toString(showAdultContent))
                .build();
        totalMoviePages =0;
        currentUri =theUri;
        String jsonString = ParseUrl(theUri);
        if (jsonString!= null){
            movieList = ParseMovieListJsonString(jsonString);
            return movieList != null && movieList.size() > 0;
        } else {
            Log.e("MainActivity","Error while getting top movies.");
            return false;
        }
    }


    public boolean loadPopularMovies() {
        Uri theUri = Uri.parse(BaseUrl.API).buildUpon()
                .appendPath(UrlPath.MOVIE)
                .appendPath(UrlPath.POPULAR)
                .appendQueryParameter(Query.API_KEY,BaseUrl.API_KEY)
                .appendQueryParameter(Query.LANGUAGE, defaultLanguage)
                .appendQueryParameter(Query.INCLUDE_ADULT,Boolean.toString(showAdultContent))
                .build();
        totalMoviePages =0;
        currentUri =theUri;
        String jsonString = ParseUrl(theUri);
        if (jsonString!= null){
            movieList = ParseMovieListJsonString(jsonString);
            return movieList !=null && movieList.size()>0;
        } else {
            Log.e("MainActivity","Error while getting popular movies.");
            return false;
        }
    }


    @SuppressWarnings("UnusedReturnValue")
    public boolean loadUpdatedList(){
        String jsonString = ParseUrl(currentUri);
        if (jsonString!= null){
            movieList = ParseMovieListJsonString(jsonString);
            return movieList !=null && movieList.size()>0;
        } else {
            Log.e("MainActivity","Error while updating movies list.");
            return false;
        }
    }


    public boolean loadNextPage(int page) {
        if (page <= totalMoviePages){
            if (nextPage!=null && page>2) movieList.addAll(nextPage);
            Uri theUri = currentUri.buildUpon()
                    .appendQueryParameter(Query.PAGE,Integer.toString(page))
                    .build();
            String jsonString = ParseUrl(theUri);
            if (jsonString!= null){
                nextPage = ParseMovieListJsonString(jsonString);
                return nextPage !=null && nextPage.size()>0;
            } else {
                Log.e("MainActivity","Error while getting popular movies.");
                return false;
            }
        }else {//No more pages
            return false;
        }

    }


    /**
     * This method gets a raw JSON string from the url
     * @param UriRequest The url which corresponds to the desired data required.
     * @return  raw JSON String
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

    /**
     * Converts a json string into MovieObject array list.
     * @param jsonstr The json string recieved from the ParseUrl(URL)
     * @return Returns an ArrayList with the required movie data
     */
    private ArrayList<MoviesObject> ParseMovieListJsonString(String jsonstr) {
        try {
            if (jsonstr.isEmpty()) {
                return null;
            }
            JSONObject MovieObj = new JSONObject(jsonstr);
            if (totalMoviePages == 0) {
                totalMoviePages = MovieObj.getInt(JsonTags.TOTAL_PAGES);
            }
            JSONArray MovieArray = MovieObj.getJSONArray(JsonTags.RESULTS);
            ArrayList<MoviesObject> ParsedMovieData = new ArrayList<>();
            String ID, Title, ReleaseDate, AvgRating, Overview, PosterSuffix;
            for (int i = 0; i < MovieArray.length(); i++) {
                JSONObject ResultObj = MovieArray.getJSONObject(i);
                ID = ResultObj.getString(JsonTags.ID);
                Title = ResultObj.getString(JsonTags.TITLE);
                ReleaseDate = ResultObj.getString(JsonTags.RELEASE_DATE);
                AvgRating = ResultObj.getString(JsonTags.VOTE_AVERAGE);
                Overview = ResultObj.getString(JsonTags.OVERVIEW);
                PosterSuffix = ResultObj.getString(JsonTags.POSTER_PATH);
                ParsedMovieData.add(new MoviesObject(ID, Title, ReleaseDate, AvgRating, Overview, PosterSuffix));
            }
            return ParsedMovieData;
        } catch (JSONException e){
            e.printStackTrace();
            Log.e("MainActivity","Error Parsing Json string: "+e.getMessage());
            return new ArrayList<>();
        }
    }

}
