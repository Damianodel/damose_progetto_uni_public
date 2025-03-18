package gestione_dati_gtfs_offline;

import gestione_dati_gtfs_offline.calendar.Calendar;
import gestione_dati_gtfs_offline.calendar.CalendarParser;
import gestione_dati_gtfs_offline.route.Route;
import gestione_dati_gtfs_offline.route.RouteParser;
import gestione_dati_gtfs_offline.shape.Shape;
import gestione_dati_gtfs_offline.shape.ShapeParser;
import gestione_dati_gtfs_offline.stop.Stop;
import gestione_dati_gtfs_offline.stop.StopParser;
import gestione_dati_gtfs_offline.stoptime.StopTime;
import gestione_dati_gtfs_offline.stoptime.StopTimeParser;
import gestione_dati_gtfs_offline.trips.Trips;
import gestione_dati_gtfs_offline.trips.TripsParser;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String stopsFile = "C:/Users/damiano/Desktop/progetto java damose/rome_static_gtfs_test/stops.txt";   // Percorso al file GTFS stops.txt
        String routesFile = "C:\\Users\\damiano\\Desktop\\progetto java damose\\rome_static_gtfs_test\\routes.txt"; // Percorso al file GTFS routes.txt
        String CalendarFile = "C:\\Users\\damiano\\Desktop\\progetto java damose\\rome_static_gtfs_test\\calendar.txt";
        String stopTimesFilePath = "C:\\Users\\damiano\\Desktop\\progetto java damose\\rome_static_gtfs_test\\stop_times.txt";
        String TripsFile = "C:\\Users\\damiano\\Desktop\\progetto java damose\\rome_static_gtfs_test\\trips.txt";
        String ShapeFile = "C:\\Users\\damiano\\Desktop\\progetto java damose\\rome_static_gtfs_test\\shapes.txt";
        List<Stop> stops = StopParser.parseStops();
        List<Route> routes = RouteParser.parseRoutes(routesFile);
        List<Calendar> Calendar = CalendarParser.parseCalendar(CalendarFile);
        List<StopTime> stopTimes = StopTimeParser.parseStopTimes(stopTimesFilePath);
        List<Trips> trips = TripsParser.parseTrips(TripsFile);
        List<Shape> Shapes = ShapeParser.parseShapes(ShapeFile);

        System.out.println("===Shape===");
        for (Shape shape : Shapes) {
            System.out.println(shape);
        }

        System.out.println("\n=== Trips ===");
        for (Trips trip : trips) {
            System.out.println(trip);
        }
        // Stampa delle informazioni sui tempi di fermata
        System.out.println("=== Stop Times ===");
        for (StopTime stopTime : stopTimes) {
            System.out.println(stopTime);
        }

        System.out.println("\n=== Calendar Services ===");
        for (gestione_dati_gtfs_offline.calendar.Calendar service : Calendar) {
            System.out.println(service);
        }

        // Stampare le fermate lette
        System.out.println("\n=== Fermate caricate ===");
        for (Stop stop : stops) {
            System.out.println(stop);
        }

        // Stampare le linee di autobus
        System.out.println("\n=== Linee di autobus caricate ===");
        for (Route route : routes) {
            System.out.println(route);
        }
    }
}
