package barqsoft.footballscores.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Amrendra Kumar on 28/01/16.
 */
public class Links {
    @SerializedName("self")
    Match match;

    @SerializedName("soccerseason")
    League league;

    @SerializedName("homeTeam")
    Team homeTeam;

    @SerializedName("awayTeam")
    Team awayTeam;


    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

}
