package projekt.cool;

import java.util.ArrayList;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import kong.unirest.Unirest;

/* * Författare: Vincent Sabrie
 * Beskrivning: Programmet fungerar som ett digitalt bibliotekssystem för hantering av litteratur. 
 * Det kommunicerar med en extern server via ett API (Unirest) för att hämta JSON-data om böcker 
 * och tidningar, deserialiserar denna till Java-objekt (Gson) och sparar dem i lokala listor. 
 * Användaren kan via en interaktiv konsolmeny visa biblioteket eller lägga till nya böcker/tidningar manuellt.
 */

public class Main {
    // Listor för att spara data lokalt

    // bookList (ArrayList) lagrar de hämtade och manuellt tillagda Book-objekten lokalt i minnet.
    private static ArrayList<Book> bookList = new ArrayList<>();

    // magazineList (ArrayList) lagrar de hämtade och manuellt tillagda Magazine-objekten lokalt i minnet.
    private static ArrayList<Magazine> magazineList = new ArrayList<>();

    // scanner (Scanner) läser in användarens text- och sifferinmatningar från tangentbordet i konsolen. 
    private static Scanner scanner = new Scanner(System.in);
    
    // gson (Gson) används för att omvandla rå JSON-text från servern till hanterbara Java-objekt. 
    private static Gson gson = new Gson();


    /* * Metoden main är programmets startpunkt. Den driver en while-loop (meny-loop)
     * som kontinuerligt läser in användarens val och styr flödet till rätt undermetod.
     * Inparametrar: String[] args (Kommandoradsargument)
     * Returvärde: Inget (void)
     */
    public static void main(String[] args) {
        // variabeln kör (boolean) kontrollerar om huvudloopen ska fortsätta eller avslutas.
        boolean kör = true;
        while (kör) {
            // Detta är vår menyval som skrivs ut med 
            System.out.println("\n Bibliotek");
            System.out.println("1. Hämta böcker från servern");
            System.out.println("2. Visa böcker och tidningar");
            System.out.println("3. Lägg till ny bok");
            System.out.println("4. Lägg till ny Tidning");
            System.out.println("5. Avslut kod");
            System.out.println("Ditt val:");

            int val = scanner.nextInt();
            scanner.nextLine();

            if (val == 1) {
                fetchData();
                // break;
            }

            else if (val == 2) {
                Showlibrary();
                // break;
            }

            else if (val == 3) {
                addNewBookManual();
                // break;
            }

            else if (val == 4) {
                addNewMagazineManual();
            }

            else if (val == 5) {
                System.out.println("Avslutar programmet...");
                kör = false;
            }
        }
    }


    /*  Metoden fetchData gör asynkrona nätverksanrop (GET-requests) till ett API för att hämta 
     * böcker och tidningar i textformatet JSON. Den rensar de gamla lokala listorna och fyller 
     * dem med den nya uppdaterade datan från servern. Metoden är inkapslad i en try-catch för 
     * att hantera eventuella nätverksfel.
     * Inparametrar: Inga
     * Returvärde: Inget (void)
     */
    private static void fetchData() {
        try {
            String bookResponse = Unirest.get("http://10.151.168.5:3122/books").asString().getBody();


            Type bookListType = new TypeToken<ArrayList<Book>>() {
            }.getType();

            ArrayList<Book> fetchedBooks = gson.fromJson(bookResponse, bookListType);

            bookList.clear();
            bookList.addAll(fetchedBooks);

            System.out.println("Hämtade " + fetchedBooks.size() + " böcker.");

            String magResponse = Unirest.get("http://10.151.168.5:3122/magazines").asString().getBody();
            Type magListType = new TypeToken<ArrayList<Magazine>>() {
            }.getType();
            ArrayList<Magazine> fetchedMags = gson.fromJson(magResponse, magListType);

            magazineList.clear();
            magazineList.addAll(fetchedMags);

            System.out.println(
                    "Klart! Hämtade " + fetchedBooks.size() + " böcker och " + fetchedMags.size() + " tidningar.");

        } catch (Exception e) {
            System.out.println("Kunde inte hämta datan :( " + e.getMessage());
        }
    }

    /* * Metoden Showlibrary skriver ut det aktuella innehållet i biblioteket till konsolen. 
     * Den använder enhanced for-loops för att stega igenom listorna och formaterar utskriften 
     * med hjälp av objektens inbyggda egenskaper och get-metoder.
     * Inparametrar: Inga
     * Returvärde: Inget (void)
     */
    private static void Showlibrary() {
        System.out.println("--- Här är alla dina böcker ---");
        for (Book b : bookList) {
            System.out.println("Id: " + b.getId() + " Titel: " + b.getTitle() + " Författare: " + b.getAuthor()
                    + " Sidor: " + b.getPages());
        }
        System.out.println("\n--- Dina Tidningar ---");
        for (Magazine m : magazineList) {
            System.out.println("ID: " + m.getId() + "  Titel: " + m.getTitle() + "  Nummer: " + "Issuenummer: " + m.getIssueNumber() + "Publishedyear: " + m.getPublishedYear());
        }
    }

    /* * Metoden addNewBookManual sköter det användarinteraktiva flödet för att skapa en bok manuellt. 
     * Den frågar användaren efter nödvändig data steg för steg, instansierar ett nytt Book-objekt 
     * och sparar referensen i den lokala boklistan.
     * Inparametrar: Inga
     * Returvärde: Inget (void)
     */

    private static void addNewBookManual() {
        System.out.print("Ange ID: ");
        String id = scanner.nextLine();

        System.out.print("Ange Titel: ");
        String title = scanner.nextLine();

        System.out.print("Ange Författare: ");
        int pages = scanner.nextInt();

        System.out.print("Ange Antal sidor: "); 
        String author = scanner.nextLine();

        Book myBook = new Book(id, title, true, author, "Okänd", pages);
        bookList.add(myBook);
        System.out.println("Boken är tillagd");
    }

    /* * Metoden addNewMagazineManual hanterar skapandet av ett nytt tidningsobjekt från konsolen. 
     * Den läser in unika egenskaper för tidningar (utgåvonummer samt publiceringsår) och 
     * placerar det nya Magazine-objektet i den avsedda listan.
     * Inparametrar: Inga
     * Returvärde: Inget (void)
     */
    private static void addNewMagazineManual() {
        System.out.print("Ange ID: ");
        String id = scanner.nextLine();
        System.out.print("Ange Titel: ");
        String title = scanner.nextLine();
        System.out.print("Ange Nummer (Issue number): ");
        int issueNumber = scanner.nextInt();
        System.out.println("Ange År publicerad: ");
        int publishedYear = scanner.nextInt();

        scanner.nextLine();

        Magazine myMagazine = new Magazine(id, title, true, issueNumber, publishedYear);

        magazineList.add(myMagazine);
        System.out.println("Tidningen är tillagd!");
    }

}