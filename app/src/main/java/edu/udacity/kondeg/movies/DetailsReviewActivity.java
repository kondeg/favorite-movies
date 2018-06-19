package edu.udacity.kondeg.movies;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class DetailsReviewActivity extends AppCompatActivity implements DetailsReviewFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_review);
        if(savedInstanceState==null || getFragmentManager().findFragmentById(R.id.review_details_page)==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.review_details_page, new DetailsReviewFragment())
                    .commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
