package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.*;
/**
 * State of the player
 *
 * @author Jérémy Barghorn (328403)
 */
public final class PlayerState extends PublicPlayerState{

    private final SortedBag<Ticket> tickets;
    private final SortedBag<Card> cards;

    /**
     * create a new player state
     * @param tickets SortedBag of all the tickets of a player
     * @param cards SortedBag of all the cards of a player
     * @param routes List of the routes of a player
     */
    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes) {
        super(tickets.size(), cards.size(), routes);
        this.tickets = tickets;
        this.cards  = cards;
    }

    /**
     * Initial state of o Player where the tickets are empty, the cards are the initial cards and the routes are empty
     * @param initialCards SortedBag of cards
     * @return a new PlayerState
     * @throws IllegalArgumentException if the initial cards are not equal to 4
     */
    public static PlayerState initial(SortedBag<Card> initialCards){
        Preconditions.checkArgument(initialCards.size() == Constants.INITIAL_CARDS_COUNT);
        return new PlayerState(SortedBag.of(), initialCards, List.of());
    }

    /**
     * return a SortedBag of tickets
     * @return a SortedBag of tickets
     */
    public SortedBag<Ticket> tickets(){
        return tickets;
    }

    /**
     * Add a new ticket to the ticket list
     * @param newTickets SortedBag of Tickets that is added to the cards
     * @return a new PlayerState with the newTickets added to the actual cards
     */
    public PlayerState withAddedTickets(SortedBag<Ticket> newTickets){
        return new PlayerState(tickets.union(newTickets), cards, routes());
    }

    /**
     * return all the cards of a PlayerState
     * @return all the cards of a PlayerState
     */
    public SortedBag<Card> cards(){
        return cards;
    }

    /**
     * Add the given card to the list of cards
     * @param card to add to the cards
     * @return a new PlayerState where the card is added to the cards
     */
    public PlayerState withAddedCard(Card card){
        return new PlayerState(tickets, cards.union(SortedBag.of(card)), routes());
    }

    /**
     * Add the SortedBag of card to the actual cards
     * @param additionalCards SortedBag that is added to the actual cards
     * @return a new PlayerState where the SortedBag is added to the cards
     */
    public PlayerState withAddedCards(SortedBag<Card> additionalCards){
        return new PlayerState(tickets, cards.union(additionalCards), routes());
    }

    /**
     * check if a player can take the control of the given route
     * @param route that is tested
     * @return true if the route can be taken false otherwise
     */
    public boolean canClaimRoute(Route route){
        return route.length() <= carCount() && !possibleClaimCards(route).isEmpty();
    }

    /**
     * Give a List of all the cards combination that can be used to take control of a route
     * @param route to analyse
     * @return a List of all the SortedBags containing the possible combinations of cards to take the given route
     */
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

    /**
     * Give a List of all the possible card combinations that are possibly required to take a tunnel
     * @param additionalCardsCount amount of additional cards to lay on the board
     * @param initialCards SortedBag of the cards that are already on the table
     * @param drawnCards SortedBag of the drawnCards
     * @return A List of all the possible combinations of additional cards that are required to take the tunnel
     * @throws IllegalArgumentException if the additionalCardsCount is not in 1,3
     * @throws IllegalArgumentException if the initialCards are Empty
     * @throws IllegalArgumentException if there are more than 2 types of cards in the initialCards
     * @throws IllegalArgumentException if the drawnCards are not equal to 3
     */
    public List<SortedBag<Card>> possibleAdditionalCards(int additionalCardsCount, SortedBag<Card> initialCards, SortedBag<Card> drawnCards){
        Preconditions.checkArgument(additionalCardsCount >= 1 && additionalCardsCount <=3);
        Preconditions.checkArgument(!initialCards.isEmpty());
        Map<Card, Integer> initialTypes =  initialCards.toMap();
        Preconditions.checkArgument(initialTypes.size() <= 2);
        Preconditions.checkArgument(drawnCards.size() == Constants.ADDITIONAL_TUNNEL_CARDS);

        SortedBag<Card> cardDifference = cards.difference(initialCards);
        SortedBag.Builder<Card> cardBuilder = new SortedBag.Builder<>();
        for (Card card : cardDifference) {
            if(initialCards.contains(card) || card==Card.LOCOMOTIVE){
                cardBuilder.add(card);
            }
        }
        SortedBag<Card> remainingCards = cardBuilder.build();  //TODO changer la syntaxe
        if(remainingCards.size()>=additionalCardsCount){
            Set<SortedBag<Card>> cardsSet = remainingCards.subsetsOfSize(additionalCardsCount);
            List<SortedBag<Card>> possibleAdditionalCards = new ArrayList<>(cardsSet);
            possibleAdditionalCards.sort(
                    Comparator.comparingInt(cs -> cs.countOf(Card.LOCOMOTIVE)));
            return possibleAdditionalCards;
        }else{
            return List.of();
        }
    }

    /**
     * Returns the same PlayerState with an added claimed route and without the claimCards in the playing hand
     * @param route that was claimed by the player
     * @param claimCards the card used to claim the route
     * @return a new PlayerState where routes is extended by route and cards shortened by claimCards
     */
    public PlayerState withClaimedRoute(Route route, SortedBag<Card> claimCards) {
        List<Route> newRoutes = new ArrayList<>(routes());
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
    public int ticketPoints() {
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
        return claimPoints() + ticketPoints();
    }

}
