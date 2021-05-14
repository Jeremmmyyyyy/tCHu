package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.stream.Collectors;

import static ch.epfl.tchu.game.Constants.FACE_UP_CARDS_COUNT;
import static ch.epfl.tchu.game.Constants.FACE_UP_CARD_SLOTS;

/**
 * Observes an entire GameState
 *
 * @author Yann Ennassih (329978)
 */
public final class ObservableGameState {

    private final PlayerId playerId;
    private PublicGameState publicGameState;
    private PlayerState playerState;

    //Public properties of the game
    private final IntegerProperty ticketPercentage;
    private final IntegerProperty cardPercentage;
    private final List<ObjectProperty<Card>> faceUpCards;
    private final Map<Route, ObjectProperty<PlayerId>> routes;

    //Properties for both players
    private final Map<PlayerId, IntegerProperty> ticketCounts;
    private final Map<PlayerId, IntegerProperty> cardCounts;
    private final Map<PlayerId, IntegerProperty> carCounts;
    private final Map<PlayerId, IntegerProperty> claimPoints;

    //Properties of the player attached to this ObservableGameState
    private final ObservableList<Ticket> ownTickets;
    private final Map<Card, IntegerProperty> cardCountOnColor;
    private final Map<Route, BooleanProperty> claimableRoutes;

    /**
     * Constructor : initializes all the properties of the class
     * @param playerId of the player attached to this
     */
    public ObservableGameState(PlayerId playerId) {
        this.playerId = playerId;

        ticketPercentage = new SimpleIntegerProperty();
        cardPercentage = new SimpleIntegerProperty();
        faceUpCards = createFaceUpCards();
        routes = createRoutes();

        ticketCounts = createCounts();
        cardCounts = createCounts();
        carCounts = createCounts();
        claimPoints = createCounts();

        ownTickets = FXCollections.observableArrayList();
        cardCountOnColor = createsCardCountOnColor();
        claimableRoutes = createsClaimableRoutes();

    }

    /**
     * Updates the publicGameState and playerState, so as the content of all the properties of the class
     * @param publicGameState new given PublicGameState
     * @param playerState new given PlayerState
     */
    public void setState(PublicGameState publicGameState, PlayerState playerState) {

        this.publicGameState = publicGameState;
        this.playerState = playerState;

        ticketPercentage.set(publicGameState.ticketsCount() * 100 / ChMap.tickets().size());
        cardPercentage.set(publicGameState.cardState().deckSize() * 100 / Constants.TOTAL_CARDS_COUNT);
        setFaceUpCards(publicGameState);
        setRoutes(publicGameState);

        ticketCounts.forEach((id, ticketCount) -> ticketCount.set(publicGameState.playerState(id).ticketCount()));
        cardCounts.forEach((id, cardCount) -> cardCount.set(publicGameState.playerState(id).cardCount()));
        carCounts.forEach((id, carCount) -> carCount.set(publicGameState.playerState(id).carCount()));
        claimPoints.forEach((id, points) -> points.set(publicGameState.playerState(id).claimPoints()));

        ownTickets.setAll(playerState.tickets().toList());
        setClaimableRoutes(publicGameState, playerState);
        cardCountOnColor.forEach((card, count) -> count.set(playerState.cards().countOf(card)));
    }

    /**
     * Updates the faceUpCards list's content
     * @param publicGameState considered
     */
    private void setFaceUpCards(PublicGameState publicGameState) {
        for (int slot : FACE_UP_CARD_SLOTS) {
            Card newCard = publicGameState.cardState().faceUpCard(slot);
            faceUpCards.get(slot).set(newCard);
        }
    }

    /**
     * Updates the route map's content
     * @param publicGameState considered
     */
    private void setRoutes(PublicGameState publicGameState) {
        PlayerId.ALL.forEach(id -> {
            for (Route r : publicGameState.playerState(id).routes()) {
                routes.get(r).set(id);
            }
        });
    }

    /**
     * Updates the claimableRoutes map's content
     * @param publicGameState considered
     * @param playerState considered
     */
    private void setClaimableRoutes(PublicGameState publicGameState, PlayerState playerState) {
        claimableRoutes.forEach((route, claimable) ->  //TODO FAIRE LE TEST DE LA ROUTE VOISINE DANS CHMAP ou pas ? (voir piazza)
            claimable.set(
                    playerId == publicGameState.currentPlayerId() &&
                    !claimableRoute(route, publicGameState) &&
                    playerState.canClaimRoute(route)));
    }

    private boolean claimableRoute(Route route, PublicGameState publicGameState) { //TODO inserer la ligne directement au dessus ou garder la methode ???
        return publicGameState.claimedRoutes()
                .stream()//TODO Bonne facon avec un stream ?
                .filter(r -> r.length() != route.length()) //TODO gain en cout avec le filter ?
                .map(Route::stations)
                .collect(Collectors.toList())
                .contains(route.stations()); //TODO comme contains utilise equals, le test est fonctionnel ?
    }

    /**
     * Constructs the initial faceUpCards list
     * @return a new list of faceUpCards, as ObjectProperties of Card
     */
    private static List<ObjectProperty<Card>> createFaceUpCards() {
        List<ObjectProperty<Card>> faceUpCards = new ArrayList<>();

        for (int i = 0; i < FACE_UP_CARDS_COUNT; i++) {
            faceUpCards.add(new SimpleObjectProperty<>());
        }

        return faceUpCards;
    }

