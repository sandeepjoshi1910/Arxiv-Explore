package sandeepjoshi1910.arxiv_explore.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sandeepjoshi on 10/3/17.
 */


public class Author implements Parcelable {

    protected String authorName;
    protected String authorAffiliation;

    public Author() {

    }

    protected Author(Parcel in) {
        authorName = in.readString();
        authorAffiliation = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(authorName);
        dest.writeString(authorAffiliation);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Author> CREATOR = new Creator<Author>() {
        @Override
        public Author createFromParcel(Parcel in) {
            return new Author(in);
        }

        @Override
        public Author[] newArray(int size) {
            return new Author[size];
        }
    };

    public void Author() {
        this.authorName = null;
        this.authorAffiliation = null;
    }

    public String getAuthorName() {
        return this.authorName;
    }

    public String getAuthorAffiliation() {
        return this.authorAffiliation;
    }

    public void setAuthorName(String name) {
        this.authorName = name;
    }

    public void setAuthorAffiliation(String affiliation) {
        this.authorAffiliation = affiliation;
    }
}
