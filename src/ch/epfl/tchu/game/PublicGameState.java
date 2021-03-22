package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PublicGameState {

    private final int ticketCount;
    private final PublicCardState cardState;
    private final PlayerId currentPlayerId;
    private final Map<PlayerId, PublicPlayerState> playerState;
    private final PlayerId lastPlayer;

    // last player peut etre null vu que le joueur precedent peut etre inconnu au debut de la partie
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
        this.lastPlayer = Objects.requireNonNull(lastPlayer);
    }

    public int ticketsCount() {
        return ticketCount;
    }

    public boolean canDrawTickets(){
        return ticketCount == 0;
    }

    public PublicCardState cardState() {
        return cardState;
    }

    public boolean canDrawCards(){
        return cardState.deckSize() + cardState.discardsSize() >= 5;
    }

    public PlayerId currentPlayerId() {
        return currentPlayerId;
    }

    public PublicPlayerState playerState(PlayerId playerId){
        return playerState.get(playerId);
    }

    public PublicPlayerState currentPlayerState(){
        return playerState.get(this.currentPlayerId);
    }

    public List<Route> claimedRoutes(){

        List<Route> totalUsedRoutes = new ArrayList<>(playerState.get(currentPlayerId).routes());
//        totalUsedRoutes.addAll() //TODO faire plus opti avec iteration sur playerID.values
        for (Route route : playerState.get(currentPlayerId.next()).routes()) {
            totalUsedRoutes.add(route);
        }

        return totalUsedRoutes;
    }

    public PlayerId lastPlayer() {
        return lastPlayer;
    }
}
