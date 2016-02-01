package barqsoft.footballscores.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import barqsoft.footballscores.R;
import barqsoft.footballscores.app.activity.MainActivity;
import barqsoft.footballscores.logger.Debug;
import barqsoft.footballscores.utils.AppConstants;

/**
 * Created by Amrendra Kumar on 31/01/16.
 * <p>
 * <p>
 * Code attribution
 * http://developer.android.com/guide/topics/appwidgets/index.html
 */
public class FootballWidget extends AppWidgetProvider {

    public FootballWidget() {

    }

    public static void onUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_title_bar, pendingIntent);
        views.setRemoteAdapter(R.id.score_list_widget, new Intent(context, WidgetService.class));
        views.setEmptyView(R.id.score_list_widget, R.id.score_list_widget_empty);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            onUpdate(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent != null) {
            Debug.c();
            if (intent.getAction().equals(AppConstants.BROADCAST_DATA_UPDATED)) {
                Debug.e("Need to update widget", false);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.score_list_widget);
            }
        }
    }
}
