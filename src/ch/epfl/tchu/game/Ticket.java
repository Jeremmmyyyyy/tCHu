package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.TreeSet;
import java.util.List;

public final class Ticket implements Comparable<Ticket> {

    private final List<Trip> trips;
    private final String text;

    /**
     * Creates a ticket with multiple trips
     * @param trips list of the trips
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
    public Ticket(Station from, Station to, int points) { //TODO a revoir
        this(List.of(new Trip(from, to, points)));
    }

    public String text(){
        return text;
    }

    /**
     * Creates the textual representation of the ticket
     * @param trips list of the trips
     * @return From - To (Points) for single trip ticket
     * @return From - {To (Points), ..., To (Points)} for multiple trip ticket
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
     * Actual points of the player owning the ticket this
     * @param connectivity of the player
     */
    public int points(StationConnectivity connectivity){
        int max = 0, min = trips.get(0).points(connectivity);
        for(Trip trip: trips){
            if(trip.points(connectivity) > 0 && trip.points(connectivity) > max){
                max = trip.points(connectivity);
            }
            else if(trip.points(connectivity) < 0 && trip.points(connectivity) > min){
                min = trip.points(connectivity);
            }
        }
        if(max != 0){
            return max;
        }
        else {
            return min;
        }
    }

    /**
     * Compares the textual representation of the tickets this and that
     * @param that
     * @return positive random integer if this > that
     * @return negative random integer if this < that
     * @return 0 if this = that
     */
    @Override
    public int compareTo(Ticket that) {
        return text.compareTo(that.text);
    }

    @Override
    public String toString(){
        return text;
    }
}
