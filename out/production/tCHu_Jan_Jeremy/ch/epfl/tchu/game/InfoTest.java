package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;
import org.junit.jupiter.api.Test;

import java.util.List;

import static ch.epfl.tchu.game.Card.*;
import static org.junit.jupiter.api.Assertions.*;

class InfoTest {

    private final Info playerInfo = new Info("Rafa");

    private static final Station BAD = new Station(0, "Baden");
    private static final Station BAL = new Station(1, "Bâle");

    @Test
    void drawWorksCorrectly() {
        List<String> playerNames = List.of("Roger", "Rafa");
        String value = Info.draw(playerNames, 10);
        assertEquals("\nRoger et Rafa sont ex æqo avec 10 points !\n", value);
    }


    @Test
    void cardName() {
    }

    @Test
    void draw() {
    }

    @Test
    void willPlayFirst() {
    }

    @Test
    void keptTickets() {
    }

    @Test
    void canPlay() {
    }

    @Test
    void drewTickets() {
    }

    @Test
    void drewBlindCard() {
    }

    @Test
    void drewVisibleCard() {
        assertEquals("Rafa a tiré une carte rouge visible.\n", playerInfo.drewVisibleCard(RED));
    }

    @Test
    void claimedRouteWorksCorrectly() {
        Route route = new Route("Bad_Bal", BAD, BAL, 3, Route.Level.UNDERGROUND, Color.RED);
        SortedBag<Card> cards = SortedBag.of(3, RED);
        String expected = "Rafa a pris possession de la route Baden – Bâle au moyen de 3 rouges.\n";
        String value = playerInfo.claimedRoute(route, cards);
        assertEquals(expected, value);
    }

    @Test
    void attemptsTunnelClaim() {
    }

    @Test
    void drewAdditionalCards() {
    }

    @Test
    void didNotClaimRoute() {
    }

    @Test
    void lastTurnBegins() {
    }

    @Test
    void getsLongestTrailBonus() {
    }

    @Test
    void won() {
    }

    //Test of private method cardsDescription
    /*@Test
    void cardDescriptionTest() {
        SortedBag<Card> cards = SortedBag.of(ALL);
        SortedBag<Card> cards2 = cards.union(SortedBag.of(2, RED));
        System.out.println(Info.cardsDescription(cards2));
    }*/
}