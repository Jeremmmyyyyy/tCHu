package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RouteTest {

    @Test
    void constructorFailsWithNullStationIdLevel(){
        assertThrows(NullPointerException.class, ()->{
            Route route = new Route(null, new Station(0, "Test"), new Station(1, "Test"), 4, Route.Level.UNDERGROUND, null);
        });
    }
}
