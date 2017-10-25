package sandeepjoshi1910.arxiv_explore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import sandeepjoshi1910.arxiv_explore.Adapters.BookmarkListAdapter;
import sandeepjoshi1910.arxiv_explore.Model.DataItem;
import sandeepjoshi1910.arxiv_explore.Utilities.DatabaseHelper;

public class BookmarkList extends AppCompatActivity {

    DatabaseHelper dbHelper;
    List<DataItem> mArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_list);

        dbHelper = new DatabaseHelper(this);

        mArticles = dbHelper.getBookmarkedArticles();

        ListView bookmarksView = (ListView)findViewById(R.id.bookmarkListView);
        BookmarkListAdapter bookmarkListAdapter = new BookmarkListAdapter(this,mArticles);

        bookmarksView.setDivider(null);
        bookmarksView.setAdapter(bookmarkListAdapter);

        bookmarkListAdapter.notifyDataSetChanged();

        bookmarksView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                DataItem article = mArticles.get(i);

                Intent articleIntent = new Intent(BookmarkList.this, Article.class);
                articleIntent.putExtra("selectedArticle",article);
                startActivity(articleIntent);
            }
        });
    }
}
