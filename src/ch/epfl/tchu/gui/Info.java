package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;

import java.util.ArrayList;
import java.util.List;

public final class Info {

    private String playerName;

    Info(String playerName) {
        this.playerName = playerName;
    }

    private static String routeName(Route route) {
        return String.format("%s %s %s", route.station1(), StringsFr.EN_DASH_SEPARATOR, route.station2());
    }

    private static String cardsName(SortedBag<Card> cards) {
        List<String> cardsName = new ArrayList<>();
        for (Card card : cards.toSet()) {
            int i = cards.countOf(card);
            cardsName.add(String.format("%s %s", i, cardName(card, i)));
        }
        int lastIndex = cardsName.size() - 1;
        return String.format("%s et %s",
                String.join(", ", cardsName.subList(0, lastIndex)), cardsName.get(lastIndex));
    }

    public static String cardName(Card card, int count) {
        String cardName = new String();
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
        }
        return cardName + StringsFr.plural(count);
    }

    public static String draw(List<String> playerNames, int points) {
        return String.format(StringsFr.DRAW, String.join(StringsFr.AND_SEPARATOR, playerNames), points);
    }

    public String willPlayFirst() {
        return String.format(StringsFr.WILL_PLAY_FIRST, playerName);
    }

    public String keptTickets(int count) {
        return String.format(StringsFr.KEPT_N_TICKETS, playerName, count);
    }

    public String canPlayer() {
        return String.format(StringsFr.CAN_PLAY, playerName);
    }

    public String drewTickets(int count) {
        return String.format(StringsFr.DREW_TICKETS, playerName, count);
    }

    public String drewBlindCard() {
        return String.format(StringsFr.DREW_BLIND_CARD, playerName);
    }

    public String drewVisibleCard(Card card) {
        return String.format(StringsFr.DREW_VISIBLE_CARD, playerName, cardName(card, 1));
    }

    public String claimedRoute(Route route, SortedBag<Card> cards) {
        return String.format(StringsFr.CLAIMED_ROUTE, playerName, routeName(route), cardsName(cards));
    }

    public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards) {
        return String.format(StringsFr.ATTEMPTS_TUNNEL_CLAIM, playerName, routeName(route), cardsName(initialCards));
    }

    public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost) {
        return String.join(" ",
                String.format(StringsFr.ADDITIONAL_CARDS_ARE, cardsName(drawnCards)),
                String.format(StringsFr.SOME_ADDITIONAL_COST, additionalCost, StringsFr.plural(additionalCost)));
    }

    public String didNotClaimRoute(Route route) {
        return String.format(StringsFr.DID_NOT_CLAIM_ROUTE, playerName, routeName(route));
    }

    public String lastTurnBegins(int carCount) {
        return String.format(StringsFr.LAST_TURN_BEGINS, playerName, carCount, StringsFr.plural(carCount));
    }

}
