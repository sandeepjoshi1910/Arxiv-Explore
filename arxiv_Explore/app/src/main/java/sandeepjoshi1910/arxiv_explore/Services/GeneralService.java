package sandeepjoshi1910.arxiv_explore.Services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import sandeepjoshi1910.arxiv_explore.Model.DataItem;
import sandeepjoshi1910.arxiv_explore.Parsers.MyXMLParser;
import sandeepjoshi1910.arxiv_explore.Utilities.HttpHelper;

/**
 * Created by sandeepjoshi on 10/3/17.
 */

public class GeneralService extends IntentService {

    public static final String TAG = "General Service";
    public static final String SERVICE_MESSAGE = "ServiceMessage";
    public static final String SERVICE_PAYLOAD = "ServicePayload";

    public GeneralService() {
        super("GeneralService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Uri uri = intent.getData();

        String response;

        try {
            response = HttpHelper.downloadUrl(uri.toString());
        } catch (IOException e) {
             Log.i(TAG, "onHandleIntent: Error getting the xml response : " + e);
            return;
        }


        DataItem[] dataItems = new DataItem[0];
        try {
            dataItems = MyXMLParser.parseFeed(response);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return;
        }

        Intent messageIntent = new Intent(SERVICE_MESSAGE);
        messageIntent.putExtra(SERVICE_PAYLOAD, dataItems);

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
    }
}
