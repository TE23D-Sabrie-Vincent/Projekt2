package projekt.cool;

public class Litteratur {
    public String id;
    public String title;
    public boolean isAvailable;

    public Litteratur(String id, String title, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.isAvailable = isAvailable;
    }

    // Behövs för Main kunna läsa av värden
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
