package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;

public class PublicPlayerState {

    private final int ticketCount;
    private final int cardCount;
    private final List<Route> routes;
    private final int carCount;
    private final int claimPoints;

    public PublicPlayerState(int ticketCount, int cardCount, List<Route> routes){
        Preconditions.checkArgument(ticketCount >= 0 && cardCount >= 0);
        this.ticketCount = ticketCount;
        this.cardCount = cardCount;
        this.routes = routes;

        int carsUsed = 0;
        int totalPoints = 0;
        for(Route route : routes){
            carsUsed += route.length();
            totalPoints += route.claimPoints();
        }
        carCount = Constants.INITIAL_CAR_COUNT - carsUsed;
        claimPoints = totalPoints;
    }

    public int ticketCount() {
        return ticketCount;
    }

    public int cardCount() {
        return cardCount;
    }

    public List<Route> routes() {
        return routes;
    }

    public int carCount(){
        return carCount;
    }

    public int claimPoints(){
        return claimPoints;
    }
}
