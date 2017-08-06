package app.mannit.nitin.com.popularmoviesapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import app.mannit.nitin.com.popularmoviesapp.models.MovieList;
import app.mannit.nitin.com.popularmoviesapp.network.ApiBuilder;
import app.mannit.nitin.com.popularmoviesapp.network.ServiceGenerator;
import retrofit2.Call;

import static app.mannit.nitin.com.popularmoviesapp.util.Constants.API_KEY;
import static app.mannit.nitin.com.popularmoviesapp.util.Constants.BASE_URL;
import static app.mannit.nitin.com.popularmoviesapp.util.Constants.POPULAR_PATH;
import static app.mannit.nitin.com.popularmoviesapp.util.Constants.TOP_RATED_PATH;

/**
 * Created by nitingeetasagardasari on 8/4/17 for the project PopularMoviesApp.
 */

public class NetworkUtil {

    public static Call<MovieList> getPopularMovieList() {
        return ServiceGenerator.createService(ApiBuilder.class, BASE_URL).getMoviesList(POPULAR_PATH, API_KEY);
    }

    public static Call<MovieList> getTopRatedMovieList() {
        return ServiceGenerator.createService(ApiBuilder.class, BASE_URL).getMoviesList(TOP_RATED_PATH, API_KEY);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
