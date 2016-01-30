package barqsoft.footballscores.api;

import java.util.List;

import barqsoft.footballscores.model.Fixture;
import barqsoft.footballscores.model.League;
import barqsoft.footballscores.model.Team;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Amrendra Kumar on 27/01/16.
 */
public interface FootballEndpointServiceInterface {

    @GET("alpha/fixtures")
    Observable<Fixture.Response> getFixtures(
            @Header("X-Auth-Token") String apiKey,
            @Query("timeFrame") String timeFrame
    );

    @GET("alpha/soccerseasons/{league_id}/teams")
    Observable<Team.Response> getTeamsInfoFromLeague(
            @Header("X-Auth-Token") String apiKey,
            @Path("league_id") String leagueId
    );

    @GET("alpha/soccerseasons/")
    Observable<List<League>> getLeagueInfo(
            @Header("X-Auth-Token") String apiKey
    );
}
