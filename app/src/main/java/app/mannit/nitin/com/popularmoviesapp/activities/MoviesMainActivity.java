package app.mannit.nitin.com.popularmoviesapp.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.parceler.Parcels;

import app.mannit.nitin.com.popularmoviesapp.R;
import app.mannit.nitin.com.popularmoviesapp.adapters.MainMovieAdapter;
import app.mannit.nitin.com.popularmoviesapp.models.MovieList;
import app.mannit.nitin.com.popularmoviesapp.util.Constants;
import app.mannit.nitin.com.popularmoviesapp.util.NetworkUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesMainActivity extends PopularMoviesActivity {

    private static final String TAG = "MoviesMainActivity";

    private static Parcelable mListState;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.movie_list)
    RecyclerView recyclerView;

    private LinearLayoutManager mLayoutManager;
    private MainMovieAdapter mMovieAdapter;
    private MovieList mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_main);
        mUnBinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mLayoutManager = new GridLayoutManager(this, 2);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemViewCacheSize(20);
    }

    private void establishConnectionAndMakeCall(String path) {

        switch (path) {
            case Constants.POPULAR_PATH:
                if (NetworkUtil.isOnline(this)) {
                    Call<MovieList> response = NetworkUtil.getPopularMovieList();

                    if (response != null) {
                        getMoviesList(response);
                    }
                } else {
                    Toast.makeText(this, Constants.NO_NETWORK, Toast.LENGTH_LONG).show();
                }
                break;
            case Constants.TOP_RATED_PATH:
                if (NetworkUtil.isOnline(this)) {
                    Call<MovieList> response = NetworkUtil.getTopRatedMovieList();

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
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                mList = response.body();
                if (mList != null)
                    mMovieAdapter = new MainMovieAdapter(mList.getResults(), MoviesMainActivity.this);

                recyclerView.setAdapter(mMovieAdapter);

            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
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
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListState != null && mList != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
            mMovieAdapter = new MainMovieAdapter(mList.getResults(), MoviesMainActivity.this);
            recyclerView.setAdapter(mMovieAdapter);
        } else {
            establishConnectionAndMakeCall(Constants.POPULAR_PATH);
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
            establishConnectionAndMakeCall(Constants.TOP_RATED_PATH);
            return true;
        } else if (id == R.id.action_popular) {
            establishConnectionAndMakeCall(Constants.POPULAR_PATH);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
