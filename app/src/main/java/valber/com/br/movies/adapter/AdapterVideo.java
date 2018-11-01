package valber.com.br.movies.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.List;

import valber.com.br.movies.BuildConfig;
import valber.com.br.movies.R;
import valber.com.br.movies.domain.MovieVideo;

public class AdapterVideo extends RecyclerView.Adapter<AdapterVideo.MyViewHolde> {

    List<MovieVideo> movieVideoList;
    private FragmentActivity context;

    public AdapterVideo(List<MovieVideo> movieVideoList, FragmentActivity context) {
        this.movieVideoList = movieVideoList;
        this.context = context;
    }

    @Override
    public MyViewHolde onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_recy, parent, false);
        return new MyViewHolde(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolde holder, int position) {
        String urlvideo = BuildConfig.OPEN_URL_YOUTUBE+""+movieVideoList.get(position).getKey();
        String url = new StringBuilder("<iframe width=\"100%\" height=\"100%\" src=")
                .append("\""+urlvideo+"\"")
                .append(" frameborder=\"0\" allowfullscreen></iframe>")
                .toString();
        holder.webView.loadData(url,"text/html","utf-8");
    }

    @Override
    public int getItemCount() {
        return movieVideoList.size();
    }

    public class MyViewHolde extends RecyclerView.ViewHolder {
        WebView webView;
        public MyViewHolde(View itemView) {
            super(itemView);
            webView = itemView.findViewById(R.id.youtube_play);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient());
        }


    }
}