    /**
     * Constructs the initial routes map
     * @return a new routes map, as new ObjectProperties of PlayerId
     */
    private static Map<Route, ObjectProperty<PlayerId>> createRoutes() {
        Map<Route, ObjectProperty<PlayerId>> routes = new HashMap<>();
        ChMap.routes().forEach(r -> routes.put(r, new SimpleObjectProperty<>()));

        return routes;
    }

    /**
     * Constructs an initial <...>Counts map for <...> in {ticket, car, card, claimPoints}
     * @return a new <...>Counts map, as IntegerProperty
     */
    private static Map<PlayerId, IntegerProperty> createCounts() {
        Map<PlayerId, IntegerProperty> counts = new EnumMap<>(PlayerId.class);
        PlayerId.ALL.forEach(i -> counts.put(i, new SimpleIntegerProperty()));

        return counts;
    }

    /**
     * Constructs the initial cardCountOnColor map
     * @return a new cardCountOnColor map, as new IntegerProperties
     */
    private static Map<Card, IntegerProperty> createsCardCountOnColor() {
        Map<Card, IntegerProperty> cardCountOnColor = new EnumMap<>(Card.class);
        Card.ALL.forEach(c -> cardCountOnColor.put(c, new SimpleIntegerProperty()));
        return cardCountOnColor;
    }

    /**
     * Constructs the initial claimableRoutes map
     * @return a new claimableRoutes map, as new BooleanProperties
     */
    private static Map<Route, BooleanProperty> createsClaimableRoutes() {
        Map<Route, BooleanProperty> claimableRoutes = new HashMap<>();
        ChMap.routes().forEach(r -> claimableRoutes.put(r, new SimpleBooleanProperty()));
        
        return claimableRoutes;
    }

    /**
     * Getter for the remaining ticketPercentage
     * @return the ticketPercentage, as ReadOnlyIntegerProperty
     */
    public ReadOnlyIntegerProperty ticketPercentage() {
        return ticketPercentage;
    }

    /**
     * Getter for the remaining cardPercentage
     * @return the cardPercentage, as ReadOnlyIntegerProperty
     */
    public ReadOnlyIntegerProperty cardPercentage() {
        return cardPercentage;
    }

    /**
     * Getter for a given faceUpCard
     * @param slot of the wanted faceUpCard
     * @return the faceUpCard of given the slot, as ReadOnlyObjectProperty
     */
    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) {
        return faceUpCards.get(slot);
    }

    /**
     * Getter for the owner of a given route
     * @param route that is considered
     * @return the PlayerId of the owner of the given route
     * or null if the route hasn't been claimed, as ReadOnlyObjectProperty
     */
    public ReadOnlyObjectProperty<PlayerId> routes(Route route){
        return routes.get(route);
    }

    /**
     * Getter for the amount of tickets of a given player
     * @param playerId of the considered player
     * @return the number of tickets of the given player, as ReadOnlyIntegerProperty
     */
    public ReadOnlyIntegerProperty ticketCounts(PlayerId playerId){
        return ticketCounts.get(playerId);
    }

    /**
     * Getter for the amount of cars of a given player
     * @param playerId of the considered player
     * @return the number of cars of the given player, as ReadOnlyIntegerProperty
     */
    public ReadOnlyIntegerProperty carCounts(PlayerId playerId){
        return carCounts.get(playerId);
    }

    /**
     * Getter for the amount of cards of a given player
     * @param playerId of the considered player
     * @return the number of cards of the given player, as ReadOnlyIntegerProperty
     */
    public ReadOnlyIntegerProperty cardCounts(PlayerId playerId){
        return cardCounts.get(playerId);
    }

    /**
     * Getter for the points of a given player
     * @param playerId of the considered player
     * @return the points of the given player, as ReadOnlyIntegerProperty
     */
    public ReadOnlyIntegerProperty claimPoints(PlayerId playerId){
        return claimPoints.get(playerId);
    }

    /**
     * Getter for the tickets of the player
     * @return an observable list of tickets owned by the player
     */
    public ObservableList<Ticket> ownTickets(){
        return FXCollections.unmodifiableObservableList(ownTickets);
    }

    /**
     * Getter that returns the amount of a given card owned by the player
     * @param card to count
     * @return the number of exemplars of a given card, as ReadOnlyIntegerProperty
     */
    public ReadOnlyIntegerProperty cardCountOnColor(Card card){
      return cardCountOnColor.get(card);
    }

    /**
     * Boolean that says if a given route can be claimed or not
     * @param route to claim
     * @return true if the route is claimable, false else, as ReadOnlyBooleanProperty
     */
    public ReadOnlyBooleanProperty claimableRoute(Route route){
        return claimableRoutes.get(route);
    }

    /**
     * Calls the canDrawTickets() of PublicGameState class
     * @return true if a ticket can be drawn from the ticket-deck
     * (the size of the discard + deck must be greater than 5)
     */
    public boolean canDrawTickets() {
        return publicGameState.canDrawTickets();
    }

    /**
     * Calls canDrawTickets() method of PublicGameState class
     * @return true if a card can be drawn from the card-deck
     */
    public boolean canDrawCards() {
        return publicGameState.canDrawCards();
    }

    /**
     * Calls possibleClaimCards(...) method of PlayerState class
     * @param route to claim
     * @return a List of all the SortedBags containing the possible combinations of cards to take the given route
     */
    public List<SortedBag<Card>> possibleClaimCards(Route route) {
        return playerState.possibleClaimCards(route);
    }
}
