package gestione_dati_gtfs_online;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.transit.realtime.GtfsRealtime;
import com.google.transit.realtime.GtfsRealtime.TripUpdate;
import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate;

public class GTFSRealtimePredictions {
    private static final String TRIP_UPDATES_URL = "https://romamobilita.it/sites/default/files/rome_rtgtfs_trip_updates_feed.pb";
    private static final String OUTPUT_DIR = "gtfs_feeds";

    public static void main(String[] args) {
        new File(OUTPUT_DIR).mkdir(); // Crea la directory se non esiste

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            downloadFile(TRIP_UPDATES_URL);
            processTripUpdates();
        }, 0, 30, TimeUnit.SECONDS);
    }

    private static void downloadFile(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
                File outputFile = new File(OUTPUT_DIR, fileName);
                try (InputStream inputStream = connection.getInputStream();
                     FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    System.out.println("Scaricato: " + fileName);
                }
            } else {
                System.err.println("Errore nel download: " + fileUrl);
            }
            connection.disconnect();
        } catch (IOException e) {
            System.err.println("Errore di connessione: " + e.getMessage());
        }
    }

    private static void processTripUpdates() {
        File tripUpdateFile = new File(OUTPUT_DIR, "rome_rtgtfs_trip_updates_feed.pb");
        if (!tripUpdateFile.exists()) {
            System.err.println("File trip updates non trovato.");
            return;
        }

        try (InputStream input = new FileInputStream(tripUpdateFile)) {
            GtfsRealtime.FeedMessage feed = GtfsRealtime.FeedMessage.parseFrom(input);
            System.out.println("\nPrevisioni di arrivo:");

            for (GtfsRealtime.FeedEntity entity : feed.getEntityList()) {
                if (entity.hasTripUpdate()) {
                    TripUpdate tripUpdate = entity.getTripUpdate();
                    System.out.println("Trip ID: " + tripUpdate.getTrip().getTripId());

                    for (StopTimeUpdate stopTimeUpdate : tripUpdate.getStopTimeUpdateList()) {
                        long arrivalTime = stopTimeUpdate.getArrival().getTime();
                        System.out.println(" - Fermata " + stopTimeUpdate.getStopId() + " Arrivo previsto: " + arrivalTime);
                    }
                }
            }
            System.out.println("\n----------------------");
        } catch (IOException e) {
            System.err.println("Errore nella lettura del file: " + e.getMessage());
        }
    }
}