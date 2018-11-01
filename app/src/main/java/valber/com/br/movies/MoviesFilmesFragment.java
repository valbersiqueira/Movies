package valber.com.br.movies;


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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import valber.com.br.movies.adapter.AdapterMovieRecy;
import valber.com.br.movies.async.AsyncPopularMovie;
import valber.com.br.movies.data.MovieDbHelp;
import valber.com.br.movies.data.contracts.MovieReaderContract;
import valber.com.br.movies.domain.Lancamentos;
import valber.com.br.movies.domain.LancamentosMetodo;
import valber.com.br.movies.domain.MaisVotadosMetodo;
import valber.com.br.movies.domain.Movie;
import valber.com.br.movies.domain.Popular;
import valber.com.br.movies.domain.PopularMetodo;
import valber.com.br.movies.utils.DataUtils;
import valber.com.br.movies.utils.EnumService;
import valber.com.br.movies.utilsInteface.ClickRecy;
import valber.com.br.movies.utilsInteface.MoviesInterface;
import valber.com.br.movies.utilsInteface.ServiceIntaface;

import java.util.ArrayList;
import java.util.Calendar;
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
    private List<Movie> movies;
    final String OBJETO = "MOVIE";
    private int page;
    private PopularMetodo popularMetodo;
    private LancamentosMetodo lancamentosMetodo;
    private MaisVotadosMetodo maisVotadosMetodo;

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
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String typo = preferences.getString(getString(R.string.typoInterface), "POPULAR");

        if (typo.equals(EnumService.POPULAR.name())){
           this.popularMetodo = (PopularMetodo) EnumService.valueOf(typo).getMetodo();
        }else if (typo.equals(EnumService.LANCAMENTO.name())){
            this.lancamentosMetodo = (LancamentosMetodo) EnumService.valueOf(typo).getMetodo();
        } else {
            this.maisVotadosMetodo = (MaisVotadosMetodo) EnumService.valueOf(typo).getMetodo();
        }

        this.movies = findAll();
        if (movies.size() == 0) {

            new AsyncPopularMovie(this).execute(EnumService.valueOf(typo).getMetodo());
        } else {
            Calendar dataNow = Calendar.getInstance();
            dataNow.setTime(new Date());
            dataNow.add(Calendar.DAY_OF_MONTH, -1);
            Calendar data = Calendar.getInstance();
            data.setTime(movies.get(0).getDataSave());
            if (data.before(dataNow)) {
                this.delete();
                new AsyncPopularMovie(this).execute(popularMetodo);
            } else {
                 preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                page = preferences.getInt(getString(R.string.page_count), 1);
                loaderRecyclerView();
            }
        }
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_populares:
                this.popularMetodo = (PopularMetodo) EnumService.POPULAR.getMetodo();
                refreshRecy(EnumService.POPULAR.name());
                new AsyncPopularMovie(this).execute(this.popularMetodo);
                break;
            case R.id.action_lancamento:
                this.lancamentosMetodo = (LancamentosMetodo) EnumService.LANCAMENTO.getMetodo();
                refreshRecy(EnumService.LANCAMENTO.name());
                new AsyncPopularMovie(this).execute(this.lancamentosMetodo);
                break;
            case R.id.action_maisvotados:
                this.maisVotadosMetodo = (MaisVotadosMetodo) EnumService.MAIS_VOTADOS.getMetodo();
                refreshRecy(EnumService.MAIS_VOTADOS.name());
                new AsyncPopularMovie(this).execute(this.maisVotadosMetodo);
                break;
        }
        return super.onOptionsItemSelected(item);
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
    public void resultMovies(Object obj) {
        List<Movie> movieList = new ArrayList<>();
        int pagina = 1;
        int totalPage = 1;
        if (obj instanceof Popular) {
            Popular popular = ((Popular) obj);
            movieList =  popular.getMovies();
            pagina = popular.getPage();
            totalPage = popular.getTotalPages();
        } else if (obj instanceof Lancamentos) {
           Lancamentos lancamentos =((Lancamentos) obj);
            movieList = lancamentos.getMovies();
            pagina = lancamentos.getPage();
            totalPage = lancamentos.getTotalPages();
        }
        editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
        editor.putInt(String.valueOf(R.string.page_count), pagina);
        if (page == totalPage) {
            Toast.makeText(getContext(), "Fim.", Toast.LENGTH_LONG).show();
        } else {
            this.page = pagina;
            if (adapterMovieRecy != null) {
                for (Movie movie : movieList) {
                    adapterMovieRecy.addItem(movie, movies.size());
                }

                progressBar.setVisibility(View.INVISIBLE);

            } else {
                this.movies = movieList;
                loaderRecyclerView();
            }
            save(movieList);
        }
    }

    @Override
    public void clicke(Movie movie) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(OBJETO, movie);
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

                    if (movies.size() == layoutManager.findLastCompletelyVisibleItemPosition() + 1) {
                        progressBar.setVisibility(View.VISIBLE);
                        carregarAsyncScroll();
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
                    if (movies.size() == layoutManager.findLastCompletelyVisibleItemPosition() + 1) {
                        carregarAsyncScroll();
                        progressBar.setVisibility(View.VISIBLE);
                        new AsyncPopularMovie(MoviesFilmesFragment.this).execute(popularMetodo);
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

    private void save(List<Movie> movies) {
        try {
            this.database = new MovieDbHelp(getContext()).getWritableDatabase();
            for (Movie movie : movies) {
                ContentValues content = new ContentValues();
                content.put(MovieReaderContract.MovieEntry.BACKGROUND_PATH, movie.getBackdropPath());
                content.put(MovieReaderContract.MovieEntry.DESCRICAO, movie.getOverview());
                content.put(MovieReaderContract.MovieEntry.DATA_SAVE, String.valueOf(DataUtils.dateToString()));
                content.put(MovieReaderContract.MovieEntry.ID, movie.getId());
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

    private List<Movie> findAll() {
        List<Movie> result = new ArrayList();
        try {
            String sql = "SELECT * FROM " + MovieReaderContract.MovieEntry.TABLE_NAME;
            this.database = new MovieDbHelp(getContext()).getReadableDatabase();
            Cursor cursor = database.rawQuery(sql, null);
            while (cursor != null && cursor.moveToNext()) {
                Movie movie = new Movie();
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

    private void refreshRecy(String typo){
        delete();
        if (typo.equals(EnumService.POPULAR.name())){
            this.maisVotadosMetodo = null;
            this.lancamentosMetodo = null;
        }else if (typo.equals(EnumService.LANCAMENTO.name())){
            this.maisVotadosMetodo = null;
            this.popularMetodo = null;
        } else {
            this.lancamentosMetodo = null;
            this.popularMetodo = null;
        }
        editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString(String.valueOf(R.string.typoInterface), typo);
        if (this.adapterMovieRecy != null)
            this.adapterMovieRecy.removeAll();
        this.adapterMovieRecy = null;
        this.progressBar.setVisibility(View.VISIBLE);
    }


    private void carregarAsyncScroll(){
        if (popularMetodo != null) {
            popularMetodo.setPage(page + 1);
            new AsyncPopularMovie(MoviesFilmesFragment.this).execute(popularMetodo);
        } else if (lancamentosMetodo != null){
            lancamentosMetodo.setPage(page +1);
            new AsyncPopularMovie(MoviesFilmesFragment.this).execute(lancamentosMetodo);
        }else if (maisVotadosMetodo != null){
            maisVotadosMetodo.setPage(page+1);
            new AsyncPopularMovie(MoviesFilmesFragment.this).execute(maisVotadosMetodo);
        }
    }

}
