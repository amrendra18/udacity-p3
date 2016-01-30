package barqsoft.footballscores.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yehya khaled on 2/25/2015.
 */
public class ScoresDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "football.db";
    private static final int DATABASE_VERSION = 4;

    final String SQL_CREATE_FIXTURES_TABLE = "CREATE TABLE " + DatabaseContract.FixtureEntry.TABLE_NAME + " ("
            + DatabaseContract.FixtureEntry._ID + " INTEGER PRIMARY KEY,"
            + DatabaseContract.FixtureEntry.DATE_COL + " TEXT NOT NULL,"
            + DatabaseContract.FixtureEntry.TIME_COL + " INTEGER NOT NULL,"
            + DatabaseContract.FixtureEntry.HOME_COL + " TEXT NOT NULL,"
            + DatabaseContract.FixtureEntry.AWAY_COL + " TEXT NOT NULL,"
            + DatabaseContract.FixtureEntry.LEAGUE_COL + " INTEGER NOT NULL,"
            + DatabaseContract.FixtureEntry.HOME_GOALS_COL + " TEXT NOT NULL,"
            + DatabaseContract.FixtureEntry.AWAY_GOALS_COL + " TEXT NOT NULL,"
            + DatabaseContract.FixtureEntry.STATUS_COL + " TEXT NOT NULL,"
            + DatabaseContract.FixtureEntry.MATCH_ID + " INTEGER NOT NULL,"
            + DatabaseContract.FixtureEntry.MATCH_DAY_COL + " INTEGER NOT NULL,"
            + " UNIQUE (" + DatabaseContract.FixtureEntry.MATCH_ID + ") ON CONFLICT REPLACE"
            + " );";

    final String SQL_CREATE_LEAGUE_TABLE = "CREATE TABLE " + DatabaseContract.LeagueEntry
            .TABLE_NAME + " ("
            + DatabaseContract.LeagueEntry._ID + " INTEGER PRIMARY KEY,"
            + DatabaseContract.LeagueEntry.LEAGUE_ID_COL + " INTEGER NOT NULL,"
            + DatabaseContract.LeagueEntry.LEAGUE_NAME_COL + " TEXT NOT NULL,"
            + " UNIQUE (" + DatabaseContract.LeagueEntry.LEAGUE_ID_COL + ") ON CONFLICT REPLACE"
            + " );";

    final String SQL_CREATE_TEAM_TABLE = "CREATE TABLE " + DatabaseContract.TeamEntry
            .TABLE_NAME + " ("
            + DatabaseContract.TeamEntry._ID + " INTEGER PRIMARY KEY,"
            + DatabaseContract.TeamEntry.TEAM_ID_COL + " INTEGER NOT NULL,"
            + DatabaseContract.TeamEntry.TEAM_NAME_COL + " TEXT NOT NULL,"
            + DatabaseContract.TeamEntry.TEAM_SHORT_NAME_COL + " TEXT NOT NULL,"
            + DatabaseContract.TeamEntry.TEAM_CODE_COL + " TEXT NOT NULL,"
            + DatabaseContract.TeamEntry.TEAM_LOGO_COL + " TEXT NOT NULL,"
            + " UNIQUE (" + DatabaseContract.TeamEntry.TEAM_ID_COL + ") ON CONFLICT REPLACE"
            + " );";

    public ScoresDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FIXTURES_TABLE);
        db.execSQL(SQL_CREATE_LEAGUE_TABLE);
        db.execSQL(SQL_CREATE_TEAM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Remove old values when upgrading.
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.FixtureEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.LeagueEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TeamEntry.TABLE_NAME);
        onCreate(db);
    }
}
