package barqsoft.footballscores.model;

import android.content.ContentValues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import barqsoft.footballscores.db.DatabaseContract;

/**
 * Created by Amrendra Kumar on 30/01/16.
 */
public class League {

    String caption;

    @SerializedName("_links")
    LeagueLinks links;

    public String getCaption() {
        return caption;
    }

    public int getLeagueId() {
        return links.leagueLink.getLeagueId();
    }


    @Override
    public String toString() {
        return "League[" + caption + "]\n";
    }

    public ContentValues getContentValues() {
        ContentValues value = new ContentValues();
        value.put(DatabaseContract.LeagueEntry.LEAGUE_ID_COL, links.leagueLink.getLeagueId());
        value.put(DatabaseContract.LeagueEntry.LEAGUE_NAME_COL, caption);
        return value;
    }

    public static final class LeagueLinks {

        @Expose
        @SerializedName("self")
        LeagueLink leagueLink;

    }
}
