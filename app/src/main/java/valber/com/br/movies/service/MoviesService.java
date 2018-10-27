package valber.com.br.movies.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import valber.com.br.movies.domain.Popular;


public interface MoviesService {

    @GET("popular")
    Call<Popular> getAllMovies(@Query("api_key") String apiKey,
                               @Query("page") Integer page,
                               @Query("language") String language);
}
