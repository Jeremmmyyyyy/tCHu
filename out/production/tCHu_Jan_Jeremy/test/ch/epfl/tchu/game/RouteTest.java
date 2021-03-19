package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static ch.epfl.tchu.game.Color.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RouteTest {
    @Test
    void constructorFailsWithSameStations() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Route("ZUR_ZUR_1", ZUR, ZUR, 2, Route.Level.OVERGROUND, null);
        });
    }

    @Test
    void constructorFailsWithNullArgument() {
        assertThrows(NullPointerException.class, () -> {
            new Route(null, ZUR, LAU, 2, Route.Level.OVERGROUND, null);
        });

        assertThrows(NullPointerException.class, () -> {
            new Route("ZUR_LAU_1", null, LAU, 2, Route.Level.OVERGROUND, null);
        });

        assertThrows(NullPointerException.class, () -> {
            new Route("ZUR_LAU_1", ZUR, null, 2, Route.Level.OVERGROUND, null);
        });

        assertThrows(NullPointerException.class, () -> {
            new Route("ZUR_LAU_1", ZUR, LAU, 2, null, null);
        });
    }

    @Test
    void constructorFailsWithFalseLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Route("ZUR_ZUR_1", ZUR, ZUR, 2, Route.Level.OVERGROUND, null);
        });
    }

    @Test
    void idIsCorrect() {
        assertEquals("AT1_STG_1", getRoute("AT1_STG_1").id());
    }

    @Test
    void stationOppositeFailsWithWrongStation() {
        assertThrows(IllegalArgumentException.class, () -> {
            getRoute("AT1_STG_1").stationOpposite(ZUR);
        });
    }

    @Test
    void possibleClaimCardsIsCorrectWithNullUndergroundRoute() {
        Route route = new Route("route", ZUR, LAU, 2, Route.Level.UNDERGROUND, null);

        List<SortedBag<Card>> possibleClaimCards = route.possibleClaimCards();

        for (SortedBag bag : possibleClaimCards) {
            //System.out.println(bag);
        }

        List<SortedBag<Card>> expectedCards = new ArrayList<>();

        for (Color color : ALL) {
            expectedCards.add(SortedBag.of(2, Card.of(color)));
        }
        for (Color color : ALL) {
            expectedCards.add(SortedBag.of(1, Card.of(color), 1, Card.LOCOMOTIVE));
        }
        expectedCards.add(SortedBag.of(2, Card.LOCOMOTIVE));

        for (int i = 0; i < 17; i++) {
            assertEquals(expectedCards.get(i), possibleClaimCards.get(i));
        }
    }

    @Test
    void possibleClaimCardsIsCorrectWithNullOvergroundRoute() {
        Route route = new Route("route", ZUR, LAU, 3, Route.Level.OVERGROUND, null);

        List<SortedBag<Card>> possibleClaimCards = route.possibleClaimCards();

        for (SortedBag bag : possibleClaimCards) {
            //System.out.println(bag);
        }

        List<SortedBag<Card>> expectedCards = new ArrayList<>();

        for (Color color : ALL) {
            expectedCards.add(SortedBag.of(3, Card.of(color)));
        }

        for (int i = 0; i < COUNT; i++) {
            assertEquals(expectedCards.get(i), possibleClaimCards.get(i));
        }
    }

    @Test
    void possibleClaimCardsIsCorrectWithRedUndergroundRoute() {
        Route route = new Route("route", ZUR, LAU, 3, Route.Level.UNDERGROUND, Color.RED);

        List<SortedBag<Card>> possibleClaimCards = route.possibleClaimCards();

        for (SortedBag bag : possibleClaimCards) {
            //System.out.println(bag);
        }

        List<SortedBag<Card>> expectedCards = new ArrayList<>();

        expectedCards.add(SortedBag.of(3, Card.RED));
        expectedCards.add(SortedBag.of(2, Card.RED, 1, Card.LOCOMOTIVE));
        expectedCards.add(SortedBag.of(1, Card.RED, 2, Card.LOCOMOTIVE));
        expectedCards.add(SortedBag.of(3, Card.LOCOMOTIVE));


        for (int i = 0; i < 4; i++) {
            assertEquals(expectedCards.get(i), possibleClaimCards.get(i));
        }
    }

    @Test
    void possibleClaimCardsIsCorrectWithRedOvergroundRoute() {
        Route route = new Route("route", ZUR, LAU, 5, Route.Level.OVERGROUND, Color.RED);

        List<SortedBag<Card>> possibleClaimCards = route.possibleClaimCards();

        for (SortedBag bag : possibleClaimCards) {
            //System.out.println(bag);
        }

        List<SortedBag<Card>> expectedCards = new ArrayList<>();

        expectedCards.add(SortedBag.of(5, Card.RED));

        assertEquals(expectedCards.get(0), possibleClaimCards.get(0));
    }

    @Test
    void additionalClaimCardsCountFailsWithOverground() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Route("ZUR_ZUR_1", ZUR, ZUR, 2, Route.Level.OVERGROUND, null);
        });
    }

    @Test
    void additionalClaimCardsCountFailsWithWrongDrawnCards() {
        Route route = getRoute("LCF_NEU_1");
        SortedBag<Card> claimCards = SortedBag.of(1, Card.ORANGE);

        SortedBag.Builder drawnCardsBuilder = new SortedBag.Builder()
                .add(1, Card.of(Color.GREEN))
                .add(1, Card.of(Color.ORANGE));
        SortedBag<Card> drawnCards = drawnCardsBuilder.build();

        assertThrows(IllegalArgumentException.class, () -> {
            route.additionalClaimCardsCount(claimCards, drawnCards);
        });
    }

    @Test
    void additionalClaimCardsCountCorrect() {

        Route route = getRoute("LCF_NEU_1");
        SortedBag<Card> claimCards = SortedBag.of(1, Card.ORANGE);

        SortedBag.Builder drawnCardsBuilder = new SortedBag.Builder()
                .add(1, Card.of(Color.GREEN))
                .add(1, Card.of(Color.ORANGE))
                .add(1, Card.of(null));
        SortedBag<Card> drawnCards = drawnCardsBuilder.build();

        assertEquals(2, route.additionalClaimCardsCount(claimCards, drawnCards));
    }

    @Test
    void additionalClaimCardsCountCorrect2() {

        Route route = getRoute("LCF_NEU_1");
        SortedBag<Card> claimCards = SortedBag.of(1, Card.ORANGE);

        SortedBag.Builder drawnCardsBuilder = new SortedBag.Builder()
                .add(1, Card.of(Color.GREEN))
                .add(1, Card.of(Color.ORANGE))
                .add(1, Card.of(null));
        SortedBag<Card> drawnCards = drawnCardsBuilder.build();

        assertEquals(2, route.additionalClaimCardsCount(claimCards, drawnCards));
    }



    private Route getRoute(String id) {
        for (Route route : ChMap.routes()) {
            if(route.id()==id) return route;
        }
        System.out.println("No Route with this id was found");
        return null;
    }

    public static final Station BAD = new Station(0, "Baden");
    public static final Station BAL = new Station(1, "Bâle");
    public static final Station BEL = new Station(2, "Bellinzone");
    public static final Station BER = new Station(3, "Berne");
    public static final Station BRI = new Station(4, "Brigue");
    public static final Station BRU = new Station(5, "Brusio");
    public static final Station COI = new Station(6, "Coire");
    public static final Station DAV = new Station(7, "Davos");
    public static final Station DEL = new Station(8, "Delémont");
    public static final Station FRI = new Station(9, "Fribourg");
    public static final Station GEN = new Station(10, "Genève");
    public static final Station INT = new Station(11, "Interlaken");
    public static final Station KRE = new Station(12, "Kreuzlingen");
    public static final Station LAU = new Station(13, "Lausanne");
    public static final Station LCF = new Station(14, "La Chaux-de-Fonds");
    public static final Station LOC = new Station(15, "Locarno");
    public static final Station LUC = new Station(16, "Lucerne");
    public static final Station LUG = new Station(17, "Lugano");
    public static final Station MAR = new Station(18, "Martigny");
    public static final Station NEU = new Station(19, "Neuchâtel");
    public static final Station OLT = new Station(20, "Olten");
    public static final Station PFA = new Station(21, "Pfäffikon");
    public static final Station SAR = new Station(22, "Sargans");
    public static final Station SCE = new Station(23, "Schaffhouse");
    public static final Station SCZ = new Station(24, "Schwyz");
    public static final Station SIO = new Station(25, "Sion");
    public static final Station SOL = new Station(26, "Soleure");
    public static final Station STG = new Station(27, "Saint-Gall"); //public
    public static final Station VAD = new Station(28, "Vaduz");
    public static final Station WAS = new Station(29, "Wassen");
    public static final Station WIN = new Station(30, "Winterthour");
    public static final Station YVE = new Station(31, "Yverdon");
    public static final Station ZOU = new Station(32, "Zoug");
    public static final Station ZUR = new Station(33, "Zürich");

    // Stations - countries
    public static final Station DE1 = new Station(34, "Allemagne");
    public static final Station DE2 = new Station(35, "Allemagne");
    public static final Station DE3 = new Station(36, "Allemagne");
    public static final Station DE4 = new Station(37, "Allemagne");
    public static final Station DE5 = new Station(38, "Allemagne");
    public static final Station AT1 = new Station(39, "Autriche"); //public
    public static final Station AT2 = new Station(40, "Autriche");
    public static final Station AT3 = new Station(41, "Autriche");
    public static final Station IT1 = new Station(42, "Italie");
    public static final Station IT2 = new Station(43, "Italie");
    public static final Station IT3 = new Station(44, "Italie");
    public static final Station IT4 = new Station(45, "Italie");
    public static final Station IT5 = new Station(46, "Italie");
    public static final Station FR1 = new Station(47, "France");
    public static final Station FR2 = new Station(48, "France");
    public static final Station FR3 = new Station(49, "France");
    public static final Station FR4 = new Station(50, "France");


}