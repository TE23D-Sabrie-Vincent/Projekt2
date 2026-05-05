package projekt.cool;

//Barnklass som lånar egenskaper från föräldrarklassen Litteratur
// Skillnaden är att denna har individuella egenskaper som author, genre och pages
public class Book extends Litteratur {
    public String author;
    public String genre;
    public int pages;

    public Book(String id, String title, boolean isAvailable, String author, String genre, int pages) {
        super(id, title, isAvailable);
        this.author = author;
        this.genre = genre;
        this.pages = pages;
    }

    // För att kunna skrivas ut i menyn senare
    public String getAuthor() {
        return author;
    }
}
