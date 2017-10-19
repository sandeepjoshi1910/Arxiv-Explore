package sandeepjoshi1910.arxiv_explore.Utilities;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sandeepjoshi on 10/1/17.
 */

public class Utils {


    public Boolean isSearchTermValid(String searchTerm) {

        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(searchTerm);
        if(m.find()) {
            Log.i("Validation", "isSearchTermValid: Search term has special characters");
            return false;
        }

        return true;
    }


    public static String getFinalUrl(String searchTerm, int start, int end) {

        String[] searchTerms = searchTerm.split(" ");

        String finalTerm = "";

        for (String term : searchTerms ) {

            if(finalTerm != "") {
                finalTerm = finalTerm + "+AND+" + "all:" + term;
            } else {
                finalTerm = finalTerm + "all:" + term;
            }

        }


        String initialUrl = "http://export.arxiv.org/api/query?search_query=";

        String url = initialUrl + finalTerm + "&start=" + start + "&max_results=" + end + "&sortBy=relevance&sortOrder=ascending";

        return url;
    }
}
