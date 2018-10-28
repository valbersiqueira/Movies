package valber.com.br.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import valber.com.br.movies.data.contracts.MovieReaderContract;

public class MovieDbHelp extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bd_movie";
    private static final Integer DATABASE_VERSION = 3;

    public MovieDbHelp(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(creatTableMovie());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieReaderContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }

    private final String creatTableMovie() {
        final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
        return new StringBuilder(CREATE_TABLE)
                .append(MovieReaderContract.MovieEntry.TABLE_NAME)
                .append("(")
                .append(MovieReaderContract.MovieEntry.ID).append(" INTEGER,")
                .append(MovieReaderContract.MovieEntry.BACKGROUND_PATH).append(" TEXT,")
                .append(MovieReaderContract.MovieEntry.DESCRICAO).append(" TEXT,")
                .append(MovieReaderContract.MovieEntry.MEDIA_VOTOS).append(" DOUBLE,")
                .append(MovieReaderContract.MovieEntry.ORIGINAL_LANGUAFE).append(" TEXT,")
                .append(MovieReaderContract.MovieEntry.ORIGINAL_TITLE).append(" TEXT,")
                .append(MovieReaderContract.MovieEntry.POPULARITY).append(" DOUBLE,")
                .append(MovieReaderContract.MovieEntry.POST_PATH).append(" TEXT,")
                .append(MovieReaderContract.MovieEntry.RELEASE).append(" TEXT,")
                .append(MovieReaderContract.MovieEntry.TITLE).append(" TEXT,")
                .append(MovieReaderContract.MovieEntry.TOTOL_VOTOS).append(" INTEGER,")
                .append(MovieReaderContract.MovieEntry.DATA_SAVE).append(" DATE")
                .append(");")
                .toString();
    }

}
