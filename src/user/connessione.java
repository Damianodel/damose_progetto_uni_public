package user;

import java.io.IOException;
import java.net.InetAddress;

public class connessione {
    private static boolean isOnline = false;

    public static void main(String[] args) {
        // Avvia il monitoraggio dello stato della connessione
        startConnectionMonitor();

        // Simulazione dell'applicazione in esecuzione
        while (true) {
            try {
                Thread.sleep(10000); // Mantiene il programma in esecuzione
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Metodo per avviare il monitoraggio della connessione
    private static void startConnectionMonitor() {
        Thread connectionMonitor = new Thread(() -> {
            while (true) {
                boolean currentStatus = checkInternetConnection();
                if (currentStatus != isOnline) {
                    isOnline = currentStatus;
                    if (isOnline) {
                        System.out.println("Sei ONLINE.");
                    } else {
                        System.out.println("Sei OFFLINE.");
                    }
                }
                try {
                    Thread.sleep(5000); // Controlla la connessione ogni 5 secondi
                } catch (InterruptedException e) {
                    System.out.println("Errore nel monitoraggio della connessione: " + e.getMessage());
                }
            }
        });
        connectionMonitor.setDaemon(true); // Chiude il thread quando il programma termina
        connectionMonitor.start();
    }

    // Metodo per verificare la connessione a Internet
    private static boolean checkInternetConnection() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return address.isReachable(2000); // Timeout di 2 secondi
        } catch (IOException e) {
            return false;
        }
    }
}