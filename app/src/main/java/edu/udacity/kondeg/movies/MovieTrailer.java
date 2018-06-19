package edu.udacity.kondeg.movies;

public class MovieTrailer {

    private String trailerName;

    private String trailerId;

    public MovieTrailer(String trailerName, String trailerId) {
        this.trailerName = trailerName;
        this.trailerId = trailerId;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    public String getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }
}
