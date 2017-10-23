package sandeepjoshi1910.arxiv_explore;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import sandeepjoshi1910.arxiv_explore.Model.DataItem;
import sandeepjoshi1910.arxiv_explore.Utilities.Utils;

public class Article extends AppCompatActivity {

    protected DataItem currentArticle;

    protected TextView articleTitle;
    protected TextView authors;
    protected TextView summary;
    protected ImageButton bookmark_btn;
    protected Button viewPDF;

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

        viewPDF.setOnClickListener(viewPdfListener);
        bookmark_btn.setOnClickListener(bookmarkBtnListener);
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
            // Add an entry to the DB
        }
    };

}
