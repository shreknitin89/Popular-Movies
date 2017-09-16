package app.mannit.nitin.com.popularmoviesapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Result {

    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("video")
    @Expose
    boolean video;
    @SerializedName("vote_average")
    @Expose
    double voteAverage;
    @SerializedName("title")
    @Expose
    String title;
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
    @SerializedName("author")
    @Expose
    public String author;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("url")
    @Expose
    public String url;

    public String getId() {
        return id;
    }

    public boolean hasVideo() {
        return video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
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

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}