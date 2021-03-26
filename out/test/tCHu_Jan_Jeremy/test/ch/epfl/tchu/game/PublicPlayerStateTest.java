package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PublicPlayerStateTest {

    /*new Route("AT1_STG_1",AT1, STG, 4,Route.Level.UNDERGROUND, null),
            new Route("AT2_VAD_1",AT2, VAD, 1,Route.Level.UNDERGROUND, Color.RED),
            new Route("BAD_BAL_1",BAD, BAL, 3,Route.Level.UNDERGROUND, Color.RED),
            new Route("BAD_OLT_1",BAD, OLT, 2,Route.Level.OVERGROUND, Color.VIOLET),
            new Route("BAD_ZUR_1",BAD, ZUR, 1,Route.Level.OVERGROUND, Color.YELLOW),
            new Route("BAL_DE1_1",BAL, DE1, 1,Route.Level.UNDERGROUND, Color.BLUE),
            new Route("BAL_DEL_1",BAL, DEL, 2,Route.Level.UNDERGROUND, Color.YELLOW),
            new Route("BAL_OLT_1",BAL, OLT, 2,Route.Level.UNDERGROUND, Color.ORANGE),
            new Route("BEL_LOC_1",BEL, LOC, 1,Route.Level.UNDERGROUND, Color.BLACK),
            new Route("BEL_LUG_1",BEL, LUG, 1,Route.Level.UNDERGROUND, Color.RED),
            new Route("BEL_LUG_2",BEL, LUG, 1,Route.Level.UNDERGROUND, Color.YELLOW),
            new Route("BEL_WAS_1",BEL, WAS, 4,Route.Level.UNDERGROUND, null),
            new Route("BEL_WAS_2",BEL, WAS, 4,Route.Level.UNDERGROUND, null),
            new Route("BER_FRI_1",BER, FRI, 1,Route.Level.OVERGROUND, Color.ORANGE),
            new Route("BER_FRI_2",BER, FRI, 1,Route.Level.OVERGROUND, Color.YELLOW),
            new Route("BER_INT_1",BER, INT, 3,Route.Level.OVERGROUND, Color.BLUE),
            new Route("BER_LUC_1",BER, LUC, 4,Route.Level.OVERGROUND, null),
            new Route("BER_LUC_2",BER, LUC, 4,Route.Level.OVERGROUND, null),*/


    @Test
    void PublicPlayerStateWorksFine() {
        List<Route> routes = new ArrayList<>();
        routes.add(getRoute("AT1_STG_1")); //4
        routes.add(getRoute("BAD_OLT_1")); //2

        PublicPlayerState p1 = new PublicPlayerState(2, 2, routes);

        assertEquals(2, p1.ticketCount());
        assertEquals(34, p1.carCount());
        assertEquals(9, p1.claimPoints());
        assertEquals(2, p1.cardCount());
    }

    @Test
    void PublicPlayerStateFailsWithNegativeArgument() {
        List<Route> routes = new ArrayList<>();
        routes.add(getRoute("AT2_VAD_1")); //4
        routes.add(getRoute("BAD_OLT_1")); //2

        assertThrows(IllegalArgumentException.class, () -> {
            new PublicPlayerState(-1, 2, routes);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            PublicPlayerState p1 = new PublicPlayerState(2, -1, routes);
        });

    }

    private Route getRoute(String id) {
        for (Route route : ChMap.routes()) {
            if(route.id()==id) return route;
        }
        System.out.println("No Route with this id was found");
        return null;
    }

}