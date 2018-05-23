package edu.udacity.kondeg.movies;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieActivity extends AppCompatActivity implements MovieFragment.OnFragmentInteractionListener{

    private static final String API_KEY = "";
    private static final String MOVIE_IMAGE_URI = "http://image.tmdb.org/t/p/";
    private static final String MOVIE_API_URI = "http://api.themoviedb.org/3/movie/popular" ;
    private static final String LOG_TAG = MovieActivity.class.getSimpleName();

    private ArrayAdapter<MovieTitle> mMovieAdapter;
    private GridView mGridView;
    private ArrayList<MovieTitle> movieTitles;
    private String mScreenDensity ="w185";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        if(getFragmentManager().findFragmentById(R.id.landing_page)==null) {
            getSupportFragmentManager().beginTransaction().add(R.id.landing_page, new MovieFragment())
            .commit();
        }


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    private void queryMovieDbApi() {
        final JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest
            (Request.Method.GET, MOVIE_API_URI+API_KEY, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("results");
                        String id;
                        String title;
                        double  rating;
                        String poster_uri;
                        String releaseDate;
                        String thumbnail;
                        String overview;
                        if (movieTitles==null) {
                            movieTitles = new ArrayList<MovieTitle>();
                        } else {
                            movieTitles.clear();
                        }
                        // TODO: Loop through the array
                        for (int i=0; i < jsonArray.length(); i++) {
                            JSONObject movie = jsonArray.getJSONObject(i);

                            // Get the required details: ID + poster_path
                            id = movie.getString("id");
                            title = movie.getString("title");
                            rating = movie.getDouble("vote_average");
                            releaseDate = movie.getString("release_date");
                            poster_uri = movie.getString("poster_path");
                            overview = movie.getString("overview");
                            thumbnail = MOVIE_IMAGE_URI+mScreenDensity+poster_uri;
                            movieTitles.add(new MovieTitle(title, releaseDate, id,
                                    MOVIE_IMAGE_URI+mScreenDensity+poster_uri, overview, rating));
                        }


                        mMovieAdapter.notifyDataSetChanged();

                    } catch (JSONException ex) {
                        Log.e(LOG_TAG,ex.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.i(LOG_TAG, error.getMessage());
                }
            });

    // Queue the async request
        Volley.newRequestQueue(getApplicationContext()).add(mJsonObjectRequest);
}
}
