package comandre_fernando.httpsgithub.flixdb.roomdb;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * This is the content provider
 * Reference Udacity tutorial on Content Providers
 */
public class FavContentProvider extends ContentProvider {

    private static final String AUTHORITY = "comandre_fernando.httpsgithub.flixdb";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+FavMovies.TABLE_NAME);

    public static final int CODE_FAVMOVIES_TABLE = 600;
    public static final int CODE_FAVMOVIES_ITEM = 601;
    private static final UriMatcher suriMatcher = buildUriMatcher();

    private static FavDB db;

    public static UriMatcher buildUriMatcher(){
        UriMatcher um = new UriMatcher(UriMatcher.NO_MATCH);
        um.addURI(AUTHORITY,FavMovies.TABLE_NAME,CODE_FAVMOVIES_TABLE);
        um.addURI(AUTHORITY,FavMovies.TABLE_NAME +"/*", CODE_FAVMOVIES_ITEM);
        return um;
    }


    @Override
    public boolean onCreate() {
        db = FavDB.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        switch (suriMatcher.match(uri)) {
            case CODE_FAVMOVIES_TABLE:
                    return db.favMoviesDao().cp_get_favourites();

            case CODE_FAVMOVIES_ITEM:
                    String path = uri.getPathSegments().get(1);
                    if (path.equals(FavMovies.COLUMN_TITLE)){
                        return db.favMoviesDao().cp_get_titles();
                    } else throw new UnsupportedOperationException("Unknown uri: " + uri);

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (suriMatcher.match(uri)) {
            case CODE_FAVMOVIES_TABLE:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + FavMovies.TABLE_NAME;

            case CODE_FAVMOVIES_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + FavMovies.TABLE_NAME;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (suriMatcher.match(uri)){
            case CODE_FAVMOVIES_TABLE:
                long id = 0;
                if (values != null) {
                    id = db.favMoviesDao()
                            .cp_insert(FavMovies.fromContentValues(values));
                }
                if ( id > 0 ) {
                        return ContentUris.withAppendedId(BASE_CONTENT_URI, id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }

            default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (suriMatcher.match(uri)){
            case CODE_FAVMOVIES_TABLE:
                    return db.favMoviesDao().cp_deleteAll();

            case CODE_FAVMOVIES_ITEM:
                    String id = uri.getPathSegments().get(1);
                    FavMovies movie = db.favMoviesDao().getFavMovieWithId(id);
                    int outcome = db.favMoviesDao().cp_delete(movie);
                    if (outcome>0){
                        return outcome;
                    } else {
                        throw new android.database.SQLException("Failed to delete row " + uri);
                    }

            default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Updating values is not allowed for this db.");
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        switch (suriMatcher.match(uri)){
            case CODE_FAVMOVIES_TABLE:
                FavMovies[] movies = new FavMovies[values.length];
                for (int i=0; i<values.length; i++) {
                    movies[i] = FavMovies.fromContentValues(values[i]);
                }
                return db.favMoviesDao().cp_insert_all(movies).length;

            case CODE_FAVMOVIES_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
}
