package edu.udacity.kondeg.movies.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;


/**
 * Immplementation taken from https://stackoverflow.com/questions/6005245/how-to-have-a-gridview-that-adapts-its-height-when-items-are-added
 */
public class MyGridView extends GridView {
    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
