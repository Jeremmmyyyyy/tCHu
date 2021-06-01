package ch.epfl.tchu.tutorial;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.Info;
import ch.epfl.tchu.gui.TutorialGraphicalPlayerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static ch.epfl.tchu.game.PlayerId.*;

public final class Tutorial {

    //In order to make the class non instantiable
    private Tutorial(){
        throw new UnsupportedOperationException();}

    private static final String INFO = "INFO";
    private static final String DO = "DO";
    private static final String NEXT = "NEXT";

    private static final int INFO_LENGTH = INFO.length();
    private static final int DO_LENGTH = DO.length();
    private static final int NEXT_LENGTH = NEXT.length();
    private static final int INTRODUCTION_LENGTH = 12;

    public static void play(String playerName) {

        TutorialGraphicalPlayerAdapter player = new TutorialGraphicalPlayerAdapter();

        TutorialText tutorialText = new TutorialText();

        Info info = new Info(playerName);

        GameState currentGameState = GameState.initial(SortedBag.of(ChMap.tickets()), new Random());
        PlayerId id = currentGameState.currentPlayerId();




        Map<PlayerId, String> playerNames = Map.of(PLAYER_1, playerName, PLAYER_2, "Ordinateur");

        player.initPlayers(id, playerNames);

        player.waitsForNext(tutorialText.nextLine());
        player.waitsForNext(tutorialText.nextLine());
        player.waitsForNext(tutorialText.nextLine());
        player.receiveInfo("Comme ceci !\n");
        player.waitsForNext(tutorialText.nextLine());
        player.waitsForNext(tutorialText.nextLine());
        player.waitsForNext(tutorialText.nextLine());
        player.waitsForNext(tutorialText.nextLine());

        player.setInitialTicketChoice(currentGameState.topTickets(Constants.INITIAL_TICKETS_COUNT));
        currentGameState = currentGameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);


        player.updateState(currentGameState, currentGameState.playerState(id));
        SortedBag<Ticket> chosenTickets = player.chooseInitialTickets();
        currentGameState = currentGameState.withInitiallyChosenTickets(id, chosenTickets);
        player.receiveInfo(info.keptTickets(chosenTickets.size()));

        player.updateState(currentGameState, currentGameState.playerState(id));

        player.next(tutorialText.nextLine());


        player.waitsForNext(tutorialText.nextLine());
        player.receiveInfo(info.willPlayFirst());
        player.waitsForNext(tutorialText.nextLine()); //10
        player.waitsForNext(tutorialText.nextLine());

        player.updateState(currentGameState, currentGameState.playerState(id));

        player.receiveInfo(info.canPlay());

        currentGameState = currentGameState.withCardsDeckRecreatedIfNeeded(new Random());
        int slot = player.drawSlot();
        if (0 <= slot && slot < Constants.FACE_UP_CARDS_COUNT) {

            player.receiveInfo(info.drewVisibleCard(currentGameState.cardState().faceUpCard(slot)));
            currentGameState = currentGameState.withDrawnFaceUpCard(slot);

        } else if (slot == Constants.DECK_SLOT) {

            player.receiveInfo(info.drewBlindCard());
            currentGameState = currentGameState.withBlindlyDrawnCard();
        }

        currentGameState = currentGameState.withCardsDeckRecreatedIfNeeded(new Random());
        player.updateState(currentGameState, currentGameState.playerState(id));

        player.next(tutorialText.nextLine()); //14
        player.waitsForNext(tutorialText.nextLine()); //13

        int slot1 = player.drawSlot();
        if (0 <= slot1 && slot1 < Constants.FACE_UP_CARDS_COUNT) {

            player.receiveInfo(info.drewVisibleCard(currentGameState.cardState().faceUpCard(slot1)));
            currentGameState = currentGameState.withDrawnFaceUpCard(slot1);

        } else if (slot1 == Constants.DECK_SLOT) {

            player.receiveInfo(info.drewBlindCard());
            currentGameState = currentGameState.withBlindlyDrawnCard();
        }
        player.updateState(currentGameState, currentGameState.playerState(id));

