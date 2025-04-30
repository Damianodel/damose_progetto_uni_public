package risorse_mappa;

import java.io.*;
import java.util.*;

public class GTFSReader {

    public static final String STOP_TIMES_FILE =  "C:\\Users\\damiano\\IdeaProjects\\damose progetto uni\\rome_static_gtfs_test\\stop_times.txt";
    public static final String ROUTES_FILE =  "C:\\Users\\damiano\\IdeaProjects\\damose progetto uni\\rome_static_gtfs_test\\routes.txt";
    public static final String TRIPS_FILE =  "C:\\Users\\damiano\\IdeaProjects\\damose progetto uni\\rome_static_gtfs_test\\trips.txt";
    public static final String STOPS_FILE = "C:\\Users\\damiano\\IdeaProjects\\damose progetto uni\\rome_static_gtfs_test\\stops.txt";

    private static final HashMap<String, List<String>> lineToStopsMap = new HashMap<>();
    private static final HashMap<String, double[]> locationData = new HashMap<>();
    private static final HashMap<String, String> stopIdToNameMap = new HashMap<>();
    public static HashMap<String, String> stopNameToIdMap = new HashMap<>();
    private static final HashMap<String, String> tripToRouteMap = new HashMap<>();
    public static List<String> getAllStopsForRoute(String routeId) {
        List<String> stops = new ArrayList<>();
        if (lineToStopsMap.containsKey(routeId)) {
            stops.addAll(lineToStopsMap.get(routeId));
        }
        return stops;
    }

    public static List<String> getRoutesForStop(String stopIdentifier) {
        System.out.println("Searching routes for stop: " + stopIdentifier);
        List<String> routes = new ArrayList<>();
        HashSet<String> tripIds = new HashSet<>();

        // Determine the stop ID based on the identifier (could be ID or name)
        String stopId = stopIdentifier;
        if (!stopIdToNameMap.containsKey(stopId)) {
            stopId = stopNameToIdMap.getOrDefault(stopId, stopId);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ROUTES_FILE))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String currentStopId = parts[3].trim();
                    if (currentStopId.equalsIgnoreCase(stopId)) {
                        tripIds.add(parts[0].trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashSet<String> routeIds = new HashSet<>();
        for (String tripId : tripIds) {
            String routeId = tripToRouteMap.get(tripId);
            if (routeId != null) {
                routeIds.add(routeId);
            }
        }

        System.out.println("Routes found: " + routeIds);
        return new ArrayList<>(routeIds);
    }
    public static List<String> getLinesForStop(String stopIdentifier) {
        System.out.println("Searching lines for stop: " + stopIdentifier);
        List<String> lines = new ArrayList<>();
        HashSet<String> tripIds = new HashSet<>();
        String stopId = stopIdentifier;
        if (!stopIdToNameMap.containsKey(stopId)) {
            stopId = stopNameToIdMap.getOrDefault(stopId, stopId);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(STOPS_FILE))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String currentStopId = parts[3].trim();
                    if (currentStopId.equalsIgnoreCase(stopId)) {
                        tripIds.add(parts[0].trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashSet<String> routeIds = new HashSet<>();
        for (String tripId : tripIds) {
            String routeId = tripToRouteMap.get(tripId);
            if (routeId != null) {
                routeIds.add(routeId);
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ROUTES_FILE))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String routeId = parts[0].trim();
                    if (routeIds.contains(routeId)) {
                        lines.add(routeId + " - " + parts[2].trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Lines found: " + lines);
        return lines;
    }

    public static List<String> getScheduleForStop(String stopIdentifier) {
        System.out.println("Searching schedule for stop: " + stopIdentifier);
        List<String> schedule = new ArrayList<>();

        // Determine the stop ID based on the identifier (could be ID or name)
        String stopId = stopIdentifier;
        if (!stopIdToNameMap.containsKey(stopId)) {
            stopId = stopNameToIdMap.getOrDefault(stopId, stopId);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(STOP_TIMES_FILE))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String currentStopId = parts[3].trim();
                    String arrivalTime = parts[1].trim();
                    if (currentStopId.equalsIgnoreCase(stopId)) {
                        schedule.add(arrivalTime);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Schedule found: " + schedule);
        return schedule;
    }

    private static boolean isDataLoaded = false;

    private static void loadLineStopsData() {
        if (isDataLoaded) return;
        isDataLoaded = true;

        try (BufferedReader tripsReader = new BufferedReader(new FileReader(TRIPS_FILE));
             BufferedReader stopTimesReader = new BufferedReader(new FileReader(STOP_TIMES_FILE))) {

            tripsReader.readLine();
            String line;
            while ((line = tripsReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String tripId = parts[2].trim();
                    String routeId = parts[0].trim();
                    tripToRouteMap.put(tripId, routeId);
                }
            }

            stopTimesReader.readLine(); // Skip header
            while ((line = stopTimesReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String tripId = parts[0].trim();
                    String stopName = parts[3].trim();

                    String routeId = tripToRouteMap.get(tripId);
                    if (routeId != null) {
                        lineToStopsMap.computeIfAbsent(routeId, k -> new ArrayList<>()).add(stopName);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento delle fermate per linea.");
            e.printStackTrace();
        }
    }

    public static HashMap<String, double[]> getLocationData() {
        if (locationData.isEmpty()) {
            parseStops(STOPS_FILE);
            if (locationData.isEmpty()) {
                System.err.println("⚠ Errore: Nessuna posizione trovata in " + STOPS_FILE);
            }
        }
        return locationData;
    }

    private static boolean isStopsDataLoaded = false;

    public static List<String> getStopsForLine(String lineName) {
        if (!isStopsDataLoaded) {
            loadLineStopsData();
            isStopsDataLoaded = true;
        }
        return lineToStopsMap.getOrDefault(lineName, new ArrayList<>());
    }

    public static void parseStops(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String stopId = parts[0].trim();
                    String stopName = parts[2].trim();
                    stopIdToNameMap.put(stopId, stopName);
                    stopNameToIdMap.put(stopName, stopId);

                    try {
                        double lat = Double.parseDouble(parts[4].trim());
                        double lon = Double.parseDouble(parts[5].trim());
                        locationData.put(stopId, new double[]{lat, lon});
                    } catch (NumberFormatException e) {
                        System.err.println("⚠ Errore: Riga non valida in stops.txt -> " + line);

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java GTFSReader <stopName>");
            return;
        }

        String stopName = args[0]; // Use the stop name provided as an argument
        System.out.println("Linee disponibili per la fermata " + stopName + ":");
        List<String> lines = getLinesForStop(stopName);
        for (String line : lines) {
            System.out.println(line);
        }

        System.out.println("\nOrari di arrivo alla fermata " + stopName + ":");
        List<String> schedule = getScheduleForStop(stopName);
        for (String time : schedule) {
            System.out.println(time);
        }
    }
}
