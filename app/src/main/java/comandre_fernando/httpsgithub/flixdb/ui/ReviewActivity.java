package comandre_fernando.httpsgithub.flixdb.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import comandre_fernando.httpsgithub.flixdb.R;
import comandre_fernando.httpsgithub.flixdb.components.constants.Tags;
import comandre_fernando.httpsgithub.flixdb.data_objects.ReviewObject;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Init();
    }

    /**
     * Main Initialization method
     */
    private void Init(){
        // Gets details from intent
        Intent FullReview = getIntent();
        ReviewObject review = FullReview.getParcelableExtra(Tags.INTENT_DETAIL_TO_REVIEW);

        TextView tv_author = findViewById(R.id.tv_full_review_author);
        TextView tv_content = findViewById(R.id.tv_full_review_content);

        tv_author.setText(review.getAuthor());
        tv_content.setText(review.getContent());

        // Attaches the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