        player.next(tutorialText.nextLine()); //14
        player.waitsForNext(tutorialText.nextLine()); //15
        player.waitsForNext(tutorialText.nextLine()); //16

        player.receiveInfo(info.drewTickets(Constants.IN_GAME_TICKETS_COUNT));
        SortedBag<Ticket> drawnTickets = currentGameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
        SortedBag<Ticket> keptTickets = player.chooseTickets(drawnTickets);
        player.receiveInfo(info.keptTickets(keptTickets.size()));
        currentGameState = currentGameState.withChosenAdditionalTickets(drawnTickets, keptTickets);
        player.updateState(currentGameState, currentGameState.playerState(id));


        player.next(tutorialText.nextLine()); //17
        player.waitsForNext(tutorialText.nextLine()); //18
        player.waitsForNext(tutorialText.nextLine()); //19

        PlayerState stateWithAddedVioletCards = currentGameState.playerState(id);
        for (int i = 0; i < 4; i++) {
            stateWithAddedVioletCards = stateWithAddedVioletCards.withAddedCard(Card.VIOLET);
        }
        player.updateState(currentGameState, stateWithAddedVioletCards);

        Route claimedRoute1 = player.claimedRoute();
        SortedBag<Card> initialClaimCards1 = player.initialClaimCards();
        PlayerState claimPlayerState1 = currentGameState.playerState(id);
        while (!claimedRoute1.equals(ChMap.routes().get(49))) {
            for (int i = 0; i < 4; i++) {
                claimPlayerState1 = claimPlayerState1.withAddedCard(Card.VIOLET);
            }
            player.next("Ce n'est pas la bonne route. Veuillez recommencer : emparez vous de la route Interlaken - Lucerne.");
            claimedRoute1 = player.claimedRoute();
            initialClaimCards1 = player.initialClaimCards();
            player.updateState(currentGameState, claimPlayerState1);
        }
        currentGameState = currentGameState.withClaimedRoute(claimedRoute1, initialClaimCards1);
        player.updateState(currentGameState, currentGameState.playerState(id));
        player.receiveInfo(info.claimedRoute(claimedRoute1, initialClaimCards1));

        player.next(tutorialText.nextLine());//20
        player.waitsForNext(tutorialText.nextLine());//21
        player.waitsForNext(tutorialText.nextLine());//22
        player.waitsForNext(tutorialText.nextLine());//23

        PlayerState stateWithAddedBlueCards = currentGameState.playerState(id);
        for (int i = 0; i < 3; i++) {
            stateWithAddedBlueCards = stateWithAddedBlueCards.withAddedCard(Card.BLUE);
        }
        player.updateState(currentGameState, stateWithAddedBlueCards);

        Route claimedRoute2 = player.claimedRoute();
        SortedBag<Card> initialClaimCards2 = player.initialClaimCards();
        PlayerState claimPlayerState2 = currentGameState.playerState(id);
        while (!claimedRoute2.equals(ChMap.routes().get(15))) {
            for (int i = 0; i < 3; i++) {
                claimPlayerState2 = claimPlayerState2.withAddedCard(Card.BLUE);
            }
            player.next("Ce n'est pas la bonne route. Veuillez recommencer : emparez vous de la route Berne - Interlaken.");
            claimedRoute2 = player.claimedRoute();
            initialClaimCards2 = player.initialClaimCards();
            player.updateState(currentGameState, claimPlayerState2);
        }
        currentGameState = currentGameState.withClaimedRoute(claimedRoute2, initialClaimCards2);
        player.updateState(currentGameState, currentGameState.playerState(id));
        player.receiveInfo(info.claimedRoute(claimedRoute2, initialClaimCards2));

        player.next(tutorialText.nextLine()); //24
        player.waitsForNext(tutorialText.nextLine()); //25

