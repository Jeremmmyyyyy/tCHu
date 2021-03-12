package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Trail;
import ch.epfl.test.ChMapPublic;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InfoTest {

    private final String player = "Luc";
    private final Info gui = new Info(player);

//    @Test
//    void routeName() {
//        assertEquals("Autriche – Saint-Gall", gui.routeName(ChMapPublic.ALL_ROUTES.get(0)));
//    }
//
//    @Test
//    void cardsName() {
//        SortedBag<Card> cards = SortedBag.of(1, Card.YELLOW, 4, Card.ORANGE);
//        assertEquals("1 jaune et 4 oranges", gui.cardsName(cards));
//    }

    @Test
    void cardName() {
        assertEquals("noire", Info.cardName(Card.BLACK, 1));
        assertEquals("noires", Info.cardName(Card.BLACK, 4));
        assertEquals("bleues", Info.cardName(Card.BLUE, -34));
    }

    @Test
    void draw() {
        List<String> playerNames = new ArrayList<>();
        playerNames.add(player);
        playerNames.add("Jose");
        assertEquals("\nLuc et Jose sont ex æqo avec 350 points !\n", Info.draw(playerNames, 350));
    }

    @Test
    void willPlayerFirst() {
        assertEquals("Luc jouera en premier.\n\n", gui.willPlayFirst());
    }

    @Test
    void keptTickets() {
        assertEquals("Luc a gardé 3 billets.\n", gui.keptTickets(3));
        assertEquals("Luc a gardé 1 billet.\n", gui.keptTickets(1));
    }

    @Test
    void canPlay() {
        assertEquals("\nC'est à Luc de jouer.\n", gui.canPlay());
    }

    @Test
    void drewTickets() {
        assertEquals("Luc a tiré 3 billets...\n", gui.drewTickets(3));
        assertEquals("Luc a tiré 1 billet...\n", gui.drewTickets(1));
    }

    @Test
    void drewBlindCar() {
        assertEquals("Luc a tiré une carte de la pioche.\n", gui.drewBlindCard());
    }

    @Test
    void drewVisibleCard() {
        assertEquals("Luc a tiré une carte bleue visible.\n", gui.drewVisibleCard(Card.BLUE));
        assertEquals("Luc a tiré une carte locomotive visible.\n", gui.drewVisibleCard(Card.LOCOMOTIVE));
    }

    @Test
    void claimedRoute() {
        SortedBag<Card> cards = SortedBag.of(3, Card.BLUE, 2, Card.YELLOW);
        assertEquals("Luc a pris possession de la route Autriche – Saint-Gall au moyen de 3 bleues et 2 jaunes.\n",
                gui.claimedRoute(ChMapPublic.ALL_ROUTES.get(0), cards));
        SortedBag<Card> cards1 = SortedBag.of(3, Card.BLUE);
        assertEquals("Luc a pris possession de la route Autriche – Saint-Gall au moyen de 3 bleues.\n",
                gui.claimedRoute(ChMapPublic.ALL_ROUTES.get(0), cards1));
        SortedBag<Card> cards2 = SortedBag.of(1, Card.WHITE);
        assertEquals("Luc a pris possession de la route Autriche – Saint-Gall au moyen de 1 blanche.\n",
                gui.claimedRoute(ChMapPublic.ALL_ROUTES.get(0), cards2));
    }

    @Test
    void attemptsTunnelClaim() {
        SortedBag<Card> cards = SortedBag.of(3, Card.BLUE, 1, Card.LOCOMOTIVE);
        assertEquals("Luc tente de s'emparer du tunnel Autriche – Saint-Gall " +
                "au moyen de 3 bleues et 1 locomotive !\n",
                gui.attemptsTunnelClaim(ChMapPublic.ALL_ROUTES.get(0), cards));
    }

    @Test
    void drewAdditionalCards() {
        SortedBag<Card> cards = SortedBag.of(3, Card.BLUE, 2, Card.YELLOW);
        assertEquals("Les cartes supplémentaires sont 3 bleues et 2 jaunes. " +
                "Elles impliquent un coût additionnel de 3 cartes.\n",
                gui.drewAdditionalCards(cards, 3));
        assertEquals("Les cartes supplémentaires sont 3 bleues et 2 jaunes. " +
                        "Elles impliquent un coût additionnel de 1 carte.\n",
                gui.drewAdditionalCards(cards, 1));
        assertEquals("Les cartes supplémentaires sont 3 bleues et 2 jaunes. " +
                        "Elles n'impliquent aucun coût additionnel.\n",
                gui.drewAdditionalCards(cards, 0));
    }

    @Test
    void didNotClaimRoute() {
        assertEquals("Luc n'a pas pu (ou voulu) s'emparer de la route Autriche – Saint-Gall.\n",
                gui.didNotClaimRoute(ChMapPublic.ALL_ROUTES.get(0)));
    }

    @Test
    void lastTurnsBegins() {
        assertEquals("\nLuc n'a plus que 32 wagons, le dernier tour commence !\n",
                gui.lastTurnBegins(32));
        assertEquals("\nLuc n'a plus que 1 wagon, le dernier tour commence !\n",
                gui.lastTurnBegins(1));
    }

    @Test
    void getsLongestTrailBonus() {
        assertEquals("\nLuc reçoit un bonus de 10 points " +
                        "pour le plus long trajet (Autriche – Saint-Gall).\n",
                gui.getsLongestTrailBonus(Trail.longest(List.of(ChMapPublic.ALL_ROUTES.get(0)))));
    }

    @Test
    void won() {
        assertEquals("\nLuc remporte la victoire avec 730 points, contre 715 points !\n",
                gui.won(730, 715));

    }









}