package edu.udacity.kondeg.movies;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MovieActivity extends AppCompatActivity implements MovieFragment.OnFragmentInteractionListener,
MovieFragment.OnChangeSortOrderListener{

    private static final String LOG_TAG = MovieActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        if(getFragmentManager().findFragmentById(R.id.landing_page)==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.landing_page, new MovieFragment())
            .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSortOrderChanged(String sortOrder) {
        Log.d(LOG_TAG, "Sort order changed "+sortOrder);
    }
}
