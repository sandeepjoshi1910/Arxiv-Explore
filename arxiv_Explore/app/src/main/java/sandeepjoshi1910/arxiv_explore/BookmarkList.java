package sandeepjoshi1910.arxiv_explore;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sandeepjoshi1910.arxiv_explore.Adapters.BookmarkListAdapter;
import sandeepjoshi1910.arxiv_explore.Model.DataItem;
import sandeepjoshi1910.arxiv_explore.Utilities.DatabaseHelper;

public class BookmarkList extends AppCompatActivity {

    DatabaseHelper dbHelper;
    List<DataItem> mArticles;
    Menu optionsMenu;
    DatabaseHelper mDBHelper;

    protected BookmarkListAdapter bookmarkListAdapter;
    protected ListView bookmarksView;
    private boolean isLongclicked = false;
    List<Integer> selectedArticles = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_list);

        dbHelper = new DatabaseHelper(this);

        mArticles = dbHelper.getBookmarkedArticles();

        if(mArticles.size() <= 0) {
            Toast.makeText(this,"No Bookmarks Available",Toast.LENGTH_SHORT).show();
            startMainActivity();
        }

        bookmarksView = (ListView)findViewById(R.id.bookmarkListView);
        bookmarkListAdapter = new BookmarkListAdapter(this,mArticles);

        bookmarksView.setDivider(null);
        bookmarksView.setAdapter(bookmarkListAdapter);

        bookmarkListAdapter.notifyDataSetChanged();

        bookmarksView.setLongClickable(true);


        bookmarksView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(isLongclicked) {

                    if(selectedArticles.contains(i)) {

                        view.setBackgroundColor(0x00000000);
                        selectedArticles.remove(selectedArticles.indexOf(i));

                    } else {
                        view.setBackgroundColor(getResources().getColor(R.color.Chocolate));
                        selectedArticles.add(i);
                    }

                    if(selectedArticles.size() == 0) {
                        isLongclicked = false;
                    }

                } else {
                    DataItem article = mArticles.get(i);

                    Intent articleIntent = new Intent(BookmarkList.this, Article.class);
                    articleIntent.putExtra("selectedArticle",article);
                    startActivity(articleIntent);
                }

            }
        });


        bookmarksView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                isLongclicked = true;
                view.setBackgroundColor(getResources().getColor(R.color.Chocolate));
                showOption(R.id.remove_selected);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bookmark_menu,menu);
        optionsMenu = menu;
        hideOption(R.id.remove_selected);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove_all:
                deleteAllArticles();
                startMainActivity();
                return true;
            case R.id.remove_selected:
                if(selectedArticles.size() == mArticles.size()) {
                    deleteAllArticles();
                    startMainActivity();
                } else {
                    deleteSelectedArticles();
                }

                return true;
            default:
                return false;
        }
    }

    private void hideOption(int id)
    {
        MenuItem item = optionsMenu.findItem(id);
        item.setVisible(false);
    }

    private void showOption(int id)
    {
        MenuItem item = optionsMenu.findItem(id);

        item.setVisible(true);
    }

    private void deleteAllArticles() {
        mDBHelper = new DatabaseHelper(this);

        for (DataItem article: mArticles) {
            mDBHelper.deleteArticle(article);
        }
    }

    private void deleteSelectedArticles() {
        mDBHelper = new DatabaseHelper(this);

        for (int index: selectedArticles) {
            if(mArticles.get(index) != null) {
                if(mDBHelper.deleteArticle(mArticles.get(index)) != -1) {
                    mArticles.remove(index);
                }
            }
        }
        hideOption(R.id.remove_selected);
        reloadArticles(mArticles);
    }

    private void reloadArticles(List<DataItem> articles) {
        bookmarkListAdapter = new BookmarkListAdapter(this,articles);
        bookmarksView.setAdapter(bookmarkListAdapter);
        bookmarkListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbHelper = new DatabaseHelper(this);
        mArticles = dbHelper.getBookmarkedArticles();

        reloadArticles(mArticles);

    }

    @Override
    public void onBackPressed() {
        if(isLongclicked) {
            isLongclicked = false;
            bookmarksView.setBackgroundColor(0x00000000);
            for(int i=0; i<mArticles.size(); i++) {
                if(bookmarksView.getChildAt(i) != null) {
                    bookmarksView.getChildAt(i).setBackgroundColor(0x00000000);
                }
            }
            hideOption(R.id.remove_selected);
            return;
        }
        super.onBackPressed();
    }

    private void startMainActivity() {
        super.onBackPressed();
    }

}
