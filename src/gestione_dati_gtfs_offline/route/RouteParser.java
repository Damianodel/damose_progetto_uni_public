package gestione_dati_gtfs_offline.route;/* questo codice serve a leggere ed ad interpretare i dati route*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RouteParser {
    public static List<Route> parseRoutes(String filePath) {
        List<Route> routes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Salta l'intestazione

            while ((line = br.readLine()) != null) {
                String[] parts = splitCSVLine(line); // Usa il metodo che gestisce le virgolette

                if (parts.length >= 4) { // Assicuriamoci che ci siano abbastanza colonne
                    String routeId = parts[0].trim();          // Colonna 0: route_id
                    String routeShortName = parts[2].trim();   // Colonna 2: route_short_name
                    String routeLongName = parts[3].trim();    // Colonna 3: route_long_name

                    routes.add(new Route(routeId, routeShortName, routeLongName));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return routes;
    }

    // Metodo per gestire CSV con virgolette (stesso usato nel gestione_dati_gtfs.GTFSParser)
    private static String[] splitCSVLine(String line) {
        List<String> values = new ArrayList<>();
        boolean insideQuotes = false;
        StringBuilder currentValue = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                insideQuotes = !insideQuotes;  // Cambia stato delle virgolette
            } else if (c == ',' && !insideQuotes) {
                values.add(currentValue.toString().trim());
                currentValue.setLength(0);  // Resetta il buffer
            } else {
                currentValue.append(c);
            }
        }
        values.add(currentValue.toString().trim()); // Aggiunge l'ultimo valore

        return values.toArray(new String[0]);
    }
}