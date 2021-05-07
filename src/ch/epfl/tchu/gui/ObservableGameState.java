package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

import static ch.epfl.tchu.game.Constants.FACE_UP_CARDS_COUNT;
import static ch.epfl.tchu.game.Constants.FACE_UP_CARD_SLOTS;

public final class ObservableGameState {

    private final PlayerId playerId;
    private PublicGameState publicGameState;
    private PlayerState playerState;

    private final IntegerProperty ticketPercentage;
    private final IntegerProperty cardPercentage;
    private final List<ObjectProperty<Card>> faceUpCards;
    private final Map<Route, ObjectProperty<PlayerId>> routes;

    private final Map<PlayerId, IntegerProperty> ticketCounts;
    private final Map<PlayerId, IntegerProperty> cardCounts;
    private final Map<PlayerId, IntegerProperty> carCounts;
    private final Map<PlayerId, IntegerProperty> claimPoints;

    private final ObservableList<Ticket> ownTickets;
    private final Map<Card, IntegerProperty> cardCountOnColor;
    private final Map<Route, BooleanProperty> claimableRoutes;


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

    private void setFaceUpCards(PublicGameState publicGameState) {
        for (int slot : FACE_UP_CARD_SLOTS) {
            Card newCard = publicGameState.cardState().faceUpCard(slot);
            faceUpCards.get(slot).set(newCard);
        }
    }

    private void setRoutes(PublicGameState publicGameState) {
        PlayerId.ALL.forEach(id -> {
            for (Route r : publicGameState.playerState(id).routes()) {
                routes.get(r).set(id);
            }
        });
    }

    private void setClaimableRoutes(PublicGameState publicGameState, PlayerState playerState) {
        claimableRoutes.forEach((route, claimable) -> { //TODO FAIRE LE TEST DE LA ROUTE VOISINE DANS CHMAP ou pas ? (voir piazza)
            if (playerId == publicGameState.currentPlayerId() &&
                    routes.get(route).get() == null &&
                    playerState.canClaimRoute(route)) {

                claimable.set(true);
            }
        });
    }


    private static List<ObjectProperty<Card>> createFaceUpCards() {
        List<ObjectProperty<Card>> faceUpCards = new ArrayList<>();

        for (int i = 0; i < FACE_UP_CARDS_COUNT; i++) {
            faceUpCards.add(new SimpleObjectProperty<>());
        }

        return faceUpCards;
    }

    private static Map<Route, ObjectProperty<PlayerId>> createRoutes() {
        Map<Route, ObjectProperty<PlayerId>> routes = new HashMap<>();
        ChMap.routes().forEach(r -> routes.put(r, new SimpleObjectProperty<>()));

        return routes;
    }

    private static Map<PlayerId, IntegerProperty> createCounts() {
        Map<PlayerId, IntegerProperty> counts = new EnumMap<>(PlayerId.class);
        PlayerId.ALL.forEach(i -> counts.put(i, new SimpleIntegerProperty()));

        return counts;
    }

    private static Map<Card, IntegerProperty> createsCardCountOnColor() {
        Map<Card, IntegerProperty> cardCountOnColor = new EnumMap<>(Card.class);
        Card.ALL.forEach(c -> cardCountOnColor.put(c, new SimpleIntegerProperty()));
        return cardCountOnColor;
    }

    private static Map<Route, BooleanProperty> createsClaimableRoutes() {
        Map<Route, BooleanProperty> claimableRoutes = new HashMap<>();
        ChMap.routes().forEach(r -> claimableRoutes.put(r, new SimpleBooleanProperty()));
        
        return claimableRoutes;
    }

    /**
     * Getter for the remaining ticketPercentage
     * @return ReadOnlyIntegerProperty of the ticketPercentage
     */
    public ReadOnlyIntegerProperty ticketPercentage() {
        return ticketPercentage;
    }

    /**
     * Getter for the remaining cardPercentage
     * @return ReadOnlyIntegerProperty of the cardPercentage
     */
    public ReadOnlyIntegerProperty cardPercentage() {
        return cardPercentage;
    }

    /**
     * Getter for a given faceUpCard
     * @param slot of the wanted faceUpCard
     * @return the right faceUpCard given the slot (ReadOnlyObjectProperty=
     */
    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) {
        return faceUpCards.get(slot);
    }

    /**
     * Getter for the owner of a route
     * @param route you want to know the owner
     * @return Maps the Id of the player to the route or null if the route isn't taken (ReadOnlyObjectProperty)
     */
    public ReadOnlyObjectProperty<PlayerId> routes(Route route){
        return routes.get(route);
    }

    /**
     * Getter for the amount of tickets of a given player
     * @param playerId of the player you want to know the amount of tickets
     * @return number of tickets (ReadOnlyIntegerProperty)
     */
    public ReadOnlyIntegerProperty ticketCounts(PlayerId playerId){
        return ticketCounts.get(playerId);
    }

    /**
     * Getter for the amount of cars of a given player
     * @param playerId of the player you want to know the amount of cars
     * @return number of cars (ReadOnlyIntegerProperty)
     */
    public ReadOnlyIntegerProperty carCounts(PlayerId playerId){
        return carCounts.get(playerId);
    }

    /**
     * Getter for the amount of cars of a given cards
     * @param playerId of the player you want to know the amount of cards
     * @return number of cards (ReadOnlyIntegerProperty)
     */
    public ReadOnlyIntegerProperty cardCounts(PlayerId playerId){
        return cardCounts.get(playerId);
    }

    /**
     * Getter for the points of a player
     * @param playerId of the player you want to know the points
     * @return points (ReadOnlyIntegerProperty)
     */
    public ReadOnlyIntegerProperty claimPoints(PlayerId playerId){
        return claimPoints.get(playerId);
    }

    /**
     * Getter for the list of Tickets for a given player
     * @return the List of tickets owned by a player
     */
    public ObservableList<Ticket> ownTickets(){ //TODO meilleur moyen ?
        return FXCollections.unmodifiableObservableList(ownTickets);
    }

    /**
     * Getter that returns the amount of a given card owned by a player
     * @param card you want to know the amount
     * @return the number of exemplars of a given card (ReadOnlyIntegerProperty)
     */
    public ReadOnlyIntegerProperty cardCountOnColor(Card card){
      return cardCountOnColor.get(card);
    }

    /**
     * Boolean that says if a given route can be claimed or not
     * @param route route you want to claim
     * @return true if the route is claimable, false else (ReadOnlyBooleanProperty)
     */
    public ReadOnlyBooleanProperty claimableRoutes(Route route){
        return claimableRoutes.get(route);
    }

    /**
     * Boolean that says if a player can draw tickets or not
     * @return true if tickets can be claimed, false else
     */
    public boolean canDrawTickets() {
        return publicGameState.canDrawTickets();
    }

    /**
     * Boolean that says if a player can draw cards or not
     * @return true if cards can be claimed, false else
     */
    public boolean canDrawCards() {
        return publicGameState.canDrawTickets();
    }

    public List<SortedBag<Card>> possibleClaimCards(Route route) {
        return playerState.possibleClaimCards(route);
    }
}
