package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Trip {

    private final Station from;
    private final Station to;
    private final int points;

    /**
     * Constructor of the trip class
     * @param from Origin Station
     * @param to Destination Station
     * @param points of the trip
     */
    public Trip(Station from, Station to, int points){
        Preconditions.checkArgument(points>0);
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
        this.points = points;
    }

    /**
     * Public methode that puts all the possible trips in one list
     * @param from List of Origin Station
     * @param to List of destination Station
     * @param points points of the trip
     * @return Trip List of all the possible connections
     */
    public static List<Trip> all (List<Station> from, List<Station> to, int points){
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        Preconditions.checkArgument(points>=0);
        List<Trip> allPossibleConnections = new ArrayList<Trip>();

        for (Station fromStation : from) {
            for (Station toStation : to) {
                allPossibleConnections.add(new Trip(fromStation, toStation, points));
            }
        }
        return allPossibleConnections;
    }

    public Station from() {
        return from;
    }

    public Station to() {
        return to;
    }

    public int points() {
        return points;
    }

    /**
     * Returns the number of points of the trip given the connection between the points
     * @param connectivity StationConnectivity
     * @return points if Trip exist
     * @return -points if Trip doesn't exist
     */
    public int points(StationConnectivity connectivity){
        if(connectivity.connected(from, to)){
            return points;
        }
        return -points;
    }
}
