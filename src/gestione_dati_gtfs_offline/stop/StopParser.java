package gestione_dati_gtfs_offline.stop;/* questo codice serve a leggere ed ad interpretare i dati gtfs*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StopParser {
    private static final String FILE_PATH = "C:/Users/damiano/Desktop/progetto java damose/rome_static_gtfs_test/stops.txt"; // Nome del file da leggere

    /**
     * Metodo che legge il file stops.txt e restituisce una lista di fermate.
     * Ogni fermata è rappresentata da un oggetto Stop con ID, nome, latitudine e longitudine.
     *
     * @return Lista di oggetti Stop con i dati delle fermate
     */
    public static List<Stop> parseStops() {
        List<Stop> stops = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // Salta la prima riga che contiene l'intestazione delle colonne

            while ((line = br.readLine()) != null) {
                // Divide la riga del CSV tenendo conto delle virgolette per evitare errori nello split
                String[] parts = splitCSVLine(line);

                if (parts.length >= 6) {  // Assicuriamoci che la riga abbia abbastanza colonne
                    String stopId = parts[0].trim();    // Colonna 0: ID della fermata
                    String stopName = parts[2].trim();  // Colonna 2: Nome della fermata
                    double latitude;
                    double longitude;

                    try {
                        latitude = Double.parseDouble(parts[4].trim()); // Colonna 4: Latitudine
                        longitude = Double.parseDouble(parts[5].trim()); // Colonna 5: Longitudine
                    } catch (NumberFormatException e) {
                        System.err.println("Errore nel parsing delle coordinate per la fermata: " + stopId);
                        continue; // Se c'è un errore nei dati, salta questa riga e passa alla successiva
                    }

                    // Aggiunge la fermata alla lista
                    stops.add(new Stop(stopId, stopName, latitude, longitude));
                }
            }
        } catch (IOException e) {
            // Se il file non esiste o non è leggibile, stampa un messaggio di errore
            e.printStackTrace();
        }

        return stops;
    }

    /**
     * Metodo che restituisce una mappa delle fermate con il nome come chiave
     * e un array di double con latitudine e longitudine come valore.
     *
     * @return HashMap<String, double[]> - Nome della fermata → [Latitudine, Longitudine]
     */
    public static HashMap<String, double[]> getLocationData() {
        HashMap<String, double[]> locationData = new HashMap<>();
        List<Stop> stops = parseStops(); // Chiama il metodo parseStops() per ottenere la lista di fermate

        // Per ogni fermata, inserisce i dati nella mappa con Nome Fermata → [Latitudine, Longitudine]
        for (Stop stop : stops) {
            locationData.put(stop.getStopName(), new double[]{stop.getLatitude(), stop.getLongitude()});
        }

        return locationData;
    }

    /**
     * Metodo per gestire la lettura di un file CSV, evitando errori con le virgolette.
     * Se un valore è tra virgolette, viene considerato come un'unica entità.
     *
     * @param line Stringa di una riga del file CSV
     * @return Array di stringhe contenente i valori divisi correttamente
     */
    private static String[] splitCSVLine(String line) {
        List<String> values = new ArrayList<>();
        boolean insideQuotes = false;
        StringBuilder currentValue = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                insideQuotes = !insideQuotes; // Cambia stato delle virgolette
            } else if (c == ',' && !insideQuotes) {
                // Se troviamo una virgola fuori dalle virgolette, separiamo il valore
                values.add(currentValue.toString().trim());
                currentValue.setLength(0); // Svuota il buffer per il prossimo valore
            } else {
                currentValue.append(c); // Aggiunge il carattere corrente al valore attuale
            }
        }

        // Aggiunge l'ultimo valore trovato nella riga
        values.add(currentValue.toString().trim());

        return values.toArray(new String[0]);
    }
    public static List<String> getLinesForStop(String stopName) {
        List<String> lines = new ArrayList<>(); // Usa java.util.List
        String filePath = "C:/Users/damiano/Desktop/progetto java damose/rome_static_gtfs_test/routes.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Salta la riga di intestazione

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String routeId = parts[0].trim();
                    String routeName = parts[2].trim();

                    if (routeName.contains(stopName)) {
                        lines.add(routeId + " - " + routeName);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    public static List<String> getScheduleForStop(String stopName) {
        List<String> schedule = new ArrayList<>();
        String filePath = "C:/Users/damiano/Desktop/progetto java damose/rome_static_gtfs_test/stop_times.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Salta l'intestazione

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String stopId = parts[3].trim();
                    String arrivalTime = parts[1].trim();

                    if (stopId.equals(stopName)) {
                        schedule.add(arrivalTime);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return schedule;
    }

}
