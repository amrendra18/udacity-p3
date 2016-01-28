package barqsoft.footballscores.model;

/**
 * Created by Amrendra Kumar on 27/01/16.
 */
public class Result {
    int goalsHomeTeam;
    int goalsAwayTeam;


    public int getGoalsAwayTeam() {
        return goalsAwayTeam;
    }

    public void setGoalsAwayTeam(int goalsAwayTeam) {
        this.goalsAwayTeam = goalsAwayTeam;
    }

    public int getGoalsHomeTeam() {
        return goalsHomeTeam;
    }

    public void setGoalsHomeTeam(int goalsHomeTeam) {
        this.goalsHomeTeam = goalsHomeTeam;
    }

    @Override
    public String toString() {
        return "RESULT[" + goalsHomeTeam + ":" + goalsAwayTeam + "]\n";
    }
}
