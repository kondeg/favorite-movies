package edu.udacity.kondeg.movies;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import edu.udacity.kondeg.movies.database.MovieDatabase;
import edu.udacity.kondeg.movies.util.EndpointParams;


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
    private static final String SORT_POPULAR = "popular";
    private static final String SORT_TOP = "top";
    private static final String SORT_FAVORITES = "favorites";
    private static final String LOG_TAG = MovieFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private String sortOrder = null;

    private Activity mActivity;
    private GridView mGridView;
    private ArrayList<MovieTitle> movieTitles;
    private ArrayAdapter<MovieTitle> mMovieAdapter;
    private OnChangeSortOrderListener mSortOrderListener;
    private OnFragmentInteractionListener mListener;

    private String mScreenDensity ="w185";



    public interface OnChangeSortOrderListener {
        void onSortOrderChanged(String sortOrder);
    }

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
        setRetainInstance(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            super.onCreateView(inflater,container,savedInstanceState);
            setHasOptionsMenu(true);

            View view = inflater.inflate(R.layout.fragment_movie, container, false);
            mGridView = (GridView) view.findViewById(R.id.gridMovieTitles);

            if (movieTitles == null) {
                movieTitles = new ArrayList<>();
            }

            if (savedInstanceState!=null && savedInstanceState.getParcelableArrayList(getResources().getString(R.string.movieList))!=null) {
                movieTitles = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.movieList));
                Log.d(LOG_TAG, "restore instance state fragment"+movieTitles.size());
            } else {
                Log.d(LOG_TAG, "new instance state fragment");
                queryMovieDbApi();
            }

            mMovieAdapter = new MovieAdapter(getActivity(), movieTitles);

            mGridView.setAdapter(mMovieAdapter);

            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MovieTitle movieTitle = (MovieTitle) parent.getItemAtPosition(position);
                    Intent intent = new Intent(mActivity, MovieDetailActivity.class);
                    intent.putExtra(getResources().getString(R.string.parcel_movie), movieTitle);
                    startActivity(intent);
                }
            });
        

        return view;
    }

    private void notifySortOrderChanged(String sortOrder) {
       mSortOrderListener.onSortOrderChanged(sortOrder);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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
        mActivity = (Activity) context;
        if (mActivity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) mActivity;}
        if (mActivity instanceof OnChangeSortOrderListener) {
            mSortOrderListener = (OnChangeSortOrderListener) mActivity;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_by_popularity:
                sortOrder = SORT_POPULAR;
                queryMovieDbApi();
                notifySortOrderChanged(SORT_POPULAR);
                return true;
            case R.id.show_by_rating:
                sortOrder = SORT_TOP;
                queryMovieDbApi();
                notifySortOrderChanged(SORT_TOP);
                return true;
            case R.id.show_by_favorites:
                sortOrder = SORT_FAVORITES;
                queryFavoriteDbApi();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void queryFavoriteDbApi() {
        final MovieDatabase database = MovieDatabase.getDatabase(getActivity());
        final LiveData<List<MovieTitle>> movieTitlesData = database.movieDao().getAll();
        movieTitlesData.observe(getActivity(), new Observer<List<MovieTitle>>() {
            @Override
            public void onChanged(@Nullable List<MovieTitle> movieTitleData) {
                if (movieTitles==null) {
                    movieTitles = new ArrayList<>();
                } else {
                    movieTitles.clear();
                }
                movieTitles.addAll(movieTitleData);
                mMovieAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "save instance state fragment");
        savedInstanceState.putParcelableArrayList(getResources().getString(R.string.movieList),movieTitles);
        super.onSaveInstanceState(savedInstanceState);
    }


    private void queryMovieDbApi() {
        String apiUri = EndpointParams.MOVIE_API_URI_POPULAR;
        if (sortOrder!=null && sortOrder.equals(SORT_TOP)) {
            apiUri = EndpointParams.MOVIE_API_URI_TOP;
        }
        final JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, apiUri+EndpointParams.API_KEY, null, new Response.Listener<JSONObject>() {
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
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM/dd/yyyy");
                                if (releaseDate!=null) {
                                    try {
                                        releaseDate = simpleDateFormat2.format(simpleDateFormat.parse(releaseDate));

                                    } catch (ParseException e) {
                                        Log.e(LOG_TAG, e.getMessage());
                                    }
                                }
                                thumbnail = EndpointParams.MOVIE_IMAGE_URI+mScreenDensity+poster_uri;
                                movieTitles.add(new MovieTitle(title, releaseDate, id, thumbnail, overview, rating));
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
