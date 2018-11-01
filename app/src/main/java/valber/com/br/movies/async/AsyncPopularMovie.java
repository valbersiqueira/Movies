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
import valber.com.br.movies.domain.Lancamentos;
import valber.com.br.movies.domain.LancamentosMetodo;
import valber.com.br.movies.domain.MaisVotadosMetodo;
import valber.com.br.movies.domain.Popular;
import valber.com.br.movies.domain.Movie;
import valber.com.br.movies.domain.PopularMetodo;
import valber.com.br.movies.service.impl.JsonConverterMovies;
import valber.com.br.movies.service.MoviesService;
import valber.com.br.movies.utils.EnumService;
import valber.com.br.movies.utilsInteface.MoviesInterface;
import valber.com.br.movies.utilsInteface.ServiceIntaface;

import java.util.ArrayList;
import java.util.List;

public class AsyncPopularMovie extends AsyncTask<ServiceIntaface, Void, Void> {

    final String DEBUG = AsyncPopularMovie.class.getSimpleName();
    private MoviesInterface moviesInterface;
    private List<Movie> movies = new ArrayList<>();

    public AsyncPopularMovie(final MoviesInterface moviesInterface) {
        this.moviesInterface = moviesInterface;
    }

    @Override
    protected Void doInBackground(ServiceIntaface... integers) {
        if (integers[0] instanceof PopularMetodo){
            searchAllPopupar(integers[0]);
        } else if (integers[0] instanceof MaisVotadosMetodo){
            searchAllPopupar(integers[0]);
        } else if(integers[0] instanceof LancamentosMetodo){
            Call<Lancamentos> call = integers[0].getMetodo();

            call.enqueue(new Callback<Lancamentos>() {
                @Override
                public void onResponse(Call<Lancamentos> call, Response<Lancamentos> response) {
                    if (response.body() != null) {
                        Lancamentos popular = response.body();
                        for (Movie movie : popular.getMovies()) {
                            movies.add(movie);
                        }
                        moviesInterface.resultMovies(popular);
                    }
                }

                @Override
                public void onFailure(Call<Lancamentos> call, Throwable throwable) {
                    Log.e(DEBUG, throwable.getMessage());
                }
            });
        }

        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }


    private void searchAllPopupar(ServiceIntaface metodo){
        Call<Popular> call = metodo.getMetodo();

        call.enqueue(new Callback<Popular>() {
            @Override
            public void onResponse(Call<Popular> call, Response<Popular> response) {
                if (response.body() != null) {
                    Popular popular = response.body();
                    for (Movie movie : popular.getMovies()) {
                        movies.add(movie);
                    }
                    moviesInterface.resultMovies(popular);
                }
            }

            @Override
            public void onFailure(Call<Popular> call, Throwable throwable) {
                Log.e(DEBUG, throwable.getMessage());
            }
        });


    }


}
