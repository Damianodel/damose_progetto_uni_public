package gestione_dati_gtfs_offline.shape;

public class Shape {
    private String shapeId;
    private double shapePtLat;
    private double shapePtLon;
    private int shapePtSequence;
    private double shapeDistTraveled;

    // Costruttore
    public Shape(String shapeId, double shapePtLat, double shapePtLon, int shapePtSequence, double shapeDistTraveled) {
        this.shapeId = shapeId;
        this.shapePtLat = shapePtLat;
        this.shapePtLon = shapePtLon;
        this.shapePtSequence = shapePtSequence;
        this.shapeDistTraveled = shapeDistTraveled;
    }


    // Metodo per la visualizzazione dei dettagli della forma
    @Override
    public String toString() {
        return "Shape{" +
                "shapeId='" + shapeId + '\'' +
                ", shapePtLat=" + shapePtLat +
                ", shapePtLon=" + shapePtLon +
                ", shapePtSequence=" + shapePtSequence +
                ", shapeDistTraveled=" + shapeDistTraveled +
                '}';
    }
}
