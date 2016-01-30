package barqsoft.footballscores.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Amrendra Kumar on 28/01/16.
 */
public class TeamLink {
    @SerializedName("href")
    String team;

    public int getTeamId() {
        String teamId = team.substring(team.lastIndexOf("/") + 1);
        return Integer.parseInt(teamId);
    }

    public String getTeamUrl() {
        return team;
    }

    @Override
    public String toString() {
        return "TeamLink[" + team + "]\n";
    }
}
