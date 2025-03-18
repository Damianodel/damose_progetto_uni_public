package gestione_dati_gtfs_offline.stoptime;

public class StopTime {
    private String tripId;
    private String arrivalTime;
    private String departureTime;
    private String stopId;
    private int stopSequence;
    private String stopHeadsign;
    private int pickupType;
    private int dropOffType;
    private double shapeDistTraveled;
    private boolean timepoint;

    public StopTime(String tripId, String arrivalTime, String departureTime, String stopId, int stopSequence,
                    String stopHeadsign, int pickupType, int dropOffType, double shapeDistTraveled, boolean timepoint) {
        this.tripId = tripId;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.stopId = stopId;
        this.stopSequence = stopSequence;
        this.stopHeadsign = stopHeadsign;
        this.pickupType = pickupType;
        this.dropOffType = dropOffType;
        this.shapeDistTraveled = shapeDistTraveled;
        this.timepoint = timepoint;
    }

    @Override
    public String toString() {
        return "StopTime{" +
                "tripId='" + tripId + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", stopId='" + stopId + '\'' +
                ", stopSequence=" + stopSequence +
                ", stopHeadsign='" + stopHeadsign + '\'' +
                ", pickupType=" + pickupType +
                ", dropOffType=" + dropOffType +
                ", shapeDistTraveled=" + shapeDistTraveled +
                ", timepoint=" + timepoint +
                '}';
    }
}
