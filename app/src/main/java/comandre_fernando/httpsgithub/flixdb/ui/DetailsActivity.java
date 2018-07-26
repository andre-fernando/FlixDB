package comandre_fernando.httpsgithub.flixdb.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.concurrent.ExecutionException;

import comandre_fernando.httpsgithub.flixdb.R;
import comandre_fernando.httpsgithub.flixdb.components.ClickListener;
import comandre_fernando.httpsgithub.flixdb.components.EndlessRecyclerViewScrollListener;
import comandre_fernando.httpsgithub.flixdb.components.RecyclerTouchListener;
import comandre_fernando.httpsgithub.flixdb.components.adapters.ReviewAdapter;
import comandre_fernando.httpsgithub.flixdb.components.adapters.TrailerAdapter;
import comandre_fernando.httpsgithub.flixdb.components.asyncTasks.FetchFavouriteMoviesListAsync;
import comandre_fernando.httpsgithub.flixdb.components.constants.Tags;
import comandre_fernando.httpsgithub.flixdb.components.viewmodels.DetailsViewModel;
import comandre_fernando.httpsgithub.flixdb.data_objects.MoviesObject;
import comandre_fernando.httpsgithub.flixdb.roomdb.FavDB;
import comandre_fernando.httpsgithub.flixdb.roomdb.FavMovies;
import comandre_fernando.httpsgithub.flixdb.components.NetworkCheck;


public class DetailsActivity extends AppCompatActivity {

    private DetailsViewModel detailsViewModel;

    private ImageView iv_detail_poster;
    private TextView tv_detail_title;
    private TextView tv_detail_release_date;
    private TextView tv_detail_avg_rating;
    private TextView tv_detail_overview;
    private RecyclerView rv_trailer;
    private RecyclerView rv_review;
    private MenuItem menu_favourite;
    private Drawable favourite_selected;
    private Drawable favourite_unselected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Init();
    }

    /**
     * Main Intiaiziation method
     */
    private void Init(){
        //Get view model object
        detailsViewModel = ViewModelProviders.of(DetailsActivity.this)
                                    .get(DetailsViewModel.class);

        //Gets intent and parcel from intent
        Intent i = getIntent();
        MoviesObject SelectedMovie = i.getParcelableExtra(Tags.INTENT_MAIN_TO_DETAIL);
        detailsViewModel.setMovie(SelectedMovie);


        iv_detail_poster = findViewById(R.id.iv_Detail_Poster);
        tv_detail_title= findViewById(R.id.tv_Detail_Title);
        tv_detail_release_date = findViewById(R.id.tv_Detail_Release_Date);
        tv_detail_avg_rating = findViewById(R.id.tv_Detail_Avg_Rating);
        tv_detail_overview = findViewById(R.id.tv_Detail_Overview);
        rv_trailer = findViewById(R.id.rv_trailer);
        rv_review = findViewById(R.id.rv_review);
        detailsViewModel.setFavourite(checkIfFavourite());

        // Attaches the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DisplaysDetails();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (NetworkCheck.isConnected(this) && detailsViewModel.loadTrailers()){
            DisplaysTrailers();
        }

        if (NetworkCheck.isConnected(this) && detailsViewModel.loadReviews()){
            DisplayReviews();
        }
    }

    private void DisplaysDetails(){
        //Displays the details
        Glide.with(this)
                .load(detailsViewModel.getMovie().getPosterImageUri())
                .apply(new RequestOptions()
                        .fitCenter()
                        .placeholder(R.mipmap.imageplaceholder))
                .into(iv_detail_poster);

        tv_detail_title.setText(detailsViewModel.getMovie().getTitle());
        tv_detail_title.isSelected();
        tv_detail_release_date.setText
                (String.format("Release Date: %s", detailsViewModel.getMovie().getReleaseDate()));
        tv_detail_avg_rating.setText
                (String.format("Rating: %s/10", detailsViewModel.getMovie().getAvgRating()));
        tv_detail_overview.setText(detailsViewModel.getMovie().getOverview());



    }

    private void DisplaysTrailers(){
        TrailerAdapter trailerAdapter = new TrailerAdapter(this, detailsViewModel.getTrailerList());
        rv_trailer.setAdapter(trailerAdapter);
        rv_trailer.setLayoutManager(new LinearLayoutManager
                (this,LinearLayoutManager.HORIZONTAL,false));
    }

    private void DisplayReviews(){
        detailsViewModel.loadNextReviewPage(2);
        final ReviewAdapter reviewAdapter = new ReviewAdapter(detailsViewModel.getReviewList());
        rv_review.setAdapter(reviewAdapter);
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this);

        if (detailsViewModel.getTotalReviewPages()>1){
            EndlessRecyclerViewScrollListener reviewEndlessScroll =
                    new EndlessRecyclerViewScrollListener(reviewLayoutManager) {
                        @Override
                        public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                            if (NetworkCheck.isConnected(DetailsActivity.this)){
                                reviewAdapter.AddDataToList(detailsViewModel.getNextPage());
                                if (!detailsViewModel.loadNextReviewPage(page)){
                                    String message = page > detailsViewModel.getTotalReviewPages()?
                                            "No more pages!!" : "Oops something went wrong!";
                                    Toast.makeText(DetailsActivity.this, message, Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        }
                    };
            rv_review.addOnScrollListener(reviewEndlessScroll);
        }
        rv_review.setLayoutManager(reviewLayoutManager);

        rv_review.addOnItemTouchListener(new RecyclerTouchListener(this, rv_review, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent FullReview = new Intent(DetailsActivity.this, ReviewActivity.class);
                FullReview.putExtra(Tags.INTENT_DETAIL_TO_REVIEW,detailsViewModel.getReviewList().get(position));
                startActivity(FullReview);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        //For smooth scrolling of nested list of reviews
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            rv_review.setNestedScrollingEnabled(false);
        }else {
            ViewCompat.setNestedScrollingEnabled(rv_review,false);
        }
    }



    //This section deals with the Favourites Button

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_activity_menu, menu);
        this.menu_favourite = menu.findItem(R.id.menu_favourites);
        favourite_selected= getResources().getDrawable(R.drawable.ic_favourites_selected);
        favourite_unselected = getResources().getDrawable(R.drawable.ic_favourites_unselected);
        if (detailsViewModel.isFavourite()){
            menu_favourite.setIcon(favourite_selected);
        } else {
            menu_favourite.setIcon(favourite_unselected);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_favourites){
            if (detailsViewModel.isFavourite()){
                menu_favourite.setIcon(favourite_unselected);
                FavDB.getInstance(this).removeMovieAtId(detailsViewModel.getMovie().getID());
                detailsViewModel.setFavourite(false);
                return true;
            } else {
                menu_favourite.setIcon(favourite_selected);
                FavDB.getInstance(this).addToFavourites(detailsViewModel.getMovie());
                detailsViewModel.setFavourite(true);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the movie id exist in db
     * @return true or false
     */
    private boolean checkIfFavourite(){

        List<FavMovies> favourites;
        try {
            favourites = new FetchFavouriteMoviesListAsync(FavDB.getInstance(this))
                                                    .execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }

        for (FavMovies movie : favourites){
            if (movie.ID.equals(detailsViewModel.getMovie().getID())){
                return true;
            }
        }

        return false;
    }
}
