package app.mannit.nitin.com.popularmoviesapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by nitingeetasagardasari on 8/5/17 for the project PopularMoviesApp.
 */

@Parcel
public class MovieList {
    @SerializedName("page")
    @Expose
    int page;
    @SerializedName("total_results")
    @Expose
    long totalResults;
    @SerializedName("total_pages")
    @Expose
    int totalPages;
    @SerializedName("results")
    @Expose
    ArrayList<Result> results;

    public int getPage() {
        return page;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public ArrayList<Result> getResults() {
        return results;
    }
}
