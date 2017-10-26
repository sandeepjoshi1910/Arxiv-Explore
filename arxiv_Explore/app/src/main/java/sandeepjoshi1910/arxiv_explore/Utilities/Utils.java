package sandeepjoshi1910.arxiv_explore.Utilities;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sandeepjoshi1910.arxiv_explore.Model.Author;

/**
 * Created by sandeepjoshi on 10/1/17.
 */

public class Utils {


    public Boolean isSearchTermValid(String searchTerm) {

        if(searchTerm == null || searchTerm.equals("")) {
            return false;
        }

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

    public static String getFormattedAuthorNames(List<Author> authors) {

        String fAuthors = "";

        if(authors.size() == 1) {
            return authors.get(0).getAuthorName();
        }

        for (Author author: authors) {
            fAuthors = fAuthors + author.getAuthorName() + " • ";
        }

        return fAuthors;
    }

    // Calculates the height of the image given the width to maintain the Aspect Ratio of 16:9 or 1.7777777778
    public static int getHeight(int width) {

        // Formula : height = 9/16 * width
        float height = Math.round((float) ((9.0/16.0) * (float) width));
        return (int) height;
    }

    /*
    The following two methods 1) decodeSampledBitmapFromResource   2) calculateInSampleSize
                      are sourced from Android documentation on "Loading Large Bitmaps Efficiently"
              Source: https://developer.android.com/topic/performance/graphics/load-bitmap.html
 */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
