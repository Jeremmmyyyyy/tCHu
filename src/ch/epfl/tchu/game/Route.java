package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.*;

/**
 * A ticket with trips.
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
        this.level = level;
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

    /**
     *
     * @return
     */
    public List<SortedBag<Card>> possibleClaimCards() {
        List<SortedBag<Card>> possibleClaimCards = new ArrayList<>();
        if(level.equals(Level.OVERGROUND) && !color.equals(null)) {
            return List.of(SortedBag.of(length, Card.of(color)));
        }else if(level.equals(Level.UNDERGROUND) && !color.equals(null)) {
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
     *
     * @param claimCards
     * @param drawnCards
     * @return
     */
    public int additionnalClaimCards(SortedBag<Card> claimCards, SortedBag<Card> drawnCards) {
        Preconditions.checkArgument(level.equals(Level.UNDERGROUND));
        Preconditions.checkArgument(drawnCards.size() == Constants.ADDITIONAL_TUNNEL_CARDS);
        int numberOfAdditionnalClaimCards = 0;
        for(Card drawnCard: drawnCards){
            if(drawnCard.equals(Card.LOCOMOTIVE)){
                numberOfAdditionnalClaimCards += 1;
            }else{
                for(Card claimCard: claimCards){
                    if(drawnCard.equals(claimCard)){
                        numberOfAdditionnalClaimCards += 1;
                    }
                }
            }
        }
        return numberOfAdditionnalClaimCards;
    }

    /**
     *
     * @return
     */
    public int claimPoints() {
        return Constants.ROUTE_CLAIM_POINTS.get(length);
    }


}
