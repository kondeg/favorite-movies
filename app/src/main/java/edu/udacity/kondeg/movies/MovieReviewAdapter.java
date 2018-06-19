package edu.udacity.kondeg.movies;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MovieReviewAdapter extends ArrayAdapter<MovieReview> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    Activity activityRef;

    List<MovieReview> movieReviews;

    TextView textViewReviewer;


    public MovieReviewAdapter(Activity context, List<MovieReview> movieReviews) {
        super(context, 0, movieReviews);
        this.activityRef = context;
        this.movieReviews = movieReviews;
    }


    @Nullable
    @Override
    public MovieReview getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null) {
            convertView = LayoutInflater.from(activityRef).inflate(R.layout.activity_movie_review, parent, false);
            textViewReviewer = (TextView) convertView.findViewById(R.id.reviewer);
        }
        textViewReviewer.setText(getItem(position).getAuthor());
        return convertView;

    }
}
