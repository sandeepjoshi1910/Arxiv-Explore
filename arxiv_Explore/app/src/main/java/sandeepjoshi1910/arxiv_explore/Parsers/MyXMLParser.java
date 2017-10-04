package sandeepjoshi1910.arxiv_explore.Parsers;

/**
 * Created by sandeepjoshi on 10/3/17.
 */

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import sandeepjoshi1910.arxiv_explore.Model.Author;
import sandeepjoshi1910.arxiv_explore.Model.DataItem;

import static android.content.ContentValues.TAG;

public class MyXMLParser {

    public static DataItem[] parseFeed(String content) throws IOException, XmlPullParserException {



        boolean inItemTag = false;

        DataItem currentArticle = null;
        Author currentAuthor = null;
        List<DataItem> articleList = new ArrayList<>();


        boolean inEntryTag = false;
        boolean inAuthorTag = false;
        String currentTagName = "";

        content = content.replace("\n","");

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(content));

        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    currentTagName = parser.getName();

                    if(currentTagName.equals("entry")) {
                        inEntryTag = true;
                    }

                    if(currentTagName.equals("author")) {
                        inAuthorTag = true;
                    }
                    if(inEntryTag){

                        if(parser.getAttributeCount()>0) {

                            if(parser.getAttributeName(0).toString().equals("title") && parser.getAttributeValue(0).toString().equals("pdf") ) {

                                currentArticle.pdf_link = parser.getAttributeValue(1);
                            }
                        }


                    }


                    break;

                case XmlPullParser.END_TAG:
                    if(parser.getName().contains("entry") ) {
                        inEntryTag = false;
                        articleList.add(currentArticle);
                        currentArticle = null;
                    }
                    if(parser.getName().contains("author")) {
                        inAuthorTag = false;
                        currentArticle.addAuthor(currentAuthor);
                        currentAuthor = null;
                    }
                    currentTagName = "";

                    break;

                case XmlPullParser.TEXT:
                    if(parser.getText().contains("\n ")){
                        break;
                    }

                    if(currentTagName.equals("opensearch:totalResults")) {
                        if(Integer.parseInt(parser.getText().toString()) == 0) {
                            Log.i(TAG, "No results for the search");
                            return null;
                        }
                    }

                    if(inEntryTag) {

                        String currentText = parser.getText().toString();

                        switch (currentTagName) {

                            case "id":
                                currentArticle = new DataItem();
                                currentArticle.id = currentText;
                                break;

                            case "published":
                                currentArticle.publishedDate = currentText;
                                break;

                            case "title":
                                currentArticle.title = currentText;
                                break;

                            case "summary":
                                currentArticle.summary = currentText;
                                break;

                            case "name":
                                currentAuthor = new Author();
                                currentAuthor.setAuthorName(currentText);
                                break;

                            case "arxiv:affiliation":
                                currentAuthor.setAuthorAffiliation(currentText);
                                break;

                            case "arxiv:comment":
                                currentArticle.comment = currentText;
                                break;

                            case "arxiv:journal_ref":
                                currentArticle.journal_ref = currentText;
                                break;

                            default:
                                Log.i(TAG, "Unhandled Text: " + currentText);
                                break;
                        }
                    }
                    break;
            }

            eventType = parser.next();

        } // end while loop

        DataItem[] dataItems = new DataItem[articleList.size()];
        return articleList.toArray(dataItems);



    }
}

