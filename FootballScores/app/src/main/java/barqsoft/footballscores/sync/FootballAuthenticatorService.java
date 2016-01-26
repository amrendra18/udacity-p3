package barqsoft.footballscores.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Amrendra Kumar on 27/01/16.
 */
public class FootballAuthenticatorService extends Service {

    private FootballAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new FootballAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
