package sandeepjoshi1910.arxiv_explore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sandeepjoshi1910.arxiv_explore.Model.Author;
import sandeepjoshi1910.arxiv_explore.Model.DataItem;
import sandeepjoshi1910.arxiv_explore.R;

/**
 * Created by sandeepjoshi on 10/25/17.
 */

public class BookmarkListAdapter extends BaseAdapter {

    private Context mContext;
    private List<DataItem> mArticles;

    public BookmarkListAdapter(Context context, List<DataItem> articles) {
        mContext = context;
        mArticles = articles;
    }

    @Override
    public int getCount() {
        return mArticles.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        DataItem article = mArticles.get(i);

        List<Author>  authorList = article.authors;

        String allAuthors = new String();

        for(i=0; i < authorList.size(); i++) {

            allAuthors = allAuthors +authorList.get(i).getAuthorName().toString() + " • ";
        }

        if(view == null) {
            final LayoutInflater lInflater = LayoutInflater.from(mContext);
            view = lInflater.inflate(R.layout.article_item, null);
        }

        final TextView articleTitle = view.findViewById(R.id.articleTitle);
        final TextView authors = view.findViewById(R.id.authors);

        articleTitle.setText(article.title);
        authors.setText(allAuthors);

        return view;
    }
}
