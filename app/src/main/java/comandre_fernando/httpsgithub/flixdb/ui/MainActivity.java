package comandre_fernando.httpsgithub.flixdb.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

import comandre_fernando.httpsgithub.flixdb.R;
import comandre_fernando.httpsgithub.flixdb.components.ClickListener;
import comandre_fernando.httpsgithub.flixdb.components.EndlessRecyclerViewScrollListener;
import comandre_fernando.httpsgithub.flixdb.components.RecyclerTouchListener;
import comandre_fernando.httpsgithub.flixdb.components.adapters.MovieListAdapter;
import comandre_fernando.httpsgithub.flixdb.components.constants.Tags;
import comandre_fernando.httpsgithub.flixdb.components.viewmodels.MainViewModel;
import comandre_fernando.httpsgithub.flixdb.components.NetworkCheck;


public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    private MovieListAdapter flixAdapter;
    private RecyclerView mainRecyclerView;
    private DrawerLayout mainDrawerLayout;
    private ActionBarDrawerToggle toggle;
    private SwipeRefreshLayout mainSwipe;
    private GridLayoutManager mainGridManager;
    private EndlessRecyclerViewScrollListener mainEndlessScroll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init_Main(savedInstanceState);
    }


    /* Following are Initializing methods */

    /**
     * Initializes the main components
     *
     */
    private void Init_Main(Bundle a) {
        //Get the view model object
        mainViewModel= ViewModelProviders
                .of(MainActivity.this)
                .get(MainViewModel.class);

        // Initialise the other components
        Init_NavigationDraw();
        Init_MainRecyclerView(a);
        Init_SwipeToRefresh();
    }

    /**
     * Initializes Swipe to refresh
     */
    private void Init_SwipeToRefresh() {
        mainSwipe = findViewById(R.id.Swipe_To_Refresh_Main);
        mainSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainViewModel.loadUpdatedList();
                flixAdapter.RefreshData(mainViewModel.getMovieList(), mainEndlessScroll);
                mainGridManager.scrollToPosition(0);
                mainSwipe.setRefreshing(false);
            }
        });
    }

    /**
     * Initializes the navigation draw
     * Reference: https://guides.codepath.com/android/fragment-navigation-drawer
     */
    private void Init_NavigationDraw() {
        mainDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView mainNavigationView = findViewById(R.id.Navigation_View);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toggle = new ActionBarDrawerToggle
                (this, mainDrawerLayout, toolbar,
                        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mainDrawerLayout.addDrawerListener(toggle);
        setSupportActionBar(toolbar);
        setupDrawerContent(mainNavigationView);
    }

    /**
     * Initializes the RecyclerView
     * @param a Bundle contains the saved state of grid Manager
     */
    private void Init_MainRecyclerView(Bundle a) {
        mainRecyclerView = findViewById(R.id.rv_main);
        mainGridManager = new GridLayoutManager(this, 3);

        if (a == null && NetworkCheck.isConnected(this)) {
            if (mainViewModel.loadTopRatedMovies()){
                mainViewModel.loadNextPage(2);
            }
        }else {
            mainGridManager.onRestoreInstanceState
                    (Objects.requireNonNull(a).getParcelable(Tags.MAIN_SAVE_SCROLL_STATE));
        }

        //Set Layout Manager
        mainRecyclerView.setLayoutManager(mainGridManager);
        mainRecyclerView.setHasFixedSize(true);

        flixAdapter = new MovieListAdapter(this, mainViewModel.getMovieList());
        mainRecyclerView.setAdapter(flixAdapter);

        //Set EndlessScroll
        mainEndlessScroll = new EndlessRecyclerViewScrollListener(mainGridManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (NetworkCheck.isConnected(MainActivity.this)){
                    flixAdapter.AddDataToList(mainViewModel.getNextPage());
                    if (!mainViewModel.loadNextPage(page)){
                        String message = page>mainViewModel.getTotalMoviePages() ?
                                "No more pages!!" : "Oops something went wrong!";
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        };
        mainRecyclerView.addOnScrollListener(mainEndlessScroll);


        // Setting onClickListeners on Recycler View
        mainRecyclerView.addOnItemTouchListener(new RecyclerTouchListener
                (this, mainRecyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent StartDetailView = new Intent
                                (MainActivity.this, DetailsActivity.class);
                        StartDetailView.putExtra(
                                Tags.INTENT_MAIN_TO_DETAIL,
                                mainViewModel.getMovieList().get(position)
                        );
                        startActivity(StartDetailView);
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        Toast.makeText(
                                getApplicationContext(),
                                mainViewModel.getMovieList().get(position).getTitle()
                                        + "\nOverview :"
                                        + mainViewModel.getMovieList().get(position).getOverview(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }));

    }


    /* Following methods deal with the Navigation Draw */

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     * Adds a listener to the navigation draw
     * @param navigationView This will be submitted automatically
     */
    private void setupDrawerContent(NavigationView navigationView) {
        //noinspection NullableProblems
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    /**
     * Carries out the action after clicking on the respective drawer option
     *
     * @param menuItem : Will be given by the OnNavigationItemSelectedListener()
     */
    private void selectDrawerItem(MenuItem menuItem) {
        boolean isConnected = NetworkCheck.isConnected(this);

        switch (menuItem.getItemId()) {
            case R.id.drawer_TopMovies:
                if (isConnected && mainViewModel.loadTopRatedMovies()){
                    mainViewModel.loadNextPage(2);
                    flixAdapter.RefreshData(mainViewModel.getMovieList(), mainEndlessScroll);
                    mainGridManager.scrollToPosition(0);// To Reset the scroll position
                }
                break;

            case R.id.drawer_PopularMovies:
                if (isConnected && mainViewModel.loadPopularMovies()){
                    mainViewModel.loadNextPage(2);
                    flixAdapter.RefreshData(mainViewModel.getMovieList(), mainEndlessScroll);
                    mainGridManager.scrollToPosition(0);// To Reset the scroll position
                }
                break;

            case R.id.drawer_Favourites:
                Intent launch_fav = new Intent(MainActivity.this, FavouritesActivity.class);
                startActivity(launch_fav);
                break;

            case R.id.drawer_Settings:
                Intent startSettings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(startSettings);
                break;

            case R.id.drawer_About:
                Intent startAbout = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(startAbout);
                break;
        }

        // Highlight the selected item in the NavigationView
        if (menuItem.getItemId() == R.id.drawer_PopularMovies
                || menuItem.getItemId() == R.id.drawer_TopMovies){
            menuItem.setChecked(true);
        }
        // Close the navigation drawer
        mainDrawerLayout.closeDrawers();
    }



    //Puts our ResultList into the saved bundle.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(
                Tags.MAIN_SAVE_SCROLL_STATE,
                mainRecyclerView.getLayoutManager().onSaveInstanceState()
        );
        super.onSaveInstanceState(outState);
    }



}

