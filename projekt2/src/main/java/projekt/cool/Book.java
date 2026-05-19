package projekt.cool;
import java.io.Serializable;

/**
 * Klass som representerar en enskild bok.
 * Uppfyller C-kravet för fullständig inkapsling och sorterbarhet via
 * Comparable.
 */
public class Book implements Comparable<Book>, Serializable {
    // Fullständig inkapsling - alla medlemsvariabler är privata
    private String id;
    private String title;
    private String author;
    private String genre;
    private int pages;

    // Konstruktor som tar emot samtliga parametrar
    public Book(String id, String title, String author, String genre, int pages) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.pages = pages;
    }

    // Getters och Setters för samtliga fält (Inkapsling)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        return this.title.compareToIgnoreCase(other.title);
    }

    // Använder alla fält i utskriften
    @Override
    public String toString() {
        return "[ID: " + id + "] " + title + " av " + author + " (" + genre + ", " + pages + " sidor)";
    }
}