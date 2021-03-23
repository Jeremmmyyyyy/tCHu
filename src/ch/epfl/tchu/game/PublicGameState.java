package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * GameState that is known by the 2 players
 *
 * @author Jérémy Barghorn (328403)
 */
public class PublicGameState {

    private final int ticketCount;
    private final PublicCardState cardState;
    private final PlayerId currentPlayerId;
    private final Map<PlayerId, PublicPlayerState> playerState;
    private final PlayerId lastPlayerId;

    /**
     * create a new gameState
     * @param ticketCount amount of tickets to the current gameState
     * @param cardState current publicCardState
     * @param currentPlayerId Id of the actual player
     * @param playerState Map containing two elements : the playerState of each player
     * @param lastPlayer Id of the previous player : could be null if it's the first turn
     */
    public PublicGameState(int ticketCount,
                           PublicCardState cardState,
                           PlayerId currentPlayerId,
                           Map<PlayerId, PublicPlayerState> playerState,
                           PlayerId lastPlayer){
        Preconditions.checkArgument(ticketCount >= 0);
        Preconditions.checkArgument(playerState.size() == 2);
        this.ticketCount = ticketCount;
        this.cardState = Objects.requireNonNull(cardState);
        this.currentPlayerId = Objects.requireNonNull(currentPlayerId);
        this.playerState = Objects.requireNonNull(playerState);
        this.lastPlayerId = Objects.requireNonNull(lastPlayer);
    }

    /**
     * return the amount of tickets
     * @return the amount of tickets
     */
    public int ticketsCount() {
        return ticketCount;
    }

    /**
     * return true if a card can be drawn from the tickets
     * @return true if a card can be drawn from the tickets
     */
    public boolean canDrawTickets(){
        return ticketCount != 0;
    }

    /**
     * return the cardState
     * @return the cardState
     */
    public PublicCardState cardState() {
        return cardState;
    }

    /**
     * return true if the player can draw cards (the size of the discard + deck must be greater than 5)
     * @return true if the player can draw a card
     */
    public boolean canDrawCards(){
        return cardState.deckSize() + cardState.discardsSize() >= 5;
    }

    /**
     * return the Id of the current player
     * @return the Id of the current player
     */
    public PlayerId currentPlayerId() {
        return currentPlayerId;
    }

    /**
     * return the publicPlayerState of the given player
     * @param playerId Id of the player
     * @return the publicPlayerState of the given player
     */
    public PublicPlayerState playerState(PlayerId playerId){
        return playerState.get(playerId);
    }

    /**
     * return the publicPlayerState of the current player
     * @return the publicPlayerState of the current player
     */
    public PublicPlayerState currentPlayerState(){
        return playerState.get(this.currentPlayerId);
    }

    /**
     * return a list of all the routes that are taken by the players
     * @return a list of all the routes that are taken by the players
     */
    public List<Route> claimedRoutes(){
        List<Route> totalUsedRoutes = new ArrayList<>(playerState.get(currentPlayerId).routes());
        playerState.forEach((k, v) -> totalUsedRoutes.addAll(playerState.get(k).routes())); // TODO bon lambda
        return totalUsedRoutes;
    }

    /**
     * return the Id of the previous player
     * @return the Id of the previous player
     */
    public PlayerId lastPlayer() {
        return lastPlayerId; //TODO verif null
    }
}
