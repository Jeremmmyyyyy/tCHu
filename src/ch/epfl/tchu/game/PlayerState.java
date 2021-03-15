package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class PlayerState extends PublicPlayerState{

    private final SortedBag<Ticket> tickets;
    private final SortedBag<Card> cards;

    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes) {
        super(tickets.size(), cards.size(), routes);
        this.tickets = tickets;
        this.cards  = cards;
    }

    public static PlayerState initial(SortedBag<Card> initialCards){
        Preconditions.checkArgument(initialCards.size() >= 4);
        return new PlayerState(SortedBag.of(), initialCards, List.of());
    }

    public SortedBag<Ticket> tickets(){
        return tickets;
    }

    public PlayerState withAddedTickets(SortedBag<Ticket> newTickets){
        return new PlayerState(tickets.union(newTickets), cards, routes());
    }

    public SortedBag<Card> cards(){
        return cards;
    }

    public PlayerState withAddedCards(SortedBag<Card> additionalCards){
        return new PlayerState(tickets, cards.union(additionalCards), routes());
    }

    public boolean canClaimRoute(Route route){
        for(SortedBag<Card> possibleCards : route.possibleClaimCards()){
            if(route.length() <= carCount() && cards.contains(possibleCards)){ // TODO vérif cette méthode
                return true;
            }
        }
        return false;
    }

    public List<SortedBag<Card>> possibleClaimCards(Route route){
        Preconditions.checkArgument(carCount() >= route.length());
        List<SortedBag<Card>> possibleClaimCards = new ArrayList<>();
        for(SortedBag<Card> possibleCards : route.possibleClaimCards()){
            if (cards.contains(possibleCards)){
                possibleClaimCards.add(possibleCards);
            }
        }
        return possibleClaimCards;
    }

    public List<SortedBag<Card>> possibleAdditionalCards(int additionalCardsCount, SortedBag<Card> initialCards, SortedBag<Card> drawnCards){




        return null;
    }

    /**
     * Returns the same PlayerState with an added claimed route and without the claimCards in the playing hand
     * @param route that was claimed by the player
     * @param claimCards the card used to claim the route
     * @return a new PlayerState where routes is extended by route and cards shortened by claimCards
     */
    public PlayerState withClaimedRoute(Route route, SortedBag<Card> claimCards) {
        List<Route> newRoutes = new ArrayList<>();
        for (Route r : routes()) {
            newRoutes.add(r);
        }
        newRoutes.add(route);
        return new PlayerState(tickets, cards.difference(claimCards), newRoutes);
    }

    /**
     * Method called by ticketsPoints()
     * @return a set of all the stations of the claimed routes of the player
     */
    private Set<Station> stations() {
        Set<Station> stations = new HashSet<>();
        for (Route route : routes()) {
            for (Station routeStation : route.stations()) {
                stations.add(routeStation);
            }
        }
        return stations;
    }

    /**
     * Returns the points the player has with his current tickets and his current connectivity
     * @return the total points the player has given its current tickets
     */
    public int ticketsPoints() {
        int maxStationIndex = 0, ticketsPoints = 0;
        for (Station station : stations()) {
            maxStationIndex = Math.max(station.id(), maxStationIndex);
        }
        //maxStationIndex = the max index of the stations fo the claimed routes of the player
        StationPartition.Builder connectivityBuilder = new StationPartition.Builder(maxStationIndex + 1);
        for (Route route : routes()) {
            connectivityBuilder.connect(route.station1(), route.station2());
        }
        StationPartition connectivity = connectivityBuilder.build();
        for (Ticket ticket : tickets) {
            ticketsPoints += ticket.points(connectivity);
        }
        return ticketsPoints;
    }

    /**
     * Returns the end-game points
     * @return the total points at the end of the game
     */
    public int finalPoints() {
        return claimPoints() + ticketsPoints();
    }

}
