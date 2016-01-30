package barqsoft.footballscores.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Amrendra Kumar on 28/01/16.
 */
public class Links {
    @SerializedName("self")
    MatchLink matchLink;

    @SerializedName("soccerseason")
    LeagueLink leagueLink;

    @SerializedName("homeTeam")
    TeamLink homeTeamLink;

    @SerializedName("awayTeam")
    TeamLink awayTeamLink;


    public TeamLink getAwayTeamLink() {
        return awayTeamLink;
    }

    public void setAwayTeamLink(TeamLink awayTeamLink) {
        this.awayTeamLink = awayTeamLink;
    }

    public MatchLink getMatchLink() {
        return matchLink;
    }

    public void setMatchLink(MatchLink matchLink) {
        this.matchLink = matchLink;
    }

    public LeagueLink getLeagueLink() {
        return leagueLink;
    }

    public void setLeagueLink(LeagueLink leagueLink) {
        this.leagueLink = leagueLink;
    }

    public TeamLink getHomeTeamLink() {
        return homeTeamLink;
    }

    public void setHomeTeamLink(TeamLink homeTeamLink) {
        this.homeTeamLink = homeTeamLink;
    }

}
