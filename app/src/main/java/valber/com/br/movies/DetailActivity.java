package valber.com.br.movies;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import valber.com.br.movies.data.MovieDbHelp;
import valber.com.br.movies.data.contracts.MovieReaderContract;
import valber.com.br.movies.domain.Result;
import valber.com.br.movies.utils.ConvertDataException;
import valber.com.br.movies.utils.DataUtils;

public class DetailActivity extends AppCompatActivity {
    final String OBJETO = "MOVIE";

    private static Result movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        try {

            this.movie = (Result) getIntent().getSerializableExtra(OBJETO);
        } catch (Exception e) {

        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_detail, new FragmentDatail())
                .commit();
    }


    public static class FragmentDatail extends Fragment {

        private View view;
        private Unbinder unbinder;

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

            return view;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            this.unbinder.unbind();
        }

    }
}
