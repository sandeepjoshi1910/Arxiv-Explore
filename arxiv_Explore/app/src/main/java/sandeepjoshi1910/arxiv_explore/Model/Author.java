package sandeepjoshi1910.arxiv_explore.Model;

/**
 * Created by sandeepjoshi on 10/3/17.
 */


public class Author {

    protected String authorName;
    protected String authorAffiliation;

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
