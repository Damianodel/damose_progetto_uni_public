package user;import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Login {
    private static final String USERS_FILE = "C:\\Users\\damiano\\IdeaProjects\\damose progetto uni\\src\\user\\user.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Scegli un'opzione:");
        System.out.println("1. Login");
        System.out.println("2. Registrazione");
        System.out.print("Scelta: ");
        int scelta = scanner.nextInt();
        scanner.nextLine(); // Consuma il newline

        if (scelta == 1) {
            if (authenticateUser(scanner)) {
                System.out.println("Login effettuato con successo!");
            } else {
                System.out.println("Login fallito! Username o password errati.");
            }
        } else if (scelta == 2) {
            registerUser(scanner);
        } else {
            System.out.println("Scelta non valida.");
        }
        scanner.close();
    }

    // Metodo per autenticare un utente
    private static boolean authenticateUser(Scanner scanner) {
        System.out.print("Inserisci username: ");
        String username = scanner.nextLine();
        System.out.print("Inserisci password: ");
        String password = scanner.nextLine();
        String hashedPassword = hashPassword(password);

        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(username) && parts[1].equals(hashedPassword)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Errore durante l'autenticazione: " + e.getMessage());
        }
        return false;
    }

    // Metodo per registrare un nuovo utente
    private static void registerUser(Scanner scanner) {
        System.out.print("Inserisci nuovo username: ");
        String username = scanner.nextLine();
        System.out.print("Inserisci nuova password: ");
        String password = scanner.nextLine();
        String hashedPassword = hashPassword(password);

        // Controlla se l'utente esiste già
        if (userExists(username)) {
            System.out.println("Errore: l'username è già in uso.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            bw.write(username + "," + hashedPassword);
            bw.newLine();
            System.out.println("Registrazione completata con successo!");
        } catch (IOException e) {
            System.err.println("Errore durante la registrazione: " + e.getMessage());
        }
    }

    // Metodo per controllare se un utente esiste già
    private static boolean userExists(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Errore durante la verifica dell'utente: " + e.getMessage());
        }
        return false;
    }

    // Metodo per generare un hash della password con SHA-256
    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Errore nell'hashing della password", e);
        }
    }
}
