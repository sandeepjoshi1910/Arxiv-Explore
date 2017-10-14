package sandeepjoshi1910.arxiv_explore.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import sandeepjoshi1910.arxiv_explore.Model.Author;
import sandeepjoshi1910.arxiv_explore.Model.DataItem;

/**
 * Created by sandeepjoshi on 10/12/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "arxivBmarkDB";

    // Table names
    private static final String TABLE_ARTICLES = "Table_articles";
    private static final String TABLE_AUTHORS = "Table_authors";
    private static final String TABLE_ARTICLE_AUTHOR_LINK = "article_authors";

    private static final String KEY_ID = "id";

    // Article Table Column names
    private static final String ARTICLE_ID = "article_id";
    private static final String PUBLISHED_DATE = "published_date";
    private static final String ARTICLE_TITLE = "article_title";
    private static final String ARTICLE_SUMMARY = "article_summary";
    private static final String ARTICLE_COMMENT = "article_comment";
    private static final String ARTICLE_JOURNAL_REF = "journal_ref";
    private static final String ARTICLE_PDF_LINK = "article_pdf_link";

    // AUTHOR Table Column names
    private static final String AUTHOR_NAME = "author_name";
    private static final String AUTHOR_AFFILIATION = "author_affiliation";

    //Article_Author Link Table Column names
    private static final String ARTICLEID = "articleID";
    private static final String AUTHORID = "authorID";

    private static final String CREATE_TABLE_ARTICLE = "CREATE TABLE " + TABLE_ARTICLES + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
            ARTICLE_ID + " TEXT," + PUBLISHED_DATE + " TEXT," + ARTICLE_TITLE + " TEXT," + ARTICLE_SUMMARY + " TEXT," +
            ARTICLE_COMMENT + " TEXT," + ARTICLE_JOURNAL_REF + " TEXT," + ARTICLE_PDF_LINK + " TEXT"  + ")";


    private static final String CREATE_TABLE_AUTHOR = "CREATE_TABLE " + TABLE_AUTHORS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + ARTICLE_ID + " TEXT," +
            AUTHOR_NAME + " TEXT," + AUTHOR_AFFILIATION + " TEXT" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_ARTICLE);
        db.execSQL(CREATE_TABLE_AUTHOR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ARTICLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_AUTHOR);

        // create new tables
        onCreate(db);
    }

    public void CreateArticle(DataItem article) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ARTICLE_ID,article.id);
        values.put(PUBLISHED_DATE,article.publishedDate);
        values.put(ARTICLE_TITLE,article.title);
        values.put(ARTICLE_SUMMARY,article.summary);
        values.put(ARTICLE_COMMENT,article.comment);
        values.put(ARTICLE_JOURNAL_REF,article.journal_ref);
        values.put(ARTICLE_PDF_LINK,article.pdf_link);

        db.insert(CREATE_TABLE_ARTICLE,null,values);

        Author[] authors = article.authors;

        for(int i = 0; i<authors.length; i++) {

            values = new ContentValues();

            values.put(ARTICLE_ID,article.id);
            values.put(AUTHOR_NAME,authors[i].getAuthorName());
            values.put(AUTHOR_AFFILIATION,authors[i].getAuthorAffiliation());

            db.insert(CREATE_TABLE_AUTHOR,null,values);
        }
        

    }

    public DataItem[] getBookmarkedArticles() {

        String articleSelectQuery = "SELECT * FROM " + TABLE_ARTICLES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(articleSelectQuery, null);

        if(c.moveToFirst()) {

            do {

                DataItem article = new DataItem();
                article.id = c.getString(c.getColumnIndex(ARTICLE_ID));
                article.title = c.getString(c.getColumnIndex(ARTICLE_TITLE));
                article.summary = c.getString(c.getColumnIndex(ARTICLE_SUMMARY));
                article.comment = c.getString(c.getColumnIndex(ARTICLE_COMMENT));
                article.journal_ref = c.getString(c.getColumnIndex(ARTICLE_JOURNAL_REF));
                article.pdf_link = c.getString(c.getColumnIndex(ARTICLE_PDF_LINK));

                List<Author> authors = getAuthorsForArticle(article.id);

            } while (c.moveToNext());
        }


        return null;
    }

    public List<Author> getAuthorsForArticle(String articleID) {

        String authorsQuery = "SELECT * FROM " + TABLE_AUTHORS + " WHERE "
                + ARTICLE_ID + " = " + articleID;

        List<Author> authors = new ArrayList<Author>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(authorsQuery, null);

        if(c.moveToFirst()) {

            do {
                Author author = new Author();
                author.setAuthorName(c.getString(c.getColumnIndex(AUTHOR_NAME)));
                author.setAuthorAffiliation(c.getString(c.getColumnIndex(AUTHOR_AFFILIATION)));
                authors.add(author);

            }while (c.moveToNext());
        }


        return authors;
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
