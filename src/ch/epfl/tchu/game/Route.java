package ch.epfl.tchu.game;


import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.*;

/**
 * A Route between two stations
 *
 * @author Yann Ennassih (329978)
 * @author Jérémy Barghorn (328403)
 */
public final class Route {
    private final String id;
    private final Station station1, station2;
    private final int length;
    private final Level level;
    private final Color color;

    public enum Level{
        OVERGROUND,
        UNDERGROUND
    }

    /**
     * Create a route with id start station, end station, length, Overground or Underground, and color
     * @param id of the route id form : "Station1(first three letters)_Station2_(first three letters)_1or2(depending on multiplicity of the route)
     * @param station1 first station of the route
     * @param station2 second station of the route
     * @param length length of the route in blocks
     * @param level Overground or underground route
     * @param color if a color is already set for the route else null
     * @throws IllegalArgumentException if station1 and 2 are the same, or if the length is greater than 6, or if the length is shorter than 1
     * @throws NullPointerException if the Station1 is null, if station2 is nul, if the id is null or if the level is null
     */
    public Route(String id, Station station1, Station station2, int length, Level level, Color color){
        Preconditions.checkArgument(!station1.equals(station2));
        Preconditions.checkArgument(length>=Constants.MIN_ROUTE_LENGTH && length<=Constants.MAX_ROUTE_LENGTH);
        this.id = Objects.requireNonNull(id);
        this.station1 = Objects.requireNonNull(station1);
        this.station2 = Objects.requireNonNull(station2);
        this.length = length;
        this.level = Objects.requireNonNull(level);
        this.color = color;
    }

    /**
     * return the id of the station
     * @return the id of the station
     */
    public String id() {
        return id;
    }

    /**
     * return the first station of the route
     * @return the first station of the route
     */
    public Station station1() {
        return station1;
    }

    /**
     * return the second station of the route
     * @return the second station of the route
     */
    public Station station2() {
        return station2;
    }

    /**
     * return the length of the route
     * @return the length of the route
     */
    public int length() {
        return length;
    }

    /**
     * return the level of the route
     * @return the level of the route
     */
    public Level level() {
        return level;
    }

    /**
     * return the color of the route
     * @return the color of the route
     */
    public Color color() {
        if(color==null){  //TODO
            return null;
        }
        return color;
    }

    /**
     * return a list that contains the two stations of the route
     * @return a list that contains the two stations of the route
     */
    public List<Station> stations(){
        return List.of(station1, station2);
    }

    /**
     * return the other station of the route
     * @param station of the route
     * @return the other station of the route
     */
    public Station stationOpposite(Station station){
        if(station.equals(station1)){
            return station2;
        }else if(station.equals(station2)){
            return station1;
        }else{
            throw new IllegalArgumentException();
        }
    }

    /**
     * returns a list of all the cards that could be played to take a route sorted by increasing order of locomotives and colors
     * @return a list of all the cards that could be played to take a route
     */
    public List<SortedBag<Card>> possibleClaimCards() {
        List<SortedBag<Card>> possibleClaimCards = new ArrayList<>();
        if(level.equals(Level.OVERGROUND) && !color.equals(null)) {  //TODO
            return List.of(SortedBag.of(length, Card.of(color)));
        }else if(level.equals(Level.UNDERGROUND) && !color.equals(null)) { //TODO
            for (int i = 0; i < length; ++i) {
                possibleClaimCards.add(SortedBag.of(length - i, Card.of(color), i, Card.LOCOMOTIVE));
            }
            return possibleClaimCards;
        }else{
            for (int i = 0; i < length; ++i) {
                for(Card card: Card.CARS) {
                    possibleClaimCards.add(SortedBag.of(length - i, Card.of(color), i, card));
                }
            }
            return possibleClaimCards;
        }
    }

    /**
     * Compare the cards the player put on the table with the drawn cards and gives the amount of additional cards the player has to play
     * @param claimCards the cards the player put on the table
     * @param drawnCards the 3 cards that are drawn
     * @return the amount of cards the player must additionally play
     */
    public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards) {
        Preconditions.checkArgument(level.equals(Level.UNDERGROUND));
        Preconditions.checkArgument(drawnCards.size() == Constants.ADDITIONAL_TUNNEL_CARDS);
        int numberOfAdditionalClaimCards = 0;
        for(Card drawnCard: drawnCards){
            if(drawnCard.equals(Card.LOCOMOTIVE)){
                numberOfAdditionalClaimCards += 1;
            }else{
                for(Card claimCard: claimCards){
                    if(drawnCard.equals(claimCard)){
                        numberOfAdditionalClaimCards += 1;
                    }
                }
            }
        }
        return numberOfAdditionalClaimCards;
    }

    /**
     * returns the amount of points of a route
     * @return the amount of points of a route
     */
    public int claimPoints() {
        return Constants.ROUTE_CLAIM_POINTS.get(length);
    }


}
