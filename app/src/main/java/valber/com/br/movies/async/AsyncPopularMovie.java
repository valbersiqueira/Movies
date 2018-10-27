package valber.com.br.movies.async;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import valber.com.br.movies.BuildConfig;
import valber.com.br.movies.domain.Popular;
import valber.com.br.movies.domain.Result;
import valber.com.br.movies.service.impl.JsonConverterMovies;
import valber.com.br.movies.service.MoviesService;
import valber.com.br.movies.utilsInteface.MoviesInterface;

import java.util.ArrayList;
import java.util.List;

public class AsyncPopularMovie extends AsyncTask<Integer, Void, Void> {

    final String DEBUG = AsyncPopularMovie.class.getSimpleName();
    private MoviesInterface moviesInterface;
    private List<Result> movies = new ArrayList<>();

    public AsyncPopularMovie(final MoviesInterface moviesInterface) {
        this.moviesInterface = moviesInterface;
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Popular.class, new JsonConverterMovies())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.OPEN_URL_PATTERN)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Integer page = integers[0];

        MoviesService service = retrofit.create(MoviesService.class);
        Call<Popular> call = service.getAllMovies(BuildConfig.OPEN_MOVIES_MAP_KEY, page, "pt-BR");

        call.enqueue(new Callback<Popular>() {
            @Override
            public void onResponse(Call<Popular> call, Response<Popular> response) {
                if (response.body() != null) {
                    Popular popular = response.body();
                    for (Result movie : popular.getResults()) {
                        movies.add(movie);
                    }
                    moviesInterface.resultMovies(movies);
                }
            }

            @Override
            public void onFailure(Call<Popular> call, Throwable throwable) {
                Log.e(DEBUG, throwable.getMessage());
            }
        });


        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }


}
