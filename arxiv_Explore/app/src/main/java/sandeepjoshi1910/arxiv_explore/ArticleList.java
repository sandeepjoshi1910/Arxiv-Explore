package sandeepjoshi1910.arxiv_explore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import sandeepjoshi1910.arxiv_explore.Model.DataItem;

public class ArticleList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

//        DataItem[] dataItems = (DataItem[]) getIntent().getExtras().get("articles");

//        DataItem[] dataItems = (DataItem[]) getIntent().getParcelableArrayExtra("articles");
        Bundle dataBundle = getIntent().getExtras();
        DataItem[] articles = new DataItem[15];
//               articles = (DataItem[]) dataBundle.getParcelableArray("articles");
//        DataItem[] dataItems = (DataItem[]) data.get("articles");

        for(int i=0; i<7; i++) {
            articles[i] = (DataItem) dataBundle.getParcelableArray("articles")[i];
        }

        GridView articlesView = (GridView)findViewById(R.id.articleListView);
        final ArticleAdapter articleAdapter = new ArticleAdapter(this, articles);

        articlesView.setAdapter(articleAdapter);

        articleAdapter.notifyDataSetChanged();
    }
}
