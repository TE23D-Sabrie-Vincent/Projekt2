package projekt.cool;

import java.util.Scanner;


/* * Författare: Vincent Sabrie
 * Beskrivning: Programmet fungerar som ett digitalt bibliotekssystem för hantering av litteratur. 
 * Det kommunicerar med en extern server via ett API (Unirest) för att hämta JSON-data om böcker 
 * och tidningar, deserialiserar denna till Java-objekt (Gson) och sparar dem i lokala listor. 
 * Användaren kan via en interaktiv konsolmeny visa biblioteket eller lägga till nya böcker/tidningar manuellt.
 */

public class Main {
    public static void main(String[] args) {
        LibraryManager manager = new LibraryManager();
    Scanner scanner = new Scanner(System.in);
        // variabeln kör (boolean) kontrollerar om huvudloopen ska fortsätta eller avslutas.
        boolean kör = true;
        while (kör) {
            // Detta är vår menyval som skrivs ut med 
            System.out.println("\n Meny: ");
            System.out.println("1. Visa alla böcker och tidningar");
            System.out.println("2. Lägg till");
            System.out.println("3. Ta bort med ID");
            System.out.println("4. Sök via genre");
            System.out.println("5. Avslut kod");
            System.out.println("Ditt val:");

            String val = scanner.nextLine().trim();

            if (val.equals("1")) {
                System.out.println("\n--- Böcker ---");
                if (manager.getBooks().isEmpty()) System.out.println("Biblioteket är tomt.");
                for (Book b : manager.getBooks()) System.out.println(b);
            }

            else if (val.equals("2")) {
                System.out.print("ID: "); String id = scanner.nextLine();
                System.out.print("Titel: "); String title = scanner.nextLine();
                System.out.print("Författare: "); String author = scanner.nextLine();
                System.out.print("Genre: "); String genre = scanner.nextLine();
            }

            else if (val.equals("3")) {
                System.out.print("Ange ID att ta bort: ");
                System.out.println(manager.removeBook(scanner.nextLine()) ? "Feedback: Boken borttagen." : "Feedback: Hittade inte ID.");
            }

            else if (val.equals("4")) {
                System.out.print("Ange genre: ");
                var results = manager.searchByGenre(scanner.nextLine());
                if (results.isEmpty()) System.out.println("Inga matchningar.");
                for (Book b : results) System.out.println(b);
            }

            else if (val.equals("5")) {
                System.out.println("Avslutar programmet...");
                kör = false;
            }
        }
        scanner.close();
    }
}    