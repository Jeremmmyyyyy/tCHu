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
    private final int length;
    private final Station station1;
    private final Station station2;
    private final List<Route> routesOfTrail;

    /**
     * Private constructor of the class
     * @param station1 first station of the trail
     * @param station2 last station of the trail
     * @param routesOfTrail that compose the trail
     * @param length of the trail
     */
    private Trail(Station station1, Station station2, List<Route> routesOfTrail, int length) {
        this.station1 = station1;
        this.station2 = station2;
        this.routesOfTrail = routesOfTrail;
        this.length = length;
    }

    /**
     * Creates the longest trail with a given list of routes
     * @param routes list of routes
     * @return the longest trail constructed by the given routes
     * If routes is empty, returns a 0-long trail, with station 1 and station2 null
     * If they are multiple longest trails, returns the length of an arbitrary one
     */
    public static Trail longest(List<Route> routes){
        List<Trail> listOfTrailsToExtend = new ArrayList<>();
        for(Route route : routes){
            listOfTrailsToExtend.add(new Trail(route.station1(), route.station2(), List.of(route), route.length()));
            listOfTrailsToExtend.add(new Trail(route.station2(), route.station1(), List.of(route), route.length()));
        }
        Trail longestTrail = listOfTrailsToExtend.get(0);
        while(!listOfTrailsToExtend.isEmpty()){
            //refreshes the longestTrail at each step
            for (Trail trail : listOfTrailsToExtend) {
                if (trail.length > longestTrail.length) {
                    longestTrail = trail;
                }
            }
            List<Trail> newListOfTrailsToExtend = new ArrayList<>();
            for (Trail trailToExtend : listOfTrailsToExtend) {
                List<Route> prolongations = trailToExtend.possibleProlongations(routes);
                for(Route prolongation : prolongations){
                    List<Route> listOfRoutesInTheTrail = new ArrayList<>();
                    //trailToExtend.routesOfTrail is final, hence the for loop to copy its content
                    for(Route route: trailToExtend.routesOfTrail){
                        listOfRoutesInTheTrail.add(route);
                    }
                    listOfRoutesInTheTrail.add(prolongation); //extends the list of routes of the current trail
                    //constructs a new trail from the extended list of routes
                    Trail newTrail = new Trail(trailToExtend.station1(),
                            prolongation.stationOpposite(trailToExtend.station2()),
                            listOfRoutesInTheTrail,
                            trailToExtend.length() + prolongation.length());
                    if(newTrail.length > longestTrail.length){
                        newListOfTrailsToExtend.add(newTrail);
                    }
                }
            }
            listOfTrailsToExtend = newListOfTrailsToExtend;
        }
        return longestTrail;

//        List<Trail> listOfTrailsToExtend = new ArrayList<>();
//        List<Trail> memory = new ArrayList<>();
//        for(Route route : routes){
//            listOfTrailsToExtend.add(new Trail(route.station1(), route.station2(), List.of(route), route.length()));
//            listOfTrailsToExtend.add(new Trail(route.station2(), route.station1(), List.of(route), route.length()));
//        }
//        Trail longestTrail = listOfTrailsToExtend.get(0);
//        while(!listOfTrailsToExtend.isEmpty()){
//            memory.clear();
//            List<Trail> newListOfTrailsToExtend = new ArrayList<>();
//            for (Trail trailToExtend : listOfTrailsToExtend) {
//                List<Route> prolongations = trailToExtend.possibleProlongations(routes);
//                for(Route prolongation : prolongations){
//                    List<Route> listOfRoutesInTheTrail = new ArrayList<>();
//                    //trailToExtend.routesOfTrail is final, hence the for loop to copy its content
//                    for(Route route: trailToExtend.routesOfTrail){
//                        listOfRoutesInTheTrail.add(route);
//                    }
//                    listOfRoutesInTheTrail.add(prolongation); //extends the list of routes of the current trail
//                    //constructs a new trail from the extended list of routes
//                    newListOfTrailsToExtend.add(new Trail(trailToExtend.station1(),
//                            prolongation.stationOpposite(trailToExtend.station2()),
//                            listOfRoutesInTheTrail,
//                            trailToExtend.length() + prolongation.length()));
//                }
//            }
//            for (Trail trail : listOfTrailsToExtend) { //stores the last non-empty listOfTrailsToExtend
//                memory.add(trail);
//            }
//            listOfTrailsToExtend = newListOfTrailsToExtend;
//        }
//        //Last step = find a trail of maximal length thanks to the list memory
//        int maxLength = 0;
//        for (Trail trail : memory) {
//            if (trail.length > maxLength) {
//                longestTrail = trail;
//            }
//        }
//        return longestTrail;
    }

    private Trail longestTrail(List<Trail> trails) {
        int max = 0;
        Trail longestTrail = trails.get(0);
        for (Trail trail : trails) {
            if (trail.length > max) {
                longestTrail = trail;
            }
        }
        return longestTrail;
    }

    /**
     * Intermediate method for longest(...) method
     * Creates a list of possible prolongations for the current trail, given a list of routes
     * @param routes list of routes to test as possible prolongations
     * @return list of possible prolongations for the current trail
     */
    private List<Route> possibleProlongations(List<Route> routes) {
        List<Route> prolongations = new ArrayList<>();
        for(Route possibleProlongation : routes) {
            if(possibleProlongation.stations().contains(station2)  //looks for a possible link between stations
                    && !routesOfTrail.contains(possibleProlongation)){

                prolongations.add(possibleProlongation);
            }
        }
        return prolongations;
    }

    /**
     * Creates textual representation of the trail according to the following format :
     * @return Station1 - Station2 - ... - StationN (length)
     */
    @Override
    public String toString() {
        List<String> routesOfTrailString = new ArrayList<>();
        routesOfTrailString.add(station1.toString());
        Station previousStation = station1;
        for (Route route : routesOfTrail) {
            Station nextStation = route.stationOpposite(previousStation);
            routesOfTrailString.add(nextStation.toString());
            previousStation = nextStation;
        }
        return String.join(" - ", routesOfTrailString) + String.format(" (%s)", length);
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
        if(station1 == null){ //TODO
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
        if(station2 == null){ //TODO
            return null;
        }
        return station2;
    }
}
