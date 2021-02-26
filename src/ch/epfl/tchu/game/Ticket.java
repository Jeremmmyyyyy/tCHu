package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.TreeSet;
import java.util.List;
import java.util.Objects;

public final class Ticket implements Comparable<Ticket> {

    private final List<Trip> trips;
    private final String text;

    /**
     * Creates a ticket with multiple trips
     */
    public Ticket(List<Trip> trips){
        Preconditions.checkArgument(trips.equals(null));
        boolean sameDeparture = true;
        String departure = trips.get(0).from().name();
        for(Trip trip: trips){
            if(!trip.from().name().equals(departure)){
                sameDeparture = false;
            }
        }
        Preconditions.checkArgument(sameDeparture);
        this.trips = trips;
        text = computeText(this.trips);
    }

    /**
     * Creates a ticket with a unique trip
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

    @Override
    public int compareTo(Ticket that) {
        return this.compareTo(that);
    }

    public int points(StationConnectivity connectivity){

    }

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

    @Override
    public String toString(){
        return text;
    }
}
