package sandeepjoshi1910.arxiv_explore;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PDFView extends AppCompatActivity {

    protected WebView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);

        pdfView = (WebView) findViewById(R.id.pdfView);



        Intent pdfIntent = getIntent();
        String url = pdfIntent.getExtras().getString("pdflink");

        if(url == "" || url == null) {
            Toast.makeText(this,"PDF link not found",Toast.LENGTH_SHORT).show();
            super.onBackPressed();
            return;
        }

        pdfView.getSettings().setJavaScriptEnabled(true);
        pdfView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        pdfView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        pdfView.getSettings().setJavaScriptEnabled(true);
        pdfView.loadUrl("https://docs.google.com/viewer?"+url);
        String googleDocs = "https://docs.google.com/viewer?url=";
        String pdfUrl = null;
        try {
            pdfUrl = googleDocs + URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        pdfView.loadUrl(pdfUrl);
    }
}
