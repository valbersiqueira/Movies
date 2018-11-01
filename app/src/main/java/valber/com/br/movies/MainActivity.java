package valber.com.br.movies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new MoviesFilmesFragment())
                .commit();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.action_populares:
//                setFragemet();
//                break;
//            case R.id.action_lancamento:
//                setFragemet();
//                break;
//            case R.id.action_maisvotados:
//                setFragemet();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void setFragemet(){
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new MoviesFilmesFragment())
                .commit();
    }
}
