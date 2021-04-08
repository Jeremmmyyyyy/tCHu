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
        List<PlayerId> playerOrder = List.of(currentGameState.currentPlayerId(), currentGameState.currentPlayerId().next());

        players.forEach((k,v) -> v.initPlayers(k, playerNames));

        playerNames.forEach((k,v)-> System.out.print(k+" "));
        System.out.println();
        players.forEach((k,v)-> System.out.print(k+" "));
        System.out.println();

        sendInfoToBoth(new Info(playerNames.get(currentGameState.currentPlayerId())).willPlayFirst(), players);

        for(PlayerId playerId : playerOrder) {
            players.get(playerId).setInitialTicketChoice(currentGameState.topTickets(Constants.INITIAL_TICKETS_COUNT));
            currentGameState = currentGameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
        }

        for(PlayerId playerId : playerOrder){
            Player player = players.get(playerId);
            player.updateState(currentGameState, currentGameState.playerState(playerId));
            SortedBag<Ticket> chosenTickets = player.chooseInitialTickets();
            currentGameState = currentGameState.withInitiallyChosenTickets(playerId, chosenTickets);
            sendInfoToBoth(new Info(playerNames.get(playerId)).keptTickets(chosenTickets.size()), players);
        }

        boolean gameEnds = false;
        boolean lastTurnHasBegun = false;

        while (!gameEnds) {
            PlayerId currentPlayerId = currentGameState.currentPlayerId();
            Player currentPlayer = players.get(currentPlayerId);
            PlayerState currentPlayerState = currentGameState.playerState(currentPlayerId);
            String currentPlayerName = playerNames.get(currentPlayerId);
            Info currentInfo = new Info(currentPlayerName);

            sendInfoToBoth(currentInfo.canPlay(), players);
            currentPlayer.updateState(currentGameState, currentPlayerState);
            Player.TurnKind turnKind = currentPlayer.nextTurn();

            if (turnKind.equals(Player.TurnKind.DRAW_TICKETS)) {
                sendInfoToBoth(currentInfo.drewTickets(Constants.IN_GAME_TICKETS_COUNT), players);
                SortedBag<Ticket> drawnTickets = currentGameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
                SortedBag<Ticket> keptTickets = currentPlayer.chooseTickets(drawnTickets);
                sendInfoToBoth(currentInfo.keptTickets(keptTickets.size()), players);
                currentGameState = currentGameState.withChosenAdditionalTickets(drawnTickets, keptTickets);

            } else if (turnKind.equals(Player.TurnKind.DRAW_CARDS)) {
                for (int i = 0; i < 2; ++i) {
                    currentGameState = withCardsRecreatedFromDeckIfDeckEmpty(currentGameState, rng);
                    currentPlayer.updateState(currentGameState, currentPlayerState);
                    int slot = currentPlayer.drawSlot();
                    if (0 <= slot && slot <= 4) {
                        System.out.println("1 " + currentGameState.cardState().faceUpCards());
                        sendInfoToBoth(currentInfo.drewVisibleCard(currentGameState.cardState().faceUpCard(slot)), players);
                        currentGameState = currentGameState.withDrawnFaceUpCard(slot);
                    } else if (slot == Constants.DECK_SLOT) {
                        System.out.println("2");
                        sendInfoToBoth(currentInfo.drewBlindCard(), players);
                        currentGameState = currentGameState.withBlindlyDrawnCard();
                    }
                }

            } else if (turnKind.equals(Player.TurnKind.CLAIM_ROUTE)) {
                Route claimedRoute = currentPlayer.claimedRoute();
                SortedBag<Card> initialClaimCards = currentPlayer.initialClaimCards();
                if (claimedRoute.level() == Route.Level.UNDERGROUND) {
                    sendInfoToBoth(currentInfo.attemptsTunnelClaim(claimedRoute, initialClaimCards), players);
                    List<Card> drawnCardsList = new ArrayList<>();
                    for (int i = 0; i < Constants.ADDITIONAL_TUNNEL_CARDS; i++) {
                        currentGameState = withCardsRecreatedFromDeckIfDeckEmpty(currentGameState, rng);
                        drawnCardsList.add(currentGameState.topCard());
                        currentGameState = currentGameState.withoutTopCard();
                    }
                    SortedBag<Card> drawnCards = SortedBag.of(drawnCardsList);
                    currentGameState = currentGameState.withMoreDiscardedCards(drawnCards);
                    int additionalCards = claimedRoute.additionalClaimCardsCount(initialClaimCards, drawnCards);
                    sendInfoToBoth(currentInfo.drewAdditionalCards(drawnCards, additionalCards), players);

                    if (additionalCards > 0) {
                        List<SortedBag<Card>> possibleAdditionalCards = currentPlayerState.
                                possibleAdditionalCards(additionalCards, initialClaimCards, drawnCards);
                        if (!possibleAdditionalCards.isEmpty()) {
                            SortedBag<Card> claimCards =
                                    initialClaimCards.union(currentPlayer.chooseAdditionalCards(currentPlayerState.
                                    possibleAdditionalCards(additionalCards, initialClaimCards, drawnCards)));
                            currentGameState = currentGameState.withClaimedRoute(
                                    claimedRoute, claimCards);
                            sendInfoToBoth(currentInfo.claimedRoute(claimedRoute, claimCards), players);
                        } else {
                            sendInfoToBoth(currentInfo.didNotClaimRoute(claimedRoute), players);
                        }
                    } else {
                        currentGameState = currentGameState.withClaimedRoute(claimedRoute, initialClaimCards);
                        sendInfoToBoth(currentInfo.claimedRoute(claimedRoute, initialClaimCards), players);
                    }
                } else if (claimedRoute.level() == Route.Level.OVERGROUND) {
                    currentGameState = currentGameState.withClaimedRoute(claimedRoute, initialClaimCards);
                    sendInfoToBoth(currentInfo.claimedRoute(claimedRoute, initialClaimCards), players);
                }//TODO un joueur peut il jouer CLAIM_ROUTE sans pouvoir la claim ??? donc boucle else ici + test canClaimRoute en haut
            }
            if (currentGameState.currentPlayerId().equals(currentGameState.lastPlayer())) {
                gameEnds = true;
            }
            currentGameState = currentGameState.forNextTurn();
            if (currentGameState.lastPlayer() != null && !lastTurnHasBegun) {
                lastTurnHasBegun = true;
                sendInfoToBoth(new Info(playerNames.get(currentGameState.lastPlayer()))
                        .lastTurnBegins(currentGameState.playerState(currentGameState.lastPlayer()).carCount()), players);
            }
        }

        updateStates(players, currentGameState, playerOrder);
        Trail trail1 = Trail.longest(currentGameState.playerState(PlayerId.PLAYER_1).routes());
        Trail trail2 = Trail.longest(currentGameState.playerState(PlayerId.PLAYER_2).routes());

        Info info1 = new Info(playerNames.get(PlayerId.PLAYER_1));
        Info info2 = new Info(playerNames.get(PlayerId.PLAYER_2));
        int points1 = currentGameState.playerState(PlayerId.PLAYER_1).finalPoints();
        int points2 = currentGameState.playerState(PlayerId.PLAYER_2).finalPoints();

        if(trail1.length() > trail2.length()){
            sendInfoToBoth(info1.getsLongestTrailBonus(trail1), players);
            points1 += Constants.LONGEST_TRAIL_BONUS_POINTS;
        }else if(trail1.length() < trail2.length()){
            sendInfoToBoth(info2.getsLongestTrailBonus(trail2), players);
            points2 += Constants.LONGEST_TRAIL_BONUS_POINTS;
        }else{
            sendInfoToBoth(info1.getsLongestTrailBonus(trail1), players);
            sendInfoToBoth(info2.getsLongestTrailBonus(trail2), players);
            points1 += Constants.LONGEST_TRAIL_BONUS_POINTS;
            points2 += Constants.LONGEST_TRAIL_BONUS_POINTS;
        } // TODO methode

        if(points1 > points2){
            sendInfoToBoth(info1.won(points1, points2), players);
        }else if(points1 < points2){
            sendInfoToBoth(info2.won(points2, points1), players);
        }else{
            sendInfoToBoth(Info.draw(new ArrayList<>(playerNames.values()), points1), players);
        }
    }

    private static void sendInfoToBoth(String infoToSend, Map<PlayerId, Player> players){
        players.forEach((K, v) -> v.receiveInfo(infoToSend));
    }

    private static void updateStates(Map<PlayerId, Player> players, GameState currentGameState, List<PlayerId> playerOrder){
        for(PlayerId playerId : playerOrder){
            players.get(playerId).updateState(currentGameState, currentGameState.playerState(playerId));
        }
    }

    private static GameState withCardsRecreatedFromDeckIfDeckEmpty(GameState currentGameState, Random rng){
        return currentGameState.withCardsDeckRecreatedIfNeeded(rng);
    }

}
