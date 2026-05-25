package projekt.cool;

import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import kong.unirest.Unirest;

/*
 * Författare: Vincent
 * Beskrivning: Logikklass (Controller/Model) för att hantera samlingen av böcker i systemet.
 * Klassen ansvarar för att spara objekt, radera dem, utföra sökningar, samt 
 * omvandla listorna till och från JSON-format för permanent datalagring.
 */

public class LibraryManager {
    // Använder en generisk ArrayList för att spara objekten
    private List<Book> books;
    private List<Magazine> magazines = new ArrayList<>(); 
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
        System.out.println("Ansluter till biblioteksserver och synkroniserar data...");

        // 1. HÄMTA BÖCKER LIVE FRÅN SERVER (Från /books)
        try {
            // Hämtar JSON-strängen från http://10.151.168.5:3122/books
            String booksJson = Unirest.get(SERVER_URL + "/books").asString().getBody();
            
            if (booksJson != null && !booksJson.trim().isEmpty()) {
                // Tömmer den gamla lokala listan
                this.books.clear();
                
                // Eftersom /books returnerar en direkt lista (Array), mappar vi den till en ArrayList<Book>
                ArrayList<Book> serverBooks = gson.fromJson(booksJson, new TypeToken<ArrayList<Book>>() {}.getType());
                
                if (serverBooks != null) {
                    this.books.addAll(serverBooks);
                    // Sorterar böckerna i bokstavsordning (Viktigt algoritmkrav för C-nivå!)
                    Collections.sort(this.books);
                }
            }
        } catch (Exception e) {
            System.out.println("Fel: Kunde inte hämta böcker från servern: " + e.getMessage());
        }

        // 2. HÄMTA TIDNINGAR LIVE FRÅN SERVER (Från /magazines eller /data)
        try {
            // Vi testar att hämta från /magazines. (Om er server istället använder /magazines)
            String magazinesJson = Unirest.get(SERVER_URL + "/magazines").asString().getBody();
            
            if (magazinesJson != null && !magazinesJson.trim().isEmpty()) {
                this.magazines.clear();
                ArrayList<Magazine> serverMagazines = gson.fromJson(magazinesJson, new TypeToken<ArrayList<Magazine>>() {}.getType());
                if (serverMagazines != null) {
                    this.magazines.addAll(serverMagazines);
                }
            }
        } catch (Exception e) {
            // Om servern inte har en separat /magazines endpoint, provar vi att läsa via /data istället
            try {
                String dataJson = Unirest.get(SERVER_URL + "/data").asString().getBody();
                LibraryData serverData = gson.fromJson(dataJson, LibraryData.class);
                if (serverData != null && serverData.magazines != null) {
                    this.magazines.clear();
                    this.magazines.addAll(serverData.magazines);
                }
            } catch (Exception ex) {
                System.out.println("Servernotis: Kunde inte uppdatera tidningslistan.");
            }
        }

        // 3. HÄMTA KUNDER OCH SPÄRRLISTA (Från /users och /suspendedUsers)
        try {
            String usersJson = Unirest.get(SERVER_URL + "/users").asString().getBody();
            if (usersJson != null && !usersJson.trim().isEmpty()) {
                this.users = gson.fromJson(usersJson, new TypeToken<ArrayList<User>>() {}.getType());
                Collections.sort(this.users); // Sorterar kunderna A-Ö
            }

            String suspendedJson = Unirest.get(SERVER_URL + "/suspendedUsers").asString().getBody();
            if (suspendedJson != null && !suspendedJson.trim().isEmpty()) {
                this.suspendedUsers = gson.fromJson(suspendedJson, new TypeToken<ArrayList<SuspendedUser>>() {}.getType());
            }
        } catch (Exception e) {
            System.out.println("Servernotis: Kundlistor kunde inte uppdateras just nu.");
        }

        System.out.println("Synkronisering klar! Just nu i minnet: " + this.books.size() + " böcker och " + this.magazines.size() + " tidningar.");
    }

    /*
     * Vad metoden gör: Söker efter en bok baserat på dess exakta titel (C-krav).
     * Inparametrar: String title
     * Returvärde: Book (Objektet om det hittas, annars null)
     */
    public Book searchByTitle(String title) {
        for (Book b : books) {
            if (b.getTitle().equalsIgnoreCase(title)) {
                return b;
            }
        }
        return null;
    }

    /*
     * Vad metoden gör: Lägger till en ny kund lokalt och gör en POST-request via
     * Unirest
     * till servern för att spara den permanent (C-krav).
     */
    public void addUser(User user) {
        users.add(user);
        Collections.sort(users);
        try {
            Unirest.post(SERVER_URL + "/users")
                    .header("Content-Type", "application/json")
                    .body(gson.toJson(user))
                    .asString();
        } catch (Exception e) {
            System.out.println("Fel vid uppladdning av kund: " + e.getMessage());
        }
    }

    /*
     * Vad metoden gör: Tar bort en kund med hjälp av e-post lokalt och skickar en
     * DELETE-request till servern baserat på kundens ID (C-krav).
     */
    public boolean removeUserByEmail(String email) {
        User toRemove = findUserByEmail(email);
        if (toRemove != null) {
            users.remove(toRemove);
            try {
                Unirest.delete(SERVER_URL + "/users/" + toRemove.getId()).asString();
                return true;
            } catch (Exception e) {
                System.out.println("Fel vid borttagning av kund på server: " + e.getMessage());
                return true;
            }
        }
        return false;
    }

    /*
     * Vad metoden gör: Spärrar en kund genom att skicka ett SuspendedUser-objekt
     * som en POST-request till servern (C-krav).
     */
    public void suspendUser(SuspendedUser su) {
        suspendedUsers.add(su);
        try {
            Unirest.post(SERVER_URL + "/suspendedUsers")
                    .header("Content-Type", "application/json")
                    .body(gson.toJson(su))
                    .asString();
        } catch (Exception e) {
            System.out.println("Fel vid spärrning av kund på server: " + e.getMessage());
        }
    }

    public List<Magazine> getMagazines() {
        return magazines;
    }

}