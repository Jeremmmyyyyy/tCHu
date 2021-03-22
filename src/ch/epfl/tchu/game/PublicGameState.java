package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        if(cardState == null || currentPlayerId == null || playerState == null){
            throw new NullPointerException(); //TODO verif si ticket count est nul impossible
        }
        this.ticketCount = ticketCount;
        this.cardState = cardState;
        this.currentPlayerId = currentPlayerId;
        this.playerState = playerState;
        this.lastPlayer = lastPlayer;
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

    public List<Route> claimedRoutes(){ // TODO opti de faire comme ca ?
        List<Route> totalUsedRoutes = new ArrayList<>(playerState.get(currentPlayerId).routes());
        for (Route route : playerState.get(currentPlayerId.next()).routes()) {
            totalUsedRoutes.add(route);
        }
        return totalUsedRoutes;
    }

    public PlayerId lastPlayer() {
        return lastPlayer; //TODO vérif que null est renvoyé dans le cas ou le dernier joueur n'est pas connu
    }
}
