package sandeepjoshi1910.arxiv_explore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import sandeepjoshi1910.arxiv_explore.Model.DataItem;
import sandeepjoshi1910.arxiv_explore.Services.GeneralService;
import sandeepjoshi1910.arxiv_explore.Utilities.NetworkHelper;
import sandeepjoshi1910.arxiv_explore.Utilities.Utils;

public class MainActivity extends AppCompatActivity {

    private static final String XML_URL =
            "http://export.arxiv.org/api/query?search_query=all:electron";


    protected Button searchBtn;
    protected EditText searchTerm;

    private BroadcastReceiver mBroadCastReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.hasExtra(GeneralService.SERVICE_PAYLOAD)) {
                DataItem[] dataItems = (DataItem[]) intent.getParcelableArrayExtra(GeneralService.SERVICE_PAYLOAD);
                Log.i("Main", "onReceive: Data Items title: " + dataItems[2].title);

//                List<DataItem[]> articleList = dataItems;

                Bundle dataBundle = new Bundle();
                dataBundle.putParcelableArray("articles", dataItems);
                Intent articlesListIntent = new Intent(MainActivity.this, ArticleList.class);
                articlesListIntent.putExtra("articles", dataItems);
                startActivity(articlesListIntent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBtn = (Button) findViewById(R.id.searchButton);
        searchTerm = (EditText) findViewById(R.id.searchTerm);

        searchBtn.setOnClickListener(searchBtnListener);

        Boolean networkOk = NetworkHelper.hasNetworkAccess(this);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadCastReciever, new IntentFilter(GeneralService.SERVICE_MESSAGE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mBroadCastReciever);
    }

    View.OnClickListener searchBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String searchTermEntered = searchTerm.getText().toString();
            Boolean shouldProceed = new Utils().isSearchTermValid(searchTermEntered);

            if(shouldProceed) {
                getResults();
            }
        }
    };

    void getResults() {

        Intent getArticlesIntent = new Intent(this, GeneralService.class);
        getArticlesIntent.setData(Uri.parse(XML_URL));
        startService(getArticlesIntent);
    }

}
