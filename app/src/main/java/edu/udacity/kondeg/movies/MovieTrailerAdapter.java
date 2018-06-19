package edu.udacity.kondeg.movies;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieTrailerAdapter extends ArrayAdapter<MovieTrailer> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    Activity activityRef;

    List<MovieTrailer> movieTrailers;

    TextView textView;


    public MovieTrailerAdapter(Activity context, List<MovieTrailer> movieTrailers) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, movieTrailers);
        this.activityRef = context;
        this.movieTrailers = movieTrailers;
    }


    @Nullable
    @Override
    public MovieTrailer getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null) {
            convertView = LayoutInflater.from(activityRef).inflate(R.layout.activity_movie_trailer, parent, false);
            textView = (TextView) convertView.findViewById(R.id.trailer_name);
            convertView.setTag(textView);
        }
        textView.setText(getItem(position).getTrailerName());
        return convertView;

    }
}
