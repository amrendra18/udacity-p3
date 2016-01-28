package barqsoft.footballscores.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;

import barqsoft.footballscores.R;
import barqsoft.footballscores.api.FootballApiClientService;
import barqsoft.footballscores.db.DatabaseContract;
import barqsoft.footballscores.logger.Debug;
import barqsoft.footballscores.model.Fixture;
import barqsoft.footballscores.utils.AppUtils;
import rx.Subscriber;

/**
 * Created by Amrendra Kumar on 27/01/16.
 */
public class FootballSyncAdapter extends AbstractThreadedSyncAdapter {
    public static final int SYNC_INTERVAL = 2 * 60 * 60;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

    public FootballSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    /**
     * Perform a sync for this account. SyncAdapter-specific parameters may
     * be specified in extras, which is guaranteed to not be null. Invocations
     * of this method are guaranteed to be serialized.
     *
     * @param account    the account that should be synced
     * @param extras     SyncAdapter-specific parameters
     * @param authority  the authority of this sync request
     * @param provider   a ContentProviderClient that points to the ContentProvider for this
     *                   authority
     * @param syncResult SyncAdapter-specific parameters
     */

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Debug.e("Sync is being performed", false);
        getData("n3");
        getData("p2");
    }

    private void getData(final String timeFrame) {
        FootballApiClientService.getInstance().getFixtures(getContext().getString(R.string
                        .api_key),
                timeFrame)
                .subscribe(new Subscriber<Fixture.Response>() {
                    @Override
                    public final void onCompleted() {
                        Debug.c();
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Debug.e("Error: " + e.getMessage(), false);
                    }

                    @Override
                    public final void onNext(Fixture.Response response) {
                        Debug.c();
                        int num = response.fixtures.size();
                        ContentValues values[] = new ContentValues[num];
                        int i = 0;
                        for (Fixture f : response.fixtures) {
                            ContentValues value = new ContentValues();
                            value.put(DatabaseContract.FixtureEntry.MATCH_ID,
                                    f.getLinks().getMatch().getMatchId());
                            value.put(DatabaseContract.FixtureEntry.DATE_COL,
                                    AppUtils.getDateFromDateTime(f.getDate()));
                            value.put(DatabaseContract.FixtureEntry.TIME_COL,
                                    AppUtils.getTimeFromDateTime(f.getDate()));
                            value.put(DatabaseContract.FixtureEntry.HOME_COL, f.getHomeTeamName());
                            value.put(DatabaseContract.FixtureEntry.AWAY_COL, f.getAwayTeamName());
                            value.put(DatabaseContract.FixtureEntry.HOME_GOALS_COL,
                                    f.getResult().getGoalsHomeTeam());
                            value.put(DatabaseContract.FixtureEntry.AWAY_GOALS_COL,
                                    f.getResult().getGoalsAwayTeam());
                            value.put(DatabaseContract.FixtureEntry.LEAGUE_COL,
                                    f.getLinks().getLeague().getLeagueId());
                            value.put(DatabaseContract.FixtureEntry.MATCH_DAY, f.getMatchDay());
                            values[i] = value;
                            i++;
                        }
                        getContext().getContentResolver().bulkInsert(
                                DatabaseContract.FixtureEntry.CONTENT_URI, values);


                        if (timeFrame.equals("p2")) {
                            String lastDate = response.timeFrameStart;
                            Debug.e("deleting before : " + lastDate, false);
                            getContext().getContentResolver().delete(
                                    DatabaseContract.FixtureEntry.CONTENT_URI,
                                    DatabaseContract.FixtureEntry.DATE_COL + " < ?",
                                    new String[]{lastDate}
                            );
                        }
                    }
                });
    }


    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.contentauthority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.contentauthority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        FootballSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string
                .contentauthority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }


}
