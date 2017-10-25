package sandeepjoshi1910.arxiv_explore.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sandeepjoshi1910.arxiv_explore.Model.Author;
import sandeepjoshi1910.arxiv_explore.Model.DataItem;
import sandeepjoshi1910.arxiv_explore.R;

import static android.content.ContentValues.TAG;

/**
 * Created by sandeepjoshi on 10/3/17.
 */

public class ArticleAdapter extends BaseAdapter {

    private final Context mContext;
    private final DataItem[] articleList;

    public ArticleAdapter(Context context, DataItem[] articles) {
        mContext = context;
        articleList = articles;
    }

    @Override
    public int getCount() {
        return articleList.length;
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

        DataItem article = articleList[i];

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
