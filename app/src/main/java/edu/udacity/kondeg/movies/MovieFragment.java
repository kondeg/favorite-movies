package edu.udacity.kondeg.movies;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String LOG_TAG = MovieFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String API_KEY = "?api_key=738e339677ca170170d0bec66adf566a";
    private static final String MOVIE_IMAGE_URI = "http://image.tmdb.org/t/p/";
    private static final String MOVIE_API_URI = "http://api.themoviedb.org/3/movie/popular" ;


    private OnFragmentInteractionListener mListener;
    private GridView mGridView;
    private ArrayList<MovieTitle> movieTitles;
    private ArrayAdapter<MovieTitle> mMovieAdapter;

    private String mScreenDensity ="w185";

    public MovieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieFragment newInstance(String param1, String param2) {
        MovieFragment fragment = new MovieFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        mGridView = (GridView) view.findViewById(R.id.gridMovieTitles);

        if (movieTitles==null) {
            movieTitles = new ArrayList<>();
        }

        queryMovieDbApi();

        mMovieAdapter = new MovieAdapter(getActivity(), movieTitles);

        mGridView.setAdapter(mMovieAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Image " + position, Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(mJsonObjectRequest);
    }

}
