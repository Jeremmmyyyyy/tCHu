package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.test.ChMapPublic;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class RouteTest {

    @Test
    void constructorFailsWithNullPointerArgument(){
        assertThrows(NullPointerException.class, ()->{
            Route route0 = new Route(null, new Station(0, "Test1"), new Station(1, "Test2"), 4, Route.Level.UNDERGROUND, null);
        });
        assertThrows(NullPointerException.class, ()->{
            Route route1 = new Route("test", null, new Station(1, "Test2"), 4, Route.Level.UNDERGROUND, null);
        });
        assertThrows(NullPointerException.class, ()->{
            Route route2 = new Route("test1", new Station(0, "Test2"), null, 4, Route.Level.UNDERGROUND, null);
        });
        assertThrows(NullPointerException.class, ()->{
            Route route3 = new Route("test1", new Station(0, "Test2"), new Station(1, "Test"), 4, null, null);
        });
    }

    @Test
    void constructorFailsWithIllegalArgumentException(){
        Station station = new Station(0, "");
        assertThrows(IllegalArgumentException.class, ()->{
            Route route4 = new Route("test", station, station, 4, Route.Level.UNDERGROUND, null);
        });
        assertThrows(IllegalArgumentException.class, ()->{
            Route route5 = new Route("test", new Station(0, "Test"), new Station(1, "Test"), 0, Route.Level.UNDERGROUND, null);
        });
        assertThrows(IllegalArgumentException.class, ()->{
            Route route6 = new Route("test", new Station(0, "Test"), new Station(1, "Test"), 7, Route.Level.UNDERGROUND, null);
        });
    }

    @Test
    void gettersAreOk(){
        Station station1 = new Station(10, "Lausanne");
        Station station2 = new Station(20, "Geneve");
        Route route7 = new Route("LausanneGeneve", station1, station2, 5, Route.Level.OVERGROUND, null );
        Route route8 = new Route("LausanneGeneve", station1, station2, 5, Route.Level.OVERGROUND, Color.WHITE );

        assertEquals("LausanneGeneve", route7.id());
        assertEquals(station1, route7.station1());
        assertEquals(station2, route7.station2());
        assertEquals(5, route7.length());
        assertEquals(Route.Level.OVERGROUND, route7.level());
        assertEquals(null, route7.color());
        assertEquals(Color.WHITE, route8.color());
        assertEquals(List.of(station1, station2), route7.stations());
        assertEquals(station1, route7.stationOpposite(route7.station2()));
    }

    @Test
    void possibleClaimCards(){
        // tester avec tous les cas : 0 , 2 , 15 , 16
        Route route = ChMapPublic.ALL_ROUTES.get(16);
        List<SortedBag<Card>> test = route.possibleClaimCards();
        for (int i = 0; i < test.size(); i++) {
            System.out.println(test.get(i));
        }
    }

    @Test
    void drawnCardsFailsWithIllegalArgument(){
        Route route = ChMapPublic.ALL_ROUTES.get(3);

        SortedBag<Card> drawnCards = SortedBag.of(Card.BLUE);
        SortedBag<Card> claimCards = SortedBag.of(Card.BLUE);

        assertThrows(IllegalArgumentException.class, () ->{
            route.additionalClaimCardsCount(drawnCards, claimCards);
        });
    }

    @Test
    void drawnCardsFailsWithEmptyDrawn(){
        Route route = ChMapPublic.ALL_ROUTES.get(0);

        SortedBag<Card> drawnCards = SortedBag.of(1, Card.BLUE, 1, Card.BLACK);
        SortedBag<Card> claimCards = SortedBag.of(1, Card.BLUE,1,Card.BLACK);

        assertThrows(IllegalArgumentException.class, () ->{
            route.additionalClaimCardsCount(drawnCards, claimCards);
        });
    }

    @Test
    void drawnCardsGiveTheCorrectAmount(){
        Route route = ChMapPublic.ALL_ROUTES.get(57);
        SortedBag<Card> drawnCards = SortedBag.of(1, Card.BLUE, 1, Card.BLACK);
        SortedBag<Card> claimCards = SortedBag.of(1, Card.BLUE,1,Card.BLACK);
    }

    @Test
    void claimPoints(){
        Route route0 = ChMapPublic.ALL_ROUTES.get(1);
        Route route1 = ChMapPublic.ALL_ROUTES.get(3);
        Route route2 = ChMapPublic.ALL_ROUTES.get(2);
        Route route3 = ChMapPublic.ALL_ROUTES.get(0);
        Route route4 = ChMapPublic.ALL_ROUTES.get(25);
        Route route5 = ChMapPublic.ALL_ROUTES.get(22);

        assertEquals(1, route0.claimPoints());
        assertEquals(2, route1.claimPoints());
        assertEquals(4, route2.claimPoints());
        assertEquals(7, route3.claimPoints());
        assertEquals(10, route4.claimPoints());
        assertEquals(15, route5.claimPoints());
    }
}
