package valber.com.br.movies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import valber.com.br.movies.BuildConfig;
import valber.com.br.movies.R;
import valber.com.br.movies.domain.Result;
import valber.com.br.movies.utilsInteface.ClickRecy;

import java.util.List;

public class AdapterMovieRecy extends RecyclerView.Adapter<AdapterMovieRecy.MyViewHolder> {

    final String DEBUG = AdapterMovieRecy.class.getSimpleName();
    private List<Result> movielist;
    private ClickRecy clickRecy;

    public AdapterMovieRecy(final List<Result> movielist, final ClickRecy clickRecy) {
        this.movielist = movielist;
        this.clickRecy = clickRecy;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recy_moveis, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Result movie = this.movielist.get(position);
        Picasso.get().load(BuildConfig.OPEN_URL_IMAGEN+ movie.getPosterPath()).into(holder.img);
        holder.noteBtn.setText(String.valueOf(movie.getVoteAverage()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRecy.clicke(movielist.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return movielist.size();
    }

    public void addItem(Result movie, int position) {
        this.movielist.add(movie);
        notifyItemInserted(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
         ImageView img;
         Button noteBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_movie);
            noteBtn = itemView.findViewById(R.id.btn_note);
        }

    }
}
