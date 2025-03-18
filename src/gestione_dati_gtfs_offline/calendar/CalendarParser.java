package gestione_dati_gtfs_offline.calendar;

import java.io.*;
import java.util.*;

public class CalendarParser {
    public static List<gestione_dati_gtfs_offline.calendar.Calendar> parseCalendar(String filePath) {
        List<gestione_dati_gtfs_offline.calendar.Calendar> calendarServices = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Salta l'intestazione

            while ((line = br.readLine()) != null) {
                String[] parts = splitCSVLine(line);

                if (parts.length >= 9) { // Assicurati che ci siano abbastanza colonne
                    String serviceId = parts[0].trim();
                    boolean monday = "1".equals(parts[1].trim());
                    boolean tuesday = "1".equals(parts[2].trim());
                    boolean wednesday = "1".equals(parts[3].trim());
                    boolean thursday = "1".equals(parts[4].trim());
                    boolean friday = "1".equals(parts[5].trim());
                    boolean saturday = "1".equals(parts[6].trim());
                    boolean sunday = "1".equals(parts[7].trim());
                    String startDate = parts[8].trim();
                    String endDate = parts[9].trim();

                    gestione_dati_gtfs_offline.calendar.Calendar calendarService = new Calendar(serviceId, monday, tuesday, wednesday,
                            thursday, friday, saturday, sunday, startDate, endDate);
                    calendarServices.add(calendarService);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return calendarServices;
    }

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
