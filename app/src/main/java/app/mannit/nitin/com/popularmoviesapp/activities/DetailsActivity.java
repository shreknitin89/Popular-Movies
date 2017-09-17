package app.mannit.nitin.com.popularmoviesapp.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import app.mannit.nitin.com.popularmoviesapp.R;
import app.mannit.nitin.com.popularmoviesapp.adapters.CustomAdapter;
import app.mannit.nitin.com.popularmoviesapp.data.MovieContract;
import app.mannit.nitin.com.popularmoviesapp.models.Result;
import app.mannit.nitin.com.popularmoviesapp.models.Reviews;
import app.mannit.nitin.com.popularmoviesapp.models.Trailers;
import app.mannit.nitin.com.popularmoviesapp.util.Constants;
import app.mannit.nitin.com.popularmoviesapp.util.NetworkUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends PopularMoviesActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int MOVIE_LOADER_ID = 0;
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
    @BindView(R.id.favourite_button)
    Button favorite;

    private Result mMovieData;
    private ArrayList<Result> mTrailersData;
    private ArrayList<Result> mReviewsData;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean mButtonClick;

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
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
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

    @OnClick(R.id.favourite_button)
    void saveInFavorites() {
        mButtonClick = true;
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, DetailsActivity.this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor movieData = null;

            @Override
            protected void onStartLoading() {
                if (movieData != null) {
                    deliverResult(movieData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            MovieContract.MovieEntry._ID + "=?",
                            new String[]{mMovieData.getId()},
                            MovieContract.MovieEntry._ID);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                movieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        boolean fav = false;
        if (data != null) {
            data.moveToFirst();

            for (int i = 0; i < data.getCount(); i++) {
                data.moveToPosition(i);

                if (data.getString(data.getColumnIndex(MovieContract.MovieEntry._ID)).equals(mMovieData.getId())) {
                    favorite.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.btn_star_big_on, 0, 0, 0);
                    fav = true;
                    if (mButtonClick) {
                        favorite.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.btn_star_big_off, 0, 0, 0);
                        mButtonClick = false;
                        String stringId = mMovieData.getId();
                        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                        uri = uri.buildUpon().appendPath(stringId).build();
                        int row = getContentResolver().delete(uri, null, null);
                        if (row != 0) {
                            Toast.makeText(getBaseContext(), "Movie deleted from favorite list", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
            if (!fav) {
                favorite.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.btn_star_big_off, 0, 0, 0);
                if (mButtonClick) {
                    favorite.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.btn_star_big_on, 0, 0, 0);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieContract.MovieEntry._ID, mMovieData.getId());
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_NAME, mMovieData.getOriginalTitle());
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, mMovieData.getOverview());
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, mMovieData.getVoteAverage());
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, mMovieData.getReleaseDate());
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, mMovieData.getPosterPath());

                    Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

                    if (uri != null) {
                        Toast.makeText(getBaseContext(), "Movie added to favourite list", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
