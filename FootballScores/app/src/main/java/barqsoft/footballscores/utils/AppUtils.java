package barqsoft.footballscores.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import barqsoft.footballscores.R;
import barqsoft.footballscores.db.DatabaseContract;
import barqsoft.footballscores.logger.Debug;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class AppUtils {

    public static String getScores(int home_goals, int awaygoals) {
        if (home_goals < 0 || awaygoals < 0) {
            return " - ";
        } else {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static void setLogo(ImageView iv, String url, Context context) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .into(iv);
    }

    public static String getTeamLogo(Context context, String teamId) {
        String logoUrl = null;
        Debug.c();
        Uri uri = DatabaseContract.TeamEntry.buildTeamWithId(Integer.parseInt(teamId));
        Cursor cursor = context.getContentResolver().query(
                uri,
                DatabaseContract.TeamEntry.TEAM_PROJECTION,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {

            logoUrl = cursor.getString(
                    cursor.getColumnIndex(DatabaseContract.TeamEntry.TEAM_LOGO_COL));
            cursor.close();
        }

        Debug.e("TeamId: " + teamId + " Logo: " + logoUrl, false);
        return logoUrl;
    }

    public static String getLeagueName(Context context, String leagueId) {
        String league = "League: NA";
        Debug.c();
        Uri uri = DatabaseContract.LeagueEntry.buildLeagueWithId(Integer.parseInt(leagueId));
        Cursor cursor = context.getContentResolver().query(
                uri,
                DatabaseContract.LeagueEntry.LEAGUE_PROJECTION,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {

            league = cursor.getString(
                    cursor.getColumnIndex(DatabaseContract.LeagueEntry.LEAGUE_NAME_COL));
            cursor.close();
        }
        Debug.e("LeagueId: " + leagueId + " League: " + league, false);
        return league;
    }
}
