package edu.udacity.kondeg.movies;

import android.app.Fragment;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MovieActivity extends AppCompatActivity implements MovieFragment.OnFragmentInteractionListener,
MovieFragment.OnChangeSortOrderListener{

    private static final String LOG_TAG = MovieActivity.class.getSimpleName();

    private String sortOrder = null;

    private android.support.v4.app.Fragment mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        if((savedInstanceState==null)) {
            Log.d(LOG_TAG, "new fragment");
            mContent = new MovieFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.landing_page, mContent)
            .commit();
        } else {
            Log.d(LOG_TAG, "replace fragment");
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, getResources().getString(R.string.fragmentMovie));
            getSupportFragmentManager().beginTransaction().replace(R.id.landing_page, mContent);
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "save instance state activity "+(mContent==null));
        getSupportFragmentManager().putFragment(savedInstanceState, getResources().getString(R.string.fragmentMovie), mContent);
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onSortOrderChanged(String sortOrder) {
        Log.d(LOG_TAG, "Sort order changed "+sortOrder);
        this.sortOrder = sortOrder;
    }
}
