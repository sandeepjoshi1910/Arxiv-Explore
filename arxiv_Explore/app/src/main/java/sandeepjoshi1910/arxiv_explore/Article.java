package sandeepjoshi1910.arxiv_explore;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import sandeepjoshi1910.arxiv_explore.Model.DataItem;
import sandeepjoshi1910.arxiv_explore.Utilities.DatabaseHelper;
import sandeepjoshi1910.arxiv_explore.Utilities.Utils;

import static android.provider.MediaStore.AUTHORITY;

public class Article extends AppCompatActivity {

    private static final String TAG = "tag";
    protected DataItem currentArticle;

    protected TextView articleTitle;
    protected TextView authors;
    protected TextView summary;
    protected Button bookmark_btn;
    protected Button viewPDF;

    protected Button openFile;
    protected DatabaseHelper dbHelper;
    protected List<DataItem> mArticles;

    protected Button shareBtn;

    protected Boolean pdfDownloaded = false;


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
        shareBtn = (Button) findViewById(R.id.shareBtn);

        viewPDF.setOnClickListener(viewPdfListener);
        bookmark_btn.setOnClickListener(bookmarkBtnListener);



        if(isCurrentArticleBookmarked()) {
            bookmark_btn.setText("Bookmarked");
            bookmark_btn.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.bookmark_full,0);
            viewPDF.setText("View Saved PDF");
        }

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, currentArticle.id.replace("http","https"));
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share the article..."));
            }
        });

        // Phased for Version 2
//        authors.setOnClickListener(authorsListener);
    }




    private boolean isCurrentArticleBookmarked() {

        if(Utils.savedArticleIds.contains(currentArticle.id)) {
            return true;
        }

        return false;
    }


    private class DownloadPDFTask extends AsyncTask<String,Integer,Long> {

        @Override
        protected Long doInBackground(String... url) {
            try {
                URL u = new URL(url[0]);
                URLConnection conn = u.openConnection();
                int contentLength = conn.getContentLength();

                DataInputStream stream = new DataInputStream(u.openStream());

                byte[] buffer = new byte[contentLength];
                stream.readFully(buffer);
                stream.close();

                DataOutputStream fos = new DataOutputStream(openFileOutput(url[1], Context.MODE_PRIVATE));
                fos.write(buffer);
                fos.flush();
                fos.close();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pdfDownloaded = true;
                        viewPDF.setText("View Saved PDF");
                    }
                });
                return (long)0;
            } catch(FileNotFoundException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Unable to download the PDF",Toast.LENGTH_SHORT).show();
                    }
                });
                Log.i("ERROR", "FileNotFoundException: " + e.getLocalizedMessage());
                return (long)1; // swallow a 404
            } catch (IOException e) {
                Log.i("ERROR", "IOException: " + e.getLocalizedMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Unable to download the PDF",Toast.LENGTH_SHORT).show();
                    }
                });
                return (long)1; // swallow a 404
            }

        }

    }


    View.OnClickListener viewPdfListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(pdfDownloaded.equals(true) || isCurrentArticleBookmarked()) {
                String fileName = currentArticle.id.split("/")[4].replace(".","") + ".pdf";
                File pdfFile = getFileStreamPath(fileName);
                viewPdf(pdfFile);
            }
            else {
                Intent viewPdfIntent = new Intent(Article.this,PDFView.class);
                viewPdfIntent.putExtra("pdflink",currentArticle.pdf_link);
                startActivity(viewPdfIntent);

            }
        }
    };

    private void viewPdf(File file) {
        Intent intent;
        intent = new Intent(Intent.ACTION_VIEW, FileProvider.getUriForFile(this, "com.sandeepjoshi1910.arxiv_explore.fileprovider", file));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.i(TAG, "viewPdf: screwed");
        }
    }


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

            String link = currentArticle.pdf_link.replace("http","https");
            String fileName = currentArticle.id.split("/")[4].replace(".","") + ".pdf";
            new DownloadPDFTask().execute(link,fileName);

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
