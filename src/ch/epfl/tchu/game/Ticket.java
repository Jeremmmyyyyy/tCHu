package ch.epfl.tchu.game;

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
        //TODO ajouter une deuxieme condition, avec IllegalArgumentException
        this.trips = Objects.requireNonNull(trips);
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

    private static String computeText(List<Trip> trips) {
        Trip firstTrip = trips.get(0);
        String from = String.format("%s - ", firstTrip.getFrom().getName());
        if (trips.size() == 1) {
            return from + String.format("%s (%s)", firstTrip.getTo().getName(), firstTrip.getPoints());
        }
        TreeSet<String> orderedDestinations = new TreeSet<>();
        for(Trip trip: trips){
            orderedDestinations.add(String.format("%s (%s)", trip.getTo().getName(), trip.getPoints()));
        }
        return from + String.format("{%s}", String.join(", ",orderedDestinations));
    }

    @Override
    public String toString(){
        return text;
    }
}
