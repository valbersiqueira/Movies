package valber.com.br.movies.utils;

import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class PicassoUtils {

    public static void downloadImagemPicasso(final String url, final ImageView imageView){
        Picasso.get().load(url).into(imageView);
    }
}
