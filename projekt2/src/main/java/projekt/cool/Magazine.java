package projekt.cool;

public class Magazine extends Litteratur {
    public int issueNumber;
    public int publishedYear;

    public Magazine(String id, String title, boolean isAvailable, int issueNumber, int publishedYear) {
        super(id, title, isAvailable);
        this.issueNumber = issueNumber;
        this.publishedYear = publishedYear;
    }
}
