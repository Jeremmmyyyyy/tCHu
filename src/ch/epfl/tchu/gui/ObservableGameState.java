package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.game.Constants.FACE_UP_CARD_SLOTS;
import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public final class ObservableGameState {

    private final PlayerId playerId;
    private final PublicGameState publicGameState;
    private final PlayerState playerState;

    private final IntegerProperty ticketPourcentage;
    private final IntegerProperty cardPourcentage;
    private final List<ObjectProperty<Card>> faceUpCards;
    private final MapProperty<Route, PlayerId> routes; //TODO MapProperty ????

    private final IntegerProperty ticketCount1;
    private final IntegerProperty ticketCount2;
    private final IntegerProperty cardCount1;
    private final IntegerProperty cardCount2;
    private final IntegerProperty carCount1;
    private final IntegerProperty carCount2;
    private final IntegerProperty claimPoints1;
    private final IntegerProperty claimPoints2;

    private final ObservableList<Ticket> ownTickets;
    private final IntegerProperty[] carCountOnColor;
    private final MapProperty<Route, Boolean> claimableRoutes;


    public ObservableGameState(PlayerId playerId) {
        this.playerId = playerId;
        playerState = new PlayerState(null, null, null); //TODO CHANGER
        publicGameState = new PublicGameState(0, null, null, null, null); //TODO CHANGER

        ticketPourcentage = new SimpleIntegerProperty(100);
        cardPourcentage = new SimpleIntegerProperty(100);
        faceUpCards = initializeFaceUpCards(); //TODO initialise 5 faceUpCards vide
        routes = initializeRoutes();

        ticketCount1 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_1).ticketCount());
        ticketCount2 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_2).ticketCount());
        cardCount1 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_1).cardCount());
        cardCount2 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_2).cardCount());
        carCount1 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_1).carCount());
        carCount2 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_2).carCount());
        claimPoints1 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_1).claimPoints());
        claimPoints2 = new SimpleIntegerProperty(publicGameState.playerState(PLAYER_2).claimPoints());

        ownTickets = FXCollections.observableList(List.of()); //TODO initialise tickets vide
        carCountOnColor = new SimpleIntegerProperty[9]; //TODO tableau statique initialisé à 0 ?
        claimableRoutes = initializeClaimableRoutes();
    }

    public void setState(PublicGameState publicGameState, PlayerState playerState) {
        PublicGameState newGameState = null;

        for (int slot : FACE_UP_CARD_SLOTS) {
            Card newCard = newGameState.cardState().faceUpCard(slot);
            faceUpCards.get(slot).set(newCard);
        }

    }

    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) {
        return faceUpCards.get(slot);
    }

    public ReadOnlyIntegerProperty ticketPourcentage() {
        return ticketPourcentage;
    }

    public ReadOnlyIntegerProperty cardPourcentage() {
        return cardPourcentage;
    }

    public ObservableList<Ticket> {
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

    private MapProperty<Route, Boolean> initializeClaimableRoutes() {
        MapProperty<Route, Boolean> claimableRoutes = new SimpleMapProperty<>();
        for (Route r : ChMap.routes()) {
            claimableRoutes.put(r, playerId == publicGameState.currentPlayerId()
                    && routes.get(r) == null
                    && playerState.canClaimRoute(r));
        }
        return claimableRoutes;
    }

    public boolean canDrawTickets() {
        return publicGameState.canDrawTickets();
    }

    public boolean canDrawCards() {

    }
}
