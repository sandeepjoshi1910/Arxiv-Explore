package sandeepjoshi1910.arxiv_explore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class PDFView extends AppCompatActivity {

    protected WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);

        webView = (MyWebView) findViewById(R.id.webview);

        Intent pdfIntent = getIntent();
        String url = pdfIntent.getExtras().getString("pdflink");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        String googleDocs = "https://docs.google.com/viewer?url=";
        webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
    }
}
