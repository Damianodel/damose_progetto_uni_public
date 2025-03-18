package gestione_dati_gtfs_offline.route;

/* questo codice legge il file route nella cartella gtfs*/
public class Route {
    private String routeId;
    private String routeName;
    private String routelongName;

    public Route(String routeId, String routeName, String routelongName) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.routelongName = routelongName;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    @Override
    public String toString() {
        return "gestione_dati_gtfs.Route{" +
                "routeId='" + routeId + '\'' +
                ", routeName='" + routeName + '\'' +
                '}';
    }

    public String getRouteColor() {
        return null;
    }
}
