package app.mannit.nitin.com.popularmoviesapp.network;

import app.mannit.nitin.com.popularmoviesapp.models.MovieList;
import app.mannit.nitin.com.popularmoviesapp.models.Reviews;
import app.mannit.nitin.com.popularmoviesapp.models.Trailers;
import app.mannit.nitin.com.popularmoviesapp.util.Constants;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by nitingeetasagardasari on 8/4/17 for the project PopularMoviesApp.
 */

public interface ApiBuilder {

    @GET("/3/movie/{type}?api_key=" + Constants.API_KEY)
    Call<MovieList> getMoviesList(@Path("type") String path, @Query("page") int page);

    @GET("/3/movie/{id}/videos?api_key=" + Constants.API_KEY)
    Call<Trailers> getVideos(@Path("id") String id);

    @GET("/3/movie/{id}/reviews?api_key=" + Constants.API_KEY)
    Call<Reviews> getreviews(@Path("id") String id);
}
