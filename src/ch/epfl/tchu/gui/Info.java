package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Trail;

import java.util.ArrayList;
import java.util.List;

/**
 * Global and specific textual information attached to a player
 *
 * @author Yann Ennassih (329978)
 */
public final class Info {

    private String playerName;

    /**
     * Constructor of the class Info
     * @param playerName of the given player attached to ingame information
     */
    public Info(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Creates a textual representation of a route
     * @param route to put as String
     * @return "fromStation - toStation"
     */
    private static String routeName(Route route) {
        return String.join(StringsFr.EN_DASH_SEPARATOR, route.station1().name(), route.station2().name());
    }

    /**
     * Creates a textual representation of a set of cards
     * @param cards to put as String
     * @return "multiplicity1 card1, ... , multiplicityN-1 cardN-1 et multiplicityN cardN"
     */
    private static String cardsName(SortedBag<Card> cards) {
        List<String> cardsName = new ArrayList<>();
        for (Card card : cards.toSet()) {
            int i = cards.countOf(card);
            cardsName.add(String.format("%s %s", i, cardName(card, i)));
        }
        int lastIndex = cardsName.size() - 1;
        if (lastIndex <= 0) { //if only one type of cards
            return cardsName.get(0);
        }
        return String.format("%s et %s",
                String.join(", ", cardsName.subList(0, lastIndex)), cardsName.get(lastIndex));

    }

    /**
     * Returns the name of the given card
     * @param card whose name is to return
     * @return name of the card, including plural form
     */
    public static String cardName(Card card, int count) {
        String cardName;
        switch (card) {
            case BLACK:
                cardName = StringsFr.BLACK_CARD;
                break;
            case VIOLET:
                cardName = StringsFr.VIOLET_CARD;
                break;
            case BLUE:
                cardName = StringsFr.BLUE_CARD;
                break;
            case GREEN:
                cardName = StringsFr.GREEN_CARD;
                break;
            case YELLOW:
                cardName = StringsFr.YELLOW_CARD;
                break;
            case ORANGE:
                cardName = StringsFr.ORANGE_CARD;
                break;
            case RED:
                cardName = StringsFr.RED_CARD;
                break;
            case WHITE:
                cardName = StringsFr.WHITE_CARD;
                break;
            case LOCOMOTIVE:
                cardName = StringsFr.LOCOMOTIVE_CARD;
                break;
            default:
                throw new EnumConstantNotPresentException(Card.class, "Invalid enum type");
        }
        return cardName + StringsFr.plural(count);
    }

    /**
     * Creates a message in case of a draw
     * @param playerNames players of the game
     * @param points of the draw (of both players)
     * @return "\nplayerName1 et playerName2 sont ex æqo avec points points !\n"
     */
    public static String draw(List<String> playerNames, int points) {
        return String.format(StringsFr.DRAW, String.join(StringsFr.AND_SEPARATOR, playerNames), points);
    }

    /**
     * Displays that the player plays first
     * @return "playerName jouera en premier.\n\n"
     */
    public String willPlayFirst() {
        return String.format(StringsFr.WILL_PLAY_FIRST, playerName);
    }

    /**
     * Displays the number of tickets kept by the player
     * @param count number of tickets kept by the player
     * @return "playerName a gardé count billet(s).\n"
     */
    public String keptTickets(int count) {
        return String.format(StringsFr.KEPT_N_TICKETS, playerName, count, StringsFr.plural(count));
    }

    /**
     * Creates a message for the player's turn
     * @return "\nC'est à playerName de jouer.\n"
     */
    public String canPlay() {
        return String.format(StringsFr.CAN_PLAY, playerName);
    }

    /**
     * Displays the number of card drew by the player
     * @param count number of card drew by the player
     * @return "playerName a tiré count billet(s)...\n"
     */
    public String drewTickets(int count) {
        return String.format(StringsFr.DREW_TICKETS, playerName, count, StringsFr.plural(count));
    }

    /**
     * Creates a message for the player taking a card from the deck
     * @return "playerName a tiré une carte de la pioche.\n"
     */
    public String drewBlindCard() {
        return String.format(StringsFr.DREW_BLIND_CARD, playerName);
    }

    /**
     * Creates a message for the player taking a card from a visible card
     * @param card taken from the player
     * @return "playerName a tiré une carte card visible.\n"
     */
    public String drewVisibleCard(Card card) {
        return String.format(StringsFr.DREW_VISIBLE_CARD, playerName, cardName(card, 1));
    }

    /**
     * Creates a message for the player claiming a route
     * @param route claimed by the player
     * @param cards used to claim the route
     * @return "playerName a pris possession de la route route au moyen de cards.\n"
     */
    public String claimedRoute(Route route, SortedBag<Card> cards) {
        return String.format(StringsFr.CLAIMED_ROUTE, playerName, routeName(route), cardsName(cards));
    }

    /**
     * Creates a message for the player attempting to claim a tunnel
     * @param route the player wants to claim
     * @param initialCards
     * @return "playerName tente de s'emparer du tunnel route au moyen de initialCards !\n"
     */
    public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards) {
        return String.format(StringsFr.ATTEMPTS_TUNNEL_CLAIM, playerName, routeName(route), cardsName(initialCards));
    }

    /**
     * Creates a message to indicate the number of additional cards that need to be played by the player
     * @param drawnCards the payer wants to play
     * @param additionalCost number of additional cards that need to be played by the player
     * @return "Les cartes supplémentaires sont drawnCards. "
     * + "Elles impliquent un coût additionnel de additionalCost carte(s).\n" if additionalCost != 0
     * + "Elles n'impliquent aucun coût additionel.\n" else
     */
    public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost) {
        return  String.join("",
                String.format(StringsFr.ADDITIONAL_CARDS_ARE, cardsName(drawnCards)), additionalCost != 0 ?
                String.format(StringsFr.SOME_ADDITIONAL_COST, additionalCost, StringsFr.plural(additionalCost))
                : StringsFr.NO_ADDITIONAL_COST);
    }

    /**
     * Displays that the player did not or could not claim a route
     * @param route the player wanted to claim
     * @return "playerName n'a pas pu (ou voulu) s'emparer de la route route.\n"
     */
    public String didNotClaimRoute(Route route) {
        return String.format(StringsFr.DID_NOT_CLAIM_ROUTE, playerName, routeName(route));
    }

    /**
     * Indicates that the last turn begins
     * @param carCount number of player's cards left
     * @return "\nplayerName n'a plus que carCount wagon(s), le dernier tour commence !\n"
     */
    public String lastTurnBegins(int carCount) {
        return String.format(StringsFr.LAST_TURN_BEGINS, playerName, carCount, StringsFr.plural(carCount));
    }

    /**
     * Creates a message for the player gaining the longest trail bonus
     * @param longestTrail longest trail of the end-game
     * @return "\nplayerName reçoit un bonus de 10 points pour le plus long trajet (longestTrail).\n"
     */
    public String getsLongestTrailBonus(Trail longestTrail) {
        return String.format(StringsFr.GETS_BONUS, playerName, String.join(StringsFr.EN_DASH_SEPARATOR,
                longestTrail.station1().name(),longestTrail.station2().name()));
    }

    /**
     * Displays the winner
     * @param points of the winner
     * @param loserPoints of the looser
     * @return "\nplayerName remporte la victoire avec points points, contre loserPoints points !\n"
     */
    public String won(int points, int loserPoints) {
        return String.format(StringsFr.WINS, playerName,
                points, StringsFr.plural(points), loserPoints, StringsFr.plural(loserPoints));
    }

}
