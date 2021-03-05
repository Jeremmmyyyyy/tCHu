package ch.epfl.tchu.game;

import ch.epfl.test.ChMapPublic;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrailTest {

    @Test
    void longestCheck() {
        assertEquals(5, Trail.longest(List.of(ChMapPublic.ALL_ROUTES.get(64), ChMapPublic.ALL_ROUTES.get(23), ChMapPublic.ALL_ROUTES.get(55))).length());
    }

    @Test
    void testToString() {
        System.out.println(Trail.longest(List.of(ChMapPublic.ALL_ROUTES.get(64),
                ChMapPublic.ALL_ROUTES.get(23), ChMapPublic.ALL_ROUTES.get(55),
                ChMapPublic.ALL_ROUTES.get(20), ChMapPublic.ALL_ROUTES.get(15),
                ChMapPublic.ALL_ROUTES.get(13), ChMapPublic.ALL_ROUTES.get(44))).station1() + " " + Trail.longest(List.of(ChMapPublic.ALL_ROUTES.get(64),
                ChMapPublic.ALL_ROUTES.get(23), ChMapPublic.ALL_ROUTES.get(55),
                ChMapPublic.ALL_ROUTES.get(20), ChMapPublic.ALL_ROUTES.get(15),
                ChMapPublic.ALL_ROUTES.get(13), ChMapPublic.ALL_ROUTES.get(44))).station2());
    }

    @Test
    void length() {

    }

    @Test
    void station1() {

    }

    @Test
    void station2() {

    }
}