package ch.epfl.tchu.tutorial;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.Info;
import ch.epfl.tchu.gui.TutorialGraphicalPlayerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static ch.epfl.tchu.game.Card.*;
import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public final class Tutorial {

    //In order to make the class non instantiable
    private Tutorial(){
        throw new UnsupportedOperationException();}

    private static final String failMessage =
            "Ce n'est pas la bonne route. Veuillez recommencer : emparez-vous de la route %s.";

    private static TutorialGraphicalPlayerAdapter player;
    private static GameState gameState;
    private static PlayerId id;
    private static PlayerState playerState;
    private static TutorialText tutorialText;
    private static Info info;

    //Runs a tutorial
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

        withAddedCards(VIOLET, 4, false);
        waitsForRouteClaim(VIOLET, 4, "Interlaken - Lucerne", false, ChMap.routes().get(49));

        goesForNext(); //22
        waitsForNext(3); //25

        withAddedCards(BLUE, 3, false);
        waitsForRouteClaim(BLUE, 3, "Berne - Interlaken",
                false, ChMap.routes().get(15));

        goesForNext(); //26
        waitsForNext(1); //27

        withAddedCards(YELLOW, 4, false);
        waitsForRouteClaim(YELLOW, 4, "Berne - Lucerne",
                false, ChMap.routes().get(16), ChMap.routes().get(17));

        goesForNext(); //28

        withAddedCards(GREEN, 4, false);
        waitsForRouteClaim(GREEN, 4, "Lausanne - Neuchâtel",
                false, ChMap.routes().get(56));

        goesForNext(); //29
        waitsForNext(2); //31

        withAddedCards(BLACK, 3, true);
        waitsForRouteClaim(BLACK, 3, "Wassen - Bellizone",
                true, ChMap.routes().get(11), ChMap.routes().get(12));

        goesForNext(); //32

        withAddedCards(LOCOMOTIVE, 6, false);
        waitsForRouteClaim(LOCOMOTIVE, 6, "Brigue - Locarno",
                false, ChMap.routes().get(22));

        goesForNext(); //33
        waitsForNext(2); //35

        withAddedCards(YELLOW, 2, false);
        waitsForTunnelClaim(YELLOW, 2, "Schwiz - Wassen", ChMap.routes().get(78),
                List.of(YELLOW, YELLOW, BLACK), List.of(SortedBag.of()), false, false);

        goesForNext(); //36
        waitsForNext(1); //37

        withAddedCards(YELLOW, 4, false);
        waitsForTunnelClaim(YELLOW, 4, "Schwiz - Wassen", ChMap.routes().get(78),
                List.of(YELLOW, YELLOW, BLACK), List.of(SortedBag.of(2, YELLOW)),
                true, false);

        goesForNext(); //38

        withAddedCards(WHITE, 5, false); //39
        waitsForTunnelClaim(WHITE, 5, "Wassen - Coire", ChMap.routes().get(30),
                List.of(RED, LOCOMOTIVE, WHITE),
                List.of(SortedBag.of(2, WHITE), SortedBag.of(1, LOCOMOTIVE, 1, WHITE)),
                false, true);

        goesForNext(); //40
        waitsForNext(2); //42

        withAddedCards(ORANGE, 6, false);
        waitsForRouteClaim(ORANGE, 6, "Genève - Yverdon",
                false, ChMap.routes().get(48));

        withAddedCards(RED, 3, false);
        waitsForRouteClaim(RED, 3, "Lausanne - Fribourg",
                false, ChMap.routes().get(44));

        goesForNext(); //43
        waitsForNext(7); //50
        player.waitsForLeave();

    }


    /**
     * Blocks JavaFX thread and waits for the player to attempt to claim a given tunnel route
     * @param card claim card
     * @param cardCount claim card count
     * @param routeName of the considered route
     * @param routeToClaim considered route
     * @param drawnAdditionalCards for potential additional cards
     * @param playedAdditionalCards to be played by the player
     * @param claimsWithCards if the player should play additional cards
     * @param claimsWithNoCards if the player should choose to not play additional cards
     */
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

    /**
     * Blocks JavaFX thread and waits for the player to attempt to claim a given route
     * @param card claim card
     * @param cardCount claim card count
     * @param routeName of the route to claim
     * @param withALocomotive if the claim cards should contain a Locomotive card
     * @param routeToClaim considered
     */
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

    /**
     * Places the given cards in the player's hand
     * @param card to add to the player's hand
     * @param cardCount card count
     * @param withALocomotive if a locomotive should be added to the player's hand
     */
    private static void withAddedCards(Card card, int cardCount, boolean withALocomotive) {
        playerState = playerState();
        for (int i = 0; i < cardCount; i++) {
            playerState = playerState.withAddedCard(card);
            player.updateState(gameState, playerState);
        }
        if (withALocomotive) {
            playerState = playerState.withAddedCard(LOCOMOTIVE);
        }
        player.updateState(gameState, playerState);
    }

    private static PlayerState playerState() {
        playerState = gameState.playerState(id);
        return playerState;
    }

    /**
     * Waits for the player to click on the Next button
     * @param nextCount number of click needed
     */
    private static void waitsForNext(int nextCount) {
        for (int i = 0; i < nextCount; i++) {
            player.waitsForNext(tutorialText.nextLine());
        }
    }

    /**
     * Goes to the next tutorial instruction without waiting for a player's click
     */
    private static void goesForNext() {
            player.next(tutorialText.nextLine());
    }

    /**
     * Simulates a Draw Card turn
     */
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

}
