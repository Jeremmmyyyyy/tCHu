package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.ArrayList;
import java.util.List;

import static ch.epfl.tchu.game.Constants.FACE_UP_CARD_SLOTS;

public final class ObservableGameState {

    private final PlayerId playerId;

    private IntegerProperty ticketPourcentage;
    private IntegerProperty cardPourcentage;
    private List<ObjectProperty<Card>> faceUpCards;
    private MapProperty<Route, PlayerId> routes; //TODO MapProperty ????

    private IntegerProperty ticketCount1; //TODO quel attribut en final ?
    private IntegerProperty ticketCount2;
    private IntegerProperty cardCount1;
    private IntegerProperty cardCount2;
    private IntegerProperty carCount1;
    private IntegerProperty carCount2;
    private IntegerProperty claimPoints1;
    private IntegerProperty claimPoints2;

    private ObservableList<Ticket> ownTickets;
    private IntegerProperty[] carCountOnColor;
    private MapProperty<Route, Boolean> claimableRoutes;


    public ObservableGameState(PlayerId playerId) {
        this.playerId = playerId;

//        ticketPourcentage = new SimpleIntegerProperty(100);
//        cardPourcentage = new SimpleIntegerProperty(100);
        ticketPourcentage = new SimpleIntegerProperty(0);
        cardPourcentage = new SimpleIntegerProperty(0);
        faceUpCards = initializeFaceUpCards(); //TODO initialise 5 faceUpCards vide
        routes = initializeRoutes();

//        ticketCount1 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_1).ticketCount());
//        ticketCount2 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_2).ticketCount());
//        cardCount1 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_1).cardCount());
//        cardCount2 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_2).cardCount());
//        carCount1 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_1).carCount());
//        carCount2 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_2).carCount());
//        claimPoints1 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_1).claimPoints());
//        claimPoints2 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_2).claimPoints());

        ticketCount1 = new SimpleIntegerProperty(0);
        ticketCount2 = new SimpleIntegerProperty(0);
        cardCount1 = new SimpleIntegerProperty(0);
        cardCount2 = new SimpleIntegerProperty(0);
        carCount1 = new SimpleIntegerProperty(0);
        carCount2 = new SimpleIntegerProperty(0);
        claimPoints1 = new SimpleIntegerProperty(0);
        claimPoints2 = new SimpleIntegerProperty(0);

//        ownTickets = FXCollections.observableList(List.of()); //TODO initialise tickets vide
        ownTickets = null;
        carCountOnColor = new SimpleIntegerProperty[9]; //TODO tableau statique initialisé à 0 ?
        claimableRoutes = null;
    }

    public void setState(PublicGameState publicGameState, PlayerState playerState) {

        ticketPourcentage = new SimpleIntegerProperty(publicGameState.ticketsCount() / ChMap.tickets().size()); //TODO pas de constante pour le nombre de tickets total ?
        ticketPourcentage = new SimpleIntegerProperty(publicGameState.cardState().deckSize() / Constants.TOTAL_CARDS_COUNT); //TODO pas de constante pour le nombre de tickets total ?


        for (int slot : FACE_UP_CARD_SLOTS) {
            Card newCard = publicGameState.cardState().faceUpCard(slot);
            faceUpCards.get(slot).set(newCard);
        }
    }

    private static List<ObjectProperty<Card>> initializeFaceUpCards() { //TODO POURQUOI INDIQUE COMME STATIC DANS L'ENONCE ??
        List<ObjectProperty<Card>> initialCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            initialCards.add(new SimpleObjectProperty<>());
        }
        return initialCards;
    }

    private static MapProperty<Route, PlayerId> initializeRoutes() {
        MapProperty<Route, PlayerId> initialRoutes = new SimpleMapProperty<>(); //TODO ou simple pour route et playerId ???
        for (Route r : ChMap.routes()) {
            initialRoutes.put(r, null); //TODO initialiser a null ??
        }
        return initialRoutes;
    }

    private MapProperty<Route, Boolean> initializeClaimableRoutes(PublicGameState publicGameState, PlayerState playerState) {
        MapProperty<Route, Boolean> claimableRoutes = new SimpleMapProperty<>(); //TODO MapPorperty ?
        for (Route r : ChMap.routes()) {
            claimableRoutes.put(r, playerId == publicGameState.currentPlayerId()
                    && routes.get(r) == null
                    && playerState.canClaimRoute(r));
        }
        return claimableRoutes;
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



    public ReadOnlyIntegerProperty ticketPourcentage() {
        return ticketPourcentage;
    }

    public ReadOnlyIntegerProperty cardPourcentage() {
        return cardPourcentage;
    }

    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) {
        return faceUpCards.get(slot);
    }

    public ReadOnlyMapProperty<Route, PlayerId> routes(){
        return routes;
    }

    public ReadOnlyIntegerProperty ticketCount1(){
        return ticketCount1;
    }

    public ReadOnlyIntegerProperty ticketCount2(){
        return ticketCount2;
    }

    public ReadOnlyIntegerProperty cardCount1(){
        return cardCount1;
    }

    public ReadOnlyIntegerProperty cardCount2(){
        return cardCount2;
    }

    public ReadOnlyIntegerProperty carCount1(){
        return carCount1;
    }
    public ReadOnlyIntegerProperty carCount2(){
        return carCount2;
    }

    public ReadOnlyIntegerProperty claimPoints1(){
        return claimPoints1;
    }

    public ReadOnlyIntegerProperty claimPoints2(){
        return claimPoints2;
    }

    public ObservableList<Ticket> ownTickets(){ //TODO meilleur moyen ?
        return FXCollections.unmodifiableObservableList(ownTickets);
    }

    public ReadOnlyIntegerProperty[] carCountOnColor(){
        return carCountOnColor;
    }

    public ReadOnlyMapProperty<Route, Boolean> claimableRoutes(){
        return claimableRoutes;
    }
}
