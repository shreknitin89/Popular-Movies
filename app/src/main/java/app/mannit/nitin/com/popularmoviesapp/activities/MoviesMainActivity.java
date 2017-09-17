package app.mannit.nitin.com.popularmoviesapp.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.ArrayList;

import app.mannit.nitin.com.popularmoviesapp.R;
import app.mannit.nitin.com.popularmoviesapp.adapters.MainMovieAdapter;
import app.mannit.nitin.com.popularmoviesapp.data.MovieContract;
import app.mannit.nitin.com.popularmoviesapp.models.MovieList;
import app.mannit.nitin.com.popularmoviesapp.models.Result;
import app.mannit.nitin.com.popularmoviesapp.util.Constants;
import app.mannit.nitin.com.popularmoviesapp.util.EndlessRecyclerViewScrollListener;
import app.mannit.nitin.com.popularmoviesapp.util.NetworkUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.mannit.nitin.com.popularmoviesapp.activities.DetailsActivity.MOVIE_LOADER_ID;

public class MoviesMainActivity extends PopularMoviesActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MoviesMainActivity";

    private static Parcelable mListState;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.movie_list)
    RecyclerView recyclerView;

    private GridLayoutManager mLayoutManager;
    private MainMovieAdapter mMovieAdapter;
    private ArrayList<Result> mList = new ArrayList<>();
    private EndlessRecyclerViewScrollListener mScrollListener;
    private String mPath;
    private int mPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_main);
        mUnBinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mMovieAdapter = new MainMovieAdapter(this);
        mLayoutManager = new GridLayoutManager(this, 2);
        mScrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi();
            }
        };
        recyclerView.setAdapter(mMovieAdapter);
        recyclerView.addOnScrollListener(mScrollListener);
    }

    private void loadNextDataFromApi() {
        if (mPath != null && mPage != 0) {
            establishConnectionAndMakeCall(mPath, mPage);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemViewCacheSize(100);
    }

    private void establishConnectionAndMakeCall(String path, int page) {

        switch (path) {
            case Constants.POPULAR_PATH:
                if (NetworkUtil.isOnline(this)) {
                    Call<MovieList> response = NetworkUtil.getPopularMovieList(page);

                    if (page == 1) {
                        mMovieAdapter.clear();
                        mList.clear();
                    }

                    if (response != null) {
                        getMoviesList(response);
                    }
                } else {
                    Toast.makeText(this, Constants.NO_NETWORK, Toast.LENGTH_LONG).show();
                }
                break;
            case Constants.TOP_RATED_PATH:
                if (NetworkUtil.isOnline(this)) {
                    Call<MovieList> response = NetworkUtil.getTopRatedMovieList(page);

                    if (page == 1) {
                        mMovieAdapter.clear();
                        mList.clear();
                    }

                    if (response != null) {
                        getMoviesList(response);
                    }
                } else {
                    Toast.makeText(this, Constants.NO_NETWORK, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void getMoviesList(Call<MovieList> response) {
        response.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(@NonNull Call<MovieList> call, @NonNull Response<MovieList> response) {
                MovieList list = response.body();

                if (list != null) {
                    mList.addAll(list.getResults());
                    mMovieAdapter.addList(list.getResults());
                    mPage = list.getPage() + 1 < list.getTotalPages() ? list.getPage() + 1 : 0;
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieList> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(Constants.LAYOUT_MANAGER_STATE, mListState);
        outState.putParcelable(Constants.LIST, Parcels.wrap(mList));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(Constants.LAYOUT_MANAGER_STATE);
            mList = Parcels.unwrap(savedInstanceState.getParcelable(Constants.LIST));
            mMovieAdapter.addList(mList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListState != null && mList.size() > 0) {
            mLayoutManager.onRestoreInstanceState(mListState);
            recyclerView.setAdapter(mMovieAdapter);
        } else {
            mPath = Constants.POPULAR_PATH;
            establishConnectionAndMakeCall(mPath, 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movies_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_top_rated) {
            mPath = Constants.TOP_RATED_PATH;
            establishConnectionAndMakeCall(mPath, 1);
            return true;
        } else if (id == R.id.action_popular) {
            mPath = Constants.POPULAR_PATH;
            establishConnectionAndMakeCall(mPath, 1);
            return true;
        } else if (id == R.id.action_favorite) {
            mPath = null;
            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                            null,
                            null,
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
        mMovieAdapter.clear();
        mList.clear();
        if (data != null) {
            data.moveToFirst();
            for (int i = 0; i < data.getCount(); i++) {
                data.moveToPosition(i);
                String id = data.getString(data.getColumnIndex(MovieContract.MovieEntry._ID));
                String title = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_NAME));
                String overview = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW));
                String poster = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH));
                String date = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE));
                double voteAverage = data.getDouble(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE));
                mList.add(new Result(id, poster, title, overview, date, voteAverage));
            }
            mMovieAdapter.addList(mList);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
