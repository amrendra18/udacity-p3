package barqsoft.footballscores.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import barqsoft.footballscores.logger.Debug;

/**
 * Created by yehya khaled on 2/25/2015.
 */
public class ScoresProvider extends ContentProvider {

    private static ScoresDBHelper mOpenHelper;

    private static final int MATCHES = 100;
    private static final int MATCHES_WITH_LEAGUE = 101;
    private static final int MATCHES_WITH_ID = 102;
    private static final int MATCHES_WITH_DATE = 103;

    private static final int LEAGUE = 200;
    private static final int LEAGUE_WITH_ID = 201;

    private static final int TEAM = 300;
    private static final int TEAM_WITH_ID = 301;


    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.CONTENT_AUTHORITY;

        /*

        From fixture table

        /fixture/
        /fixture/leauge/18
        /fixture/id/18
        /fixture/date/2016-02-01

        From League table

        /league/
        /league/# leagueId


        From Team table

        /team/
        /team/# teamId


         */
        matcher.addURI(authority, DatabaseContract.PATH_FIXTURE, MATCHES);
        matcher.addURI(authority, DatabaseContract.PATH_FIXTURE + "/" + DatabaseContract
                        .PATH_LEAGUE + "/#",
                MATCHES_WITH_LEAGUE);
        matcher.addURI(authority, DatabaseContract.PATH_FIXTURE + "/" + DatabaseContract.PATH_ID + "/#", MATCHES_WITH_ID);
        matcher.addURI(authority, DatabaseContract.PATH_FIXTURE + "/" + DatabaseContract.PATH_DATE + "/*", MATCHES_WITH_DATE);


        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new ScoresDBHelper(getContext());
        return false;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdated;
        switch (match) {
            case MATCHES: {
                rowsUpdated = db.update(DatabaseContract.FixtureEntry.TABLE_NAME, values, selection,
                        selectionArgs);
            }
            break;
            default:
                Debug.e("ERROR : " + uri, false);
                throw new UnsupportedOperationException("Update : Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            notify(uri);
        }
        Debug.e("CP update : " + uri + " match : " + match + " updated : " + rowsUpdated, false);
        return rowsUpdated;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MATCHES:
                return DatabaseContract.FixtureEntry.CONTENT_TYPE;
            case MATCHES_WITH_LEAGUE:
                return DatabaseContract.FixtureEntry.CONTENT_TYPE;
            case MATCHES_WITH_ID:
                return DatabaseContract.FixtureEntry.CONTENT_ITEM_TYPE;
            case MATCHES_WITH_DATE:
                return DatabaseContract.FixtureEntry.CONTENT_TYPE;
            default:
                Debug.e("ERROR : " + uri, false);
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int match = sUriMatcher.match(uri);
        Debug.e("Query : " + uri + " match : " + match, false);
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor retCursor;
        switch (match) {
            case MATCHES:
                retCursor = db.query(
                        DatabaseContract.FixtureEntry.TABLE_NAME, //table name
                        projection, //projection
                        null, //selection
                        null, //selectionArgs
                        null,
                        null,
                        sortOrder
                );
                break;

            case MATCHES_WITH_DATE:
                String date = DatabaseContract.FixtureEntry.getDateFromUri(uri);
                Debug.e("uri: " + uri + " date: " + date, false);
                retCursor = db.query(
                        DatabaseContract.FixtureEntry.TABLE_NAME,
                        projection,
                        DatabaseContract.FixtureEntry.DATE_COL + " LIKE ?",
                        new String[]{date},
                        null,
                        null,
                        sortOrder
                );
                break;

            case MATCHES_WITH_ID:
                long id = DatabaseContract.FixtureEntry.getMatchIdFromUri(uri);
                Debug.e("uri: " + uri + " matchid: " + id, false);
                retCursor = db.query(
                        DatabaseContract.FixtureEntry.TABLE_NAME,
                        projection,
                        DatabaseContract.FixtureEntry.MATCH_ID + " = ?",
                        new String[]{Long.toString(id)},
                        null,
                        null,
                        sortOrder
                );
                break;

            case MATCHES_WITH_LEAGUE:
                long leagueId = DatabaseContract.FixtureEntry.getLeagueFromUri(uri);
                Debug.e("uri: " + uri + " league: " + leagueId, false);
                retCursor = db.query(
                        DatabaseContract.FixtureEntry.TABLE_NAME,
                        projection,
                        DatabaseContract.FixtureEntry.LEAGUE_COL + " = ?",
                        new String[]{Long.toString(leagueId)},
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
        notifyChange(retCursor, getContext(), uri);
        return retCursor;
    }

    private void notifyChange(Cursor retCursor, Context context, Uri uri) {
        if (retCursor != null) {
            retCursor.setNotificationUri(context.getContentResolver(), uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        Debug.e("CP insert : " + uri + " match : " + match, false);
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        switch (match) {
            case MATCHES: {
                long _id = db.insert(DatabaseContract.FixtureEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DatabaseContract.FixtureEntry.buildScoreWithMatchId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
            }
            break;
            default:
                Debug.e("ERROR : " + uri, false);
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        notify(uri);
        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final int match = sUriMatcher.match(uri);
        String tableName = null;
        switch (match) {
            case MATCHES:
                tableName = DatabaseContract.FixtureEntry.TABLE_NAME;
                break;
            default:
                break;
        }

        int inserted = 0;
        if (tableName != null) {

            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            db.beginTransaction();
            try {
                for (ContentValues value : values) {
                    long _id = db.insertWithOnConflict(tableName, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                    if (_id != -1) {
                        inserted++;
                    }
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            notify(uri);
        }
        Debug.e("[Bulk Insert] uri: " + uri + " match : " + match + " table : " + tableName + " " +
                        "inserted : " + inserted,
                false);
        return inserted;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int deleted = 0;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) {
            selection = "1";
        }
        switch (match) {
            case MATCHES:
                deleted = db.delete(DatabaseContract.FixtureEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Delete : Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (deleted != 0) {
            notify(uri);
        }
        Debug.e("CP delete : " + uri + " match : " + match + " deleted : " + deleted, false);
        return deleted;
    }


    private void notify(Uri uri) {
        try {
            getContext().getContentResolver().notifyChange(uri, null);
        } catch (NullPointerException e) {
            Debug.e(e.getMessage(), false);
        }
    }
}
