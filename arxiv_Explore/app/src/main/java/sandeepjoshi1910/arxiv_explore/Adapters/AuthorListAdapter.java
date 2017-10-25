package sandeepjoshi1910.arxiv_explore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sandeepjoshi1910.arxiv_explore.Model.Author;
import sandeepjoshi1910.arxiv_explore.R;

/**
 * Created by sandeepjoshi on 10/24/17.
 */

public class AuthorListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Author> mAuthors;

    public AuthorListAdapter(Context context, List<Author> authors) {

        mContext = context;
        mAuthors = authors;
    }

    @Override
    public int getCount() {
        return mAuthors.size();
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

        Author author = mAuthors.get(i);

        if(view == null) {
            final LayoutInflater lInflater = LayoutInflater.from(mContext);
            view = lInflater.inflate(R.layout.author_list_item, null);
        }

        TextView authorName = (TextView)view.findViewById(R.id.authorName);
        TextView authorAffl = (TextView)view.findViewById(R.id.authorAffiliation);

        authorName.setText(author.getAuthorName());

        if(author.getAuthorAffiliation() == null || author.getAuthorAffiliation() == "") {
            authorAffl.setText(" ");
        } else {
            authorAffl.setText(author.getAuthorAffiliation());
        }

        return view;
    }
}
