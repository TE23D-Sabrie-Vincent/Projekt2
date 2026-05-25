package projekt.cool;

import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import kong.unirest.Unirest;

/*
 * Författare: [Ditt Namn]
 * Beskrivning: Logikklass (Controller/Model) för att hantera samlingen av böcker i systemet.
 * Klassen ansvarar för att spara objekt, radera dem, utföra sökningar, samt 
 * omvandla listorna till och från JSON-format för permanent datalagring.
 */

public class LibraryManager {
    // Använder en generisk ArrayList för att spara objekten
    private List<Book> books;
    private static final String FILE_PATH = "books.json";
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<SuspendedUser> suspendedUsers = new ArrayList<>();
    private Gson gson = new Gson();
    private static String SERVER_URL = "http://10.151.168.5:3122";

    /*
     * Konstruktor för LibraryManager.
     * Vad den gör: Initierar boklistan och anropar loadFromJSON för att direkt läsa
     * in sparad data vid programstart.
     * Inparametrar: Inga
     * Returvärde: Inget (Konstruktor)
     */
    public LibraryManager() {
        this.books = new ArrayList<>();
    }

    /*
     * Vad metoden gör: Lägger till ett nytt Book-objekt i listan, sorterar hela
     * listan
     * (efter titel) och sparar därefter de uppdaterade ändringarna permanent till
     * JSON-filen.
     * Inparametrar: Book book (Objektet som ska läggas till i biblioteket)
     * Returvärde: Inget (void)
     */
    public void addBook(Book book) {
        books.add(book);
        Collections.sort(books); // Sorterar automatiskt listan efter titel vid tillägg

        try {
            Unirest.post(SERVER_URL + "/books").header("Content-Type", "application/json").body(gson.toJson(book))
                    .asString();
        } catch (Exception e) {
            System.out.println("Kunde inte synka tillägget med servern: " + e.getMessage());
        }
    }

