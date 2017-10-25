package sandeepjoshi1910.arxiv_explore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import sandeepjoshi1910.arxiv_explore.Utilities.ArticlesRetriever;
import sandeepjoshi1910.arxiv_explore.Utilities.DatabaseHelper;
import sandeepjoshi1910.arxiv_explore.Utilities.NetworkHelper;
import sandeepjoshi1910.arxiv_explore.Utilities.Utils;

public class MainActivity extends AppCompatActivity {

    protected Button searchBtn;
    protected EditText searchTerm;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBtn = (Button) findViewById(R.id.searchButton);
        searchTerm = (EditText) findViewById(R.id.searchTerm);

        searchBtn.setOnClickListener(searchBtnListener);

        Boolean networkOk = NetworkHelper.hasNetworkAccess(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    View.OnClickListener searchBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(!NetworkHelper.hasNetworkAccess(getApplicationContext())) {
                Log.i("Click", "onClick: No Network Access... Can't get articles");
                Toast.makeText(getApplicationContext(),"No Network Access",Toast.LENGTH_SHORT);
                return;
            }

            String searchTermEntered = searchTerm.getText().toString();
            Boolean shouldProceed = new Utils().isSearchTermValid(searchTermEntered);

            if(shouldProceed) {
                Intent articlesRetIntent = new Intent(MainActivity.this, ArticlesRetriever.class);
                articlesRetIntent.putExtra("SearchTerm",searchTermEntered);
                startActivity(articlesRetIntent);
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bkmarkedArticles:
                Intent bookmarkIntent = new Intent(MainActivity.this, BookmarkList.class);
                startActivity(bookmarkIntent);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }
}
