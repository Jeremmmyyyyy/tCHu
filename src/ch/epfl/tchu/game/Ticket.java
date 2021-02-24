package ch.epfl.tchu.game;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Ticket implements Comparable<Ticket> {

    private List<Trip> trips;
    private final String text;

    /**
     * Creates a ticket with multiple trips
     */
    public Ticket(List<Trip> trips){
        //TODO ajouter une deuxieme condition, avec IllegalArgumentException
        this.trips = Objects.requireNonNull(trips);
        text = computeText();
    }

    /**
     * Creates a ticket with a unique trip
     * @param from departure of the trip
     * @param to destination of the trip
     * @param points value of the trip
     */
    public Ticket(Station from, Station to, int points) { //TODO a revoir
    }

    @Override
    public int compareTo(Ticket ticket) {
        return 0;
    }

    private static String computeText(){

    }
    public String text(){
        return text;

    }
}
