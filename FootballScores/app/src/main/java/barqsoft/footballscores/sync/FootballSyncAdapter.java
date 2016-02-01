package barqsoft.footballscores.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import barqsoft.footballscores.R;
import barqsoft.footballscores.api.FootballApiClientService;
import barqsoft.footballscores.db.DatabaseContract;
import barqsoft.footballscores.logger.Debug;
import barqsoft.footballscores.model.Fixture;
import barqsoft.footballscores.model.League;
import barqsoft.footballscores.model.Team;
import barqsoft.footballscores.utils.AppConstants;
import barqsoft.footballscores.utils.AppUtils;
import barqsoft.footballscores.widget.FootballWidget;
import rx.Subscriber;
import rx.schedulers.Schedulers;

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
        getLeagueData();
        Intent intent = new Intent(getContext(), FootballWidget.class);
        intent.setAction(AppConstants.BROADCAST_DATA_UPDATED);
        getContext().sendBroadcast(intent);
    }

    private void getLeagueData() {
        // should be done only once
        // or like once in 15 days
        FootballApiClientService.getInstance()
                .getLeagueInfo(getContext().getString(R.string.api_key))
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<League>>() {
                    @Override
                    public final void onCompleted() {
                        Debug.c();
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Debug.e("Error: " + e.getMessage(), false);
                    }

                    @Override
                    public final void onNext(List<League> response) {
                        Debug.c();
                        List<ContentValues> list = new ArrayList<>();
                        for (int i = 0; i < response.size(); i++) {
                            League l = response.get(i);
                            if (AppUtils.leagueCovered(l.getLeagueId())) {
                                list.add(l.getContentValues());
                                fetchTeamsFromLeague(Integer.toString(l.getLeagueId()));
                            }
                        }
                        Debug.e("Leagues adding : " + response.size(), false);
                        ContentValues[] insert_data = new ContentValues[list.size()];
                        list.toArray(insert_data);
                        getContext().getContentResolver().bulkInsert(
                                DatabaseContract.LeagueEntry.CONTENT_URI, insert_data);
                    }
                });
    }

    private void getData(final String timeFrame) {
        FootballApiClientService.getInstance()
                .getFixtures(getContext().getString(R.string.api_key), timeFrame)
                .subscribeOn(Schedulers.io())
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
                    public final void onNext(Fixture.Response r) {
                        Debug.c();
                        List<Fixture> response = r.fixtures;
                        List<ContentValues> list = new ArrayList<>();
                        for (int i = 0; i < response.size(); i++) {
                            Fixture f = response.get(i);
                            if (AppUtils.leagueCovered(f.getLinks().getLeagueLink().getLeagueId())) {
                                list.add(f.getContentValues());
                            }
                        }
                        Debug.e("fixtures adding : " + response.size(), false);
                        ContentValues[] insert_data = new ContentValues[list.size()];
                        list.toArray(insert_data);
                        getContext().getContentResolver().bulkInsert(
                                DatabaseContract.FixtureEntry.CONTENT_URI, insert_data);


                        if (timeFrame.equals("p2")) {
                            String lastDate = r.timeFrameStart;
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

    public void fetchTeamsFromLeague(String leagueId) {
        FootballApiClientService.getInstance()
                .getTeamsInfoFromLeague(getContext().getString(R.string.api_key), leagueId)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Team.Response>() {
                    @Override
                    public final void onCompleted() {
                        Debug.c();
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Debug.e("Error: " + e.getMessage(), false);
                    }

                    @Override
                    public final void onNext(Team.Response r) {
                        List<Team> response = r.teams;
                        Debug.c();
                        List<ContentValues> list = new ArrayList<>();
                        for (int i = 0; i < response.size(); i++) {
                            list.add(response.get(i).getContentValues());
                        }
                        Debug.e("teams adding : " + response.size(), false);
                        ContentValues[] insert_data = new ContentValues[list.size()];
                        list.toArray(insert_data);
                        getContext().getContentResolver().bulkInsert(
                                DatabaseContract.TeamEntry.CONTENT_URI, insert_data);
                    }
                });
    }

    /*
    // Ideally we would want to do this,
    // but server cannot handle these many requests quickly in parallel

    public void fetchTeams(List<String> teams) {
        Observable.from(teams)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .distinct()
                .flatMap(link -> FootballApiClientService.getInstance()
                        .getTeamInfo(getContext().getString(R.string.api_key), AppUtils
                                .getTeamId(link)))
                .subscribe(new Subscriber<Team>() {
                    List<ContentValues> list = new ArrayList<>();

                    @Override
                    public final void onCompleted() {
                        // do nothing
                        Debug.e("totalTeam : " + list.size(), false);
                        ContentValues[] insert_data = new ContentValues[list.size()];
                        list.toArray(insert_data);
                        getContext().getContentResolver().bulkInsert(
                                DatabaseContract.TeamEntry.CONTENT_URI, insert_data);
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Debug.e("ErrorLeague : " + e.getMessage(), false);
                    }

                    @Override
                    public final void onNext(Team response) {
                        list.add(response.getContentValues());
                        Debug.e("teammm : " + response.toString(), false);
                    }
                });
    }
    */


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
