package edu.udacity.kondeg.movies;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import edu.udacity.kondeg.movies.database.MovieDatabase;
import edu.udacity.kondeg.movies.database.MovieExecutor;
import edu.udacity.kondeg.movies.util.EndpointParams;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MovieTitle title = null;
    private List<MovieTrailer> trailers = null;
    private List<MovieReview> reviews = null;
    private ArrayAdapter<MovieTrailer> mMovieTrailerAdapter = null;
    private ArrayAdapter<MovieReview> mMovieReviewAdapter = null;
    private GridView movieTrailerView = null;
    private GridView movieReviewView = null;
    private Executor executor = null;
    Boolean favorite = false;
    private MovieDatabase mDatabase;
    private FloatingActionButton button = null;

    private OnFragmentInteractionListener mListener;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieDetailFragment newInstance(String param1, String param2) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        executor = new MovieExecutor();
        mDatabase = MovieDatabase.getDatabase(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        setHasOptionsMenu(false);
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Intent intent = getActivity().getIntent();

        if (intent!=null && intent.hasExtra(getResources().getString(R.string.parcel_movie))) {
            title = intent.getParcelableExtra(getResources().getString(R.string.parcel_movie));
        }

        if(title!=null) {
            // Load the poster image into the ImageView
            ImageView imageView = (ImageView) view.findViewById(R.id.poster_image);
            Picasso.with(getContext()).load(title.thumbnailImage).into(imageView);

            // Set the title of the movie in the TextView
            TextView titleTextView = (TextView) view.findViewById(R.id.movie_title);
            titleTextView.setText(title.title);

            TextView releaseDateTextView = (TextView) view.findViewById(R.id.release_date);
            releaseDateTextView.setText(title.releaseDate);

            // Set the rating of the movie in the TextView
            TextView voteAverageTextView = (TextView) view.findViewById(R.id.movie_rating);
            voteAverageTextView.setText(Double.toString(title.voteAverage));

            // Set the synopsis of the movie in the TextView
            TextView overviewTextView = (TextView) view.findViewById(R.id.plot);
            overviewTextView.setText(title.overview);

            trailers = new ArrayList<>();

            reviews = new ArrayList<>();

            queryMovieTrailerApi();

            mMovieTrailerAdapter = new MovieTrailerAdapter(getActivity(), trailers);

            movieTrailerView = (GridView) view.findViewById(R.id.movie_trailers_grid);

            movieTrailerView.setAdapter(mMovieTrailerAdapter);

            movieTrailerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MovieTrailer movieTitle = (MovieTrailer) parent.getItemAtPosition(position);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + movieTitle.getTrailerId())));
                }
            });

            queryMovieReviewApi();

            mMovieReviewAdapter = new MovieReviewAdapter(getActivity(), reviews);

            movieReviewView = (GridView) view.findViewById(R.id.movie_review_grid);

            movieReviewView.setAdapter(mMovieReviewAdapter);

            movieReviewView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MovieReview movieReview = (MovieReview) parent.getItemAtPosition(position);
                    Intent intent = new Intent(getActivity(), DetailsReviewActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString(getResources().getString(R.string.review_title),title.title);
                    extras.putString(getResources().getString(R.string.review_author),movieReview.getAuthor());
                    extras.putString(getResources().getString(R.string.review_content), movieReview.getContent());
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
            button = view.findViewById(R.id.favbtn);

        }

        return view;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated");
        if (button == null) {
            Log.d(LOG_TAG, "Button is null");
            return;
        }
        final ColorStateList pink = getActivity().getResources().getColorStateList(R.color.pink);
        final ColorStateList white = getActivity().getResources().getColorStateList(R.color.offwhite);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (favorite) {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            mDatabase.movieDao().delete(title);
                        }
                    });
                    favorite = false;
                    button.setBackgroundTintList(white);
                    Toast.makeText(getActivity(), getResources().getText(R.string.removed_favorites), Toast.LENGTH_SHORT).show();
                } else {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            mDatabase.movieDao().insert(title);
                        }
                    });
                    favorite = true;
                    button.setBackgroundTintList(pink);
                    Toast.makeText(getActivity(), getResources().getText(R.string.saved_favorites), Toast.LENGTH_SHORT).show();
                }
            }
        });


        executor.execute(new Runnable() {
            @Override
            public void run() {
                MovieTitle dbTitle = mDatabase.movieDao().getMovieById(title.id);
                if (dbTitle != null) {
                    favorite = true;
                    button.setBackgroundTintList(pink);
                } else {
                    favorite = false;
                    button.setBackgroundTintList(white);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    private void queryMovieTrailerApi() {
        String apiUri = EndpointParams.MOVIE_API_BASE_URL + title.id + EndpointParams.MOVIE_API_TRAILERS;
        final JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, apiUri + EndpointParams.API_KEY, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            String id;
                            String name;
                            trailers.clear();
                            // TODO: Loop through the array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject movie = jsonArray.getJSONObject(i);
                                id = movie.getString("key");
                                name = movie.getString("name");
                                trailers.add(new MovieTrailer(name, id));
                            }
                            mMovieTrailerAdapter.notifyDataSetChanged();

                        } catch (JSONException ex) {
                            Log.e(LOG_TAG, ex.getMessage());
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
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(mJsonObjectRequest);
    }


    private void queryMovieReviewApi() {
        String apiUri = EndpointParams.MOVIE_API_BASE_URL + title.id + EndpointParams.MOVIE_API_REVIEWS;
        final JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, apiUri + EndpointParams.API_KEY, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            String id;
                            String author;
                            String content;
                            reviews.clear();
                            // TODO: Loop through the array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject review = jsonArray.getJSONObject(i);
                                id = review.getString("id");
                                author = review.getString("author");
                                content = review.getString("content");
                                reviews.add(new MovieReview(id,author,content));
                            }
                            mMovieReviewAdapter.notifyDataSetChanged();

                        } catch (JSONException ex) {
                            Log.e(LOG_TAG, ex.getMessage());
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
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(mJsonObjectRequest);
    }

}
