package ch.epfl.tchu.game;

import ch.epfl.test.ChMapPublic;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrailTest {

    @Test
    void longestCheck() {
        assertEquals(9, Trail.longest(List.of(ChMapPublic.ALL_ROUTES.get(64),
                ChMapPublic.ALL_ROUTES.get(23), ChMapPublic.ALL_ROUTES.get(55))).length());
        assertEquals(20, Trail.longest(List.of(ChMapPublic.ALL_ROUTES.get(64),
                ChMapPublic.ALL_ROUTES.get(23), ChMapPublic.ALL_ROUTES.get(55),
                ChMapPublic.ALL_ROUTES.get(20), ChMapPublic.ALL_ROUTES.get(15),
                ChMapPublic.ALL_ROUTES.get(13), ChMapPublic.ALL_ROUTES.get(44), ChMapPublic.ALL_ROUTES.get(67),
                ChMapPublic.ALL_ROUTES.get(3), ChMapPublic.ALL_ROUTES.get(2), ChMapPublic.ALL_ROUTES.get(60),
                ChMapPublic.ALL_ROUTES.get(7), ChMapPublic.ALL_ROUTES.get(68), ChMapPublic.ALL_ROUTES.get(82),
                ChMapPublic.ALL_ROUTES.get(83)))
                .length());
        System.out.println(Trail.longest(List.of(ChMapPublic.ALL_ROUTES.get(64),
                ChMapPublic.ALL_ROUTES.get(23), ChMapPublic.ALL_ROUTES.get(55),
                ChMapPublic.ALL_ROUTES.get(20), ChMapPublic.ALL_ROUTES.get(15),
                ChMapPublic.ALL_ROUTES.get(13), ChMapPublic.ALL_ROUTES.get(44), ChMapPublic.ALL_ROUTES.get(67),
                ChMapPublic.ALL_ROUTES.get(3), ChMapPublic.ALL_ROUTES.get(2), ChMapPublic.ALL_ROUTES.get(60),
                ChMapPublic.ALL_ROUTES.get(7), ChMapPublic.ALL_ROUTES.get(68), ChMapPublic.ALL_ROUTES.get(82),
                ChMapPublic.ALL_ROUTES.get(83))));
        System.out.println(Trail.longest(List.of(ChMapPublic.ALL_ROUTES.get(64),
                ChMapPublic.ALL_ROUTES.get(23), ChMapPublic.ALL_ROUTES.get(55))));
        System.out.println(Trail.longest(List.of(ChMapPublic.ALL_ROUTES.get(6),
                ChMapPublic.ALL_ROUTES.get(5), ChMapPublic.ALL_ROUTES.get(40), ChMapPublic.ALL_ROUTES.get(67),
                ChMapPublic.ALL_ROUTES.get(49), ChMapPublic.ALL_ROUTES.get(15))));
        System.out.println(Trail.longest(List.of(ChMapPublic.ALL_ROUTES.get(49),
                ChMapPublic.ALL_ROUTES.get(62), ChMapPublic.ALL_ROUTES.get(25), ChMapPublic.ALL_ROUTES.get(79))));
    }

    @Test
    void testToString() {

        System.out.println(Trail.longest(List.of(ChMapPublic.ALL_ROUTES.get(64),
                ChMapPublic.ALL_ROUTES.get(23), ChMapPublic.ALL_ROUTES.get(55),
                ChMapPublic.ALL_ROUTES.get(20), ChMapPublic.ALL_ROUTES.get(15),
                ChMapPublic.ALL_ROUTES.get(13), ChMapPublic.ALL_ROUTES.get(44))));
        System.out.println(Trail.longest(List.of(ChMapPublic.ALL_ROUTES.get(64),
                ChMapPublic.ALL_ROUTES.get(23), ChMapPublic.ALL_ROUTES.get(55),
                ChMapPublic.ALL_ROUTES.get(20), ChMapPublic.ALL_ROUTES.get(15),
                ChMapPublic.ALL_ROUTES.get(13), ChMapPublic.ALL_ROUTES.get(44), ChMapPublic.ALL_ROUTES.get(67),
                ChMapPublic.ALL_ROUTES.get(3), ChMapPublic.ALL_ROUTES.get(2), ChMapPublic.ALL_ROUTES.get(60),
                ChMapPublic.ALL_ROUTES.get(7), ChMapPublic.ALL_ROUTES.get(68), ChMapPublic.ALL_ROUTES.get(82),
                ChMapPublic.ALL_ROUTES.get(83))));
        System.out.println(Trail.longest(List.of(ChMapPublic.ALL_ROUTES.get(77),ChMapPublic.ALL_ROUTES.get(78))));
        System.out.println(Trail.longest(List.of(ChMapPublic.ALL_ROUTES.get(77), ChMapPublic.ALL_ROUTES.get(78), ChMapPublic.ALL_ROUTES.get(30))));
    }

    @Test
    void length() {
        assertEquals(9, Trail.longest(List.of(ChMapPublic.ALL_ROUTES.get(64), ChMapPublic.ALL_ROUTES.get(23),
                ChMapPublic.ALL_ROUTES.get(55))).length());
    }

    @Test
    void station1() {

    }

    @Test
    void station2() {

    }
}