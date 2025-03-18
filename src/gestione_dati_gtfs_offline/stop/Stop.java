package gestione_dati_gtfs_offline.stop;/* questo codice legge il file stop nella cartella gtfs*/

public class Stop {
    private String stopId;
    private String stopName;
    private double latitude;
    private double longitude;

    public Stop(String stopId, String stopName, double latitude, double longitude) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getStopId() {
        return stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
