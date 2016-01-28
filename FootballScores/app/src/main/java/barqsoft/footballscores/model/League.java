package barqsoft.footballscores.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Amrendra Kumar on 28/01/16.
 */
public class League {

    @SerializedName("href")
    String league;

    public int getLeagueId() {
        String leagueId = league.substring(league.lastIndexOf("/") + 1);
        return Integer.parseInt(leagueId);
    }


    @Override
    public String toString() {
        return "League[" + league + "]\n";
    }
}
