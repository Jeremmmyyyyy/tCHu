package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.TreeSet;
import java.util.List;

/**
 * A ticket with trips.
 *
 * @author Yann Ennassih (329978)
 */
public final class Ticket implements Comparable<Ticket> {

    private final List<Trip> trips;
    private final String text;

    /**
     * Creates a ticket with multiple trips
     * @param trips list of the trips
     * @throws IllegalArgumentException when the list is empty
     * or when at least two trips have different departure stations
     */
    public Ticket(List<Trip> trips){
        Preconditions.checkArgument(!trips.equals(List.of()));
        boolean sameDeparture = true;
        String departure = trips.get(0).from().name();
        for(Trip trip: trips){
            if (!trip.from().name().equals(departure)) {
                sameDeparture = false;
                break;
            }
        }
        Preconditions.checkArgument(sameDeparture);
        this.trips = trips;
        text = computeText(this.trips);
    }

    /**
     * Creates a ticket with a single trip
     * @param from departure of the trip
     * @param to destination of the trip
     * @param points value of the trip
     */
    public Ticket(Station from, Station to, int points) {
        this(List.of(new Trip(from, to, points)));
    }

    /**
     * Getter for the attribute text
     * @return the attribute text
     */
    public String text(){
        return text;
    }

    /**
     * Creates the textual representation of the ticket
     * @param trips list of the trips
     * @return From - To (Points) for single trip ticket
     * or From - {To (Points), ..., To (Points)} for multiple trips ticket
     */
    private static String computeText(List<Trip> trips) {
        Trip firstTrip = trips.get(0);
        String from = String.format("%s - ", firstTrip.from().name());
        if (trips.size() == 1) {
            return from + String.format("%s (%s)", firstTrip.to().name(), firstTrip.points());
        }
        TreeSet<String> orderedDestinations = new TreeSet<>();
        for(Trip trip: trips){
            orderedDestinations.add(String.format("%s (%s)", trip.to().name(), trip.points()));
        }
        return from + String.format("{%s}", String.join(", ",orderedDestinations));
    }

    /**
     * Current points of the player owning the ticket this
     * @param connectivity of the player
     * @return the current points of the player owning the ticket this
     */
    public int points(StationConnectivity connectivity){
        int max = Integer.MIN_VALUE;
        for (Trip trip : trips) {
            if (trip.points(connectivity) > max) {
                max = trip.points(connectivity);
            }
        }
        return max;
    }

    /**
     * Compares the textual representation of the tickets this and that
     * @param that ticket to be compared with this
     * @return positive random integer if this > that (in the sense of the lexicoraphical order)
     * or negative random integer if this < that
     * or 0 if this = that
     */
    @Override
    public int compareTo(Ticket that) {
        return text.compareTo(that.text);
    }

    /**
     * Redefinition of the toString() method
     * @return the attribute text of the station
     */
    @Override
    public String toString(){
        return text;
    }
}
