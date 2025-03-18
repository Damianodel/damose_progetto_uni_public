package gestione_dati_gtfs_offline.trips;

import java.io.*;
import java.util.*;

public class TripsParser {

    public static List<Trips> parseTrips(String filePath) {
        List<Trips> trips = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Salta l'intestazione

            while ((line = br.readLine()) != null) {
                String[] parts = splitCSVLine(line);

                if (parts.length >= 9) { // Assicurati che ci siano abbastanza colonne
                    String routeId = parts[0].trim();
                    String serviceId = parts[1].trim();
                    String tripId = parts[2].trim();
                    String tripHeadsign = parts[3].trim();
                    String tripShortName = parts[4].trim();
                    int directionId = parseInteger(parts[5].trim(), -1);  // Valore di default -1
                    String blockId = parts[6].trim();
                    String shapeId = parts[7].trim();
                    int wheelchairAccessible = parseInteger(parts[8].trim(), -1);  // Valore di default -1

                    Trips trip = new Trips(routeId, serviceId, tripId, tripHeadsign, tripShortName, directionId,
                            blockId, shapeId, wheelchairAccessible);
                    trips.add(trip);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return trips;
    }

    // Funzione per analizzare gli interi con gestione degli errori
    private static int parseInteger(String value, int defaultValue) {
        if (value.isEmpty()) {
            return defaultValue; // Restituisce il valore di default se la stringa è vuota
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue; // Restituisce il valore di default in caso di errore
        }
    }

    // Funzione di supporto per il parsing delle righe CSV
    private static String[] splitCSVLine(String line) {
        List<String> values = new ArrayList<>();
        boolean insideQuotes = false;
        StringBuilder currentValue = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                insideQuotes = !insideQuotes;
            } else if (c == ',' && !insideQuotes) {
                values.add(currentValue.toString().trim());
                currentValue.setLength(0); // Resetta il buffer
            } else {
                currentValue.append(c);
            }
        }
        values.add(currentValue.toString().trim()); // Aggiungi l'ultimo valore
        return values.toArray(new String[0]);
    }
}
