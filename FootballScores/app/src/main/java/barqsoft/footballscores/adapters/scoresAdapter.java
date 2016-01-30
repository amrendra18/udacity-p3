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
        //Log.v(FetchScoreTask.LOG_TAG,"new View inflated");
        return mItem;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder mHolder = (ViewHolder) view.getTag();

        mHolder.home_name.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract
                .FixtureEntry.HOME_COL)));

        mHolder.away_name.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract
                .FixtureEntry.AWAY_COL)));
        mHolder.date.setText(DateUtils.get12HoursTime(cursor.getString(cursor.getColumnIndex(DatabaseContract
                .FixtureEntry.TIME_COL))));
        mHolder.score.setText(AppUtils.getScores(cursor.getInt(cursor.getColumnIndex(DatabaseContract
                .FixtureEntry.HOME_GOALS_COL)), cursor.getInt(cursor.getColumnIndex
                (DatabaseContract
                        .FixtureEntry.AWAY_GOALS_COL))));
        mHolder.match_id = cursor.getDouble(cursor.getColumnIndex(DatabaseContract
                .FixtureEntry._ID));
        mHolder.leaugeTv.setText(
                AppUtils.getLeagueName(context,
                        cursor.getString(cursor.getColumnIndex(DatabaseContract.FixtureEntry.LEAGUE_COL)))
        );
/*        mHolder.home_crest.setImageResource(AppUtils.getTeamCrestByTeamName(cursor.getString(COL_HOME)));
        mHolder.away_crest.setImageResource(AppUtils.getTeamCrestByTeamName(cursor.getString(COL_AWAY)));*/
        mHolder.matchDayTv.setText(context.getString(R.string.match_day, cursor.getString(cursor
                .getColumnIndex(DatabaseContract.FixtureEntry.MATCH_DAY_COL))));
        mHolder.statusTv.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract
                .FixtureEntry.STATUS_COL)));
        LayoutInflater vi = (LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.detail_fragment, null);
/*        ViewGroup container = (ViewGroup) view.findViewById(R.id.details_fragment_container);
        if (mHolder.match_id == detail_match_id) {
            //Log.v(FetchScoreTask.LOG_TAG,"will insert extraView");

            container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT));
            TextView match_day = (TextView) v.findViewById(R.id.matchday_textview);
            match_day.setText(AppUtils.getMatchDay(cursor.getInt(COL_MATCHDAY),
                    cursor.getInt(COL_LEAGUE)));
            TextView league = (TextView) v.findViewById(R.id.league_textview);
            league.setText(AppUtils.getLeagueLink(cursor.getInt(COL_LEAGUE)));
            Button share_button = (Button) v.findViewById(R.id.share_button);
            share_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add Share Action
                    context.startActivity(createShareForecastIntent(mHolder.home_name.getText() + " "
                            + mHolder.score.getText() + " " + mHolder.away_name.getText() + " "));
                }
            });
        } else {
            container.removeAllViews();
        }*/

    }

    public Intent createShareForecastIntent(Context context, String ShareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + " #" + context.getString(R.string
                .app_name));
        return shareIntent;
    }

}
