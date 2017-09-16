package app.mannit.nitin.com.popularmoviesapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by nitingeetasagardasari on 9/16/17 for the project PopularMoviesApp.
 */

@Parcel
public class Trailers {
    @SerializedName("id")
    @Expose
    public long id;
    @SerializedName("results")
    @Expose
    public ArrayList<Result> results = null;

    public long getId() {
        return id;
    }

    public ArrayList<Result> getResults() {
        return results;
    }
}
