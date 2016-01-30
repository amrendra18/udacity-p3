package barqsoft.footballscores.app;

import android.app.Application;

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
    public void onCreate() {
        super.onCreate();
        //Stetho.initializeWithDefaults(this);
    }

}
