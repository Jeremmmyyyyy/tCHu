package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Ticket;

/**
 * Interface for different specific handlers
 *
 * @author Yann Ennassih (329978)
 */
public interface ActionHandlers {

    /**
     * Interface for the tickets draw as onDrawTickets(...) function
     */
    @FunctionalInterface
    interface DrawTicketsHandler {
        void onDrawTickets();
    }

    /**
     * Interface for the cards draw as onDrawCard(...) function
     *
     */
    @FunctionalInterface
    interface DrawCardHandler {
        void onDrawCard(int drawSlot);
    }

    /**
     * Interface for the route claim as onClaimRoute(...) function
     */
    @FunctionalInterface
    interface ClaimRouteHandler {
        void onClaimRoute(Route route, SortedBag<Card> cards);
    }

    /**
     * Interface for the tickets choice as onChooseTickets(...) function
     */
    @FunctionalInterface
    interface ChooseTicketsHandler {
        void onChooseTickets(SortedBag<Ticket> tickets);
    }

    /**
     * Interface for the cards choice as onChooseCards(...) function
     */
    @FunctionalInterface
    interface ChooseCardsHandler {
        void onChooseCards(SortedBag<Card> cards);
    }

    @FunctionalInterface
    interface TutorialHandler {
        void onButtonClick(boolean leaves);
    }
}
