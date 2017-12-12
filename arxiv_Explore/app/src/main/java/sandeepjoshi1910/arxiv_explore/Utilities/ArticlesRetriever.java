package sandeepjoshi1910.arxiv_explore.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import sandeepjoshi1910.arxiv_explore.ArticleList;
import sandeepjoshi1910.arxiv_explore.MainActivity;
import sandeepjoshi1910.arxiv_explore.Model.DataItem;
import sandeepjoshi1910.arxiv_explore.R;
import sandeepjoshi1910.arxiv_explore.Services.GeneralService;

public class ArticlesRetriever extends AppCompatActivity {

    public int start = 0;
    public int max = 20;
    public String search;
    public int pageNo =0;


    public BroadcastReceiver mBroadCastReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.hasExtra(GeneralService.SERVICE_PAYLOAD)) {
                DataItem[] dataItems = (DataItem[]) intent.getParcelableArrayExtra(GeneralService.SERVICE_PAYLOAD);

                if(dataItems != null) {
                    Intent articlesListIntent = new Intent(ArticlesRetriever.this, ArticleList.class);
                    articlesListIntent.putExtra("articles", dataItems);
                    articlesListIntent.putExtra("PageNo",pageNo);
                    articlesListIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(articlesListIntent);
                } else {
                    Toast.makeText(context,"No results found!",Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(ArticlesRetriever.this,MainActivity.class);
                    startActivity(mainIntent);
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_retriever);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadCastReciever, new IntentFilter(GeneralService.SERVICE_MESSAGE));

        Intent searchIntent = getIntent();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        if(searchIntent.getExtras().get("SearchTerm") != null) {

            search = (String) searchIntent.getExtras().get("SearchTerm");
            editor.putString("SearchTerm",search);
            editor.apply();
        } else {

            search = prefs.getString("SearchTerm","");

        }



        Integer pageNum = searchIntent.getExtras().getInt("pageNo");

        String action = searchIntent.getExtras().getString("action");

        if (action != null) {

            if(action.equals("Next")) {

                pageNo = (int) searchIntent.getExtras().get("pageNo") + 1;
                start =  pageNo * 20;

            } else if(action.equals("Previous")) {
                pageNo = (int) searchIntent.getExtras().get("pageNo") - 1;
                start =  pageNo * 20;
            }
        }

        getResults(search);
    }


    void getResults(String searchTerm) {

        Intent getArticlesIntent = new Intent(this, GeneralService.class);
        if(searchTerm.contains("http")) {
            getArticlesIntent.setData(Uri.parse(searchTerm));
        } else {
            getArticlesIntent.setData(Uri.parse(Utils.getFinalUrl(searchTerm,start,max)));
        }
        startService(getArticlesIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mBroadCastReciever);
    }

}
