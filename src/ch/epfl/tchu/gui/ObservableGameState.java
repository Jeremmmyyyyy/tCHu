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
    private PublicGameState publicGameState; //TODO not final ? maj dans setState et utilisation dans les 3 methodes a la fin
    private PlayerState playerState;

    private final IntegerProperty ticketPercentage;
    private final IntegerProperty cardPercentage;
    private final List<ObjectProperty<Card>> faceUpCards;
    private final Map<Route, ObjectProperty<PlayerId>> routes; //TODO comme ca ??

    private final Map<PlayerId, IntegerProperty> ticketCounts;
    private final Map<PlayerId, IntegerProperty> cardCounts;
    private final Map<PlayerId, IntegerProperty> carCounts;
    private final Map<PlayerId, IntegerProperty> claimPoints;

    private final ObservableList<Ticket> ownTickets; //TODO ou ObservableList<ObjectProperty<Ticket>>
    private final Map<Card, IntegerProperty> carCountOnCard;
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

        ownTickets = FXCollections.observableArrayList(); //TODO ou unmodifiableList
        carCountOnCard = createsCarCountOnCard(); //TODO pas de SimpleIntegerProperty ?
        claimableRoutes = createsClaimableRoutes(); //TODO pas de SimpleBooleanProperty ?

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

        ownTickets.setAll(playerState.tickets().toList());  //TODO ne modifie pas la liste ?
        setClaimableRoutes(publicGameState, playerState);
        carCountOnCard.forEach((card, count) -> count.set(playerState.carCount()));

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
                routes.get(r).set(id);//TODO bien comme ca ?
            }
        });
    }

    private void setClaimableRoutes(PublicGameState publicGameState, PlayerState playerState) {
        claimableRoutes.forEach((route, claimable) -> { //TODO FAIRE LE TEST DE LA ROUTE VOISINE DANS CHMAP ou pas ? (voir piazza)
            if (playerId == publicGameState.currentPlayerId() &&
                    routes.get(route) == null &&
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

    private static Map<Card, IntegerProperty> createsCarCountOnCard() {
        Map<Card, IntegerProperty> carCountOnCard = new EnumMap<>(Card.class);
        Card.ALL.forEach(c -> carCountOnCard.put(c, new SimpleIntegerProperty()));
        return carCountOnCard;
    }

    private static Map<Route, BooleanProperty> createsClaimableRoutes() {
        Map<Route, BooleanProperty> claimableRoutes = new HashMap<>();
        ChMap.routes().forEach(r -> claimableRoutes.put(r, new SimpleBooleanProperty()));
        
        return claimableRoutes;
    }

    public ReadOnlyIntegerProperty ticketPercentage() {
        return ticketPercentage;
    }

    public ReadOnlyIntegerProperty cardPercentage() {
        return cardPercentage;
    }

    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) {
        return faceUpCards.get(slot);
    }

    public ReadOnlyObjectProperty<PlayerId> routes(Route route){
        return routes.get(route);
    }

    public ReadOnlyIntegerProperty ticketCounts(PlayerId playerId){
        return ticketCounts.get(playerId);
    }

    public ReadOnlyIntegerProperty carCounts(PlayerId playerId){
        return carCounts.get(playerId);
    }

    public ReadOnlyIntegerProperty cardCounts(PlayerId playerId){
        return cardCounts.get(playerId);
    }

    public ReadOnlyIntegerProperty claimPoints(PlayerId playerId){
        return claimPoints.get(playerId);
    }

    public ObservableList<Ticket> ownTickets(){ //TODO meilleur moyen ?
        return FXCollections.unmodifiableObservableList(ownTickets);
    }

    public ReadOnlyIntegerProperty carCountOnCard(Card card){
      return carCountOnCard.get(card);
    }

    public ReadOnlyBooleanProperty claimableRoutes(Route route){
        return claimableRoutes.get(route);
    }

    public boolean canDrawTickets() {
        return publicGameState.canDrawTickets();
    }

    public boolean canDrawCards() {
        return publicGameState.canDrawTickets();
    }

    public List<SortedBag<Card>> possibleClaimCards(Route route) {
        return playerState.possibleClaimCards(route);
    }
}
