package sandeepjoshi1910.arxiv_explore;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import sandeepjoshi1910.arxiv_explore.Utilities.ArticlesRetriever;
import sandeepjoshi1910.arxiv_explore.Utilities.DatabaseHelper;
import sandeepjoshi1910.arxiv_explore.Utilities.NetworkHelper;
import sandeepjoshi1910.arxiv_explore.Utilities.Utils;

public class MainActivity extends AppCompatActivity {

    protected Button searchBtn;
    protected EditText searchTerm;
    protected ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBtn = (Button) findViewById(R.id.searchButton);
        searchTerm = (EditText) findViewById(R.id.searchTerm);

        searchBtn.setOnClickListener(searchBtnListener);

        Boolean networkOk = NetworkHelper.hasNetworkAccess(this);

        logo = (ImageView)findViewById(R.id.logo);

        searchTerm.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.appColor), PorterDuff.Mode.SRC_ATOP);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        int logoWidth = (int) width/2;

        logo.setImageBitmap(Utils.decodeSampledBitmapFromResource(this.getResources(),
                R.drawable.brain,100, 100));
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
        finish();
    }
}
