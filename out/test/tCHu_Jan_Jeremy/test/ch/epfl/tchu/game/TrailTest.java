package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static ch.epfl.tchu.game.Color.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TrailTest {
    @Test
    void longestReturnsAnEmptyTrailIfGivenListIsEmpty() {

        assertEquals(0, Trail.longest(List.of()).length());
    }

    @Test
    void longestIsCorrectWithNonEmptyGivenList1() {
        // Routes : Baden-Zurich , Zurich-Olten, Olten-Baden
        List<Route> routeList = new ArrayList<>();
        routeList.add(getRoute("BAD_ZUR_1"));
        routeList.add(getRoute("OLT_ZUR_1"));
        routeList.add(getRoute("BAD_OLT_1"));

        Trail longestTrail = Trail.longest(routeList);
        assertEquals(6, longestTrail.length());


    }

    @Test
    void longestIsCorrectWithNonEmptyGivenList2() {
        // Routes : Baden-Zurich , Zurich-Olten, Olten-Baden
        List<Route> routeList = new ArrayList<>();
        routeList.add(getRoute("BRI_WAS_1"));
        routeList.add(getRoute("FR2_GEN_1"));
        routeList.add(getRoute("MAR_SIO_1"));
        routeList.add(getRoute("BRI_SIO_1"));
        routeList.add(getRoute("BER_SOL_1"));


        Trail longestTrail = Trail.longest(routeList);
        assertEquals(9, longestTrail.length());


    }


    private Route getRoute(String id) {
        for (Route route : ChMap.routes()) {
            if(route.id()==id) return route;
        }
        System.out.println("No Route with this id was found");
        return null;
    }

    @Test
    void colorAllIsDefinedCorrectly() {
        assertEquals(List.of(Color.values()), ALL);
    }

    @Test
    void colorCountIsDefinedCorrectly() {
        assertEquals(8, COUNT);
    }

    /*@Test
    void toStringCorrectWithEmptyTrail() {
        assertEquals("Chemin vide", Trail.longest(List.of()));
    }*/
}