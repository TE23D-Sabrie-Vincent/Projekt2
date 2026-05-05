package projekt.cool;
//Barnklass som lånar egenskaper från föräldrarklassen Litteratur
//Skillnaden med denna är att den innehåller 
// IssueNumber och Publishedyear som individuell egenskap
public class Magazine extends Litteratur {
    public int issueNumber;
    public int publishedYear;

    public Magazine(String id, String title, boolean isAvailable, int issueNumber, int publishedYear) {
        super(id, title, isAvailable);
        this.issueNumber = issueNumber;
        this.publishedYear = publishedYear;
    }

    // för att kunna skriva ut numret i menyn sen
    public int getIssueNumber() {
        return issueNumber;
    }
}
