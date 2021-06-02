package ch.epfl.tchu.tutorial;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.Info;
import ch.epfl.tchu.gui.TutorialGraphicalPlayerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public final class Tutorial {

    //In order to make the class non instantiable
    private Tutorial(){
        throw new UnsupportedOperationException();}

    private static final String failMessage = "Ce n'est pas la bonne route. Veuillez recommencer : emparez-vous de la route %s.";

    private static TutorialGraphicalPlayerAdapter player;
    private static GameState gameState;
    private static PlayerId id;
    private static PlayerState playerState;
    private static TutorialText tutorialText;
    private static Info info;

    public static void play(String playerName) {

        player = new TutorialGraphicalPlayerAdapter();
        tutorialText = new TutorialText();
        info = new Info(playerName);
        gameState = GameState.initial(SortedBag.of(ChMap.tickets()), new Random(2302));
        id = gameState.currentPlayerId();
        playerState = playerState();

        Map<PlayerId, String> playerNames = Map.of(PLAYER_1, playerName, PLAYER_2, "Ordinateur");

        player.initPlayers(id, playerNames);

        goesForNext(); //1
        waitsForNext(3); //4
        player.receiveInfo("Comme ceci !\n");
        waitsForNext(4); //8

        player.setInitialTicketChoice(gameState.topTickets(Constants.INITIAL_TICKETS_COUNT));
        gameState = gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
        player.updateState(gameState, playerState());
        SortedBag<Ticket> chosenTickets = player.chooseInitialTickets();
        gameState = gameState.withInitiallyChosenTickets(id, chosenTickets);
        player.receiveInfo(info.keptTickets(chosenTickets.size()));
        player.updateState(gameState, playerState());

        goesForNext(); //9
        waitsForNext(1); //10
        player.receiveInfo(info.willPlayFirst());
        waitsForNext(2); //12

        player.updateState(gameState, playerState());
        player.receiveInfo(info.canPlay());

        drawACard();
        player.updateState(gameState, playerState());

        goesForNext(); //13
        waitsForNext(1); //14

        drawACard();
        player.updateState(gameState, playerState());

        goesForNext(); //15
        waitsForNext(2); //17

        player.receiveInfo(info.drewTickets(Constants.IN_GAME_TICKETS_COUNT));
        SortedBag<Ticket> drawnTickets = gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
        SortedBag<Ticket> keptTickets = player.chooseTickets(drawnTickets);
        player.receiveInfo(info.keptTickets(keptTickets.size()));
        gameState = gameState.withChosenAdditionalTickets(drawnTickets, keptTickets);
        player.updateState(gameState, playerState());

        goesForNext(); //18

        //claim route example and empties also the player's deck
        gameState = gameState.withClaimedRoute(ChMap.routes().get(43), SortedBag.of(playerState().cards()));
        player.updateState(gameState, playerState());

        waitsForNext(3); //21

        withAddedCards(Card.VIOLET, 4, false);
        waitsForRouteClaim(Card.VIOLET, 4, "Interlaken - Lucerne", false, ChMap.routes().get(49));

        goesForNext(); //22
        waitsForNext(3); //25

        withAddedCards(Card.BLUE, 3, false);
        waitsForRouteClaim(Card.BLUE, 3, "Berne - Interlaken",
                false, ChMap.routes().get(15));

        goesForNext(); //26
        waitsForNext(1); //27

        withAddedCards(Card.YELLOW, 4, false);
        waitsForRouteClaim(Card.YELLOW, 4, "Berne - Lucerne",
                false, ChMap.routes().get(16), ChMap.routes().get(17));

        goesForNext(); //28

        withAddedCards(Card.GREEN, 4, false);
        waitsForRouteClaim(Card.GREEN, 4, "Lausanne - Neuchâtel",
                false, ChMap.routes().get(56));

        goesForNext(); //29
        waitsForNext(2); //31

        withAddedCards(Card.BLACK, 3, true);
        waitsForRouteClaim(Card.BLACK, 3, "Wassen - Bellizone",
                true, ChMap.routes().get(11), ChMap.routes().get(12));

        goesForNext(); //32

        withAddedCards(Card.LOCOMOTIVE, 6, false);
        waitsForRouteClaim(Card.LOCOMOTIVE, 6, "Brigue - Locarno",
                false, ChMap.routes().get(22));

        goesForNext(); //33
        waitsForNext(2); //35

        withAddedCards(Card.YELLOW, 2, false);
        waitsForTunnelClaim(Card.YELLOW, 2, "Schwiz - Wassen", ChMap.routes().get(78),
                List.of(Card.YELLOW, Card.YELLOW, Card.BLACK), List.of(SortedBag.of()), false, false);

        goesForNext(); //36
        waitsForNext(1); //37

        withAddedCards(Card.YELLOW, 4, false);
        waitsForTunnelClaim(Card.YELLOW, 4, "Schwiz - Wassen", ChMap.routes().get(78),
                List.of(Card.YELLOW, Card.YELLOW, Card.BLACK), List.of(SortedBag.of(2, Card.YELLOW)),
                true, false);

        goesForNext(); //38

        withAddedCards(Card.WHITE, 5, false); //39
        waitsForTunnelClaim(Card.WHITE, 5, "Wassen - Coire", ChMap.routes().get(30),
                List.of(Card.RED, Card.LOCOMOTIVE, Card.WHITE),
                List.of(SortedBag.of(2, Card.WHITE), SortedBag.of(1, Card.LOCOMOTIVE, 1, Card.WHITE)),
                false, true);

        goesForNext(); //40
        waitsForNext(2); //42

        withAddedCards(Card.ORANGE, 6, false);
        waitsForRouteClaim(Card.ORANGE, 6, "Genève - Yverdon",
                false, ChMap.routes().get(48));

        withAddedCards(Card.RED, 3, false);
        waitsForRouteClaim(Card.RED, 3, "Lausanne - Fribourg",
                false, ChMap.routes().get(44));

        goesForNext(); //43
        waitsForNext(7); //50
        player.waitsForLeave();

    }

    private static void waitsForTunnelClaim(Card card, int cardCount, String routeName, Route routeToClaim, List<Card> drawnAdditionalCards, List<SortedBag<Card>> playedAdditionalCards, boolean claimsWithCards, boolean claimsWithNoCards) {
        Route route = player.claimedRoute();
        SortedBag<Card> initialClaimCards = player.initialClaimCards();
        playerState = playerState();

        while (!route.equals(routeToClaim)) {
            withAddedCards(card, cardCount, false);
            player.next(String.format(failMessage, routeName));
            route = player.claimedRoute();
            initialClaimCards = player.initialClaimCards();
            player.updateState(gameState, playerState);
        }
        player.receiveInfo(info.attemptsTunnelClaim(route, initialClaimCards));

        SortedBag<Card> drawnCards = SortedBag.of(new ArrayList<>(drawnAdditionalCards));
        int additionalCards = route.additionalClaimCardsCount(initialClaimCards, drawnCards);
        player.receiveInfo(info.drewAdditionalCards(drawnCards, additionalCards));

        if (claimsWithCards) {

            SortedBag<Card> additionalChosenCards = player.chooseAdditionalCards(playedAdditionalCards);
            while (additionalChosenCards.isEmpty()) {
                player.next(String.format("Vous n'avez pas choisi les %s cartes supplémentaires. Réessayez !", additionalCards));
                additionalChosenCards = player.chooseAdditionalCards(playedAdditionalCards);
            }
            SortedBag<Card> claimCards = initialClaimCards.union(additionalChosenCards);
            gameState = gameState.withClaimedRoute(route, claimCards);
            player.receiveInfo(info.claimedRoute(route, claimCards));

        } else if (claimsWithNoCards ) {

            SortedBag<Card> additionalChosenCards = player.chooseAdditionalCards(playedAdditionalCards);
            player.next(tutorialText.nextLine()); //37
            while (!additionalChosenCards.isEmpty()) {
                player.next("Vous ne devez sélectionner aucun choix. Réessayez !");
                additionalChosenCards = player.chooseAdditionalCards(playedAdditionalCards);
            }
            player.receiveInfo(info.didNotClaimRoute(route));

        } else {
            player.receiveInfo(info.didNotClaimRoute(route));
        }

    }

    private static void waitsForRouteClaim(Card card, int cardCount, String routeName, boolean withALocomotive, Route... routeToClaim) {
        Route route = player.claimedRoute();
        SortedBag<Card> initialClaimCards = player.initialClaimCards();
        playerState = playerState();
        while (isNotTheWantedRoute(route, routeToClaim)) {
            withAddedCards(card, cardCount, withALocomotive);
            player.next(String.format(failMessage, routeName));
            route = player.claimedRoute();
            initialClaimCards = player.initialClaimCards();
            player.updateState(gameState, playerState);
        }
        gameState = gameState.withClaimedRoute(route, initialClaimCards);
        player.updateState(gameState, playerState());
        player.receiveInfo(info.claimedRoute(route, initialClaimCards));
    }

    private static boolean isNotTheWantedRoute(Route route, Route... routeToClaim) {
        for (Route r : routeToClaim) {
            if (route.equals(r)) {
                return false;
            }
        }
        return true;
    }

    private static void withAddedCards(Card card, int cardCount, boolean withALocomotive) {
        playerState = playerState();
        for (int i = 0; i < cardCount; i++) {
            playerState = playerState.withAddedCard(card);
            player.updateState(gameState, playerState);
        }
        if (withALocomotive) {
            playerState = playerState.withAddedCard(Card.LOCOMOTIVE);
        }
        player.updateState(gameState, playerState);
    }

    private static PlayerState playerState() {
        playerState = gameState.playerState(id);
        return playerState;
    }

    private static void waitsForNext(int nextCount) {
        for (int i = 0; i < nextCount; i++) {
            player.waitsForNext(tutorialText.nextLine());
        }
    }

    private static void goesForNext() {
            player.next(tutorialText.nextLine());
    }

    private static void drawACard() {
        int slot = player.drawSlot();
        if (0 <= slot && slot < Constants.FACE_UP_CARDS_COUNT) {

            player.receiveInfo(info.drewVisibleCard(gameState.cardState().faceUpCard(slot)));
            gameState = gameState.withDrawnFaceUpCard(slot);

        } else if (slot == Constants.DECK_SLOT) {

            player.receiveInfo(info.drewBlindCard());
            gameState = gameState.withBlindlyDrawnCard();
        }
    }


//    public static void play(String playerName) {
//
//        TutorialGraphicalPlayerAdapter player = new TutorialGraphicalPlayerAdapter();
//
//        TutorialText tutorialText = new TutorialText();
//
//        Info info = new Info(playerName);
//
//        GameState currentGameState = GameState.initial(SortedBag.of(ChMap.tickets()), new Random(2302));
//        PlayerId id = currentGameState.currentPlayerId();
//
//        Map<PlayerId, String> playerNames = Map.of(PLAYER_1, playerName, PLAYER_2, "Ordinateur");
//
//        player.initPlayers(id, playerNames);
//
//        player.next("Pour commencer le tutoriel, cliquer sur le bouton continuer ci-dessous. " +
//                "Vous pouvez à tout moment quitter le tutoriel en cliquant sur l'autre bouton.");
//
//        player.waitsForNext(tutorialText.nextLine());
//        player.waitsForNext(tutorialText.nextLine());
//        player.waitsForNext(tutorialText.nextLine());
//        player.receiveInfo("Comme ceci !\n");
//        player.waitsForNext(tutorialText.nextLine());
//        player.waitsForNext(tutorialText.nextLine());
//        player.waitsForNext(tutorialText.nextLine());
//        player.waitsForNext(tutorialText.nextLine());
//
//        player.setInitialTicketChoice(currentGameState.topTickets(Constants.INITIAL_TICKETS_COUNT));
//        currentGameState = currentGameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
//
//        player.updateState(currentGameState, currentplayerState());
//        SortedBag<Ticket> chosenTickets = player.chooseInitialTickets();
//        currentGameState = currentGameState.withInitiallyChosenTickets(id, chosenTickets);
//        player.receiveInfo(info.keptTickets(chosenTickets.size()));
//
//        player.updateState(currentGameState, currentplayerState());
//
//        player.next(tutorialText.nextLine());
//
//
//        player.waitsForNext(tutorialText.nextLine());
//        player.receiveInfo(info.willPlayFirst());
//        player.waitsForNext(tutorialText.nextLine()); //10
//        player.waitsForNext(tutorialText.nextLine());
//
//        player.updateState(currentGameState, currentplayerState());
//
//        player.receiveInfo(info.canPlay());
//
//        currentGameState = currentGameState.withCardsDeckRecreatedIfNeeded(new Random());
//        int slot = player.drawSlot();
//        if (0 <= slot && slot < Constants.FACE_UP_CARDS_COUNT) {
//
//            player.receiveInfo(info.drewVisibleCard(currentGameState.cardState().faceUpCard(slot)));
//            currentGameState = currentGameState.withDrawnFaceUpCard(slot);
//
//        } else if (slot == Constants.DECK_SLOT) {
//
//            player.receiveInfo(info.drewBlindCard());
//            currentGameState = currentGameState.withBlindlyDrawnCard();
//        }
//
//        currentGameState = currentGameState.withCardsDeckRecreatedIfNeeded(new Random());
//        player.updateState(currentGameState, currentplayerState());
//
//        player.next(tutorialText.nextLine()); //14
//        player.waitsForNext(tutorialText.nextLine()); //13
//
//        int slot1 = player.drawSlot();
//        if (0 <= slot1 && slot1 < Constants.FACE_UP_CARDS_COUNT) {
//
//            player.receiveInfo(info.drewVisibleCard(currentGameState.cardState().faceUpCard(slot1)));
//            currentGameState = currentGameState.withDrawnFaceUpCard(slot1);
//
//        } else if (slot1 == Constants.DECK_SLOT) {
//
//            player.receiveInfo(info.drewBlindCard());
//            currentGameState = currentGameState.withBlindlyDrawnCard();
//        }
//        player.updateState(currentGameState, currentplayerState());
//
//        player.next(tutorialText.nextLine()); //14
//        player.waitsForNext(tutorialText.nextLine()); //15
//        player.waitsForNext(tutorialText.nextLine()); //16
//
//        player.receiveInfo(info.drewTickets(Constants.IN_GAME_TICKETS_COUNT));
//        SortedBag<Ticket> drawnTickets = currentGameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
//        SortedBag<Ticket> keptTickets = player.chooseTickets(drawnTickets);
//        player.receiveInfo(info.keptTickets(keptTickets.size()));
//        currentGameState = currentGameState.withChosenAdditionalTickets(drawnTickets, keptTickets);
//        player.updateState(currentGameState, currentplayerState());
//
//
//        player.next(tutorialText.nextLine()); //17
//        player.waitsForNext(tutorialText.nextLine()); //18
//        player.waitsForNext(tutorialText.nextLine()); //19
//
//
//        currentGameState = currentGameState.withClaimedRoute(ChMap.routes().get(43), SortedBag.of(currentplayerState().cards()));
//
//
//        PlayerState stateWithAddedVioletCards = currentplayerState();
//        for (int i = 0; i < 4; i++) {
//            stateWithAddedVioletCards = stateWithAddedVioletCards.withAddedCard(Card.VIOLET);
//            player.updateState(currentGameState, stateWithAddedVioletCards);
//        }
//        player.updateState(currentGameState, stateWithAddedVioletCards);
//
//
//
//        Route claimedRoute1 = player.claimedRoute();
//        SortedBag<Card> initialClaimCards1 = player.initialClaimCards();
//        PlayerState claimPlayerState1 = currentplayerState();
//        while (!claimedRoute1.equals(ChMap.routes().get(49))) {
//            for (int i = 0; i < 4; i++) {
//                claimPlayerState1 = claimPlayerState1.withAddedCard(Card.VIOLET);
//            }
//            player.next("Ce n'est pas la bonne route. Veuillez recommencer : emparez vous de la route Interlaken - Lucerne.");
//            claimedRoute1 = player.claimedRoute();
//            initialClaimCards1 = player.initialClaimCards();
//            player.updateState(currentGameState, claimPlayerState1);
//        }
//        currentGameState = currentGameState.withClaimedRoute(claimedRoute1, initialClaimCards1);
//        player.updateState(currentGameState, currentplayerState());
//        player.receiveInfo(info.claimedRoute(claimedRoute1, initialClaimCards1));
//
//        player.next(tutorialText.nextLine());//20
//        player.waitsForNext(tutorialText.nextLine());//21
//        player.waitsForNext(tutorialText.nextLine());//22
//        player.waitsForNext(tutorialText.nextLine());//23
//
//        PlayerState stateWithAddedBlueCards = currentplayerState();
//        for (int i = 0; i < 3; i++) {
//            stateWithAddedBlueCards = stateWithAddedBlueCards.withAddedCard(Card.BLUE);
//        }
//        player.updateState(currentGameState, stateWithAddedBlueCards);
//
//        Route claimedRoute2 = player.claimedRoute();
//        SortedBag<Card> initialClaimCards2 = player.initialClaimCards();
//        PlayerState claimPlayerState2 = currentplayerState();
//        while (!claimedRoute2.equals(ChMap.routes().get(15))) {
//            for (int i = 0; i < 3; i++) {
//                claimPlayerState2 = claimPlayerState2.withAddedCard(Card.BLUE);
//            }
//            player.next("Ce n'est pas la bonne route. Veuillez recommencer : emparez vous de la route Berne - Interlaken.");
//            claimedRoute2 = player.claimedRoute();
//            initialClaimCards2 = player.initialClaimCards();
//            player.updateState(currentGameState, claimPlayerState2);
//        }
//        currentGameState = currentGameState.withClaimedRoute(claimedRoute2, initialClaimCards2);
//        player.updateState(currentGameState, currentplayerState());
//        player.receiveInfo(info.claimedRoute(claimedRoute2, initialClaimCards2));
//
//        player.next(tutorialText.nextLine()); //24
//        player.waitsForNext(tutorialText.nextLine()); //25
//
//        PlayerState stateWithAddedYellowCards = currentplayerState();
//        for (int i = 0; i < 4; i++) {
//            stateWithAddedYellowCards = stateWithAddedYellowCards.withAddedCard(Card.YELLOW);
//        }
//        player.updateState(currentGameState, stateWithAddedYellowCards);
//
//        Route claimedRoute3 = player.claimedRoute();
//        SortedBag<Card> initialClaimCards3 = player.initialClaimCards();
//        PlayerState claimPlayerState3 = currentplayerState();
//        while (!claimedRoute3.equals(ChMap.routes().get(16)) && !claimedRoute3.equals(ChMap.routes().get(17))) {
//            for (int i = 0; i < 4; i++) {
//                claimPlayerState3 = claimPlayerState3.withAddedCard(Card.YELLOW);
//            }
//            player.next("Ce n'est pas la bonne route. Veuillez recommencer : emparez vous de la route Berne - Lucerne.");
//            claimedRoute3 = player.claimedRoute();
//            initialClaimCards3 = player.initialClaimCards();
//            player.updateState(currentGameState, claimPlayerState3);
//        }
//        currentGameState = currentGameState.withClaimedRoute(claimedRoute3, initialClaimCards3);
//        player.updateState(currentGameState, currentplayerState());
//        player.receiveInfo(info.claimedRoute(claimedRoute3, initialClaimCards3));
//
//        player.next(tutorialText.nextLine()); //26
//
//        PlayerState stateWithAddedGreenCards = currentplayerState();
//        for (int i = 0; i < 4; i++) {
//            stateWithAddedGreenCards = stateWithAddedGreenCards.withAddedCard(Card.GREEN);
//        }
//        player.updateState(currentGameState, stateWithAddedGreenCards);
//
//        Route claimedRoute4 = player.claimedRoute();
//        SortedBag<Card> initialClaimCards4 = player.initialClaimCards();
//        PlayerState claimPlayerState4 = currentplayerState();
//        while (!claimedRoute4.equals(ChMap.routes().get(56))) {
//            for (int i = 0; i < 4; i++) {
//                claimPlayerState4 = claimPlayerState4.withAddedCard(Card.GREEN);
//            }
//            player.next("Ce n'est pas la bonne route. Veuillez recommencer : emparez vous de la route Lausanne - Neuchâtel.");
//            claimedRoute4 = player.claimedRoute();
//            initialClaimCards4 = player.initialClaimCards();
//            player.updateState(currentGameState, claimPlayerState4);
//        }
//        currentGameState = currentGameState.withClaimedRoute(claimedRoute4, initialClaimCards4);
//        player.updateState(currentGameState, currentplayerState());
//        player.receiveInfo(info.claimedRoute(claimedRoute4, initialClaimCards4));
//
//        player.next(tutorialText.nextLine()); //27
//        player.waitsForNext(tutorialText.nextLine()); //28
//        player.waitsForNext(tutorialText.nextLine()); //29
//
//        PlayerState stateWithAddedBlackAndLocoCards = currentplayerState();
//        for (int i = 0; i < 3; i++) {
//            stateWithAddedBlackAndLocoCards = stateWithAddedBlackAndLocoCards.withAddedCard(Card.BLACK);
//        }
//        stateWithAddedBlackAndLocoCards = stateWithAddedBlackAndLocoCards.withAddedCard(Card.LOCOMOTIVE);
//        player.updateState(currentGameState, stateWithAddedBlackAndLocoCards);
//
//        Route claimedRoute5 = player.claimedRoute();
//        SortedBag<Card> initialClaimCards5 = player.initialClaimCards();
//        PlayerState claimPlayerState5 = currentplayerState();
//        while (!claimedRoute5.equals(ChMap.routes().get(11)) && !claimedRoute5.equals(ChMap.routes().get(12))) {
//            for (int i = 0; i < 3; i++) {
//                claimPlayerState5 = claimPlayerState5.withAddedCard(Card.BLACK);
//            }
//            claimPlayerState5 = claimPlayerState5.withAddedCard(Card.LOCOMOTIVE);
//            player.next("Ce n'est pas la bonne route. Veuillez recommencer : emparez vous de la route Wassen - Bellizone.");
//            claimedRoute5 = player.claimedRoute();
//            initialClaimCards5 = player.initialClaimCards();
//            player.updateState(currentGameState, claimPlayerState5);
//        }
//        currentGameState = currentGameState.withClaimedRoute(claimedRoute5, initialClaimCards5);
//        player.updateState(currentGameState, currentplayerState());
//        player.receiveInfo(info.claimedRoute(claimedRoute5, initialClaimCards5));
//
//        player.next(tutorialText.nextLine()); //30
//
//
//        PlayerState stateWithAddedLocoCards = currentplayerState();
//        for (int i = 0; i < 6; i++) {
//            stateWithAddedLocoCards = stateWithAddedLocoCards.withAddedCard(Card.LOCOMOTIVE);
//        }
//        player.updateState(currentGameState, stateWithAddedLocoCards);
//
//        Route claimedRoute6 = player.claimedRoute();
//        SortedBag<Card> initialClaimCards6 = player.initialClaimCards();
//        PlayerState claimPlayerState6 = currentplayerState();
//        while (!claimedRoute6.equals(ChMap.routes().get(22))) {
//            for (int i = 0; i < 6; i++) {
//                claimPlayerState6 = claimPlayerState6.withAddedCard(Card.LOCOMOTIVE);
//            }
//            player.next("Ce n'est pas la bonne route. Veuillez recommencer : emparez vous de la route Brigue - Locarno.");
//            claimedRoute6 = player.claimedRoute();
//            initialClaimCards6 = player.initialClaimCards();
//            player.updateState(currentGameState, claimPlayerState6);
//        }
//        currentGameState = currentGameState.withClaimedRoute(claimedRoute6, initialClaimCards6);
//        player.updateState(currentGameState, currentplayerState());
//        player.receiveInfo(info.claimedRoute(claimedRoute6, initialClaimCards6));
//
//        player.next(tutorialText.nextLine()); //31
//        player.waitsForNext(tutorialText.nextLine()); //32
//        player.waitsForNext(tutorialText.nextLine()); //33
//
//        PlayerState stateWithAddedTwoYellowCards = currentplayerState();
//        for (int i = 0; i < 2; i++) {
//            stateWithAddedTwoYellowCards = stateWithAddedTwoYellowCards.withAddedCard(Card.YELLOW);
//        }
//        player.updateState(currentGameState, stateWithAddedTwoYellowCards);
//
//
//        //========================================================================================
//
//        Route claimedRoute7 = player.claimedRoute();
//        SortedBag<Card> initialClaimCards7 = player.initialClaimCards();
//        PlayerState claimPlayerState7 = currentplayerState();
//        while (!claimedRoute7.equals(ChMap.routes().get(78))) {
//            for (int i = 0; i < 2; i++) {
//                claimPlayerState7 = claimPlayerState7.withAddedCard(Card.YELLOW);
//            }
//            player.next("Ce n'est pas la bonne route. Veuillez recommencer : emparez vous de la route Brigue - Locarno.");
//            claimedRoute7 = player.claimedRoute();
//            initialClaimCards7 = player.initialClaimCards();
//            player.updateState(currentGameState, claimPlayerState7);
//        }
////        currentGameState = currentGameState.withClaimedRoute(claimedRoute7, initialClaimCards7);
////        player.updateState(currentGameState, currentplayerState());
//
//        player.receiveInfo(info.attemptsTunnelClaim(claimedRoute7, initialClaimCards7));
//
//        SortedBag<Card> drawnCards = SortedBag.of(new ArrayList<>(List.of(Card.YELLOW, Card.YELLOW, Card.BLACK)));
//        int additionalCards = claimedRoute7.additionalClaimCardsCount(initialClaimCards7, drawnCards);
//        player.receiveInfo(info.drewAdditionalCards(drawnCards, additionalCards));
//
//
//        player.receiveInfo(info.didNotClaimRoute(claimedRoute7));
//
//
//        //========================================================================================
//
//        player.next(tutorialText.nextLine()); //34
//        player.waitsForNext(tutorialText.nextLine()); //35
//
//
//        PlayerState stateWithAddedTwoMoreYellowCards = currentplayerState();
//        for (int i = 0; i < 4; i++) {
//            stateWithAddedTwoMoreYellowCards = stateWithAddedTwoMoreYellowCards.withAddedCard(Card.YELLOW);
//        }
//        player.updateState(currentGameState, stateWithAddedTwoMoreYellowCards);
//
//
//
//        //==========================================================
//        Route claimedRoute8 = player.claimedRoute();
//        SortedBag<Card> initialClaimCards8 = player.initialClaimCards();
//        PlayerState claimPlayerState8 = currentplayerState();
//        while (!claimedRoute8.equals(ChMap.routes().get(78))) {
//            for (int i = 0; i < 2; i++) {
//                claimPlayerState8 = claimPlayerState8.withAddedCard(Card.YELLOW);
//            }
//            player.next("Ce n'est pas la bonne route. Veuillez recommencer : emparez vous de la route Brigue - Locarno.");
//            claimedRoute8 = player.claimedRoute();
//            initialClaimCards8 = player.initialClaimCards();
//            player.updateState(currentGameState, claimPlayerState8);
//        }
//
//        player.receiveInfo(info.attemptsTunnelClaim(claimedRoute8, initialClaimCards8));
//
//        SortedBag<Card> drawnCards1 = SortedBag.of(new ArrayList<>(List.of(Card.YELLOW, Card.YELLOW, Card.BLACK)));
//        int additionalCards1 = claimedRoute8.additionalClaimCardsCount(initialClaimCards8, drawnCards1);
//        player.receiveInfo(info.drewAdditionalCards(drawnCards1, additionalCards1));
//        player.updateState(currentGameState, currentplayerState());
//
//        SortedBag<Card> additionalChosenCards = player.chooseAdditionalCards(List.of(SortedBag.of(2, Card.YELLOW)));
//        while (additionalChosenCards.isEmpty()) {
//            player.next("Vous n'avez pas choisi les 2 cartes jaunes supplémentaires. Réessayez !");
//            additionalChosenCards = player.chooseAdditionalCards(List.of(SortedBag.of(2, Card.YELLOW)));
//        }
//
//        SortedBag<Card> claimCards = initialClaimCards8.union(additionalChosenCards);
//        currentGameState = currentGameState.withClaimedRoute(claimedRoute8, claimCards);
//        player.receiveInfo(info.claimedRoute(claimedRoute8, claimCards));
//
//        player.next(tutorialText.nextLine()); //36
//
//        PlayerState stateWithAddedWhiteCards = currentplayerState();
//        for (int i = 0; i < 5; i++) {
//            stateWithAddedWhiteCards = stateWithAddedWhiteCards.withAddedCard(Card.WHITE);
//        }
//        player.updateState(currentGameState, stateWithAddedWhiteCards);
//
//
//
//
//        //==========================================================
//        Route claimedRoute9 = player.claimedRoute();
//        SortedBag<Card> initialClaimCards9 = player.initialClaimCards();
//        PlayerState claimPlayerState9 = currentplayerState();
//        while (!claimedRoute9.equals(ChMap.routes().get(78))) {
//            for (int i = 0; i < 2; i++) {
//                claimPlayerState9 = claimPlayerState9.withAddedCard(Card.YELLOW);
//            }
//            player.next("Ce n'est pas la bonne route. Veuillez recommencer : emparez vous de la route Brigue - Locarno.");
//            claimedRoute9 = player.claimedRoute();
//            initialClaimCards9 = player.initialClaimCards();
//            player.updateState(currentGameState, claimPlayerState9);
//        }
//
//
//        player.receiveInfo(info.attemptsTunnelClaim(claimedRoute9, initialClaimCards9));
//
//        SortedBag<Card> drawnCards2 = SortedBag.of(new ArrayList<>(List.of(Card.RED, Card.LOCOMOTIVE, Card.WHITE)));
//        int additionalCards2 = claimedRoute9.additionalClaimCardsCount(initialClaimCards9, drawnCards2);
//        player.receiveInfo(info.drewAdditionalCards(drawnCards2, additionalCards2));
//        player.updateState(currentGameState, currentplayerState());
//
//        player.next(tutorialText.nextLine()); //37
//
//        SortedBag<Card> additionalChosenCards1 = player.chooseAdditionalCards(List.of(
//                SortedBag.of(2, Card.WHITE),
//                SortedBag.of(1, Card.LOCOMOTIVE, 1, Card.WHITE)));
//        while (!additionalChosenCards1.isEmpty()) {
//            player.next("Vous ne devez sélectionner aucun choix. Réessayez !");
//            additionalChosenCards1 = player.chooseAdditionalCards(List.of(
//                    SortedBag.of(2, Card.WHITE),
//                    SortedBag.of(1, Card.LOCOMOTIVE, 1, Card.WHITE)));
//        }
//
//        player.receiveInfo(info.didNotClaimRoute(claimedRoute9));
//
//        player.next(tutorialText.nextLine()); //38
//        player.waitsForNext(tutorialText.nextLine()); //39
//        player.waitsForNext(tutorialText.nextLine()); //40
//
//        PlayerState stateWithAddedOrangeCards = currentplayerState();
//        for (int i = 0; i < 6; i++) {
//            stateWithAddedOrangeCards = stateWithAddedOrangeCards.withAddedCard(Card.ORANGE);
//        }
//        player.updateState(currentGameState, stateWithAddedOrangeCards);
//
//        Route claimedRoute10 = player.claimedRoute();
//        SortedBag<Card> initialClaimCards10 = player.initialClaimCards();
//        PlayerState claimPlayerState10 = currentplayerState();
//        while (!claimedRoute10.equals(ChMap.routes().get(48))) {
//            claimPlayerState10 = claimPlayerState10.withAddedCard(Card.ORANGE);
//            player.next("Ce n'est pas la bonne route. Veuillez recommencer : emparez-vous de la route Genève - Yverdon.");
//            claimedRoute10 = player.claimedRoute();
//            initialClaimCards10 = player.initialClaimCards();
//            player.updateState(currentGameState, claimPlayerState10);
//        }
//        currentGameState = currentGameState.withClaimedRoute(claimedRoute10, initialClaimCards10);
//        player.updateState(currentGameState, currentplayerState());
//        player.receiveInfo(info.claimedRoute(claimedRoute10, initialClaimCards10));
//
//        PlayerState stateWithAddedThreeRedCards = currentplayerState();
//        for (int i = 0; i < 3; i++) {
//            stateWithAddedThreeRedCards = stateWithAddedThreeRedCards.withAddedCard(Card.RED);
//        }
//        player.updateState(currentGameState, stateWithAddedThreeRedCards);
//
//        Route claimedRoute11 = player.claimedRoute();
//        SortedBag<Card> initialClaimCards11 = player.initialClaimCards();
//        PlayerState claimPlayerState11 = currentplayerState();
//        while (!claimedRoute11.equals(ChMap.routes().get(44))) {
//            claimPlayerState11 = claimPlayerState11.withAddedCard(Card.RED);
//            player.next("Ce n'est pas la bonne route. Veuillez recommencer : emparez-vous de la route rouge Lausanne - Friboug.");
//            claimedRoute11 = player.claimedRoute();
//            initialClaimCards11 = player.initialClaimCards();
//            player.updateState(currentGameState, claimPlayerState11);
//        }
//        currentGameState = currentGameState.withClaimedRoute(claimedRoute11, initialClaimCards11);
//        player.updateState(currentGameState, currentplayerState());
//        player.receiveInfo(info.claimedRoute(claimedRoute11, initialClaimCards11));
//
//        player.next(tutorialText.nextLine()); //41
//        player.waitsForNext(tutorialText.nextLine()); //42
//        player.next(tutorialText.nextLine()); //43
//        player.next(tutorialText.nextLine()); //44
//        player.next(tutorialText.nextLine()); //45
//        player.next(tutorialText.nextLine()); //46
//
//
//        player.waitsForLeave();
//
//
//
//        System.out.println("has reached the end");
//
//    }


}
