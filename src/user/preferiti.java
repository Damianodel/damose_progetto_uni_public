package user;
import java.io.*;
import java.util.*;

class FavoriteManager {
    private static final String FAVORITES_FILE = "favorites.txt";
    private List<String> favorites;

    public FavoriteManager() {
        favorites = new ArrayList<>();
        loadFavorites();  // Carica i preferiti dal file all'avvio
    }

    // Aggiunge una fermata/linea ai preferiti
    public void addFavorite(String favorite) {
        if (!favorites.contains(favorite)) {
            favorites.add(favorite);
            saveFavorites();
            System.out.println("✅ Aggiunto ai preferiti: " + favorite);
        } else {
            System.out.println("⚠️ Questo preferito esiste già!");
        }
    }

    // Rimuove una fermata/linea dai preferiti
    public void removeFavorite(String favorite) {
        if (favorites.remove(favorite)) {
            saveFavorites();
            System.out.println("❌ Rimosso dai preferiti: " + favorite);
        } else {
            System.out.println("⚠️ Il preferito non esiste nella lista.");
        }
    }

    // Mostra l'elenco dei preferiti
    public void showFavorites() {
        if (favorites.isEmpty()) {
            System.out.println("🚫 Nessun preferito salvato.");
        } else {
            System.out.println("⭐ I tuoi preferiti:");
            for (String fav : favorites) {
                System.out.println("  - " + fav);
            }
        }
    }

    // Carica i preferiti dal file
    private void loadFavorites() {
        try (BufferedReader br = new BufferedReader(new FileReader(FAVORITES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                favorites.add(line);
            }
        } catch (IOException e) {
            System.out.println("📂 Nessun file di preferiti trovato. Creando uno nuovo...");
        }
    }

    //  Salva i preferiti nel file
    private void saveFavorites() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FAVORITES_FILE))) {
            for (String fav : favorites) {
                bw.write(fav);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Errore nel salvataggio dei preferiti: " + e.getMessage());
        }
    }
}

// Classe principale che gestisce l'interazione con l'utente
public class preferiti {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FavoriteManager favoriteManager = new FavoriteManager();

        while (true) {
            System.out.println("\n📍 Gestione Preferiti:");
            System.out.println("1️⃣ Aggiungi fermata/linea ai preferiti");
            System.out.println("2️⃣ Visualizza i preferiti");
            System.out.println("3️⃣ Rimuovi un preferito");
            System.out.println("4️⃣ Esci");
            System.out.print("🔹 Scelta: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consuma il newline

            switch (choice) {
                case 1:
                    System.out.print("Inserisci il nome della fermata o linea: ");
                    String addFav = scanner.nextLine();
                    favoriteManager.addFavorite(addFav);
                    break;
                case 2:
                    favoriteManager.showFavorites();
                    break;
                case 3:
                    System.out.print("Inserisci il nome della fermata o linea da rimuovere: ");
                    String removeFav = scanner.nextLine();
                    favoriteManager.removeFavorite(removeFav);
                    break;
                case 4:
                    System.out.println("Uscita...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Scelta non valida!");
            }
        }
    }
}