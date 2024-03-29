package ch.epfl.tchu.game;


import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.*;

/**
 * A Route between two stations
 *
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
     * Creates a route with id start station, end station, length, Overground or Underground, and color
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
     * Returns the id of the route
     * @return the id of the route
     */
    public String id() {
        return id;
    }

    /**
     * Returns the first station of the route
     * @return the first station of the route
     */
    public Station station1() {
        return station1;
    }

    /**
     * Returns the second station of the route
     * @return the second station of the route
     */
    public Station station2() {
        return station2;
    }

    /**
     * Returns the length of the route
     * @return the length of the route
     */
    public int length() {
        return length;
    }

    /**
     * Returns the level of the route
     * @return the level of the route
     */
    public Level level() {
        return level;
    }

    /**
     * Returns the color of the route
     * @return the color of the route
     */
    public Color color() {
        return color;
    }

    /**
     * Returns a list that contains the two stations of the route
     * @return a list that contains the two stations of the route
     */
    public List<Station> stations(){
        return List.of(station1, station2);
    }

    /**
     * Returns the other station of the route
     * @param station of the route
     * @return the other station of the route
     */
    public Station stationOpposite(Station station){
        Preconditions.checkArgument(station.equals(station1) || station.equals(station2));
        return station.equals(station1) ? station2 : station1;
    }

    /**
     * Returns a list of all the cards that could be played to take a route sorted by increasing order of locomotives and colors
     * @return a list of all the cards that could be played to take a route
     */
    public List<SortedBag<Card>> possibleClaimCards() {
        List<SortedBag<Card>> possibleClaimCards = new ArrayList<>();
        int locomotiveAmount = level.equals(Level.OVERGROUND) ? 0 : length;

        for (int i = 0; i <= locomotiveAmount; i++) {
            if (color == null) {
                for (Card card : Card.CARS) {
                    if(!possibleClaimCards.contains(SortedBag.of(locomotiveAmount, Card.LOCOMOTIVE))){
                        possibleClaimCards.add(SortedBag.of(length - i, card, i, Card.LOCOMOTIVE));
                    }
                }
            } else {
                possibleClaimCards.add(SortedBag.of(length - i, Card.of(color), i, Card.LOCOMOTIVE));
            }
        }
        return possibleClaimCards;
    }

    /**
     * Compares the cards the player put on the table with the drawn cards and gives the amount of additional cards the player has to play
     * @param claimCards the cards the player put on the table
     * @param drawnCards the 3 cards that are drawn
     * @return the amount of cards the player must additionally play
     * @throws IllegalArgumentException if the route is OVERGROUND
     * @throws IllegalArgumentException if there are less than 3 cards in the drawnCards
     */
    public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards) {
        Preconditions.checkArgument(level.equals(Level.UNDERGROUND));
        Preconditions.checkArgument(drawnCards.size() == Constants.ADDITIONAL_TUNNEL_CARDS);
        int numberOfAdditionalClaimCards = 0;

        for(Card drawnCard: drawnCards){

            if(drawnCard.equals(Card.LOCOMOTIVE)){
                numberOfAdditionalClaimCards += 1;
            }else{
                if(claimCards.contains(drawnCard)){
                    numberOfAdditionalClaimCards += 1;
                }
            }

        }
        return numberOfAdditionalClaimCards;
    }

    /**
     * Returns the amount of points of a route
     * @return the amount of points of a route
     */
    public int claimPoints() {
        return Constants.ROUTE_CLAIM_POINTS.get(length);
    }


}
