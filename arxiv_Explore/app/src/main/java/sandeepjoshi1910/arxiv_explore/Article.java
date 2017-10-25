package sandeepjoshi1910.arxiv_explore;

import android.content.Intent;
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

import sandeepjoshi1910.arxiv_explore.Model.DataItem;
import sandeepjoshi1910.arxiv_explore.Utilities.DatabaseHelper;
import sandeepjoshi1910.arxiv_explore.Utilities.Utils;

public class Article extends AppCompatActivity {

    protected DataItem currentArticle;

    protected TextView articleTitle;
    protected TextView authors;
    protected TextView summary;
    protected ImageButton bookmark_btn;
    protected Button viewPDF;

    protected DatabaseHelper dbHelper;

    protected Button deleteBtn;

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
        bookmark_btn = (ImageButton)findViewById(R.id.bookmark_btn);
        viewPDF = (Button)findViewById(R.id.viewpdf);
        deleteBtn = (Button)findViewById(R.id.deleteArticle);

        viewPDF.setOnClickListener(viewPdfListener);
        bookmark_btn.setOnClickListener(bookmarkBtnListener);
        deleteBtn.setOnClickListener(deleteListener);

        // Phased for Version 2
//        authors.setOnClickListener(authorsListener);
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

            dbHelper = new DatabaseHelper(getApplicationContext());

            long id = dbHelper.CreateArticle(currentArticle);

            if(id == -1) {
                Log.i("DB", "onClick: UnSuccessfully added a record");
            } else {
                Log.i("DB", "onClick: Successfully added a record");
            }
        }
    };

    View.OnClickListener deleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dbHelper = new DatabaseHelper(getApplicationContext());

            long id = dbHelper.deleteArticle(currentArticle);

            if(id == -1) {
                Log.i("DB", "onClick: UnSuccessfully deleted a record");
            } else {
                Log.i("DB", "onClick: Successfully deleted a record");
            }

        }
    };

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
