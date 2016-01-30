package barqsoft.footballscores.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by yehya khaled on 2/25/2015.
 */
public class DatabaseContract {

    //URI data
    public static final String CONTENT_AUTHORITY = "barqsoft.footballscores";
    public static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FIXTURE = "fixture";
    public static final String PATH_LEAGUE = "league";
    public static final String PATH_ID = "id";
    public static final String PATH_DATE = "date";
    public static final String PATH_TEAM = "team";

    public static final class LeagueEntry implements BaseColumns {
        public static final String TABLE_NAME = "leagues";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LEAGUE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LEAGUE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LEAGUE;

        public static final String LEAGUE_ID_COL = "id";
        public static final String LEAGUE_NAME_COL = "name";

        public static Uri buildLeagueWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getLeagueIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }
    }

    public static final class TeamEntry implements BaseColumns {
        public static final String TABLE_NAME = "teams";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TEAM).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TEAM;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TEAM;

        public static final String TEAM_ID_COL = "id";
        public static final String TEAM_NAME_COL = "name";
        public static final String TEAM_SHORT_NAME_COL = "short";
        public static final String TEAM_CODE_COL = "code";
        public static final String TEAM_LOGO_COL = "logo";

        public static Uri buildTeamWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getTeamIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }
    }

    public static final class FixtureEntry implements BaseColumns {
        public static final String TABLE_NAME = "fixtures";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FIXTURE)
                .build();
        public static final Uri CONTENT_LEAGUE_URI = CONTENT_URI.buildUpon().appendPath
                (PATH_LEAGUE)
                .build();
        public static final Uri CONTENT_MATCH_ID_URI = CONTENT_URI.buildUpon().appendPath
                (PATH_ID)
                .build();
        public static final Uri CONTENT_DATE_URI = CONTENT_URI.buildUpon().appendPath
                (PATH_DATE)
                .build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FIXTURE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FIXTURE;

        public static final String LEAGUE_COL = "league";
        public static final String DATE_COL = "date";
        public static final String TIME_COL = "time";
        public static final String HOME_COL = "home";
        public static final String AWAY_COL = "away";
        public static final String HOME_GOALS_COL = "home_goals";
        public static final String AWAY_GOALS_COL = "away_goals";
        public static final String MATCH_ID = "match_id";
        public static final String MATCH_DAY_COL = "match_day";
        public static final String STATUS_COL = "status";

        public static Uri buildScoreWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

        public static Uri buildScoreWithLeague(long id) {
            return ContentUris.withAppendedId(CONTENT_LEAGUE_URI, id);
        }

        public static Uri buildScoreWithMatchId(long id) {
            return ContentUris.withAppendedId(CONTENT_MATCH_ID_URI, id);
        }

        public static Uri buildScoreWithDate(String date) {
            return Uri.withAppendedPath(CONTENT_DATE_URI, date);
        }

        public static long getLeagueFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

        public static long getMatchIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

        public static String getDateFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }

        public static final String[] FIXTURE_PROJECTION = new String[]{
                _ID,
                LEAGUE_COL,
                DATE_COL,
                TIME_COL,
                HOME_COL,
                AWAY_COL,
                HOME_GOALS_COL,
                AWAY_GOALS_COL,
                MATCH_ID,
                MATCH_DAY_COL,
                STATUS_COL
        };
    }
}
