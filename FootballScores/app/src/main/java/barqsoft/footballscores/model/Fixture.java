package barqsoft.footballscores.model;

import android.content.ContentValues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import barqsoft.footballscores.db.DatabaseContract;
import barqsoft.footballscores.logger.Debug;
import barqsoft.footballscores.utils.DateUtils;

/**
 * Created by Amrendra Kumar on 27/01/16.
 */
public class Fixture {

    String date;
    String status;
    int matchday;
    String homeTeamName;
    String awayTeamName;
    Result result;

    @SerializedName("_links")
    Links links;


    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(homeTeamName + " vs " + awayTeamName + "\n");
        sb.append("date : " + date + "\n");
        sb.append("status : " + status + "\n");
        sb.append(result.toString());
        String ret = sb.toString();
        Debug.i(ret, false);
        return ret;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMatchDay() {
        return matchday;
    }

    public void setMatchDay(int matchDay) {
        this.matchday = matchDay;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public static final class Response {

        @Expose
        public int count;

        @Expose
        public String timeFrameStart;

        @Expose
        public String timeFrameEnd;

        @Expose
        @SerializedName("fixtures")
        public List<Fixture> fixtures = new ArrayList<>();
    }

    public ContentValues getContentValues() {
        ContentValues value = new ContentValues();
        Links l = getLinks();
        value.put(DatabaseContract.FixtureEntry.MATCH_ID,
                l.getMatchLink().getMatchId());
        value.put(DatabaseContract.FixtureEntry.DATE_COL,
                DateUtils.getDateFromDateTime(getDate()));
        value.put(DatabaseContract.FixtureEntry.TIME_COL,
                DateUtils.getTimeFromDateTime(getDate()));
        value.put(DatabaseContract.FixtureEntry.HOME_COL, getHomeTeamName());
        value.put(DatabaseContract.FixtureEntry.AWAY_COL, getAwayTeamName());
        value.put(DatabaseContract.FixtureEntry.HOME_GOALS_COL,
                getResult().getGoalsHomeTeam());
        value.put(DatabaseContract.FixtureEntry.AWAY_GOALS_COL,
                getResult().getGoalsAwayTeam());
        int leagueId = l.getLeagueLink().getLeagueId();
        value.put(DatabaseContract.FixtureEntry.LEAGUE_COL, leagueId);
        value.put(DatabaseContract.FixtureEntry.MATCH_DAY_COL, getMatchDay());
        value.put(DatabaseContract.FixtureEntry.STATUS_COL, getStatus());
        value.put(DatabaseContract.FixtureEntry.HOME_TEAM_ID_COL,
                getLinks().getHomeTeamLink().getTeamId());
        value.put(DatabaseContract.FixtureEntry.AWAY_TEAM_ID_COL,
                getLinks().getAwayTeamLink().getTeamId());
        return value;
    }

}
