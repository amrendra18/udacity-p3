package it.jaschke.alexandria;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Amrendra Kumar on 07/01/16.
 */
public class AlexandriaApp extends Application {
    private static AlexandriaApp mApplication = null;

    // should use static getInstance() method instead
    public AlexandriaApp() {
        mApplication = this;
    }

    public static AlexandriaApp getInstance() {
        if (mApplication == null) {
            mApplication = new AlexandriaApp();
        }
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
