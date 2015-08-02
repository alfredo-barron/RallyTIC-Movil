package alfredobarron.com.rallytic.rest;

import alfredobarron.com.rallytic.models.server.Rally;
import alfredobarron.com.rallytic.models.server.RequestTeam;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface API {

    @GET("/login")
    void login(@Query("name") String name, @Query("password") String password, Callback<RequestTeam> cb);

    @GET("/evento")
    void getEvent(@Query("id") int id, Callback<Rally> cb);

}
