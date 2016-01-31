package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import barqsoft.footballscores.R;
import barqsoft.footballscores.logger.Debug;

/**
 * Created by Amrendra Kumar on 31/01/16.
 * <p>
 * <p>
 * Code taken from
 * http://developer.android.com/guide/topics/appwidgets/index.html
 */
public class FootballWidget extends AppWidgetProvider {

    public FootballWidget() {

    }

    public static void onUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        Intent intent = new Intent(context, WidgetService.class);
        // Set up the collection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            views.setRemoteAdapter(R.id.score_list_widget, intent);
        } else {
            views.setRemoteAdapter(appWidgetId, R.id.score_list_widget, intent);
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
                /*
        This is called to update the App Widget at intervals
        defined by the updatePeriodMillis attribute in the AppWidgetProviderInfo
        (see Adding the AppWidgetProviderInfo Metadata above).
        This method is also called when the user adds the App Widget,
        so it should perform the essential setup,
         */

        Debug.c();
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int appWidgetId : appWidgetIds) {
            onUpdate(context, appWidgetManager, appWidgetId);
        }
        //super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        /*
        This is called when the widget is first placed and any time the widget is resized.
        You can use this callback to show or hide content based on the widget's size ranges
         */
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        Debug.c();
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        //This is called every time an App Widget is deleted from the App Widget host.
        Debug.c();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        /*
        This is called for every broadcast and before each of the above callback methods.
        You normally don't need to implement this method because the default AppWidgetProvider
        implementation filters all App Widget broadcasts and calls the above methods as appropriate.
         */

        Debug.c();
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        /*
        This is called when the last instance of your App Widget
        is deleted from the App Widget host.
        This is where you should clean up any work done in onEnabled(Context),
        such as delete a temporary database.
         */
        Debug.c();
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        /*
        This is called when an instance the App Widget is created for the first time.
        For example, if the user adds two instances of your App Widget,
        this is only called the first time.
        If you need to open a new database or perform other setup that only needs to occur once
        for all App Widget instances, then this is a good place to do it.
         */
        Debug.c();
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
        Debug.c();
    }

}
