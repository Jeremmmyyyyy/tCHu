package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RouteTest {

    @Test
    void constructorFailsWithNullPointer(){
        assertThrows(NullPointerException.class, ()->{
            Route route = new Route(null, new Station(0, "Test"), new Station(1, "Test"), 4, Route.Level.UNDERGROUND, null);
        });
        assertThrows(NullPointerException.class, ()->{
            Route route = new Route("test", null, new Station(1, "Test"), 4, Route.Level.UNDERGROUND, null);
        });
        assertThrows(NullPointerException.class, ()->{
            Route route = new Route("test", new Station(0, "Test"), null, 4, Route.Level.UNDERGROUND, null);
        });
        assertThrows(NullPointerException.class, ()->{
            Route route = new Route("test", new Station(0, "Test"), new Station(1, "Test"), 4, null, null);
        });
    }

    @Test
    void constructorFailsWithIllegalArgumentException(){
        Station station = new Station(0, "");
        assertThrows(IllegalArgumentException.class, ()->{
            Route route = new Route("test", new Station(0, "Test"), station, 4, Route.Level.UNDERGROUND, null);
        });
        assertThrows(IllegalArgumentException.class, ()->{
            Route route = new Route("test", new Station(0, "Test"), new Station(1, "Test"), 1, Route.Level.UNDERGROUND, null);
        });
        assertThrows(IllegalArgumentException.class, ()->{
            Route route = new Route("test", new Station(0, "Test"), new Station(1, "Test"), 7, Route.Level.UNDERGROUND, null);
        });
    }
}
