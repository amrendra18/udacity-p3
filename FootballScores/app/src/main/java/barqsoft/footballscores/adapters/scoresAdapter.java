package barqsoft.footballscores.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import barqsoft.footballscores.R;
import barqsoft.footballscores.db.DatabaseContract;
import barqsoft.footballscores.utils.AppUtils;
import barqsoft.footballscores.utils.DateUtils;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ScoresAdapter extends CursorAdapter {
    public double detail_match_id = 0;

    public ScoresAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View mItem = LayoutInflater.from(context).inflate(R.layout.scores_list_item, parent, false);
        ViewHolder mHolder = new ViewHolder(mItem);
        mItem.setTag(mHolder);
        return mItem;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder mHolder = (ViewHolder) view.getTag();

        mHolder.home_name.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract
                .FixtureEntry.HOME_COL)));

        mHolder.away_name.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract
                .FixtureEntry.AWAY_COL)));
        String time = DateUtils.get12HoursTime(cursor.getString(cursor.getColumnIndex(DatabaseContract
                .FixtureEntry.TIME_COL)));
        String date = cursor.getString(cursor.getColumnIndex(DatabaseContract
                .FixtureEntry.DATE_COL));
        mHolder.date.setText(time);
        mHolder.score.setText(AppUtils.getScores(cursor.getInt(cursor.getColumnIndex(DatabaseContract
                .FixtureEntry.HOME_GOALS_COL)), cursor.getInt(cursor.getColumnIndex
                (DatabaseContract
                        .FixtureEntry.AWAY_GOALS_COL))));
        mHolder.match_id = cursor.getDouble(cursor.getColumnIndex(DatabaseContract
                .FixtureEntry._ID));
        String leagueName = AppUtils.getLeagueName(context,
                cursor.getString(cursor.getColumnIndex(DatabaseContract.FixtureEntry.LEAGUE_COL)));
        mHolder.leagueTv.setText(leagueName);

        AppUtils.setLogo(
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseContract
                        .FixtureEntry.LEAGUE_COL))),
                mHolder.home_crest,
                AppUtils.getTeamLogo(context, cursor.getString(cursor.getColumnIndex(DatabaseContract.FixtureEntry.HOME_TEAM_ID_COL))),
                context
        );

        AppUtils.setLogo(
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseContract
                        .FixtureEntry
                        .LEAGUE_COL))),
                mHolder.away_crest,
                AppUtils.getTeamLogo(context, cursor.getString(cursor.getColumnIndex(DatabaseContract.FixtureEntry.AWAY_TEAM_ID_COL))),
                context
        );
        String matchDay = context.getString(R.string.match_day, cursor.getString(cursor
                .getColumnIndex(DatabaseContract.FixtureEntry.MATCH_DAY_COL)));
        mHolder.matchDayTv.setText(matchDay);
        mHolder.statusTv.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract
                .FixtureEntry.STATUS_COL)));
        mHolder.shareButton.setOnClickListener(v -> {
            //add Share Action
            context.startActivity(createShareForecastIntent(
                    context,
                    leagueName + " \n"
                            + matchDay + " \n"
                            + date + " " + time + "\n"
                            + mHolder.home_name.getText() + " "
                            + mHolder.score.getText() + " "
                            + mHolder.away_name.getText() + " "));
        });
    }

    public Intent createShareForecastIntent(Context context, String shareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shareIntent.setType("text/plain");
        shareText = context.getString(R.string.shareit) + " \n"
                + shareText + " \n" +
                context.getString(R.string.hash_app_name);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        return shareIntent;
    }

}
