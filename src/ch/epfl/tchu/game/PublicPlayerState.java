package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;
/**
 * Public State of the player
 *
 * @author Jérémy Barghorn (328403)
 */
public class PublicPlayerState {

    private final int ticketCount;
    private final int cardCount;
    private final List<Route> routes;
    private final int carCount;
    private final int claimPoints;

    /**
     * Constructor of a new Player State where the amount of tickets, the amount of cards and the routes of a player are known.
     * The Constructor also initialize the amount of remaining cars and points of the player
     * @param ticketCount amount of tickets
     * @param cardCount amount of cards
     * @param routes list of all the routes of a player
     * @throws IllegalArgumentException if the amount of ticket or cards is less or equal 0
     *
     */
    public PublicPlayerState(int ticketCount, int cardCount, List<Route> routes){
        Preconditions.checkArgument(ticketCount >= 0 && cardCount >= 0);
        this.ticketCount = ticketCount;
        this.cardCount = cardCount;
        this.routes = List.copyOf(routes);
        int carsUsed = 0;
        int totalPoints = 0;

        for(Route route : routes){
            carsUsed += route.length();
            totalPoints += route.claimPoints();
        }

        carCount = Constants.INITIAL_CAR_COUNT - carsUsed;
        claimPoints = totalPoints;
    }

    /**
     * return the amount of tickets
     * @return the amount of tickets
     */
    public int ticketCount() {
        return ticketCount;
    }

    /**
     * return the amount of cards
     * @return the amount of cards
     */
    public int cardCount() {
        return cardCount;
    }

    /**
     * return the list of all the routes
     * @return the list of all the routes
     */
    public List<Route> routes() {
        return routes;
    }

    /**
     * return the amount of remaining cars
     * @return the amount of remaining cars
     */
    public int carCount(){
        return carCount;
    }

    /**
     * return the points of the player
     * @return the points of the player
     */
    public int claimPoints(){
        return claimPoints;
    }
}
