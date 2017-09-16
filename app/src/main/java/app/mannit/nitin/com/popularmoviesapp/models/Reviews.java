package app.mannit.nitin.com.popularmoviesapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by nitingeetasagardasari on 9/16/17 for the project PopularMoviesApp.
 */

@Parcel
public class Reviews {
    @SerializedName("id")
    @Expose
    public long id;
    @SerializedName("page")
    @Expose
    public long page;
    @SerializedName("results")
    @Expose
    public ArrayList<Result> results = null;
    @SerializedName("total_pages")
    @Expose
    public long totalPages;
    @SerializedName("total_results")
    @Expose
    public long totalResults;

    public long getId() {
        return id;
    }

    public long getPage() {
        return page;
    }

    public ArrayList<Result> getResults() {
        return results;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public long getTotalResults() {
        return totalResults;
    }
}
