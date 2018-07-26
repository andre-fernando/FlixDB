package comandre_fernando.httpsgithub.flixdb.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import comandre_fernando.httpsgithub.flixdb.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //This attaches the toolbar to the activity. The parent activity
        // must be set before the back button is available in the toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //noinspection ConstantConditions
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("About");
    }

    //Intent to open the Tmdb page
    public void OpenTmdb(@SuppressWarnings("unused") View view){
        Intent openTmdbIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/"));
        if (openTmdbIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(openTmdbIntent);
        }
    }

    //Intent to open my personal github page.
    public void OpenGithub(@SuppressWarnings("unused") View view){
        Intent openGithubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/andre-fernando"));
        if (openGithubIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(openGithubIntent);
        }
    }
}
