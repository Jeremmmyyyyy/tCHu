package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.PlayerId;
import java.util.Map;

public class GraphicalPlayer {

    private final ObservableGameState observableGameState;
    private final PlayerId playerId;
    private final Map<PlayerId, String> playerNames;

    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> playerNames){
        this.playerId = playerId;
        this.playerNames = playerNames;
        observableGameState = new ObservableGameState(playerId);

    }

    public void setState(){}

    public void receiveInfo(){}

    public void startTurn(){}

    public void chooseTickets(){}

    public void drawCard(){}

    public void chooseClaimCards(){}

    public void chooseAdditionalCards(){}

}
