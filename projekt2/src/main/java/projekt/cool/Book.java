package projekt.cool;
import java.io.Serializable;

/**
 * Klass som representerar en enskild bok.
 * fullständig inkapsling och sorterbarhet via
 * Comparable.
 */
public class Book extends Litteratur implements Comparable<Book>, Serializable {
    // Fullständig inkapsling - rensat bort id och title eftersom de ärvs från Litteratur!
    private String author;
    private String genre;
    private int pages;

    // Konstruktor som tar emot samtliga parametrar
    public Book(String id, String title, String author, String genre, int pages) {
        super(id, title, true); // Skickar upp id och title till föräldern Litteratur
        this.author = author;
        this.genre = genre;
        this.pages = pages;
    }

    // Hämtar från superklassen Litteratur
    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public String getTitle() {
        return super.getTitle();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    // Sorterar böcker i bokstavsordning baserat på titel
    @Override
    public int compareTo(Book other) {
        return this.getTitle().compareToIgnoreCase(other.getTitle());
    }

    // Använder alla fält i utskriften
    @Override
    public String toString() {
        return "[ID: " + getId() + "] " + getTitle() + " av " + author + " (" + genre + ", " + pages + " sidor)";
    }
}