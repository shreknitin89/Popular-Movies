package app.mannit.nitin.com.popularmoviesapp.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

/**
 * Created by nitingeetasagardasari on 8/6/17 for the project PopularMoviesApp.
 */

public class PopularMoviesApplication extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {

    private static final String LIFE_CYCLE_TAG = "Life Cycle";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Log.i(LIFE_CYCLE_TAG, "onActivityCreated: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.i(LIFE_CYCLE_TAG, "onActivityStarted: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.i(LIFE_CYCLE_TAG, "onActivityResumed: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.i(LIFE_CYCLE_TAG, "onActivityPaused: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.i(LIFE_CYCLE_TAG, "onActivityStopped: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        Log.i(LIFE_CYCLE_TAG, "onActivitySaveInstanceState: " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.i(LIFE_CYCLE_TAG, "onActivityDestroyed: " + activity.getClass().getSimpleName());
    }
}
