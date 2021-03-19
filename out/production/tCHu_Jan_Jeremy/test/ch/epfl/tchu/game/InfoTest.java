package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;
import ch.epfl.tchu.gui.StringsFr;
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
    void willPlayFirstWorksCorrectly() {
        String value =playerInfo.willPlayFirst();
        assertEquals("Rafa jouera en premier.\n\n", value);
    }

    @Test
    void keptTicketsWorksCorrectly() {
        String value = playerInfo.keptTickets(10);
        assertEquals("Rafa a gardé 10 billets.\n", value);

    }

    @Test
    void canPlayWorksCorrecty() {
        String value =playerInfo.canPlay();
        assertEquals("\nC'est à Rafa de jouer.\n", value);
    }

    @Test
    void drewTickets() {
        String value=playerInfo.drewTickets(10);
        assertEquals(  "Rafa a tiré 10 billets...\n", value);
    }

    @Test
    void drewBlindCard() {
        String value =playerInfo.drewBlindCard();
        assertEquals( "Rafa a tiré une carte de la pioche.\n", value);
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
        Route route = new Route("Bad_Bal", BAD, BAL, 3, Route.Level.UNDERGROUND, Color.RED);
        SortedBag<Card> initialCards = SortedBag.of(3, RED);
        String expected = "Rafa tente de s'emparer du tunnel Baden – Bâle au moyen de 3 rouges !\n";
        String value= playerInfo.attemptsTunnelClaim(route,initialCards);
        assertEquals(expected, value);
    }

    @Test
    void drewAdditionalCards() {
        //SortedBag<Card> drawnCards, int additionalCost
        SortedBag<Card> drawnCards = SortedBag.of(3, RED);
        int additionalCost1=0;
        int additionalCost2=2;
        String expected1 ="Les cartes supplémentaires sont 3 rouges. "+ "Elles n'impliquent aucun coût additionnel.\n";
        String value1 = playerInfo.drewAdditionalCards(drawnCards, additionalCost1);
        assertEquals(expected1,value1);
        String expected2 = "Les cartes supplémentaires sont 3 rouges. "+ "Elles impliquent un coût additionnel de 2 cartes.\n";

        String value2= playerInfo.drewAdditionalCards(drawnCards, additionalCost2);
        assertEquals(expected2, value2);


    }

    @Test
    void didNotClaimRoute() {
        Route route = new Route("Bad_Bal", BAD, BAL, 3, Route.Level.UNDERGROUND, Color.RED);
        String value =playerInfo.didNotClaimRoute(route);
        assertEquals("Rafa n'a pas pu (ou voulu) s'emparer de la route Baden – Bâle.\n", value);
    }

    @Test
    void lastTurnBegins() {
        String valuee = playerInfo.lastTurnBegins(2);

        assertEquals("\nRafa n'a plus que 2 wagons, le dernier tour commence !\n", valuee);
    }

    @Test
    void getsLongestTrailBonus() {
    }

    @Test
    void won() {
        String value = playerInfo.won(200,100);
        assertEquals(  "\nRafa remporte la victoire avec 200 points, contre 100 points !\n", value);
    }

    //Test of private method cardsDescription
    /*@Test
    void cardDescriptionTest() {
        SortedBag<Card> cards = SortedBag.of(ALL);
        SortedBag<Card> cards2 = cards.union(SortedBag.of(2, RED));
        System.out.println(Info.cardsDescription(cards2));
    }*/
}