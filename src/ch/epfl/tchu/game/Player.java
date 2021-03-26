package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.Map;
/**
 * Interface for all the informations that could be asked for a player
 *
 * @author Jérémy Barghorn (328403)
 */
public interface Player {

    /**
     * Enum representing the tree different actions a player has during his turn
     */
    enum TurnKind{
        DRAW_TICKETS,
        DRAW_CARDS,
        CLAIM_ROUTE;

        /**
         * List containing all the options a player have during his turn
         **/
        public static final List<TurnKind> ALL = List.of(TurnKind.values());
    }

    /**
     * Called at the beginning of the game to communicate his Id to the player and the playerNames of the other players
     * @param ownId Id of the player
     * @param playerNames names of all the players
     */
    void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames);

    /**
     * Called each time an information is communicated to a player during the game
     * @param info that ahs to be transmitted : generally created with Info.java
     */
    void receiveInfo(String info);

    /**
     * Called each time the GameState changes
     * @param newState new PublicGameState that has to be known by the player
     * @param ownState PlayerState
     */
    void updateState(PublicGameState newState, PlayerState ownState);

    /**
     * Called at the beginning of the game to communicate the 5 tickets to the player
     * @param tickets SortedBag containing all the 5 drawn tickets
     */
    void setInitialTicketChoice(SortedBag<Ticket> tickets);

    /**
     * Called at the beginning of the game : ask the player which 3 tickets he wants
     * @return the 3 chosen tickets
     */
    SortedBag<Ticket> chooseInitialTickets();

    /**
     * Called at the beginning of a turn to know which action the player wants to perform
     * @return the action the player wants to play during his turn
     */
    TurnKind nextTurn();

    /**
     * Called if the player wants to draw additional billets
     * @param options
     * @return
     */
    SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options);

    /**
     * Called if the player wants to draw Cards
     * @return 0-4 if the card is a faceUpCard or Constants.DECK_SLOT(-1) if drawn from the deck
     */
    int drawSlot();

    /**
     * Called if the player try to take control of a route
     * @return the route that the player try to control
     */
    Route claimedRoute();

    /**
     * Called if the player try to take a route and ask him for the initial cards
     * @return the cards that are given by the player
     */
    SortedBag<Card> initialClaimCards();

    /**
     * Called if a player try to take control of a tunnel and additional cards are required
     * @param options list of the possible additional cards combinations (empty if the player can't control or don't want to control the route
     * @return a SortedBag
     */
    SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options);
}
