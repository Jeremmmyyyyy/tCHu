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

        //TODO update state ici ou dans la boucle ????

        for(PlayerId playerId : playerOrder){
            updateStates(players, currentGameState, playerOrder);
            SortedBag<Ticket> chosenTickets = players.get(playerId).chooseInitialTickets();
            currentGameState = currentGameState.withInitiallyChosenTickets(playerId, chosenTickets);
            sendInfoToBoth(new Info(playerNames.get(playerId)).keptTickets(chosenTickets.size()), players);
        }

        while (!currentGameState.lastTurnBegins()){
            currentGameState = middleGame(currentGameState, players, playerNames, rng, playerOrder);
        }


        updateStates(players, currentGameState, playerOrder);
        sendInfoToBoth(new Info(playerNames.get(currentGameState.currentPlayerId())).lastTurnBegins(currentGameState.currentPlayerState().carCount()), players);
        currentGameState = middleGame(currentGameState, players, playerNames, rng, playerOrder);
        Trail trail1 = Trail.longest(currentGameState.playerState(PlayerId.PLAYER_1).routes());
        Trail trail2 = Trail.longest(currentGameState.playerState(PlayerId.PLAYER_2).routes());


        if(trail1.length() > trail2.length()){
            sendInfoToBoth(new Info(playerNames.get(PlayerId.PLAYER_1)).getsLongestTrailBonus(trail1), players);
        }else if(trail1.length() < trail2.length()){
            sendInfoToBoth(new Info(playerNames.get(PlayerId.PLAYER_1)).getsLongestTrailBonus(trail2), players);
        }else{
            sendInfoToBoth(new Info(playerNames.get(PlayerId.PLAYER_1)).getsLongestTrailBonus(trail1), players);
            sendInfoToBoth(new Info(playerNames.get(PlayerId.PLAYER_2)).getsLongestTrailBonus(trail2), players);
        } // TODO methode

        int points1 = currentGameState.playerState(PlayerId.PLAYER_1).finalPoints();
        int points2 = currentGameState.playerState(PlayerId.PLAYER_2).finalPoints();

        if(points1 > points2){
            sendInfoToBoth(new Info(playerNames.get(PlayerId.PLAYER_1)).won(points1,points2), players);
        }else if(points1 < points2){
            sendInfoToBoth(new Info(playerNames.get(PlayerId.PLAYER_2)).won(points2, points1), players);
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

    private static GameState middleGame(GameState currentGameState, Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, Random rng , List<PlayerId> playerOrder){
        for(PlayerId playerId : playerOrder) {
            sendInfoToBoth(new Info(playerNames.get(playerId)).canPlay(), players);
            Player currentPlayer = players.get(playerId);
            players.get(playerId).updateState(currentGameState, currentGameState.playerState(playerId));
            Player.TurnKind turnKind = players.get(playerId).nextTurn();

            if(turnKind.equals(Player.TurnKind.DRAW_TICKETS)){
                sendInfoToBoth(new Info(playerNames.get(playerId)).drewTickets(Constants.IN_GAME_TICKETS_COUNT), players);
                SortedBag<Ticket> drawnTickets = currentGameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
                SortedBag<Ticket> keptTickets = currentPlayer.chooseTickets(drawnTickets);
                sendInfoToBoth(new Info(playerNames.get(playerId)).keptTickets(keptTickets.size()), players); // TODO bonne position?
                currentGameState = currentGameState.withChosenAdditionalTickets(drawnTickets, keptTickets);

            }else if(turnKind.equals(Player.TurnKind.DRAW_CARDS)){
                for (int i = 0; i < 2; i++) {
                    currentGameState = withCardsRecreatedFromDeckIfDeckEmpty(currentGameState, rng);
                    players.get(playerId).updateState(currentGameState, currentGameState.playerState(playerId));
                    int slot = players.get(playerId).drawSlot();
                    if(0<=slot && slot<=4){
                        System.out.println("1 " + currentGameState.cardState().faceUpCards());
                        sendInfoToBoth(new Info(playerNames.get(playerId)).drewVisibleCard(currentGameState.cardState().faceUpCard(slot)), players);
                        currentGameState = currentGameState.withDrawnFaceUpCard(slot);
                    }else if(slot == Constants.DECK_SLOT){ //TODO deck slot
                        System.out.println("2");
                        sendInfoToBoth(new Info(playerNames.get(playerId)).drewBlindCard(), players);
                        currentGameState = currentGameState.withBlindlyDrawnCard();
                    }
                }

            }else if(turnKind.equals(Player.TurnKind.CLAIM_ROUTE)){
                Route claimedRoute = players.get(playerId).claimedRoute();
                SortedBag<Card> initialClaimCards = players.get(playerId).initialClaimCards();
                if(claimedRoute.level() == Route.Level.UNDERGROUND){
                    sendInfoToBoth(new Info(playerNames.get(playerId)).attemptsTunnelClaim(claimedRoute, initialClaimCards), players);
                    List<Card> drawnCardsList = new ArrayList<>();
                    for (int i = 0; i < Constants.ADDITIONAL_TUNNEL_CARDS; i++) {
                        currentGameState = withCardsRecreatedFromDeckIfDeckEmpty(currentGameState, rng);
                        drawnCardsList.add(currentGameState.topCard());
                        currentGameState = currentGameState.withoutTopCard();
                    }
                    SortedBag<Card> drawnCards = SortedBag.of(drawnCardsList);
                    int additionalCards = claimedRoute.additionalClaimCardsCount(initialClaimCards, drawnCards);
                    sendInfoToBoth(new Info(playerNames.get(playerId)).drewAdditionalCards(drawnCards, additionalCards), players);

                    if (additionalCards > 0) {
                        List<SortedBag<Card>> possibleAdditionalCards = currentGameState.playerState(playerId).
                                possibleAdditionalCards(additionalCards, initialClaimCards, drawnCards);
                        if (!possibleAdditionalCards.isEmpty()) {
                            currentGameState = currentGameState.withClaimedRoute(
                                    claimedRoute,
                                    currentPlayer.chooseAdditionalCards(currentGameState.playerState(playerId).
                                            possibleAdditionalCards(additionalCards, initialClaimCards, drawnCards)));
                            sendInfoToBoth(new Info(playerNames.get(playerId)).claimedRoute(claimedRoute, initialClaimCards), players); //TODO union with additional cards
                        } else {
                            sendInfoToBoth(new Info(playerNames.get(playerId)).didNotClaimRoute(claimedRoute), players);
                        }
                    } else {
                        currentGameState = currentGameState.withClaimedRoute(claimedRoute, initialClaimCards);
                        sendInfoToBoth(new Info(playerNames.get(playerId)).claimedRoute(claimedRoute, initialClaimCards), players);
                    }
                } else if (claimedRoute.level() == Route.Level.OVERGROUND) {
                    currentGameState = currentGameState.withClaimedRoute(claimedRoute, initialClaimCards);
                    sendInfoToBoth(new Info(playerNames.get(playerId)).claimedRoute(claimedRoute, initialClaimCards), players);
                }//TODO un joueur peut il jouer CLAIM_ROUTE sans pouvoir la claim ??? donc boucle else ici + test canClaimRoute en haut
            }
        }
        return currentGameState;
    }

}
