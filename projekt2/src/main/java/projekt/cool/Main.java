package projekt.cool;

import java.util.ArrayList;
import java.util.Scanner;
import com.google.gson.Gson;
import kong.unirest.Unirest;


public class Main {
    // Listor för att spara data lokalt
    private static ArrayList<Book> bookList = new ArrayList<>();
    private static ArrayList<Magazine> magazineList = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    Gson gson = new Gson();

    public static void main(String[] args) {
        boolean kör = true;
        while (kör) {
            System.out.println("\n Bibliotek");
            System.out.println("1. Hämta data från servern");
            System.out.println("2. Visa böcker och tidningar");
            System.out.println("3. Lägg till ny bok eller tidning");
            System.out.println("4. Avslut kod");
            System.out.println("Ditt val:");
        }

        int val = scanner.nextInt();
        scanner.nextLine();

        if (val == 1) {
            fetchData();

        }

        else if (val == 2) {
            Showlibrary();
        }

        else if (val == 3) {
            addNewBookManual();
        }

        else if (val == 4) {
            System.out.println("Avslutar programmet...");
            kör = false;
        }

    }

    private static void fetchData() {
        String response = Unirest.get("http://10.151.168.5:3122/").asString().getBody();
        System.out.println("Data hittad och hämtad");
    }

    private static void Showlibrary() {
        System.out.println("--- Här är alla dina böcker ---");
        for (Book b : bookList) {
            System.out.println("Id: " + b.getId() + " Titel: " + b.getTitle() + " Författare: " + b.getAuthor());
        }
        System.out.println("\n--- Dina Tidningar ---");
        for (Magazine m : magazineList) {
            System.out.println("ID: " + m.getId() + " | Titel: " + m.getTitle() + " | Nummer: " + m.getIssueNumber());
        }
    }

    private static void addNewBookManual() {
        System.out.print("Ange ID: ");
        String id = scanner.nextLine();
        System.out.print("Ange Titel: ");
        String title = scanner.nextLine();
        System.out.print("Ange Författare: ");
        String author = scanner.nextLine();

        Book myBook = new Book(id, title, true, author, "Okänd", 200);
        bookList.add(myBook);
        System.out.println("Boken är tillagd!");
    }
}