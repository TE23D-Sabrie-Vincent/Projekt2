package projekt.cool;
import java.util.Scanner;


/*
 * Författare: Vincent Sabrie
 * Beskrivning: Main-klassen utgör programmets startpunkt och ansvarar för användargränssnittet (View).
 * Den presenterar textbaserade menyer för användaren, tar emot input och skickar 
 * vidare instruktioner till LibraryManager-objektet för att ändra eller hämta data.
 * Uppbyggnaden följer grundprincipen att separera UI (användargränssnitt) från programmets logik/data.
 */

public class Main {
    /*
     * Vad metoden gör: Programmets huvudmetod som driver meny-loopen. Den tar emot inmatning 
     * från användaren och anropar rätt funktioner i LibraryManager. Metoden innehåller även 
     * felhantering (try-catch) för att förhindra krascher vid inmatning av bokstäver istället för siffror.
     * Inparametrar: String[] args (Kommandoradsargument)
     * Returvärde: Inget (void)
     */
    public static void main(String[] args) {
        LibraryManager manager = new LibraryManager();
    Scanner scanner = new Scanner(System.in);
        // variabeln kör (boolean) kontrollerar om huvudloopen ska fortsätta eller avslutas.
        boolean kör = true;
        while (kör) {
            // Detta är vår menyval som skrivs ut med 
            System.out.println("\n Meny: ");
            System.out.println("1. Visa alla böcker och tidningar");
            System.out.println("2. Lägg till bok");
            System.out.println("3. Ta bort med ID");
            System.out.println("4. Sök via genre");
            System.out.println("5. Hitta kund via epost");
            System.out.println("6. Visa alla kunder (A-Ö)");
            System.out.println("7. Avslut Koden");
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

                
                int pages = 0;
                while (true) {
                    try {
                        System.out.print("Sidor: ");
                        pages = Integer.parseInt(scanner.nextLine());
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Fel! Ange antal sidor med siffror.");
                    }
                }
                manager.addBook(new Book(id, title, author, genre, pages));
                System.out.println("Feedback: Boken har sparats!");
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
                System.out.print("Ange kundens e-postadress: ");
                String emailInput = scanner.nextLine();
                User foundUser = manager.findUserByEmail(emailInput);
                
                if (foundUser != null) {
                    System.out.println("Kund hittad: " + foundUser.getName() + " (ID: " + foundUser.getId() + ")");
                    // Använder algoritmen som kollar mot listan med avstängda
                    if (manager.canBorrow(foundUser.getId())) {
                        System.out.println("Status: Får låna.");
                    } else {
                        System.out.println("Status: AVSTÄNGD! Får ej låna.");
                    }
                } else {
                    System.out.println("Ingen kund med den e-postadressen hittades.");
                }
            }

            else if (val.equals("6")) {
                System.out.println("\n--- Registrerade kunder (A-Ö) ---");
                if (manager.getSortedUsers().isEmpty()) System.out.println("Inga kunder registrerade (datan har inte laddats ännu).");
                for (User u : manager.getSortedUsers()) {
                    System.out.println(u.getName() + " | E-post: " + u.getEmail() + " | ID: " + u.getId());
                }
            }

            else if (val.equals("7")) {
                System.out.println("Avslutar programmet...");
                kör = false;
            }
        }
        scanner.close();
    }
}    