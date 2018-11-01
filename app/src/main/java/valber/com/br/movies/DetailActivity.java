package valber.com.br.movies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import valber.com.br.movies.adapter.AdapterVideo;
import valber.com.br.movies.domain.Movie;
import valber.com.br.movies.domain.MovieVideo;
import valber.com.br.movies.domain.ResultVideo;
import valber.com.br.movies.service.MoviesService;
import valber.com.br.movies.service.impl.JsonConverterVideo;
import valber.com.br.movies.utils.DataUtils;
import valber.com.br.movies.utilsInteface.MovieVideoInterface;

public class DetailActivity extends AppCompatActivity {
    final String OBJETO = "MOVIE";

    private static Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        try {

            this.movie = (Movie) getIntent().getSerializableExtra(OBJETO);
        } catch (Exception e) {

        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_detail, new FragmentDatail())
                .commit();
    }


    public static class FragmentDatail extends Fragment implements MovieVideoInterface {

        private View view;
        private Unbinder unbinder;

        private AdapterVideo adapterVideo;
        private RecyclerView.LayoutManager layoutManager;

        @BindView(R.id.img_movie_dt)
        ImageView pictureMovie;

        @BindView(R.id.title_dt_txt)
        TextView title;

        @BindView(R.id.txt_dt_title_original)
        TextView titleOriginal;

        @BindView(R.id.txt_dt_popoluarity)
        TextView popularity;

        @BindView(R.id.txt_dt_media)
        TextView media;

        @BindView(R.id.txt_dt_data)
        TextView data;

        @BindView(R.id.txt_dt_description)
        TextView description;

        @BindView(R.id.txt_total_votos)
        TextView totalVotos;

        @BindView(R.id.text_treiller)
        TextView treiller;

        @BindView(R.id.recy_video_dt)
        RecyclerView recyclerView;

        @BindView(R.id.progressBar_dt)
        ProgressBar progressBar;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);

            view = inflater.inflate(R.layout.detail_fragment, container, false);
            unbinder = ButterKnife.bind(this, view);
            Picasso.get().load(BuildConfig.OPEN_URL_IMAGEN + movie.getPosterPath()).into(pictureMovie);

            this.title.setText(movie.getTitle());
            this.titleOriginal.setText(movie.getOriginalTitle());
            this.popularity.setText("" + movie.getPopularity());
            this.media.setText(movie.getVoteAverage() + "");
            this.totalVotos.setText(movie.getVoteCount() + "");
            this.data.setText(DataUtils.stringToLocale(movie.getReleaseDate()));
            this.description.setText(movie.getOverview());
            new AsynckVideo(this).execute(movie.getId());
            return view;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            this.unbinder.unbind();
        }

        @Override
        public void resultAsync(List<MovieVideo> resultVideos) {
            if (resultVideos.size() == 0) treiller.setVisibility(View.INVISIBLE);
            this.progressBar.setVisibility(View.INVISIBLE);
            this.recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            adapterVideo = new AdapterVideo(resultVideos, getActivity());
            recyclerView.setAdapter(adapterVideo);

        }
    }

    public static class AsynckVideo extends AsyncTask<Integer, Void, Void>{

        private MovieVideoInterface movieVideoInterface;

        public AsynckVideo(MovieVideoInterface movieVideoInterface) {
            this.movieVideoInterface = movieVideoInterface;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(ResultVideo.class, new JsonConverterVideo())
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.OPEN_URL_PATTERN)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            Integer id = integers[0];

            MoviesService service = retrofit.create(MoviesService.class);
            Call<ResultVideo> call = service.getVideo(id,BuildConfig.OPEN_MOVIES_MAP_KEY);

            call.enqueue(new Callback<ResultVideo>() {
                @Override
                public void onResponse(Call<ResultVideo> call, Response<ResultVideo> response) {
                    if (response.body() != null) {
                        ResultVideo movieVideo = response.body();
                        movieVideoInterface.resultAsync(movieVideo.getMovieVideos());
                    }
                }

                @Override
                public void onFailure(Call<ResultVideo> call, Throwable throwable) {
                    Log.e(DetailActivity.class.getSimpleName(), throwable.getMessage());
                }
            });
            return null;
        }
    }
}
