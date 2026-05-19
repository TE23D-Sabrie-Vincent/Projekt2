import java.io.*;
import java.util.*;

/**
 * Logikklass för att hantera samlingen av böcker.
 * Hanterar JSON-lagring, sökning och radering.
 */
public class LibraryManager {
    // Använder en generisk ArrayList för att spara objekten 
    private List<Book> books;
    private static final String FILE_PATH = "books.json";

    public LibraryManager() {
        this.books = new ArrayList<>();
        loadFromJSON(); // Läser in data direkt vid start
    }

    public void addBook(Book book) {
        books.add(book);
        Collections.sort(books); // Sorterar automatiskt listan efter titel vid tillägg
        saveToJSON();
    }

    // C-krav: Kan ta bort objekt baserat på unikt ID
    public boolean removeBook(String id) {
        for (Book b : books) {
            if (b.getId().equalsIgnoreCase(id)) {
                books.remove(b);
                saveToJSON();
                return true;
            }
        }
        return false;
    }

    public List<Book> getBooks() {
        return books;
    }

    // C-krav: Kan hitta/söka efter specifika objekt via en algoritm (Här söker vi på genre)
    public List<Book> searchByGenre(String genre) {
        List<Book> results = new ArrayList<>();
        for (Book b : books) {
            if (b.getGenre().equalsIgnoreCase(genre)) {
                results.add(b);
            }
        }
        return results;
    }

    // JSON-serialisering: Sparar ALLA fält till filen (C-krav)
    public void saveToJSON() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("[\n");
            for (int i = 0; i < books.size(); i++) {
                Book b = books.get(i);
                writer.write("  {\n");
                writer.write("    \"id\": \"" + b.getId() + "\",\n");
                writer.write("    \"title\": \"" + b.getTitle() + "\",\n");
                writer.write("    \"author\": \"" + b.getAuthor() + "\",\n");
                writer.write("    \"genre\": \"" + b.getGenre() + "\",\n");
                writer.write("    \"pages\": " + b.getPages() + "\n");
                writer.write("  }");
                if (i < books.size() - 1) writer.write(",");
                writer.write("\n");
            }
            writer.write("]");
        } catch (IOException e) {
            System.out.println("Fel vid sparande till JSON-fil: " + e.getMessage());
        }
    }

    // JSON-deserialisering: Återställer ALLA fält från filen utan att krascha vid fel
    public void loadFromJSON() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            String data = content.toString().replace("[", "").replace("]", "").trim();
            if (data.isEmpty()) return;

            String[] objects = data.split("\\},");
            books.clear();

            for (String obj : objects) {
                String cleanObj = obj.replace("{", "").replace("}", "").trim();
                
                // Extraherar samtliga 5 fält ur JSON-strängen
                String id = extractValue(cleanObj, "id");
                String title = extractValue(cleanObj, "title");
                String author = extractValue(cleanObj, "author");
                String genre = extractValue(cleanObj, "genre");
                int pages = Integer.parseInt(extractValue(cleanObj, "pages"));

                books.add(new Book(id, title, author, genre, pages));
            }
            Collections.sort(books); // Håller listan sorterad efter inläsning
        } catch (Exception e) {
            System.out.println("Runtime-fel förhindrat vid JSON-inläsning: " + e.getMessage());
        }
    }

    // Hjälpmetod för att parsa JSON-strängar manuellt och robust
    private String extractValue(String block, String key) {
        int keyIndex = block.indexOf("\"" + key + "\"");
        int colonIndex = block.indexOf(":", keyIndex);
        int startIndex = block.indexOf("\"", colonIndex);
        
        // Om det är ett heltal (som pages) saknas citattecken runt värdet
        if (startIndex == -1 || startIndex > block.indexOf("\n", colonIndex) && block.indexOf("\n", colonIndex) != -1) {
            int endIndex = block.indexOf(",", colonIndex);
            if (endIndex == -1) endIndex = block.length();
            return block.substring(colonIndex + 1, endIndex).trim();
        }
        
        int endIndex = block.indexOf("\"", startIndex + 1);
        return block.substring(startIndex + 1, endIndex);
    }
}