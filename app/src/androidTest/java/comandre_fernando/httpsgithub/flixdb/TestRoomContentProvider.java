package comandre_fernando.httpsgithub.flixdb;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import comandre_fernando.httpsgithub.flixdb.data_objects.MoviesObject;
import comandre_fernando.httpsgithub.flixdb.roomdb.FavContentProvider;
import comandre_fernando.httpsgithub.flixdb.roomdb.FavDB;
import comandre_fernando.httpsgithub.flixdb.roomdb.FavMovies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;





@RunWith(AndroidJUnit4.class)
public class TestRoomContentProvider {

    //Context used to access various parts of the system

    private final Context context = InstrumentationRegistry.getTargetContext();
    private ArrayList<MoviesObject> backup;
    private FavDB db;

    //Backs up data before the test
    @Before
    public void setup(){
        db = FavDB.getInstance(context);
        backup = FavMovies.toMoviesObjectList(db.favMoviesDao().getFavMovies());
    }


    //Returns backed up data.
    @After
    public void cleanUp(){
        db.clearFavourites();
        for (MoviesObject movie: backup){
            db.addToFavourites(movie);
        }
    }


    //Tests if the content provider is registered properly.
    @Test
    public void testProviderRegistry(){
        String packagename = context.getPackageName();
        String content_provider_class_name = FavContentProvider.class.getName();
        ComponentName componentName = new ComponentName(packagename,content_provider_class_name);

        try{
            PackageManager pm = context.getPackageManager();
            ProviderInfo providerInfo = pm.getProviderInfo(componentName,0);
            String actualAuthority = providerInfo.authority;

            String Error =
                    "Error: FavContentProvider registered with authority: " + actualAuthority +
                            " instead of expected authority: " + packagename;
            assertEquals(Error,actualAuthority, packagename);
        }
        catch (PackageManager.NameNotFoundException e){
            fail("Error: FavContentProvider not registered at " + context.getPackageName());
        }
    }

    private final Uri ENTITY_URI = FavContentProvider.BASE_CONTENT_URI;
    private final Uri ITEM_URI =  ENTITY_URI.buildUpon().appendPath("1").build();

    @Test
    public void testUriMatcher(){
        UriMatcher testUriMatcher = FavContentProvider.buildUriMatcher();

        // For Entity
        int actualEntityMatchCode = testUriMatcher.match(ENTITY_URI);
        int expectedEntityMatchCode = FavContentProvider.CODE_FAVMOVIES_TABLE;

        assertEquals("Error: Entity code mismatch!",expectedEntityMatchCode,actualEntityMatchCode);

        // For Item
        int actualItemMatchCode = testUriMatcher.match(ITEM_URI);
        int expectedItemMatchCode = FavContentProvider.CODE_FAVMOVIES_ITEM;

        assertEquals("Error: Item code mismatch!",expectedItemMatchCode,actualItemMatchCode);
    }


    @Test
    public void testInsert(){
        ContentValues cv = new ContentValues();

        cv.put(FavMovies.COLUMN_ID,"101");
        cv.put(FavMovies.COLUMN_TITLE,"movie title");
        cv.put(FavMovies.COLUMN_AVG_RATING ,"5");
        cv.put(FavMovies.COLUMN_RELEASE_DATE ,"15 November 2010");
        cv.put(FavMovies.COLUMN_OVERVIEW ,"Overview");
        cv.put(FavMovies.COLUMN_POSTER_IMAGE_URI ,"posteruri");

        Uri actualUri = context.getContentResolver()
                        .insert(FavContentProvider.BASE_CONTENT_URI,cv);

        long id = 0;
        if (actualUri != null) {
            id = Long.parseLong(actualUri.getPathSegments().get(1));
        }

        assertTrue("Error: Failed insert!!",id>0);

    }

    @Test
    public void testQueryItem(){
        ContentValues cv = new ContentValues();

        cv.put(FavMovies.COLUMN_ID,"102");
        cv.put(FavMovies.COLUMN_TITLE,"movie title2");
        cv.put(FavMovies.COLUMN_AVG_RATING ,"5");
        cv.put(FavMovies.COLUMN_RELEASE_DATE ,"16 November 2010");
        cv.put(FavMovies.COLUMN_OVERVIEW ,"Overview2");
        cv.put(FavMovies.COLUMN_POSTER_IMAGE_URI ,"posteruri2");

        context.getContentResolver()
                .insert(FavContentProvider.BASE_CONTENT_URI,cv);


        Cursor cursor = context.getContentResolver().query(FavContentProvider.BASE_CONTENT_URI,
                null ,null ,null ,null);

        //To check that the right data was received
        ArrayList<String[]> data = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()){
                String[] a = new String[6];
                for (int x=0;x<6;x++){
                    a[x] = cursor.getString(x);
                }
                data.add(a);
            }
        }
        assertTrue("Failed Query", data.size()>0);
    }

    @Test
    public void testDelete(){
        ContentValues cv = new ContentValues();

        cv.put(FavMovies.COLUMN_ID,"202");
        cv.put(FavMovies.COLUMN_TITLE,"movie title");
        cv.put(FavMovies.COLUMN_AVG_RATING ,"5");
        cv.put(FavMovies.COLUMN_RELEASE_DATE ,"15 November 2010");
        cv.put(FavMovies.COLUMN_OVERVIEW ,"Overview");
        cv.put(FavMovies.COLUMN_POSTER_IMAGE_URI ,"posteruri");

        context.getContentResolver()
                .insert(FavContentProvider.BASE_CONTENT_URI,cv);

        Uri delete202 = FavContentProvider.BASE_CONTENT_URI.buildUpon()
                        .appendPath("202").build();

        int result = context.getContentResolver().delete(delete202,null,null);
        assertTrue("Error: Movie not deleted!", result>0);
    }

    @Test
    public void testInsertAll(){
        ContentValues[] values = new ContentValues[10];
        for (int i =0; i<10 ; i++){
            ContentValues cv = new ContentValues();
            cv.put(FavMovies.COLUMN_ID,"30"+i);
            cv.put(FavMovies.COLUMN_TITLE,"movie title "+i);
            cv.put(FavMovies.COLUMN_AVG_RATING ,"Rating "+i);
            cv.put(FavMovies.COLUMN_RELEASE_DATE ,"Date "+i);
            cv.put(FavMovies.COLUMN_OVERVIEW ,"Overview "+i);
            cv.put(FavMovies.COLUMN_POSTER_IMAGE_URI ,"posteruri "+i);
            values[i] = cv;
        }

        int actual = context.getContentResolver().bulkInsert(FavContentProvider.BASE_CONTENT_URI, values);
        int expected = 10;

        assertEquals("Error:Bulk insert success should be "+expected+",instead of "+actual,expected,actual);
    }

}
