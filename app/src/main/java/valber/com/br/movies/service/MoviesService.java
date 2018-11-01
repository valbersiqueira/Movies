package valber.com.br.movies.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import valber.com.br.movies.domain.Lancamentos;
import valber.com.br.movies.domain.Popular;
import valber.com.br.movies.domain.ResultVideo;


public interface MoviesService {

    @GET("popular")
    Call<Popular> getAllMovies(@Query("api_key") String apiKey,
                               @Query("page") Integer page,
                               @Query("language") String language);

    @GET("upcoming")
    Call<Lancamentos> getLancamentos(@Query("api_key") String apiKey,
                                     @Query("page") Integer page,
                                     @Query("language") String language);

    @GET("top_rated")
    Call<Popular> getMaisVotados(@Query("api_key") String apiKey,
                               @Query("page") Integer page,
                               @Query("language") String language);


    @GET("{movie_id}/videos")
    Call<ResultVideo> getVideo(@Path("movie_id" ) Integer movieId,
                               @Query("api_key") String apiKey);


}
