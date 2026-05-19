package projekt.cool;

//Barnklass som lånar egenskaper från föräldrarklassen Litteratur
// Skillnaden är att denna har individuella egenskaper som author, genre och pages

/* * Författare: Vincent sabrie 
 * Beskrivning: Underklass (barnklass) till Litteratur. Representerar en specifik bok i 
 * systemet och utökar basklassen med unika attribut för författare, genre och sidantal.
 */
public class Book extends Litteratur {
    private String author;
    private String genre;
    private int pages;

    /*
     * Konstruktor för att skapa ett unikt Book-objekt.
     * Vad den gör: Skickar basdata till superklassens konstruktor via super() och
     * initierar
     * de bokspecifika fälten (author, genre, pages).
     * Inparametrar: String id, String title, boolean isAvailable, String author,
     * String genre, int pages
     * Returvärde: Inget (Konstruktor)
     */
    public Book(String id, String title, boolean isAvailable, String author, String genre, int pages) {
        // Super för att låna egenskaper från föräldrarklassen Litteratur.java
        super(id, title, isAvailable);
        this.author = author;
        this.genre = genre;
        this.pages = pages;
    }

    /*
      Vad metoden gör: Returnerar namnet på bokens författare.
      Inparametrar: Inga
      Returvärde: String (Författarens namn)
     */
    public String getAuthor() {
        return author;
    }

    /*
      Vad metoden gör: Hämtar och returnerar bokens totala antal sidor.
      Inparametrar: Inga
      Returvärde: int (Antal sidor)
     */
    public int getPages() {
        return this.pages;
    }

}
