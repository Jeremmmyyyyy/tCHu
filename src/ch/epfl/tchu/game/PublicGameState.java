package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

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
        return null;
    }

    public Map<PlayerId, PublicPlayerState> getPlayerState() {
        return playerState;
    }

    public PlayerId getLastPlayer() {
        return lastPlayer;
    }
}
