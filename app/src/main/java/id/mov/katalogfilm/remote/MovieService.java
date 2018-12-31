package id.mov.katalogfilm.remote;

import id.mov.katalogfilm.response.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    @GET("search/movie")
    Call<MovieResponse> searchMovie(@Query("api_key") String api,
                                    @Query("language") String lang,
                                    @Query("query") String query);

    @GET("movie/now_playing")
    Call<MovieResponse> nowPlayingMovies(@Query("api_key") String api,
                                         @Query("language") String lang);

    @GET("movie/upcoming")
    Call<MovieResponse> upcomingMovies(@Query("api_key") String api,
                                       @Query("language") String lang);

}
