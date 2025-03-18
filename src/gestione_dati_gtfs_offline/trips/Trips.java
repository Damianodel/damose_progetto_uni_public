package gestione_dati_gtfs_offline.trips;

public class Trips {
    private String routeId;
    private String serviceId;
    private String tripId;
    private String tripHeadsign;
    private String tripShortName;
    private int directionId;
    private String blockId;
    private String shapeId;
    private int wheelchairAccessible;
    private int exceptional;

    public Trips(String routeId, String serviceId, String tripId, String tripHeadsign, String tripShortName,
                int directionId, String blockId, String shapeId, int wheelchairAccessible) {
        this.routeId = routeId;
        this.serviceId = serviceId;
        this.tripId = tripId;
        this.tripHeadsign = tripHeadsign;
        this.tripShortName = tripShortName;
        this.directionId = directionId;
        this.blockId = blockId;
        this.shapeId = shapeId;
        this.wheelchairAccessible = wheelchairAccessible;
        this.exceptional = exceptional;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "routeId='" + routeId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", tripId='" + tripId + '\'' +
                ", tripHeadsign='" + tripHeadsign + '\'' +
                ", tripShortName='" + tripShortName + '\'' +
                ", directionId=" + directionId +
                ", blockId='" + blockId + '\'' +
                ", shapeId='" + shapeId + '\'' +
                ", wheelchairAccessible=" + wheelchairAccessible +
                ", exceptional=" + exceptional +
                '}';
    }
}
