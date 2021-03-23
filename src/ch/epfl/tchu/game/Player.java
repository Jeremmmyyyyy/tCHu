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

    void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames);

    void receiveInfo(String info);

    void updateState(PublicGameState newState, PlayerState ownState);

    void setInitialTicketChoice(SortedBag<Ticket> tickets);

    SortedBag<Ticket> chooseInitialTickets();

    TurnKind nextTurn();

    SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options);

    int drawSlot();

    Route claimedRoute();

    SortedBag<Card> initialClaimCards();

    SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options);
}
