package sandeepjoshi1910.arxiv_explore.Utilities;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sandeepjoshi on 10/1/17.
 */

public class Utils {


    public Boolean isSearchTermValid(String searchTerm) {

        return true;

//        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
//        Matcher m = p.matcher(searchTerm);
//        if(m.find()) {
//            Log.i("Validation", "isSearchTermValid: Search term has special characters");
//            return false;
//        }
//
//        return true;
    }
}