        Route claimedRoute3 = player.claimedRoute();
        SortedBag<Card> initialClaimCards3 = player.initialClaimCards();
        PlayerState claimPlayerState3 = currentGameState.playerState(id);
        while (!claimedRoute3.equals(ChMap.routes().get(16)) || !claimedRoute3.equals(ChMap.routes().get(17))) {
            System.out.println("enters here");
            for (int i = 0; i < 4; i++) {
                claimPlayerState3 = claimPlayerState3.withAddedCard(Card.YELLOW);
            }
            player.next("Ce n'est pas la bonne route. Veuillez recommencer : emparez vous de la route Berne - Lucerne.");
            claimedRoute3 = player.claimedRoute();
            initialClaimCards3 = player.initialClaimCards();
            player.updateState(currentGameState, claimPlayerState3);
        }
        currentGameState = currentGameState.withClaimedRoute(claimedRoute3, initialClaimCards3);
        player.updateState(currentGameState, currentGameState.playerState(id));
        player.receiveInfo(info.claimedRoute(claimedRoute3, initialClaimCards3));

        player.next(tutorialText.nextLine()); //26

        Route claimedRoute4 = player.claimedRoute();
        SortedBag<Card> initialClaimCards4 = player.initialClaimCards();
        PlayerState claimPlayerState4 = currentGameState.playerState(id);
        while (!claimedRoute4.equals(ChMap.routes().get(16)) || !claimedRoute4.equals(ChMap.routes().get(17))) {
            for (int i = 0; i < 4; i++) {
                claimPlayerState4 = claimPlayerState4.withAddedCard(Card.GREEN);
            }
            player.next("Ce n'est pas la bonne route. Veuillez recommencer : emparez vous de la route Lausanne - Neuchâtel.");
            claimedRoute4 = player.claimedRoute();
            initialClaimCards4 = player.initialClaimCards();
            player.updateState(currentGameState, claimPlayerState4);
        }
        currentGameState = currentGameState.withClaimedRoute(claimedRoute4, initialClaimCards4);
        player.updateState(currentGameState, currentGameState.playerState(id));
        player.receiveInfo(info.claimedRoute(claimedRoute4, initialClaimCards4));

        player.next(tutorialText.nextLine()); //27



        System.out.println("has reached the end");

    }

    private static void withRe(TutorialGraphicalPlayerAdapter player, GameState currentGameState, PlayerId id, Info info) {
        Route claimedRoute4 = player.claimedRoute();
        SortedBag<Card> initialClaimCards4 = player.initialClaimCards();
        PlayerState claimPlayerState4 = currentGameState.playerState(id);
        while (!claimedRoute4.equals(ChMap.routes().get(16)) || !claimedRoute4.equals(ChMap.routes().get(17))) {
            for (int i = 0; i < 4; i++) {
                claimPlayerState4 = claimPlayerState4.withAddedCard(Card.GREEN);
            }
            player.next("Ce n'est pas la bonne route. Veuillez recommencer : emparez vous de la route Lausanne - Neuchâtel.");
            claimedRoute4 = player.claimedRoute();
            initialClaimCards4 = player.initialClaimCards();
            player.updateState(currentGameState, claimPlayerState4);
        }
        currentGameState = currentGameState.withClaimedRoute(claimedRoute4, initialClaimCards4);
        player.updateState(currentGameState, currentGameState.playerState(id));
        player.receiveInfo(info.claimedRoute(claimedRoute4, initialClaimCards4));
    }

    private static void drawTickets(Player player, GameState gameState, Info info) {
        player.receiveInfo(info.drewTickets(Constants.IN_GAME_TICKETS_COUNT));
        SortedBag<Ticket> drawnTickets = gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
        SortedBag<Ticket> keptTickets = player.chooseTickets(drawnTickets);
        player.receiveInfo(info.keptTickets(keptTickets.size()));
        gameState = gameState.withChosenAdditionalTickets(drawnTickets, keptTickets);
    }



    private static void needsToPlay(String text) {
        String command = text.substring(DO_LENGTH);

    }

    private static void updateStates(Map<PlayerId, Player> players, GameState currentGameState){
        for(PlayerId playerId : ALL){
            players.get(playerId).updateState(currentGameState, currentGameState.playerState(playerId));
        }
    }
}
