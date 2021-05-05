package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

import static ch.epfl.tchu.game.Constants.FACE_UP_CARD_SLOTS;
import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public final class ObservableGameState {

    private final PlayerId playerId;

    private IntegerProperty ticketPercentage;
    private IntegerProperty cardPercentage;
    private List<ObjectProperty<Card>> faceUpCards;
    private MapProperty<Route, PlayerId> routes; //TODO MapProperty ????

    private IntegerProperty[] ticketCounts;
    private IntegerProperty[] cardCounts;
    private IntegerProperty[] carCounts;
    private IntegerProperty[] claimPoints;


    private ObservableList<Ticket> ownTickets;
    private IntegerProperty[] carCountOnColor;
    private MapProperty<Route, Boolean> claimableRoutes;


    public ObservableGameState(PlayerId playerId) {
        this.playerId = playerId;

//        ticketPourcentage = new SimpleIntegerProperty(100);
//        cardPourcentage = new SimpleIntegerProperty(100);
        ticketPercentage = new SimpleIntegerProperty(0);
        cardPercentage = new SimpleIntegerProperty(0);
        faceUpCards = null;
        routes = null;

//        ticketCount1 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_1).ticketCount());
//        ticketCount2 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_2).ticketCount());
//        cardCount1 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_1).cardCount());
//        cardCount2 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_2).cardCount());
//        carCount1 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_1).carCount());
//        carCount2 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_2).carCount());
//        claimPoints1 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_1).claimPoints());
//        claimPoints2 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_2).claimPoints());
        ticketCounts = new IntegerProperty[2]; //TODO tableau statique ??
        carCounts = new IntegerProperty[2];
        cardCounts = new IntegerProperty[2];
        claimPoints = new IntegerProperty[2];


//        ownTickets = FXCollections.observableList(List.of()); //TODO initialise tickets vide
        ownTickets = null;
        carCountOnColor = new SimpleIntegerProperty[9]; //TODO tableau statique initialisé à 0 ?
        claimableRoutes = null;
    }

    public void setState(PublicGameState publicGameState, PlayerState playerState) {

        List<PublicPlayerState> publicPlayerStates = List.of(
                publicGameState.playerState(PLAYER_1),
                publicGameState.playerState(PLAYER_2));

        ticketPercentage = new SimpleIntegerProperty(publicGameState.ticketsCount() / ChMap.tickets().size()); //TODO pas de constante pour le nombre de tickets total ?
        cardPercentage = new SimpleIntegerProperty(publicGameState.cardState().deckSize() / Constants.TOTAL_CARDS_COUNT);
        setFaceUpCards(publicGameState);
        setRoutes(publicPlayerStates);


        //TODO pour ceux qui bouclent faire des stream non ???
        for (int i = 0; i < 2; i++) {
            ticketCounts[i] = new SimpleIntegerProperty(publicPlayerStates.get(i).ticketCount());
            carCounts[i] = new SimpleIntegerProperty(publicPlayerStates.get(i).carCount());
            cardCounts[i] = new SimpleIntegerProperty(publicPlayerStates.get(i).cardCount());
            claimPoints[i] = new SimpleIntegerProperty(publicPlayerStates.get(i).claimPoints());
        }

        ownTickets = FXCollections.observableList(playerState.tickets().toList());
        for (int i = 0; i < 9; i++) {
            carCountOnColor[i] = new SimpleIntegerProperty(playerState.cards().countOf(Card.values()[i])); //TODO bien ici ?
        }
        setClaimableRoutes(publicGameState, playerState);

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

    private void setFaceUpCards(PublicGameState publicGameState) { //TODO pourquoi indiquer comme static dans l'enonce ??
        for (int slot : FACE_UP_CARD_SLOTS) {
            Card newCard = publicGameState.cardState().faceUpCard(slot);
            faceUpCards.get(slot).set(newCard);
        }
    }

    private void setRoutes(List<PublicPlayerState> publicPlayerStates) {
        for (Route route : ChMap.routes()) {
            routes.put(route,
                    !publicPlayerStates.get(0).routes().contains(route) ? //TODO pas un meilleur test possible que contains ?s
                    !publicPlayerStates.get(1).routes().contains(route) ? //TODO deux operateurs terniares joli ?
                            null :
                            PLAYER_2 :
                            PLAYER_1);
        }
    }

    private void setClaimableRoutes(PublicGameState publicGameState, PlayerState playerState) {
        for (Route r : ChMap.routes()) {
            claimableRoutes.put(r, playerId == publicGameState.currentPlayerId()
                    && routes.get(r) == null
                    && playerState.canClaimRoute(r));
        }
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

    public ReadOnlyMapProperty<Route, PlayerId> routes(){
        return routes;
    }

    public ReadOnlyIntegerProperty[] ticketCounts(){
        return ticketCounts;
    }

    public ReadOnlyIntegerProperty[] carCounts(){
        return carCounts;
    }

    public ReadOnlyIntegerProperty[] cardCounts(){
        return cardCounts;
    }

    public ReadOnlyIntegerProperty[] claimPoints(){
        return claimPoints;
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
