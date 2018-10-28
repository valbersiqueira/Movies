package valber.com.br.movies.views;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import valber.com.br.movies.DetailActivity;
import valber.com.br.movies.MainActivity;
import valber.com.br.movies.R;
import valber.com.br.movies.adapter.AdapterMovieRecy;
import valber.com.br.movies.async.AsyncPopularMovie;
import valber.com.br.movies.data.MovieDbHelp;
import valber.com.br.movies.data.contracts.MovieReaderContract;
import valber.com.br.movies.domain.Popular;
import valber.com.br.movies.domain.Result;
import valber.com.br.movies.utils.DataUtils;
import valber.com.br.movies.utilsInteface.ClickRecy;
import valber.com.br.movies.utilsInteface.MoviesInterface;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFilmesFragment extends Fragment implements MoviesInterface, ClickRecy {

    final String DEBUG = MoviesFilmesFragment.class.getSimpleName();
    private SQLiteDatabase database;

    private RecyclerView.LayoutManager layoutManager;
    private AdapterMovieRecy adapterMovieRecy;
    private SharedPreferences.Editor editor;

    private View view;
    private Unbinder unbinder;
    private List<Result> movies;
    private List<Result> moviesAux;
    final String OBJETO = "MOVIE";
    final String PAGE = "PAGE";
    private int page;

    @BindView(R.id.my_recy)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_recy)
    ProgressBar progressBar;

    public MoviesFilmesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_movies_filmes, container, false);
        unbinder = ButterKnife.bind(this, view);

        this.movies = findAll();
        if (movies.size() == 0) {
            new AsyncPopularMovie(this).execute(1);
        } else {
            if (movies.get(0).getDataSave().after(new Date())) {
                this.delete();
                new AsyncPopularMovie(this).equals(1);
            } else {
                SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                page = preferences.getInt(getString(R.string.page_count), 1);
                loaderRecyclerView();
            }
        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unbinder.unbind();
    }


    @Override
    public void resultMovies(Popular popular) {
        editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
        editor.putInt(String.valueOf(R.string.page_count), popular.getPage());
        if (page == popular.getTotalPages()){
            Toast.makeText(getContext(), "Fim.", Toast.LENGTH_LONG).show();
        } else {
            this.page = popular.getPage();
            if (adapterMovieRecy != null) {
                for (Result movie : popular.getResults()) {
                    adapterMovieRecy.addItem(movie, movies.size());
                }

                progressBar.setVisibility(View.INVISIBLE);

            } else {
                this.movies = popular.getResults();
                loaderRecyclerView();
            }
            save(popular.getResults());
        }
    }

    @Override
    public void clicke(Result result) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(OBJETO, result);
        startActivity(intent);
    }

    private void loaderRecyclerView() {

        progressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setHasFixedSize(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.mRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
                    AdapterMovieRecy adp = (AdapterMovieRecy) mRecyclerView.getAdapter();

                    if (movies.size() == layoutManager.findLastCompletelyVisibleItemPosition() + 1) {
                        progressBar.setVisibility(View.VISIBLE);
                        new AsyncPopularMovie(MoviesFilmesFragment.this).execute(page + 1);
                    }
                }
            });
        } else {
            this.mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
                    AdapterMovieRecy adp = (AdapterMovieRecy) mRecyclerView.getAdapter();

                    if (movies.size() == layoutManager.findLastCompletelyVisibleItemPosition() + 1) {
                        progressBar.setVisibility(View.VISIBLE);
                        new AsyncPopularMovie(MoviesFilmesFragment.this).execute(page + 1);
                    }

                }
            });
        }

        layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.adapterMovieRecy = new AdapterMovieRecy(this.movies, this);
        mRecyclerView.setAdapter(this.adapterMovieRecy);

    }

    private void save(List<Result> movies) {
        try {
            this.database = new MovieDbHelp(getContext()).getWritableDatabase();
            for (Result movie : movies) {
                ContentValues content = new ContentValues();
                content.put(MovieReaderContract.MovieEntry.BACKGROUND_PATH, movie.getBackdropPath());
                content.put(MovieReaderContract.MovieEntry.DESCRICAO, movie.getOverview());
                content.put(MovieReaderContract.MovieEntry.DATA_SAVE, String.valueOf(DataUtils.dateToString()));
                content.put(MovieReaderContract.MovieEntry.ID, movie.getReleaseDate());
                content.put(MovieReaderContract.MovieEntry.MEDIA_VOTOS, movie.getVoteAverage());
                content.put(MovieReaderContract.MovieEntry.ORIGINAL_LANGUAFE, movie.getOriginalLanguage());
                content.put(MovieReaderContract.MovieEntry.ORIGINAL_TITLE, movie.getOriginalTitle());
                content.put(MovieReaderContract.MovieEntry.TITLE, movie.getTitle());
                content.put(MovieReaderContract.MovieEntry.POPULARITY, movie.getPopularity());
                content.put(MovieReaderContract.MovieEntry.POST_PATH, movie.getPosterPath());
                content.put(MovieReaderContract.MovieEntry.RELEASE, movie.getReleaseDate());
                content.put(MovieReaderContract.MovieEntry.TOTOL_VOTOS, movie.getVoteAverage());
                database.insert(MovieReaderContract.MovieEntry.TABLE_NAME, null, content);
            }
        } catch (Exception e) {
            Log.e(DEBUG, e.getMessage());
        }
    }

    private List<Result> findAll() {
        List<Result> result = new ArrayList();
        try {
            String sql = "SELECT * FROM " + MovieReaderContract.MovieEntry.TABLE_NAME;
            this.database = new MovieDbHelp(getContext()).getReadableDatabase();
            Cursor cursor = database.rawQuery(sql, null);
            while (cursor != null && cursor.moveToNext()) {
                Result movie = new Result();
                movie.setId(cursor.getInt(cursor.getColumnIndex(MovieReaderContract.MovieEntry.ID)));
                movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MovieReaderContract.MovieEntry.BACKGROUND_PATH)));
                movie.setOriginalLanguage(cursor.getString(cursor.getColumnIndex(MovieReaderContract.MovieEntry.ORIGINAL_LANGUAFE)));
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieReaderContract.MovieEntry.ORIGINAL_TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieReaderContract.MovieEntry.DESCRICAO)));
                movie.setPopularity(cursor.getDouble(cursor.getColumnIndex(MovieReaderContract.MovieEntry.POPULARITY)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieReaderContract.MovieEntry.POST_PATH)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieReaderContract.MovieEntry.TITLE)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieReaderContract.MovieEntry.RELEASE)));
                movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(MovieReaderContract.MovieEntry.MEDIA_VOTOS)));
                movie.setVoteCount(cursor.getInt(cursor.getColumnIndex(MovieReaderContract.MovieEntry.MEDIA_VOTOS)));
                movie.setDataSave(DataUtils.stringToDate(cursor.getString(cursor.getColumnIndex(MovieReaderContract.MovieEntry.DATA_SAVE))));
                result.add(movie);
            }

        } catch (Exception e) {
            Log.e(DEBUG, e.getMessage());
        }
        return result;
    }

    private void delete() {
        this.database = new MovieDbHelp(getContext()).getWritableDatabase();
        this.database.delete(MovieReaderContract.MovieEntry.TABLE_NAME, null, null);

    }

}
