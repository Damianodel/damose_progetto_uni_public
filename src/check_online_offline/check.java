package check_online_offline;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class check {
    private static final String LOCAL_PATH = "C://Users//0utente//Desktop//damose_progetto_uni_public-master//rome_static_gtfs_test";
    private static final String[] REALTIME_URLS = {
            "https://romamobilita.it/sites/default/files/rome_rtgtfs_vehicle_positions_feed.pb",
            "https://romamobilita.it/sites/default/files/rome_rtgtfs_trip_updates_feed.pb",
            "https://romamobilita.it/sites/default/files/rome_rtgtfs_service_alerts_feed.pb"
    };
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

    private static void checkAndDownloadData() {
        if (isOnline) {
            boolean online = true;
            for (String url : REALTIME_URLS) {
                if (!downloadFile(url, getFileName(url))) {
                    online = false;
                    break;
                }
            }
            if (!online) {
                System.out.println("Stato offline: uso dati statici da " + LOCAL_PATH);
                useStaticData();
                // Aggiungi il messaggio di avviso
                System.out.println("AVVISO: Problemi nel recupero dei dati GTFS real-time.");
            } else {
                System.out.println("Dati real-time scaricati con successo.");
            }
        } else {
            System.out.println("Sei OFFLINE: uso dati statici da " + LOCAL_PATH);
            useStaticData();
        }
    }

    private static boolean downloadFile(String fileURL, String saveAs) {
        try {
            URL url = new URL(fileURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            if (connection.getResponseCode() != 200) {
                System.out.println("Errore nel download da " + fileURL);
                return false;
            }

            try (InputStream in = connection.getInputStream();
                 FileOutputStream out = new FileOutputStream(saveAs)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            System.out.println("Scaricato: " + saveAs);
            return true;
        } catch (Exception e) {
            System.out.println("Download fallito per " + fileURL + ": " + e.getMessage());
            return false;
        }
    }

    private static void useStaticData() {
        File folder = new File(LOCAL_PATH);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    System.out.println("Utilizzando file statico: " + file.getName());
                }
            } else {
                System.out.println("Nessun file statico trovato.");
            }
        } else {
            System.out.println("Percorso dati statici non trovato.");
        }
    }

    private static String getFileName(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

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
                    checkAndDownloadData();
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

    private static boolean checkInternetConnection() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return address.isReachable(2000); // Timeout di 2 secondi
        } catch (IOException e) {
            return false;
        }
    }
}