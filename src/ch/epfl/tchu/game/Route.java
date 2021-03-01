package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;

/**
 * A ticket with trips.
 *
 * @author Yann Ennassih (329978)
 * @author Jérémy Barghorn (328403)
 */
public final class Route{
    private String id;
    private Station station1, station2;
    private int length;
    private Level level;
    private Color color;

    public enum Level{
        OVERGROUND,
        UNDERGROUND;
    }

    /**
     *
     * @param id
     * @param station1
     * @param station2
     * @param length
     * @param level
     * @param color
     */
    public Route(String id, Station station1, Station station2, int length, Level level, Color color){
        this.id = id;
        this.station1 = station1;
        this.station2 = station2;
        this.length = length;
        this.color = color;
        Preconditions.checkArgument(!station1.equals(station2) & length>=Constants.MIN_ROUTE_LENGTH & length<=Constants.MAX_ROUTE_LENGTH);
        if(station1.equals(null) || station2.equals(null) || id.equals(null) || level.equals(null)){
            throw new NullPointerException();
        }
    }

    /**
     *
     * @return
     */
    public String id() {
        return id;
    }

    /**
     *
     * @return
     */
    public Station station1() {
        return station1;
    }

    /**
     *
     * @return
     */
    public Station station2() {
        return station2;
    }

    /**
     *
     * @return
     */
    public int length() {
        return length;
    }

    /**
     *
     * @return
     */
    public Level level() {
        return level;
    }

    /**
     *
     * @return
     */
    public Color color() {
        if(color.equals(null)){
            return null;
        }
        return color;
    }

    /**
     *
     * @return
     */
    public List<Station> stations(){
        return List.of(station1, station2);
    }

    public Station stationOpposite(Station station){
        if(station.equals(station1)){
            return station2;
        }else if(station.equals(station2)){
            return station1;
        }else{
            throw new IllegalArgumentException();
        }
    }

    public List<SortedBag<Card>> possibleClaimCards(){

    }
}
