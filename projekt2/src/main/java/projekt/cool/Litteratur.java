package projekt.cool;
// Föräldrarklassen som lånar ut egenskaper till Book och Magazine

/* Författare: Vincent Sabrie
   Beskrivning: Superklass (föräldraklass) som representerar generisk litteratur i biblioteket.
   Klassen samlar gemensamma attribut och metoder som delas av alla underklasser (t.ex. Book och Magazine).
 */
public class Litteratur {
    private String id;
    private String title;
    private boolean isAvailable;

    /*
     * * Konstruktor för att initiera ett nytt Litteratur-objekt.
     * Vad den gör: Sätter de grundläggande värdena för id, titel och
     * tillgänglighet.
     * Inparametrar: String id, String title, boolean isAvailable
     * Returvärde: Inget (Konstruktor)
     */
    public Litteratur(String id, String title, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.isAvailable = isAvailable;
    }

    /*
     * * Vad metoden gör: Returnerar objektets unika ID-sträng.
     * Inparametrar: Inga
     * Returvärde: String (Det unika ID:t)
     */
    // Behövs för Main kunna läsa av värden
    public String getId() {
        return id;
    }

    /*
     * * Vad metoden gör: Returnerar litteraturens titel.
     * Inparametrar: Inga
     * Returvärde: String (Titeln på verket)
     */
    public String getTitle() {
        return title;
    }

    /*
       Vad metoden gör: Kontrollerar om litteraturen är tillgänglig för utlåning.
       Inparametrar: Inga
       Returvärde: boolean (true om tillgänglig, annars false)
     */
    public boolean isAvailable() {
        return isAvailable;
    }

}
