package edu.udacity.kondeg.movies;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;


@Entity
public class MovieTitle implements Parcelable{
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public String id;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "releaseDate")
    public String releaseDate;
    @ColumnInfo(name = "thumnailImage")
    public String thumbnailImage;
    @ColumnInfo(name = "overview")
    public String overview;
    @ColumnInfo(name = "voteAverage")
    public Double voteAverage;

    @Ignore
    private List<MovieTitle> movieTitles;

    public MovieTitle() {
    }

    @Ignore
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

    public static final Parcelable.Creator<MovieTitle> CREATOR = new
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
