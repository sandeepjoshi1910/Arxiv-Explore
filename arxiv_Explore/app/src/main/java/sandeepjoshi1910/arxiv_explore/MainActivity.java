package sandeepjoshi1910.arxiv_explore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Set;

import sandeepjoshi1910.arxiv_explore.Model.DataItem;
import sandeepjoshi1910.arxiv_explore.Utilities.ArticlesRetriever;
import sandeepjoshi1910.arxiv_explore.Utilities.DatabaseHelper;
import sandeepjoshi1910.arxiv_explore.Utilities.NetworkHelper;
import sandeepjoshi1910.arxiv_explore.Utilities.Utils;

public class MainActivity extends AppCompatActivity {

    protected Button searchBtn;
    protected EditText searchTerm;
    protected ImageView logo;

    protected DatabaseHelper dbHelper;
    protected Menu optionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBtn = (Button) findViewById(R.id.searchButton);
        searchTerm = (EditText) findViewById(R.id.searchTerm);

        searchBtn.setOnClickListener(searchBtnListener);

        TextView logo = (TextView)findViewById(R.id.logoText);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/logofont.ttf");
        logo.setText("Arx Explore");
        logo.setTypeface(custom_font);
        putBookmaekedIdsToSprefs();

    }

    private void putBookmaekedIdsToSprefs() {

        dbHelper = new DatabaseHelper(this);
        List<DataItem> bkmarkedArticles = dbHelper.getBookmarkedArticles();

        Set<String> bkArticleIDs = new ArraySet<>();

        for (DataItem article: bkmarkedArticles) {
            bkArticleIDs.add(article.id);
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putStringSet("bookmarkedIds",bkArticleIDs);
        editor.apply();

        Utils.savedArticleIds = bkArticleIDs;
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
                Toast.makeText(getApplicationContext(),"Check your network connection and retry",Toast.LENGTH_SHORT);
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
        optionsMenu = menu;
//        checkAndHideOptions();
        return true;
    }

//    void checkAndHideOptions() {
//        dbHelper = new DatabaseHelper(this);
//
//        List<DataItem> bookmarkedArticles = dbHelper.getBookmarkedArticles();
//
//        if(bookmarkedArticles.size() <= 0) {
//            optionsMenu.getItem(R.id.bkmarkedArticles).setVisible(false);
//        }
//    }

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
        finish();
    }
}
