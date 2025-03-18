package gestione_dati_gtfs_offline.shape;
import java.io.*;
import java.util.*;

public class ShapeParser {

    public static List<Shape> parseShapes(String filePath) {
        List<Shape> shapes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Salta l'intestazione

            while ((line = br.readLine()) != null) {
                String[] parts = splitCSVLine(line);

                if (parts.length >= 5) { // Assicurati che ci siano abbastanza colonne
                    String shapeId = parts[0].trim();
                    double shapePtLat = parseDouble(parts[1].trim(), Double.NaN);  // Valore di default Double.NaN
                    double shapePtLon = parseDouble(parts[2].trim(), Double.NaN);  // Valore di default Double.NaN
                    int shapePtSequence = parseInteger(parts[3].trim(), -1);  // Valore di default -1
                    double shapeDistTraveled = parseDouble(parts[4].trim(), Double.NaN);  // Valore di default Double.NaN

                    Shape shape = new Shape(shapeId, shapePtLat, shapePtLon, shapePtSequence, shapeDistTraveled);
                    shapes.add(shape);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shapes;
    }

    // Funzione per analizzare i numeri a virgola mobile (Double) con gestione degli errori
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
