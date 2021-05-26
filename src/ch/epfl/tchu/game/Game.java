package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
/**
 * Class that plays a whole game once started
 *
 * @author Yann Ennassih (329978)
 * @author Jérémy Barghorn (328403)
 */
public final class Game {

    //In order to make the class non instantiable
    private Game() {}


    /**
     * class that plays a whole game once started
     * @param players map of PLayerID and Players that knows all the actions during the course of the game
     * @param playerNames Map that associate the Id of the player to it's literal name
     * @param tickets SortedBag containing all the tickets
     * @param rng Random generator used to shuffle the cards
     */
    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, SortedBag<Ticket> tickets,
                            Random rng){
        Preconditions.checkArgument(players.size() == PlayerId.COUNT);
        Preconditions.checkArgument(playerNames.size() == PlayerId.COUNT);

        GameState currentGameState = GameState.initial(tickets, rng);
        List<PlayerId> playerOrder = List.of(currentGameState.currentPlayerId(), currentGameState.currentPlayerId().next());

        players.forEach((k,v) -> v.initPlayers(k, playerNames));

        sendInfoToBoth(new Info(playerNames.get(currentGameState.currentPlayerId())).willPlayFirst(), players);

        for(PlayerId playerId : playerOrder) {
            players.get(playerId).setInitialTicketChoice(currentGameState.topTickets(Constants.INITIAL_TICKETS_COUNT));
            currentGameState = currentGameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
        }

        for(PlayerId playerId : playerOrder){
            Player player = players.get(playerId);
            updateStates(players, currentGameState, playerOrder);
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
            updateStates(players, currentGameState, playerOrder);
            Player.TurnKind turnKind = currentPlayer.nextTurn();


            switch (turnKind) {

                case DRAW_TICKETS:

                    sendInfoToBoth(currentInfo.drewTickets(Constants.IN_GAME_TICKETS_COUNT), players);
                    SortedBag<Ticket> drawnTickets = currentGameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
                    SortedBag<Ticket> keptTickets = currentPlayer.chooseTickets(drawnTickets);
                    sendInfoToBoth(currentInfo.keptTickets(keptTickets.size()), players);
                    currentGameState = currentGameState.withChosenAdditionalTickets(drawnTickets, keptTickets);
                    break;

                case DRAW_CARDS:

                    for (int i = 0; i < 2; ++i) {
                        currentGameState = currentGameState.withCardsDeckRecreatedIfNeeded(rng);
                        updateStates(players, currentGameState, playerOrder);
                        int slot = currentPlayer.drawSlot();

                        if (0 <= slot && slot < Constants.FACE_UP_CARDS_COUNT) {

                            sendInfoToBoth(currentInfo.drewVisibleCard(currentGameState.cardState().faceUpCard(slot)), players);
                            currentGameState = currentGameState.withDrawnFaceUpCard(slot);

                        } else if (slot == Constants.DECK_SLOT) {

                            sendInfoToBoth(currentInfo.drewBlindCard(), players);
                            currentGameState = currentGameState.withBlindlyDrawnCard();
                        }
                    }
                    break;

                case CLAIM_ROUTE:

                    Route claimedRoute = currentPlayer.claimedRoute();
                    SortedBag<Card> initialClaimCards = currentPlayer.initialClaimCards();

                    if (claimedRoute.level() == Route.Level.UNDERGROUND) {

                        sendInfoToBoth(currentInfo.attemptsTunnelClaim(claimedRoute, initialClaimCards), players);
                        List<Card> drawnCardsList = new ArrayList<>();
                        for (int i = 0; i < Constants.ADDITIONAL_TUNNEL_CARDS; i++) {
                            currentGameState = currentGameState.withCardsDeckRecreatedIfNeeded(rng);;
                            drawnCardsList.add(currentGameState.topCard());
                            currentGameState = currentGameState.withoutTopCard();
                        }
                        SortedBag<Card> drawnCards = SortedBag.of(drawnCardsList);
                        currentGameState = currentGameState.withMoreDiscardedCards(drawnCards);
                        int additionalCards = claimedRoute.additionalClaimCardsCount(initialClaimCards, drawnCards);
                        sendInfoToBoth(currentInfo.drewAdditionalCards(drawnCards, additionalCards), players);

                        if (additionalCards > 0) {

                            List<SortedBag<Card>> possibleAdditionalCards = currentPlayerState.
                                    possibleAdditionalCards(additionalCards, initialClaimCards);

                            if (!possibleAdditionalCards.isEmpty() && !currentPlayer.chooseAdditionalCards(currentPlayerState.
                                    possibleAdditionalCards(additionalCards, initialClaimCards)).isEmpty()) { //TODO demander a lassistant

                                SortedBag<Card> claimCards =
                                        initialClaimCards.union(currentPlayer.chooseAdditionalCards(currentPlayerState.
                                                possibleAdditionalCards(additionalCards, initialClaimCards)));
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
                    }
                    break;
                default:
                    throw new EnumConstantNotPresentException(Player.TurnKind.class, "Invalid enumType");
            }

            if (currentGameState.currentPlayerId() == currentGameState.lastPlayer()) {
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
        }
        if(points1 > points2){

            sendInfoToBoth(info1.won(points1, points2), players);
        }else if(points1 < points2){
            sendInfoToBoth(info2.won(points2, points1), players);
        }else{
            sendInfoToBoth(Info.draw(new ArrayList<>(playerNames.values()), points1), players);
        }
    }

    /**
     * Private method that sends an Info string to the two players
     * @param infoToSend String to send
     * @param players list of the players whom the list should be sent
     */
    private static void sendInfoToBoth(String infoToSend, Map<PlayerId, Player> players){
        players.forEach((k, v) -> v.receiveInfo(infoToSend));
    }

    /**
     * Private method that updates the state of the two players
     * @param players Map of the two players
     * @param currentGameState actual gameState
     * @param playerOrder Random order of the two players
     */
    private static void updateStates(Map<PlayerId, Player> players, GameState currentGameState, List<PlayerId> playerOrder){
        for(PlayerId playerId : playerOrder){
            players.get(playerId).updateState(currentGameState, currentGameState.playerState(playerId));
        }
    }

}
