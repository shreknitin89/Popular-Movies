package app.mannit.nitin.com.popularmoviesapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.Unbinder;

/**
 * Created by nitingeetasagardasari on 8/5/17 for the project PopularMoviesApp.
 */

public class PopularMoviesActivity extends AppCompatActivity {
    private static final String TAG = "PopularMoviesActivity";
    protected Unbinder mUnBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null)
            mUnBinder.unbind();
    }
}
