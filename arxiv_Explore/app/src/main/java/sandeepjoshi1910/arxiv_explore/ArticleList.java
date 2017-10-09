package sandeepjoshi1910.arxiv_explore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import sandeepjoshi1910.arxiv_explore.Model.DataItem;

public class ArticleList extends AppCompatActivity {

    protected DataItem[] articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

//        DataItem[] dataItems = (DataItem[]) getIntent().getExtras().get("articles");

//        DataItem[] dataItems = (DataItem[]) getIntent().getParcelableArrayExtra("articles");
        Bundle dataBundle = getIntent().getExtras();

        articles = new DataItem[dataBundle.getParcelableArray("articles").length];
//               articles = (DataItem[]) dataBundle.getParcelableArray("articles");
//        DataItem[] dataItems = (DataItem[]) data.get("articles");



        for(int i=0; i<dataBundle.getParcelableArray("articles").length; i++) {
            articles[i] = (DataItem) dataBundle.getParcelableArray("articles")[i];
        }

        ListView articlesView = (ListView)findViewById(R.id.articleListView);
        final ArticleAdapter articleAdapter = new ArticleAdapter(this, articles);

        articlesView.setAdapter(articleAdapter);

        articleAdapter.notifyDataSetChanged();

        articlesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.i("test", "onItemClick: clicked");

                Intent articleViewIntent = new Intent(ArticleList.this, Article.class);
                articleViewIntent.putExtra("selectedArticle",articles[i]);
                startActivity(articleViewIntent);
            }
        });
    }


}
