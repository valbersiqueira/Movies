package valber.com.br.movies.views;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import valber.com.br.movies.DetailActivity;
import valber.com.br.movies.R;
import valber.com.br.movies.adapter.AdapterMovieRecy;
import valber.com.br.movies.async.AsyncPopularMovie;
import valber.com.br.movies.domain.Result;
import valber.com.br.movies.utilsInteface.ClickRecy;
import valber.com.br.movies.utilsInteface.MoviesInterface;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFilmesFragment extends Fragment implements MoviesInterface, ClickRecy {

    private RecyclerView.LayoutManager layoutManager;
    private AdapterMovieRecy adapterMovieRecy;

    private View view;
    private Unbinder unbinder;

    final String OBJETO= "MOVIE";

    @BindView(R.id.my_recy) RecyclerView mRecyclerView;
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
        unbinder= ButterKnife.bind(this, view);

        new AsyncPopularMovie(this).execute(1);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unbinder.unbind();
    }


    @Override
    public void resultMovies(List<Result> movies) {

        progressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setHasFixedSize(true);

        layoutManager  = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.adapterMovieRecy = new AdapterMovieRecy(movies, this);
        mRecyclerView.setAdapter(this.adapterMovieRecy);

    }

    @Override
    public void clicke(Result result) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(OBJETO, result);
        startActivity(intent);
    }
}
