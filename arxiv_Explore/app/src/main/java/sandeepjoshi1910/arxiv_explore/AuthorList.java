package sandeepjoshi1910.arxiv_explore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import sandeepjoshi1910.arxiv_explore.Model.Author;

public class AuthorList extends AppCompatActivity {

    LayoutInflater layoutInflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_list);

        Intent authorsIntent = getIntent();

        List<Author> authors = authorsIntent.getParcelableArrayListExtra("authorList");

        ListView authorsView = (ListView)findViewById(R.id.authorListView);
        AuthorListAdapter authorsAdapter = new AuthorListAdapter(this,authors);

        authorsView.setDivider(null);
        authorsView.setAdapter(authorsAdapter);

        authorsAdapter.notifyDataSetChanged();

    }
}
