package sandeepjoshi1910.arxiv_explore;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sandeepjoshi1910.arxiv_explore.Model.DataItem;
import sandeepjoshi1910.arxiv_explore.Utilities.DatabaseHelper;
import sandeepjoshi1910.arxiv_explore.Utilities.Utils;

public class Article extends AppCompatActivity {

    protected DataItem currentArticle;

    protected TextView articleTitle;
    protected TextView authors;
    protected TextView summary;
    protected Button bookmark_btn;
    protected Button viewPDF;

    protected DatabaseHelper dbHelper;
    protected List<DataItem> mArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Intent articleIntent = getIntent();

        currentArticle = (DataItem) articleIntent.getExtras().get("selectedArticle");

        articleTitle = (TextView) findViewById(R.id.article_title);
        authors = (TextView) findViewById(R.id.article_authors);
        summary = (TextView) findViewById(R.id.article_summary);

        articleTitle.setText(currentArticle.title);
        authors.setText(Utils.getFormattedAuthorNames(currentArticle.authors));
        summary.setText(currentArticle.summary);
        bookmark_btn = (Button)findViewById(R.id.bookmark_btn);
        viewPDF = (Button)findViewById(R.id.viewpdf);

        viewPDF.setOnClickListener(viewPdfListener);
        bookmark_btn.setOnClickListener(bookmarkBtnListener);

        if(isCurrentArticleBookmarked()) {
            bookmark_btn.setText("Bookmarked");
            bookmark_btn.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.bookmark_full,0);
        }

        // Phased for Version 2
//        authors.setOnClickListener(authorsListener);
    }

    private boolean isCurrentArticleBookmarked() {

        if(Utils.savedArticleIds.contains(currentArticle.id)) {
            return true;
        }

        return false;
    }

    View.OnClickListener viewPdfListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent viewPdfIntent = new Intent(Article.this,PDFView.class);
            viewPdfIntent.putExtra("pdflink",currentArticle.pdf_link);
            startActivity(viewPdfIntent);
        }
    };

    View.OnClickListener bookmarkBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            if(bookmark_btn.getText().equals("Bookmarked")) {
                if(deleteArticle()) {
                    bookmark_btn.setText("Bookmark");
                    bookmark_btn.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.bookmark_outline,0);
                    return;
                } else {
                    Toast.makeText(getApplicationContext(),"Unable to delete the article",Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if(isCurrentArticleBookmarked()) {
                bookmark_btn.setText("Bookmarked");
                bookmark_btn.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.bookmark_full,0);
                return;
            }


            dbHelper = new DatabaseHelper(getApplicationContext());

            long id = dbHelper.CreateArticle(currentArticle);

            if(id == -1) {
                Log.i("DB", "onClick: UnSuccessfully added a record");
                Toast.makeText(getApplicationContext(),"Unable to bookmark the article",Toast.LENGTH_SHORT).show();
                return;
            } else {
                Log.i("DB", "onClick: Successfully added a record");

                bookmark_btn.setText("Bookmarked");
                bookmark_btn.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.bookmark_full,0);

            }
        }
    };

    private boolean deleteArticle() {
        dbHelper = new DatabaseHelper(getApplicationContext());

        long id = dbHelper.deleteArticle(currentArticle);

        if(id == -1) {
            Log.i("DB", "onClick: UnSuccessfully deleted a record");
            return false;
        } else {
            Log.i("DB", "onClick: Successfully deleted a record");
            return true;

        }
    }

    // Phased for 2nd version of the App
    View.OnClickListener authorsListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent authorsIntent = new Intent(Article.this, AuthorList.class);
            authorsIntent.putParcelableArrayListExtra("authorList", (ArrayList<? extends Parcelable>) currentArticle.authors);
            startActivity(authorsIntent);
        }
    };

}
