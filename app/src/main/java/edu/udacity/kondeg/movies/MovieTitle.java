package edu.udacity.kondeg.movies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

public class MovieTitle implements Parcelable{
    String title;
    String releaseDate;
    String id;
    String thumbnailImage;
    String overview;
    Double voteAverage;

    private List<MovieTitle> movieTitles;

    public MovieTitle(String title, String releaseDate, String id, String thumbnailImage, String overview, Double voterAverage) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.id = id;
        this.thumbnailImage = thumbnailImage;
        this.overview = overview;
        this.voteAverage = voterAverage;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(this.releaseDate);
        parcel.writeString(this.id);
        parcel.writeString(this.thumbnailImage);
        parcel.writeString(this.overview);
        parcel.writeDouble(this.voteAverage);
    }

    private MovieTitle (Parcel in) {
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.id = in.readString();
        this.thumbnailImage = in.readString();
        this.overview = in.readString();
        this.voteAverage = in.readDouble();
    }

    public final Parcelable.Creator<MovieTitle> CREATOR = new
            Parcelable.Creator<MovieTitle>() {
        @Override
        public MovieTitle createFromParcel(Parcel parcel) {
            return new MovieTitle(parcel);
        }
        @Override
        public MovieTitle[] newArray(int i) {
            return new MovieTitle[i];
        }
    };
}
