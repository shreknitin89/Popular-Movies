package app.mannit.nitin.com.popularmoviesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import app.mannit.nitin.com.popularmoviesapp.R;
import app.mannit.nitin.com.popularmoviesapp.adapters.CustomAdapter;
import app.mannit.nitin.com.popularmoviesapp.models.Result;
import app.mannit.nitin.com.popularmoviesapp.models.Reviews;
import app.mannit.nitin.com.popularmoviesapp.models.Trailers;
import app.mannit.nitin.com.popularmoviesapp.util.Constants;
import app.mannit.nitin.com.popularmoviesapp.util.NetworkUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends PopularMoviesActivity {
    private static final String TAG = "DetailsActivity";

    @BindView(R.id.movie_title)
    TextView title;
    @BindView(R.id.poster_thumbnail)
    ImageView poster;
    @BindView(R.id.release)
    TextView releasedYear;
    @BindView(R.id.duration)
    TextView movieDuration;
    @BindView(R.id.rating)
    TextView userRating;
    @BindView(R.id.description)
    TextView synopsis;
    @BindView(R.id.trailers_reviews)
    RecyclerView recyclerView;

    private Result mMovieData;
    private ArrayList<Result> mTrailersData;
    private ArrayList<Result> mReviewsData;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mUnBinder = ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            mMovieData = Parcels.unwrap(intent.getParcelableExtra(Constants.MOVIE_DATA));
        }
        mLinearLayoutManager = new LinearLayoutManager(this);
    }

    private void setMovieData() {
        if (mMovieData != null) {

            title.setText(mMovieData.getOriginalTitle());
            releasedYear.setText(mMovieData.getReleaseDate());
            userRating.setText(String.valueOf(mMovieData.getVoteAverage()));
            synopsis.setText(mMovieData.getOverview());
            Picasso.with(this)
                    .load(Constants.IMAGE_URL + mMovieData.getPosterPath())
                    .placeholder(R.drawable.poster)
                    .error(R.drawable.poster)
                    .fit()
                    .into(poster);
        }
    }

    private void loadMovieData() {
        if (NetworkUtil.isOnline(this)) {
            Call<Trailers> trailers = NetworkUtil.getTrailers(mMovieData.getId());
            trailers.enqueue(new Callback<Trailers>() {
                @Override
                public void onResponse(@NonNull Call<Trailers> call, @NonNull Response<Trailers> response) {
                    Trailers trailersData = response.body();
                    if (trailersData != null) {
                        mTrailersData = trailersData.getResults();
                        Call<Reviews> reviews = NetworkUtil.getReviews(mMovieData.getId());
                        reviews.enqueue(new Callback<Reviews>() {
                            @Override
                            public void onResponse(@NonNull Call<Reviews> call, @NonNull Response<Reviews> response) {
                                Reviews reviewData = response.body();
                                if (reviewData != null) {
                                    mReviewsData = reviewData.getResults();
                                    if (mTrailersData != null && mReviewsData != null && mTrailersData.size() > 0 && mReviewsData.size() > 0) {
                                        setAdapter();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Reviews> call, @NonNull Throwable t) {
                                Log.e(TAG, "onFailure: " + t.getMessage(), t);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Trailers> call, @NonNull Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage(), t);
                }
            });
        } else {
            Toast.makeText(this, Constants.NO_NETWORK, Toast.LENGTH_LONG).show();
        }
    }

    private void setAdapter() {
        CustomAdapter customAdapter = new CustomAdapter(DetailsActivity.this, mTrailersData, mReviewsData);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(DetailsActivity.this, mLinearLayoutManager.getOrientation()));
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(mLinearLayoutManager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.MOVIE_DATA, Parcels.wrap(mMovieData));
        outState.putParcelable(Constants.TRAILERS_LIST, Parcels.wrap(mTrailersData));
        outState.putParcelable(Constants.REVIEWS_LIST, Parcels.wrap(mReviewsData));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mMovieData = Parcels.unwrap(savedInstanceState.getParcelable(Constants.MOVIE_DATA));
            mTrailersData = Parcels.unwrap(savedInstanceState.getParcelable(Constants.TRAILERS_LIST));
            mReviewsData = Parcels.unwrap(savedInstanceState.getParcelable(Constants.REVIEWS_LIST));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTrailersData == null && mReviewsData == null) {
            setMovieData();
            loadMovieData();
        } else {
            setMovieData();
            setAdapter();
        }
    }
}
