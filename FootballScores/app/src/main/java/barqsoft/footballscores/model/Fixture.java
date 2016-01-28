package barqsoft.footballscores.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import barqsoft.footballscores.logger.Debug;

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


}
