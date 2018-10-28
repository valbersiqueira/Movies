package valber.com.br.movies.data.contracts;

import android.provider.BaseColumns;

public class MovieReaderContract {

    public MovieReaderContract() {
    }

    public static final class MovieEntry implements BaseColumns{

        public static final String TABLE_NAME= "movie";
        public static final String TOTOL_VOTOS= "total_votos";
        public static final String ID= "id";
        public static final String MEDIA_VOTOS= "media_votos";
        public static final String TITLE= "title";
        public static final String POPULARITY= "popularity";
        public static final String POST_PATH= "post_path";
        public static final String ORIGINAL_LANGUAFE= "language";
        public static final String ORIGINAL_TITLE= "original_title";
        public static final String DESCRICAO= "descricao";
        public static final String RELEASE= "release";
        public static final String BACKGROUND_PATH= "background_path";
        public static final String DATA_SAVE = "data_save";
        public static final String PAGE = "data_save";

    }
}
