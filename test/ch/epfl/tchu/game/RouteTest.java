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
        Route route = ChMapPublic.ALL_ROUTES.get(3); //new Route("BAD_OLT_1", ChMapPublic.BAD, ChMapPublic.OLT, 2, Route.Level.OVERGROUND, Color.VIOLET);
        assertEquals(2,route.length());
    }
}