    // Kan ta bort objekt baserat på unikt ID
    /*
     * Vad metoden gör: Letar upp ett specifikt bokobjekt baserat på dess ID.
     * Om boken hittas tas den bort från listan och JSON-filen uppdateras.
     * Inparametrar: String id (Det unika ID:t för boken som ska raderas)
     * Returvärde: boolean (true om borttagningen lyckades, annars false)
     */
    public boolean removeBook(String id) {
        for (Book b : books) {
            if (b.getId().equalsIgnoreCase(id)) {
                books.remove(b);

                try {
                    Unirest.delete(SERVER_URL + "/books/" + id).asString();
                    return true;
                } catch (Exception e) {
                    System.out.println("Fel vid bortagning på servern: " + e.getMessage());
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * Vad metoden gör: Returnerar hela den aktuella minneslistan med bibliotekets
     * böcker.
     * Inparametrar: Inga
     * Returvärde: List<Book> (Listan med alla inlästa Book-objekt)
     */
    public List<Book> getBooks() {
        return books;
    }

    /*
     * Vad metoden gör: Filtrerar boklistan via en sökalgoritm. Alla böcker vars
     * genre
     * matchar söksträngen läggs till i en ny resultatlista.
     * Inparametrar: String genre (Textsträngen med genren som eftersöks)
     * Returvärde: List<Book> (En ny arraylista med enbart de böcker som matchade
     * sökningen)
     */
    // Kan hitta/söka efter specifika objekt via en algoritm (Här söker vi på genre)
    public List<Book> searchByGenre(String genre) {
        List<Book> results = new ArrayList<>();
        for (Book b : books) {
            if (b.getGenre().equalsIgnoreCase(genre)) {
                results.add(b);
            }
        }
        return results;
    }

    // JSON-serialisering: Sparar ALLA fält till filen
    /*
     * Vad metoden gör: Serialiserar boklistan till en formaterad JSON-sträng och
     * sparar över innehållet i målfilen. Metoden är skyddad med en try-catch för
     * att hindra I/O-fel.
     * Inparametrar: Inga
     * Returvärde: Inget (void)
     */
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
                if (i < books.size() - 1)
                    writer.write(",");
                writer.write("\n");
            }
            writer.write("]");
        } catch (IOException e) {
            System.out.println("Fel vid sparande till JSON-fil: " + e.getMessage());
        }
    }

    // JSON-deserialisering: Återställer ALLA fält från filen utan att krascha vid
    // fel
    /*
     * Vad metoden gör: Öppnar JSON-filen och läser in textdatan. Texten parsas
     * (deserialiseras) manuellt tillbaka till riktiga Book-objekt som sparas i
     * book-listan.
     * En try-catch fångar upp eventuella runtime-fel om filen skulle vara skadad.
     * Inparametrar: Inga
     * Returvärde: Inget (void)
     */
    public void loadFromJSON() {
        File file = new File(FILE_PATH);
        if (!file.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            String data = content.toString().replace("[", "").replace("]", "").trim();
            if (data.isEmpty())
                return;

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
    /*
     * Vad metoden gör: Hjälpmetod för parsing som extraherar det faktiska värdet
     * tillhörande en specifik nyckel inuti ett JSON-block.
     * Inparametrar: String block (Ett enskilt bokobjekt i JSON-format), String key
     * (Nyckeln, t.ex. "title")
     * Returvärde: String (Värdet kopplat till nyckeln, rensat från citattecken)
     */
    private String extractValue(String block, String key) {
        int keyIndex = block.indexOf("\"" + key + "\"");
        int colonIndex = block.indexOf(":", keyIndex);
        int startIndex = block.indexOf("\"", colonIndex);

        // Om det är ett heltal (som pages) saknas citattecken runt värdet
        if (startIndex == -1 || startIndex > block.indexOf("\n", colonIndex) && block.indexOf("\n", colonIndex) != -1) {
            int endIndex = block.indexOf(",", colonIndex);
            if (endIndex == -1)
                endIndex = block.length();
            return block.substring(colonIndex + 1, endIndex).trim();
        }

        int endIndex = block.indexOf("\"", startIndex + 1);
        return block.substring(startIndex + 1, endIndex);
    }

    /*
     * Vad metoden gör: Letar upp en specifik kund baserat på e-postadress.
     * Inparametrar: String email
     * Returvärde: User (Kundobjektet om det hittas, annars null)
     */
    public User findUserByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    /*
     * Vad metoden gör: Kontrollerar om ett kund-ID finns i spärrlistan.
     * Inparametrar: String userId
     * Returvärde: boolean (true om kunden får låna, false om avstängd)
     */
    public boolean canBorrow(String userId) {
        for (SuspendedUser su : suspendedUsers) {
            if (su.getUserId().equals(userId)) {
                return false; // Hittades i spärrlistan
            }
        }
        return true; // Får låna
    }

    /*
     * Vad metoden gör: Sorterar kunderna i bokstavsordning och returnerar listan.
     * Inparametrar: Inga
     * Returvärde: List<User>
     */
    public List<User> getSortedUsers() {
        Collections.sort(users);
        return users;
    }

    public void fetchDataFromServer() {
        try {
            System.out.println("Ansluter till biblioteksserver...");

            // 1. Hämta alla böcker från servern
            String booksJson = Unirest.get(SERVER_URL + "/books").asString().getBody();
            this.books = gson.fromJson(booksJson, new TypeToken<ArrayList<Book>>() {
            }.getType());

            // 2. Hämta alla kunder (users) från servern
            String usersJson = Unirest.get(SERVER_URL + "/users").asString().getBody();
            this.users = gson.fromJson(usersJson, new TypeToken<ArrayList<User>>() {
            }.getType());

            // 3. Hämta alla avstängda kunder från servern
            String suspendedJson = Unirest.get(SERVER_URL + "/suspendedUsers").asString().getBody();
            this.suspendedUsers = gson.fromJson(suspendedJson, new TypeToken<ArrayList<SuspendedUser>>() {
            }.getType());

            Collections.sort(this.books);
            Collections.sort(this.users);
            System.out.println("Serverdata laddad, inga problem ännu");
        } catch (Exception e) {
            System.out.println("Nätverksfel, kunde inte hämta data: " + e.getMessage());
        }
    }
}