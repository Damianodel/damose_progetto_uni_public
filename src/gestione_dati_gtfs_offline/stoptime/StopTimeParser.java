package gestione_dati_gtfs_offline.stoptime;

import java.io.*;
import java.util.*;

public class StopTimeParser {

    public static List<StopTime> parseStopTimes(String filePath) {
        List<StopTime> stopTimes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Salta l'intestazione

            while ((line = br.readLine()) != null) {
                String[] parts = splitCSVLine(line);

                if (parts.length >= 10) { // Assicurati che ci siano abbastanza colonne
                    String tripId = parts[0].trim();
                    String arrivalTime = parts[1].trim();
                    String departureTime = parts[2].trim();
                    String stopId = parts[3].trim();
                    int stopSequence = parseInteger(parts[4].trim(), -1);  // Default value -1 in caso di errore
                    String stopHeadsign = parts[5].trim();
                    int pickupType = parseInteger(parts[6].trim(), -1); // Default value -1 in caso di errore
                    int dropOffType = parseInteger(parts[7].trim(), -1); // Default value -1 in caso di errore
                    double shapeDistTraveled = parseDouble(parts[8].trim(), -1.0); // Default value -1.0 in caso di errore
                    boolean timepoint = parseBoolean(parts[9].trim()); // Parso direttamente il booleano

                    StopTime stopTime = new StopTime(tripId, arrivalTime, departureTime, stopId, stopSequence,
                            stopHeadsign, pickupType, dropOffType, shapeDistTraveled, timepoint);
                    stopTimes.add(stopTime);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopTimes;
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

    // Funzione per analizzare i numeri decimali (double) con gestione degli errori
    private static double parseDouble(String value, double defaultValue) {
        if (value.isEmpty()) {
            return defaultValue; // Restituisce il valore di default se la stringa è vuota
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue; // Restituisce il valore di default in caso di errore
        }
    }

    // Funzione per analizzare i booleani
    private static boolean parseBoolean(String value) {
        return !value.isEmpty() && value.equals("1"); // Restituisce true se il valore è "1", false altrimenti
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
