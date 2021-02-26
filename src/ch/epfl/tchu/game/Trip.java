package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The trip with origin destination and points
 *
 * @author Jérémy Barghorn (328403)
 */
public final class Trip {

    private final Station from;
    private final Station to;
    private final int points;

    /**
     * Constructor of the trip class
     * @param from Origin Station
     * @param to Destination Station
     * @param points of the trip
     * @throws IllegalArgumentException if points is 0 or negative
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

    /**
     * getter of the origin of the trip
     * @return the origin from
     */
    public Station from() {
        return from;
    }

    /**
     * getter for the destination of the trip
     * @return the destination to
     */
    public Station to() {
        return to;
    }

    /**
     * getter for the points of a trip
     * @return the number of points
     */
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
