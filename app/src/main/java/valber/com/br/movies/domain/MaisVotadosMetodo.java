package valber.com.br.movies.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import valber.com.br.movies.BuildConfig;
import valber.com.br.movies.service.MoviesService;
import valber.com.br.movies.service.impl.JsonConverterMovies;
import valber.com.br.movies.utilsInteface.ServiceIntaface;

public class MaisVotadosMetodo implements ServiceIntaface {
    private String apiKey = BuildConfig.OPEN_MOVIES_MAP_KEY;
    private Integer page = 1;
    private String language = "pt-BR";


    @Override
    public Call<Popular> getMetodo() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Popular.class, new JsonConverterMovies())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.OPEN_URL_PATTERN)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        MoviesService service =retrofit.create(MoviesService.class);
        return service.getMaisVotados( apiKey, page, language);
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
