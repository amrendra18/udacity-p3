package barqsoft.footballscores.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Amrendra Kumar on 27/01/16.
 */
public class FootballSyncService extends Service {

    private static final Object mSyncAdapterLock = new Object();
    private static FootballSyncAdapter mFootballSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (mSyncAdapterLock) {
            if (mFootballSyncAdapter == null) {
                mFootballSyncAdapter = new FootballSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mFootballSyncAdapter.getSyncAdapterBinder();
    }
}
