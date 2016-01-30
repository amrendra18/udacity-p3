package barqsoft.footballscores.model;

import android.content.ContentValues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import barqsoft.footballscores.db.DatabaseContract;
import barqsoft.footballscores.logger.Debug;

/**
 * Created by Amrendra Kumar on 30/01/16.
 */
public class Team {

    String name;
    String code;
    String shortName;
    String crestUrl;

    @SerializedName("_links")
    TeamLinks links;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCrestUrl() {
        return crestUrl;
    }

    public void setCrestUrl(String crestUrl) {
        this.crestUrl = crestUrl;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Team:" + name + "\n");
        sb.append("shortName : " + shortName + "\n");
        sb.append("crestUrl : " + crestUrl + "\n");
        String ret = sb.toString();
        Debug.i(ret, false);
        return ret;
    }

    public ContentValues getContentValues() {
        ContentValues value = new ContentValues();
        value.put(DatabaseContract.TeamEntry.TEAM_ID_COL, links.teamLink.getTeamId());
        value.put(DatabaseContract.TeamEntry.TEAM_NAME_COL, name);
        value.put(DatabaseContract.TeamEntry.TEAM_SHORT_NAME_COL, shortName);
        value.put(DatabaseContract.TeamEntry.TEAM_CODE_COL, code);
        value.put(DatabaseContract.TeamEntry.TEAM_LOGO_COL, crestUrl);
        return value;
    }

    public static final class TeamLinks {

        @Expose
        @SerializedName("self")
        TeamLink teamLink;

    }

    public static final class Response {
        @Expose
        public int count;

        @Expose
        @SerializedName("teams")
        public List<Team> teams = new ArrayList<>();

    }
}
