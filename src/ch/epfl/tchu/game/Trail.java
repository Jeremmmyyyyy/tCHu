package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;
/**
 * A Trail composed by one or more routes
 *
 * @author Yann Ennassih (329978)
 * @author Jérémy Barghorn (328403)
 */
public final class  Trail {
    private int length;
    private Station station1;
    private Station station2;
    private List<Route> routes;

    /**
     * Private constructor of the class
     * @param station1 first station of the trail
     * @param station2 last station of the trail
     * @param routes that compose the trail
     * @param length of the trail
     */
    private Trail(Station station1, Station station2, List<Route> routes, int length) {
        this.station1 = station1;
        this.station2 = station2;
        this.routes = routes;
        this.length = length;
    }

    /**
     * Creates the longest trail with a given list of routes
     * @param routes list of routes
     * @return the longest trail constructed by the given routes
     * If routes is empty, returns a 0-long trail, with station 1 and station2 null
     * If they are multiple longest trails, returns an arbitrary one
     */
    public static Trail longest(List<Route> routes){
        while(!routes.isEmpty()){
            List<Route> temp1 = new ArrayList<>();
            for (Route initialTrail : routes) {
                List<Route> temp2 = new ArrayList<>();
                for(Route prolongation : routes) {
                    if();
                    temp2.add(prolongation);
                }
            }

        }
        return null;
    }

    /**
     * Getter for the attribute length
     * @return the attribute length
     */
    public int length(){
        return length;
    }

    /**
     * Getter for the attribute station1
     * @return the attribute station1
     * else null if the station1 is null
     */
    public Station station1(){
        if(station1.equals(null)){
            return null;
        }
        return station1;
    }

    /**
     * Getter for the attribute station2
     * @return the attribute station2
     * else null if the station2 is null
     */
    public Station station2(){
        if(station2.equals(null)){
            return null;
        }
        return station2;
    }
}
