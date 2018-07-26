package comandre_fernando.httpsgithub.flixdb.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import comandre_fernando.httpsgithub.flixdb.R;
import comandre_fernando.httpsgithub.flixdb.components.ClickListener;
import comandre_fernando.httpsgithub.flixdb.components.RecyclerTouchListener;
import comandre_fernando.httpsgithub.flixdb.components.adapters.FavouritesAdapter;
import comandre_fernando.httpsgithub.flixdb.components.constants.Tags;
import comandre_fernando.httpsgithub.flixdb.components.viewmodels.FavouritesViewModel;
import comandre_fernando.httpsgithub.flixdb.roomdb.FavMovies;

/**
 * This Activity displays the favourite movie list of the user
 */
public class FavouritesActivity extends AppCompatActivity {
    private FavouritesViewModel favViewModel;
    private RecyclerView rv_favourites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faourites);

        // Attaches the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //noinspection ConstantConditions
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.label_favourites));

        rv_favourites = findViewById(R.id.rv_favourites);

        Init_Favourites_Observer();
        Init_Recycler_View();

    }

    private void Init_Recycler_View(){
        FavouritesAdapter favouritesAdapter = new FavouritesAdapter
                (this, favViewModel.getFavouriteMovies());
        rv_favourites.setAdapter(favouritesAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rv_favourites.setLayoutManager(gridLayoutManager);
        rv_favourites.setHasFixedSize(true);

        // Setting onClickListeners on Recycler View
        rv_favourites.addOnItemTouchListener(new RecyclerTouchListener(this, rv_favourites, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent StartDetailView = new Intent
                        (FavouritesActivity.this, DetailsActivity.class);
                StartDetailView.putExtra(
                        Tags.INTENT_MAIN_TO_DETAIL,
                        favViewModel.getFavouriteMovies().get(position));
                startActivity(StartDetailView);
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getApplicationContext(),
                        favViewModel.getFavouriteMovies().get(position).getTitle()
                        , Toast.LENGTH_LONG).show();
            }
        }));
    }

    /**
     * This method attaches an observer to the FavMovie table
     * and updates the recycler view when changed.
     */
    private void Init_Favourites_Observer(){
        favViewModel = ViewModelProviders.of(FavouritesActivity.this).get(FavouritesViewModel.class);
        favViewModel.getFavouriteMoviesObservable().observe(this, new Observer<List<FavMovies>>() {
            @Override
            public void onChanged(@Nullable List<FavMovies> favMovies) {
                Init_Recycler_View();
            }
        });

    }
}
