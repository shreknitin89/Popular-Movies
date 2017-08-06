package app.mannit.nitin.com.popularmoviesapp.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
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

import java.util.ArrayList;

import app.mannit.nitin.com.popularmoviesapp.R;
import app.mannit.nitin.com.popularmoviesapp.adapters.MainMovieAdapter;
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

public class MoviesMainActivity extends PopularMoviesActivity {

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
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListState != null && mList.size() > 0) {
            mLayoutManager.onRestoreInstanceState(mListState);
            mMovieAdapter.addList(mList);
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
        }
        return super.onOptionsItemSelected(item);
    }
}
