package barqsoft.footballscores.app;

import android.app.Application;

import barqsoft.footballscores.utils.svg.SvgImageLoader;

/**
 * Created by Amrendra Kumar on 27/01/16.
 */
public class FootballApp extends Application {
    private static FootballApp mApplication = null;

    // should use static getInstance() method instead
    public FootballApp() {
        mApplication = this;
    }

    public static FootballApp getInstance() {
        if (mApplication == null) {
            mApplication = new FootballApp();
        }
        return mApplication;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        SvgImageLoader.clearCache(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*
        Stetho doesnot work in debug mode, while running unit test cases with Robolectric.
        Need to check why?
         */
        //Stetho.initializeWithDefaults(this);
    }

}
