package projekt.cool;
//Barnklass som lånar egenskaper från föräldrarklassen Litteratur
// Skillnaden med denna är att den innehåller IssueNumber och Publishedyear som individuell egenskap

/* * Författare: Vincent Sabrie
 * Beskrivning: Underklass (barnklass) till Litteratur. Representerar en tidning eller ett 
 * magasin och utökar basklassen med unika attribut för utgåvonummer (issueNumber) och utgivningsår.
 */
public class Magazine extends Litteratur {
    public int issueNumber;
    public int publishedYear;

    /* Konstruktor för att skapa ett unikt Magazine-objekt.
       Vad den gör: Kedjar data till superklassens konstruktor och tilldelar värden till 
       tidningens unika attribut (issueNumber och publishedYear).
       Inparametrar: String id, String title, boolean isAvailable, int issueNumber, int publishedYear
       Returvärde: Inget (Konstruktor)
     */
    public Magazine(String id, String title, boolean isAvailable, int issueNumber, int publishedYear) {
        super(id, title, isAvailable);
        this.issueNumber = issueNumber;
        this.publishedYear = publishedYear;
    }

    /* Vad metoden gör: Returnerar tidningens utgåvonummer.
       Inparametrar: Inga
       Returvärde: int (Utgåvonumret)
     */
    public int getIssueNumber() {
        return issueNumber;
    }


    /* Vad metoden gör: Returnerar tidningens utgivningsår.
       Inparametrar: Inga
       Returvärde: int (Publiceringsåret)
     */
    public int getPublishedYear(){
        return publishedYear;
    }
}
