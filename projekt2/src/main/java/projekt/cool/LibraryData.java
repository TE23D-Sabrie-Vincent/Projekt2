package projekt.cool;
import java.util.ArrayList;

// en klass bara för att kunna lagra
//  listan av böckerna och magazinerna i från hemsidan

/* * Författare: Vincent sabrie 
 * Beskrivning: En databehållarklass (DTO - Data Transfer Object) som används av Gson 
 * för att mappa och strukturera den samlade JSON-datan som returneras från serverns API.
 */

public class LibraryData {
    // books (ArrayList) fungerar som en behållare för alla samlade bokobjekt från servern. 
    public ArrayList<Book> books;

    // magazines (ArrayList) fungerar som en behållare för alla samlade tidningsobjekt från servern.
    public ArrayList<Magazine> magazines;

    
}