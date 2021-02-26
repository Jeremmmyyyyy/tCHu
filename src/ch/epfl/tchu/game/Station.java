package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

/**
 * A train station.
 *
 * @author Yann Ennassih (329978)
 */
public final class Station {

    private final int id;
    private final String name;

    /**
     * Constructor of the Station class
     * @param id unique id of the station
     * @param name name of the station
     * @throws IllegalArgumentException if id is a negative integer
     */
    public Station(int id, String name) {
        Preconditions.checkArgument(id >= 0);
        this.id = id;
        this.name = name;
    }

    /**
     * Getter for the attribute id
     * @return the id of the station this
     */
    public int id(){
        return id;
    }

    /**
     * Getter for the attribute name
     * @return the name of the station this
     */
    public String name(){
        return name;
    }

    /**
     * Redefinition of the toString() method
     * @return the attribute name of the station
     */
    @Override
    public String toString(){
        return name;
    }
}
