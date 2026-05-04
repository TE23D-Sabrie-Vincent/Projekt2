package projekt.cool;

public class Book extends Litteratur {
    public String author;
    public String genre;
    public int pages;

    public Book(String id, String title, boolean isAvailable, String author, String genre, int pages){
        super(id, title, isAvailable);
        this.author = author;
        this.genre = genre;
        this.pages = pages;
    }
}
