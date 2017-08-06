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

    @SerializedName("results")
    @Expose
    ArrayList<Result> results;

    public ArrayList<Result> getResults() {
        return results;
    }
}
