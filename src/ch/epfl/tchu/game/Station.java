package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

/**
 * Train station
 *
 * @author Jérémy Barghorn (328403)
 */
public final class Station {

    private final int id;
    private final String name;

    /**
     * Constructor of the Station class
     * @param id unique id of the station
     * @param name name of the station
     * @throws IllegalArgumentException if the id is negative or 0
     */
    public Station(int id, String name) {
        Preconditions.checkArgument(id >= 0);
        this.id = id;
        this.name = name;
    }

    /**
     * getter for the id of the station
     * @return the id
     */
    public int id(){
        return id;
    }

    /**
     * getter for teh name of the station
     * @return name
     */
    public String name(){
        return name;
    }

    /**
     * Override of the toString functioçn so that the name of the station is returned
     * @return name
     */
    @Override
    public String toString(){
        return name;
    }
}
