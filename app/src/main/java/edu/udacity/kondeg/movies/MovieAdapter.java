package edu.udacity.kondeg.movies;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieAdapter extends ArrayAdapter<MovieTitle> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    Activity activityRef;
    List<MovieTitle> movieTitles;
    ImageView imageView;

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param movieTitles    A List of MovieTitle objects to display in a list
     */
    public MovieAdapter(Activity context, List<MovieTitle> movieTitles) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, movieTitles);
        this.activityRef = context;
        this.movieTitles = movieTitles;
    }

    @Nullable
    @Override
    public MovieTitle getItem(int position) {
        return super.getItem(position);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null) {
            convertView = LayoutInflater.from(activityRef).inflate(R.layout.image_view, parent, false);
            imageView = (ImageView) convertView.findViewById(R.id.movieImage);
            convertView.setTag(imageView);
        } else {
            imageView = (ImageView) convertView.getTag();
        }
        Picasso.with(activityRef).load(movieTitles.get(position).thumbnailImage).into(imageView);
        return convertView;

    }
}
