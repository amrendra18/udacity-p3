package barqsoft.footballscores.api;

import barqsoft.footballscores.model.Fixture;
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


    @GET("alpha/teams/{team_id}")
    void getTeamInfo(
            @Header("X-Auth-Token") String apiKey,
            @Path("team_id") String teamId
    );

    @GET("alpha/teams/{team_id}/fixtures")
    void getTeamFixtures(
            @Header("X-Auth-Token") String apiKey,
            @Path("team_id") String teamId
    );

    @GET("alpha/teams/{team_id}/players")
    void getTeamPlayers(
            @Header("X-Auth-Token") String apiKey,
            @Path("team_id") String teamId
    );
}
