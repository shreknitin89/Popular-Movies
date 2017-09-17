package app.mannit.nitin.com.popularmoviesapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Result {

    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("vote_average")
    @Expose
    double voteAverage;
    @SerializedName("poster_path")
    @Expose
    String posterPath;
    @SerializedName("original_title")
    @Expose
    String originalTitle;
    @SerializedName("overview")
    @Expose
    String overview;
    @SerializedName("release_date")
    @Expose
    String releaseDate;
    @SerializedName("content")
    @Expose
    String content;
    @SerializedName("key")
    @Expose
    String key;

    public Result() {
    }

    public Result(String id, String posterPath, String originalTitle, String overview, String releaseDate, double voteAverage) {
        this.id = id;
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    public String getId() {
        return id;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getContent() {
        return content;
    }

    public String getKey() {
        return key;
    }
}