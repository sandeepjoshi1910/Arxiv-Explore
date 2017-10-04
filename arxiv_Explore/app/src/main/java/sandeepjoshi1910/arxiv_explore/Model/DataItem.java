package sandeepjoshi1910.arxiv_explore.Model;

/**
 * Created by sandeepjoshi on 10/3/17.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class DataItem implements Parcelable {

    public String id;
    public String publishedDate;
    public String title;
    public String summary;
    public String comment;
    public String journal_ref;
    public String pdf_link;

    public List<Author> authors = new ArrayList<Author>();

    public DataItem(Parcel in) {
        id = in.readString();
        publishedDate = in.readString();
        title = in.readString();
        summary = in.readString();
        comment = in.readString();
        journal_ref = in.readString();
        pdf_link = in.readString();
    }

    public static final Creator<DataItem> CREATOR = new Creator<DataItem>() {
        @Override
        public DataItem createFromParcel(Parcel in) {
            return new DataItem(in);
        }

        @Override
        public DataItem[] newArray(int size) {
            return new DataItem[size];
        }
    };

    public DataItem() {

    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(publishedDate);
        dest.writeString(title);
        dest.writeString(summary);
        dest.writeString(comment);
        dest.writeString(journal_ref);
        dest.writeString(pdf_link);
    }
}