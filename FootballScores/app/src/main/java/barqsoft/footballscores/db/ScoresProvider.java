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

    private static final int FIXTURE = 100;
    private static final int FIXTURE_WITH_LEAGUE_ID = 101;
    private static final int FIXTURE_WITH_ID = 102;
    private static final int FIXTURE_WITH_DATE = 103;

    private static final int LEAGUE = 200;
    private static final int LEAGUE_WITH_ID = 201;

    private static final int TEAM = 300;
    private static final int TEAM_WITH_ID = 301;


    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.CONTENT_AUTHORITY;

        // Fixture Table
        matcher.addURI(authority, DatabaseContract.PATH_FIXTURE, FIXTURE);

        matcher.addURI(authority,
                DatabaseContract.PATH_FIXTURE + "/" + DatabaseContract.PATH_LEAGUE + "/#",
                FIXTURE_WITH_LEAGUE_ID);

        matcher.addURI(authority,
                DatabaseContract.PATH_FIXTURE + "/" + DatabaseContract.PATH_ID + "/#", FIXTURE_WITH_ID);

        matcher.addURI(authority,
                DatabaseContract.PATH_FIXTURE + "/" + DatabaseContract.PATH_DATE + "/*", FIXTURE_WITH_DATE);

        // League Table
        matcher.addURI(authority, DatabaseContract.PATH_LEAGUE, LEAGUE);
        matcher.addURI(authority, DatabaseContract.PATH_LEAGUE + "/#", LEAGUE_WITH_ID);

        // Team table
        matcher.addURI(authority, DatabaseContract.PATH_TEAM, TEAM);
        matcher.addURI(authority, DatabaseContract.PATH_TEAM + "/#", TEAM_WITH_ID);


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
            case FIXTURE: {
                rowsUpdated = db.update(DatabaseContract.FixtureEntry.TABLE_NAME, values, selection,
                        selectionArgs);
            }
            case LEAGUE: {
                rowsUpdated = db.update(DatabaseContract.LeagueEntry.TABLE_NAME, values, selection,
                        selectionArgs);
            }
            case TEAM: {
                rowsUpdated = db.update(DatabaseContract.TeamEntry.TABLE_NAME, values, selection,
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
            case FIXTURE:
                return DatabaseContract.FixtureEntry.CONTENT_TYPE;
            case FIXTURE_WITH_LEAGUE_ID:
                return DatabaseContract.FixtureEntry.CONTENT_TYPE;
            case FIXTURE_WITH_ID:
                return DatabaseContract.FixtureEntry.CONTENT_ITEM_TYPE;
            case FIXTURE_WITH_DATE:
                return DatabaseContract.FixtureEntry.CONTENT_TYPE;
            case LEAGUE:
                return DatabaseContract.LeagueEntry.CONTENT_TYPE;
            case LEAGUE_WITH_ID:
                return DatabaseContract.LeagueEntry.CONTENT_ITEM_TYPE;
            case TEAM:
                return DatabaseContract.TeamEntry.CONTENT_TYPE;
            case TEAM_WITH_ID:
                return DatabaseContract.TeamEntry.CONTENT_ITEM_TYPE;
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
            case FIXTURE: {
                retCursor = db.query(
                        DatabaseContract.FixtureEntry.TABLE_NAME, //table name
                        projection, //projection
                        null, //selection
                        null, //selectionArgs
                        null,
                        null,
                        sortOrder
                );
            }
            break;

            case FIXTURE_WITH_DATE: {
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
            }
            break;

            case FIXTURE_WITH_ID: {
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
            }
            break;

            case FIXTURE_WITH_LEAGUE_ID: {
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
            }
            break;

            case LEAGUE: {
                retCursor = db.query(
                        DatabaseContract.LeagueEntry.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        sortOrder
                );
            }
            break;

            case LEAGUE_WITH_ID: {
                long id = DatabaseContract.LeagueEntry.getLeagueIdFromUri(uri);
                retCursor = db.query(
                        DatabaseContract.LeagueEntry.TABLE_NAME,
                        projection,
                        DatabaseContract.LeagueEntry.LEAGUE_ID_COL + " = ?",
                        new String[]{Long.toString(id)},
                        null,
                        null,
                        sortOrder
                );
            }
            break;

            case TEAM: {
                retCursor = db.query(
                        DatabaseContract.TeamEntry.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        sortOrder
                );
            }
            break;

            case TEAM_WITH_ID: {
                long teamId = DatabaseContract.TeamEntry.getTeamIdFromUri(uri);
                retCursor = db.query(
                        DatabaseContract.TeamEntry.TABLE_NAME,
                        projection,
                        DatabaseContract.TeamEntry.TEAM_CODE_COL + " = ?",
                        new String[]{Long.toString(teamId)},
                        null,
                        null,
                        sortOrder
                );
            }
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
            case FIXTURE: {
                long _id = db.insert(DatabaseContract.FixtureEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DatabaseContract.FixtureEntry.buildScoreWithMatchId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
            }
            break;

            case LEAGUE: {
                long _id = db.insert(DatabaseContract.LeagueEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DatabaseContract.LeagueEntry.buildLeagueWithId(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
            }
            break;

            case TEAM: {
                long _id = db.insert(DatabaseContract.TeamEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DatabaseContract.TeamEntry.buildTeamWithId(_id);
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
            case FIXTURE:
                tableName = DatabaseContract.FixtureEntry.TABLE_NAME;
                break;
            case LEAGUE:
                tableName = DatabaseContract.LeagueEntry.TABLE_NAME;
                break;
            case TEAM:
                tableName = DatabaseContract.TeamEntry.TABLE_NAME;
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
            case FIXTURE:
                deleted = db.delete(DatabaseContract.FixtureEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case LEAGUE:
                deleted = db.delete(DatabaseContract.LeagueEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TEAM:
                deleted = db.delete(DatabaseContract.TeamEntry.TABLE_NAME, selection, selectionArgs);
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
