package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.test.ChMapPublic;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        Route route = ChMapPublic.ALL_ROUTES.get(0);
        List<SortedBag<Card>> test = route.possibleClaimCards();
        for (int i = 0; i < test.size(); i++) {
            System.out.println(test.get(i));
        }
    }

//    @Test
//    void drawnCardsFailsWithIllegalArgument(){
//        Route route = ChMapPublic.ALL_ROUTES.get(0);
//        List<SortedBag<Card>> drawnCards = new ArrayList<SortedBag<Card>>();
//        List<SortedBag<Card>> claimCards = new ArrayList<SortedBag<Card>>();
//        drawnCards.add(Card.LOCOMOTIVE);
//
//        assertThrows(IllegalArgumentException.class, () ->{
//            route.additionalClaimCardsCount(drawnCards, claimCards);
//        });
//    }

    // faire les exceptions

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

//        Route routeNull = new Route("Test", ChMapPublic.BAD, ChMapPublic.OLT, -5, Route.Level.OVERGROUND, Color.WHITE);
//        assertEquals(0, routeNull.claimPoints()); // TODO vu qu'une route de longueur 0 ne peut être construite comment tester que claim points renvoie Integer.MIN_VALUE pour une longeur 0


    }
}
