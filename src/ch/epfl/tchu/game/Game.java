package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class Game {

    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, SortedBag<Ticket> tickets, Random rng){
        Preconditions.checkArgument(players.size() == 2);
        Preconditions.checkArgument(playerNames.size() == 2);
        GameState currentGameState = GameState.initial(tickets, rng);

        players.forEach((k,v) -> v.initPlayers(k, playerNames));

        sendInfoToBoth(new Info(playerNames.get(currentGameState.currentPlayerId())).willPlayFirst(), players);

        for(PlayerId playerId : PlayerId.ALL) {
            players.get(playerId).setInitialTicketChoice(currentGameState.topTickets(Constants.INITIAL_TICKETS_COUNT));
            currentGameState = currentGameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
        }

        updateStates(players, currentGameState);

        for(PlayerId playerId : PlayerId.ALL){
            SortedBag<Ticket> chosenTickets = players.get(playerId).chooseInitialTickets();
            currentGameState = currentGameState.withInitiallyChosenTickets(playerId, chosenTickets);
            sendInfoToBoth(new Info(playerNames.get(playerId)).keptTickets(chosenTickets.size()), players);
        }

        while (!currentGameState.lastTurnBegins()){
            for(PlayerId playerId : PlayerId.ALL) {
                Player currentPlayer = players.get(playerId);
                Player.TurnKind turnKind = players.get(playerId).nextTurn();

                if(turnKind.equals(Player.TurnKind.DRAW_TICKETS)){
                    SortedBag<Ticket> drawnTickets = currentGameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
                    currentGameState = currentGameState.withChosenAdditionalTickets(drawnTickets, currentPlayer.chooseTickets(drawnTickets));

                }else if(turnKind.equals(Player.TurnKind.DRAW_CARDS)){
                    for (int i = 0; i < 2; i++) {
                        withCardsRecreatedFromDeckIfDeckEmpty(currentGameState, rng);
                        int slot = players.get(playerId).drawSlot();
                        if(0<=slot && slot<=4){
                            currentGameState = currentGameState.withDrawnFaceUpCard(slot);
                        }else{
                            currentGameState = currentGameState.withBlindlyDrawnCard();
                        }
                    }

                }else{
                    Route claimedRoute = players.get(playerId).claimedRoute();
                    SortedBag<Card> initialClaimCards = players.get(playerId).initialClaimCards();
                    if(claimedRoute.level() == Route.Level.UNDERGROUND){
                        List<Card> drawnCardsList = new ArrayList<>();
                        for (int i = 0; i < Constants.ADDITIONAL_TUNNEL_CARDS; i++) {
                            withCardsRecreatedFromDeckIfDeckEmpty(currentGameState, rng);
                            drawnCardsList.add(currentGameState.topCard());
                            currentGameState = currentGameState.withoutTopCard();
                        }
                        SortedBag<Card> drawnCards = SortedBag.of(drawnCardsList);

                        int additionalCards = claimedRoute.additionalClaimCardsCount(initialClaimCards, drawnCards);
                        if(additionalCards > 0 && currentGameState.playerState(playerId).canClaimRoute(claimedRoute)){
                            currentGameState = currentGameState.withClaimedRoute(
                                    claimedRoute,
                                    currentPlayer.chooseAdditionalCards(currentGameState.playerState(playerId).
                                            possibleAdditionalCards(additionalCards, initialClaimCards, drawnCards)));

                            //TODO C'est LA HESS Recieve Info et update State
                        }
                    }
                }
            }
        }
    }


    private static void sendInfoToBoth(String infoToSend, Map<PlayerId, Player> players){
        players.forEach((K, v) -> v.receiveInfo(infoToSend));
    }

    private static void updateStates(Map<PlayerId, Player> players, GameState currentGameState){
        for(PlayerId playerId : PlayerId.ALL){
            players.get(playerId).updateState(currentGameState, currentGameState.playerState(playerId));
        }
    }

    private static void withCardsRecreatedFromDeckIfDeckEmpty(GameState currentGameState, Random rng){
        currentGameState = currentGameState.withCardsDeckRecreatedIfNeeded(rng);
    }

}
