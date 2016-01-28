package barqsoft.footballscores.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

import barqsoft.footballscores.R;
import barqsoft.footballscores.api.FootballApiClientService;
import barqsoft.footballscores.db.DatabaseContract;
import barqsoft.footballscores.logger.Debug;
import barqsoft.footballscores.model.Fixture;
import barqsoft.footballscores.utils.AppUtils;
import rx.Subscriber;

/**
 * Created by yehya khaled on 3/2/2015.
 */
public class MyFetchService extends IntentService {

    public MyFetchService() {
        super("MyFetchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        getData("n2");
        getData("p2");
    }

    private void getData(String timeFrame) {
        FootballApiClientService.getInstance().getFixtures(getString(R.string.api_key), timeFrame)
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
                        getApplication().getContentResolver().bulkInsert(
                                DatabaseContract.FixtureEntry.CONTENT_URI, values);
                    }
                });
    }
}

