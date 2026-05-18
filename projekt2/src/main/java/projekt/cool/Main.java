package projekt.cool;

import java.util.ArrayList;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import kong.unirest.Unirest;

public class Main {
    // Listor för att spara data lokalt
    private static ArrayList<Book> bookList = new ArrayList<>();
    private static ArrayList<Magazine> magazineList = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static Gson gson = new Gson();

    public static void main(String[] args) {
        boolean kör = true;
        while (kör) {
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

    private static void fetchData() {
        try {
            String bookResponse = Unirest.get("http://10.151.168.5:3122/books").asString().getBody();

            // LibraryData fetchedBooks = gson.fromJson(bookResponse, bookListType);

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

    private static void Showlibrary() {
        System.out.println("--- Här är alla dina böcker ---");
        for (Book b : bookList) {
            System.out.println("Id: " + b.getId() + " Titel: " + b.getTitle() + " Författare: " + b.getAuthor()
                    + " Sidor: " + b.getPages());
        }
        System.out.println("\n--- Dina Tidningar ---");
        for (Magazine m : magazineList) {
            System.out.println("ID: " + m.getId() + "  Titel: " + m.getTitle() + "  Nummer: " + "Issuenummer: " + m.getIssueNumber() + "Publishedyear: " + m.publishedYear());
        }
    }

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