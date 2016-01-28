package barqsoft.footballscores.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yehya khaled on 2/25/2015.
 */
public class ScoresDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Scores.db";
    private static final int DATABASE_VERSION = 2;

    final String SQL_CREATE_FIXTURES_TABLE = "CREATE TABLE " + DatabaseContract.FixtureEntry.TABLE_NAME + " ("
            + DatabaseContract.FixtureEntry._ID + " INTEGER PRIMARY KEY,"
            + DatabaseContract.FixtureEntry.DATE_COL + " TEXT NOT NULL,"
            + DatabaseContract.FixtureEntry.TIME_COL + " INTEGER NOT NULL,"
            + DatabaseContract.FixtureEntry.HOME_COL + " TEXT NOT NULL,"
            + DatabaseContract.FixtureEntry.AWAY_COL + " TEXT NOT NULL,"
            + DatabaseContract.FixtureEntry.LEAGUE_COL + " INTEGER NOT NULL,"
            + DatabaseContract.FixtureEntry.HOME_GOALS_COL + " TEXT NOT NULL,"
            + DatabaseContract.FixtureEntry.AWAY_GOALS_COL + " TEXT NOT NULL,"
            + DatabaseContract.FixtureEntry.MATCH_ID + " INTEGER NOT NULL,"
            + DatabaseContract.FixtureEntry.MATCH_DAY + " INTEGER NOT NULL,"
            + " UNIQUE (" + DatabaseContract.FixtureEntry.MATCH_ID + ") ON CONFLICT REPLACE"
            + " );";

    public ScoresDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FIXTURES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Remove old values when upgrading.
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.FixtureEntry.TABLE_NAME);
        onCreate(db);
    }
}
