package ch.epfl.tchu.tutorial;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.Info;
import ch.epfl.tchu.gui.TutorialGraphicalPlayerAdapter;

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

        PlayerState currentPlayerState = currentGameState.playerState(id);



        Map<PlayerId, String> playerNames = Map.of(PLAYER_1, playerName, PLAYER_2, "Ordinateur");

        player.initPlayers(id, playerNames);

        player.waitsForNext(tutorialText.nextLine());
        player.waitsForNext(tutorialText.nextLine());
        player.waitsForNext(tutorialText.nextLine());
        player.receiveInfo("Comme ceci !\n\n");
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
//        player.updateState(currentGameState, currentGameState.playerState(PLAYER_1));
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

        player.next(tutorialText.nextLine()); //17
        player.waitsForNext(tutorialText.nextLine()); //18
        player.waitsForNext(tutorialText.nextLine()); //19

        System.out.println(player);
        for (int i = 0; i < 4; i++) {
            player.updateState(currentGameState, currentGameState.playerState(id).withAddedCard(Card.VIOLET));
        }
        player.updateState(currentGameState, currentGameState.playerState(id));

        player.waitsForNext(tutorialText.nextLine());




        String text;

//        while ((text = tutorialText.nextLine()) != null) {
//            if (text.startsWith("INFO")) {
//                String turnKindName = text.substring(INFO_LENGTH);
//                TurnKind turnKind = TurnKind.valueOf(turnKindName);
//                switch (turnKind) {
//
//                    case DRAW_TICKETS:
//                        player.receiveInfo(info.drewTickets(Constants.IN_GAME_TICKETS_COUNT));
//                        SortedBag<Ticket> drawnTickets = currentGameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
//                        SortedBag<Ticket> keptTickets = player.chooseTickets(drawnTickets);
//                        player.receiveInfo(info.keptTickets(keptTickets.size()));
//                        currentGameState = currentGameState.withChosenAdditionalTickets(drawnTickets, keptTickets);
//                        break;
//
//                    case DRAW_CARDS:
//
//                        for (int i = 0; i < 2; ++i) {
//                            currentGameState = currentGameState.withCardsDeckRecreatedIfNeeded(new Random());
//                            player.updateState(currentGameState, currentPlayerState);
//                            int slot = player.drawSlot();
//
//                            if (0 <= slot && slot < Constants.FACE_UP_CARDS_COUNT) {
//
//                                player.receiveInfo(info.drewVisibleCard(currentGameState.cardState().faceUpCard(slot)));
//                                currentGameState = currentGameState.withDrawnFaceUpCard(slot);
//
//                            } else if (slot == Constants.DECK_SLOT) {
//
//                                player.receiveInfo(info.drewBlindCard());
//                                currentGameState = currentGameState.withBlindlyDrawnCard();
//                            }
//                        }
//                        break;
//
//                    case CLAIM_ROUTE:
//
//                        Route claimedRoute = player.claimedRoute();
//                        SortedBag<Card> initialClaimCards = player.initialClaimCards();
//
//                        if (claimedRoute.level() == Route.Level.UNDERGROUND) {
//
//                            player.receiveInfo(info.attemptsTunnelClaim(claimedRoute, initialClaimCards));
//                            List<Card> drawnCardsList = new ArrayList<>();
//                            for (int i = 0; i < Constants.ADDITIONAL_TUNNEL_CARDS; i++) {
//                                currentGameState = currentGameState.withCardsDeckRecreatedIfNeeded(new Random());;
//                                drawnCardsList.add(currentGameState.topCard());
//                                currentGameState = currentGameState.withoutTopCard();
//                            }
//                            SortedBag<Card> drawnCards = SortedBag.of(drawnCardsList);
//                            currentGameState = currentGameState.withMoreDiscardedCards(drawnCards);
//                            int additionalCards = claimedRoute.additionalClaimCardsCount(initialClaimCards, drawnCards);
//                            player.receiveInfo(info.drewAdditionalCards(drawnCards, additionalCards));
//
//                            if (additionalCards > 0) {
//
//                                List<SortedBag<Card>> possibleAdditionalCards  = currentPlayerState.
//                                        possibleAdditionalCards(additionalCards, initialClaimCards);
//
//                                SortedBag<Card> additionalChosenCards = possibleAdditionalCards.isEmpty() ? null :
//                                        player.chooseAdditionalCards(currentPlayerState.
//                                                possibleAdditionalCards(additionalCards, initialClaimCards));
//
//                                if (additionalChosenCards != null && !additionalChosenCards.isEmpty()) {
//
//                                    SortedBag<Card> claimCards = initialClaimCards.union(additionalChosenCards);
//                                    currentGameState = currentGameState.withClaimedRoute(
//                                            claimedRoute, claimCards);
//                                    player.receiveInfo(info.claimedRoute(claimedRoute, claimCards));
//
//                                } else {
//                                    player.receiveInfo(info.didNotClaimRoute(claimedRoute));
//                                }
//                            } else {
//                                currentGameState = currentGameState.withClaimedRoute(claimedRoute, initialClaimCards);
//                                player.receiveInfo(info.claimedRoute(claimedRoute, initialClaimCards));
//                            }
//                        } else if (claimedRoute.level() == Route.Level.OVERGROUND) {
//                            currentGameState = currentGameState.withClaimedRoute(claimedRoute, initialClaimCards);
//                            player.receiveInfo(info.claimedRoute(claimedRoute, initialClaimCards));
//                        }
//                        break;
//                    default:
//                        throw new Error();
//
//                }
//            } else if (text.startsWith("NEXT")) {
//                player.waitsForNext(text);
//            }


//        }
        System.out.println("has reached the end");

    }

    private static void drawCard(TutorialGraphicalPlayerAdapter player, GameState gameState, PlayerState playerState, Info info) {
        gameState = gameState.withCardsDeckRecreatedIfNeeded(new Random());
        player.updateState(gameState, playerState);
        int slot = player.drawSlot();
         if (0 <= slot && slot < Constants.FACE_UP_CARDS_COUNT) {

             player.receiveInfo(info.drewVisibleCard(gameState.cardState().faceUpCard(slot)));
             gameState = gameState.withDrawnFaceUpCard(slot);

         } else if (slot == Constants.DECK_SLOT) {

             player.receiveInfo(info.drewBlindCard());
             gameState = gameState.withBlindlyDrawnCard();
         }

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
