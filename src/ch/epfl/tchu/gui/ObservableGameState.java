package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

import static ch.epfl.tchu.game.Constants.FACE_UP_CARDS_COUNT;
import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public final class ObservableGameState {

    private final PlayerId playerId;

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


        //TODO arraylist et pas de SimpleObjectProperty ?
        routes = createRoutes(); //TODO hashMap et pas de SimpleObjectProperty ?

        ticketCounts = createCounts(); //TODO pas besoin de SimpleIntegerProperty ??
        cardCounts = createCounts();
        carCounts = createCounts();
        claimPoints = createCounts();

        ownTickets = FXCollections.observableArrayList(); //TODO ou unmodifiableList
        carCountOnCard = createsCarCountOnCard(); //TODO pas de SimpleIntegerProperty ?
        claimableRoutes = createsClaimableRoutes(); //TODO pas de SimpleBooleanProperty ?

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
        Card.CARS.forEach(c -> carCountOnCard.put(c, new SimpleIntegerProperty()));
        return carCountOnCard;
    }

    private static Map<Route, BooleanProperty> createsClaimableRoutes() {
        Map<Route, BooleanProperty> claimableRoutes = new HashMap<>();
        ChMap.routes().forEach(r -> claimableRoutes.put(r, new SimpleBooleanProperty()));
        return claimableRoutes;
    }






    public void setState(PublicGameState publicGameState, PlayerState playerState) {

        List<PublicPlayerState> publicPlayerStates = List.of(
                publicGameState.playerState(PLAYER_1),
                publicGameState.playerState(PLAYER_2));

        ticketPercentage.set(publicGameState.ticketsCount() / ChMap.tickets().size());
        cardPercentage.set(publicGameState.cardState().deckSize() / Constants.TOTAL_CARDS_COUNT);
//        setFaceUpCards(publicGameState);
        setRoutes(publicPlayerStates);


        //TODO pour ceux qui bouclent faire des stream non ???
//        for (int i = 0; i < 2; i++) {
//            ticketCounts[i] = new SimpleIntegerProperty(publicPlayerStates.get(i).ticketCount());
//            carCounts[i] = new SimpleIntegerProperty(publicPlayerStates.get(i).carCount());
//            cardCounts[i] = new SimpleIntegerProperty(publicPlayerStates.get(i).cardCount());
//            claimPoints[i] = new SimpleIntegerProperty(publicPlayerStates.get(i).claimPoints());
//        }

//        ownTickets = FXCollections.observableList(playerState.tickets().toList());
//        for (int i = 0; i < 9; i++) {
//            carCountOnColor[i] = new SimpleIntegerProperty(playerState.cards().countOf(Card.values()[i])); //TODO bien ici ?
//        }
//        setClaimableRoutes(publicGameState, playerState);

    }

    public boolean canDrawTickets(PublicGameState publicGameState) {
        return publicGameState.canDrawTickets();
    }

    public boolean canDrawCards(PublicGameState publicGameState) {
        return publicGameState.canDrawTickets();
    }

    public List<SortedBag<Card>> possibleClaimCards(Route route, PlayerState playerState) {
        return playerState.possibleClaimCards(route);
    }

//    private static void setFaceUpCards(PublicGameState publicGameState) { //TODO pourquoi indiquer comme static dans l'enonce ??
//        for (int slot : FACE_UP_CARD_SLOTS) {
//            Card newCard = publicGameState.cardState().faceUpCard(slot);
//            faceUpCards.get(slot).set(newCard);
//        }
//    }

    private void setRoutes(List<PublicPlayerState> publicPlayerStates) {
        for (int i = 0; i < PlayerId.ALL.size(); i++) {
            for (Route r : publicPlayerStates.get(i).routes()) {
                routes.get  (r).set(PlayerId.values()[i]); //TODO bien comme ca ?
            }
        }
    }

//    private void setClaimableRoutes(PublicGameState publicGameState, PlayerState playerState) {
//        for (Route r : ChMap.routes()) {
//            claimableRoutes.put(r, playerId == publicGameState.currentPlayerId()
//                    && routes.get(r) == null
//                    && playerState.canClaimRoute(r));
//        }



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
}
