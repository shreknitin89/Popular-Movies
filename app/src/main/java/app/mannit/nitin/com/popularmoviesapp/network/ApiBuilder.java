package app.mannit.nitin.com.popularmoviesapp.network;

import app.mannit.nitin.com.popularmoviesapp.models.MovieList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by nitingeetasagardasari on 8/4/17 for the project PopularMoviesApp.
 */

public interface ApiBuilder {

    @GET("/3/movie/{type}")
    Call<MovieList> getMoviesList(@Path("type") String path, @Query("api_key") String apiKey);
}
