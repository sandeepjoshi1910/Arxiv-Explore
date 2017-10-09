package sandeepjoshi1910.arxiv_explore;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sandeepjoshi1910.arxiv_explore.Model.DataItem;

public class Article extends AppCompatActivity {

    protected DataItem currentArticle;

    protected TextView articleTitle;
    protected TextView authors;
    protected TextView summary;

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
        authors.setText(currentArticle.authors.get(0).getAuthorName());
        summary.setText(currentArticle.summary);

        viewPDF = (Button)findViewById(R.id.viewpdf);

        viewPDF.setOnClickListener(viewPdfListener);
    }


    View.OnClickListener viewPdfListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent viewPdfIntent = new Intent(Article.this,PDFView.class);
            viewPdfIntent.putExtra("pdflink",currentArticle.pdf_link);
            startActivity(viewPdfIntent);
        }
    };

}
