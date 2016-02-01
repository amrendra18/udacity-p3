package barqsoft.footballscores.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import barqsoft.footballscores.R;
import barqsoft.footballscores.db.DatabaseContract;
import barqsoft.footballscores.logger.Debug;
import barqsoft.footballscores.model.Fixture;
import barqsoft.footballscores.utils.AppUtils;
import barqsoft.footballscores.utils.DateUtils;

/**
 * Created by Amrendra Kumar on 31/01/16.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = "WidgetDataProvider";

    List<Fixture> fixtures = null;
    Context mContext = null;
    ContentResolver mContentResolver = null;

    public WidgetDataProvider(Context context, Intent intent) {
        Debug.c();
        mContext = context;
        mContentResolver = mContext.getContentResolver();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Debug.c();
        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_list_item);
        Fixture f = fixtures.get(position);
        view.setTextViewText(R.id.home_name_widget, f.getHomeTeamName());
        view.setTextViewText(R.id.home_goal_widget, AppUtils.getScoreForWidget(f.getResult().getGoalsHomeTeam()));
        view.setTextViewText(R.id.away_name_widget, f.getAwayTeamName());
        view.setTextViewText(R.id.away_goal_widget, AppUtils.getScoreForWidget(f.getResult().getGoalsAwayTeam()));
        view.setTextViewText(R.id.status_widget, f.getStatus());
        view.setTextViewText(R.id.time_widget, DateUtils.get12HoursTime(f.getDate()));
        return view;
    }

    private void initData() {
        fixtures = new ArrayList<>();
        Debug.c();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = mFormat.format(date);
        Cursor cursor = mContentResolver.query(
                DatabaseContract.FixtureEntry.buildScoreWithDate(dateString),
                null,
                null,
                null,
                DatabaseContract.FixtureEntry.TIME_COL + " ASC"
        );
        if (cursor != null && cursor.getCount() > 0) {
            Debug.e("Fetch for widget : " + cursor.getCount(), false);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                fixtures.add(Fixture.getFixtureForWidgetFromCursor(cursor));
                cursor.moveToNext();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public void onCreate() {
        Debug.c();
    }

    @Override
    public void onDataSetChanged() {
        Debug.c();
        initData();
    }

    @Override
    public void onDestroy() {
        fixtures.clear();
    }

    @Override
    public int getCount() {
        return fixtures.size();
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


}
