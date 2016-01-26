package barqsoft.footballscores.model;

/**
 * Created by Amrendra Kumar on 27/01/16.
 */
public class Result {
    int goalsHomeTeam;
    int goalsAwayTeam;

    @Override
    public String toString() {
        return "RESULT[" + goalsHomeTeam + ":" + goalsAwayTeam + "]\n";
    }
}
