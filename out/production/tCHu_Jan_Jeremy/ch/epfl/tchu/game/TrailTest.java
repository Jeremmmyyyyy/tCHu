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
        System.out.println("================");
        System.out.println(Trail.longest(List.of(ChMapPublic.ALL_ROUTES.get(61),
                ChMapPublic.ALL_ROUTES.get(62), ChMapPublic.ALL_ROUTES.get(70), ChMapPublic.ALL_ROUTES.get(18))));
        System.out.println("===Test 2 Trails même taille mais nbre de routes différents===");
        System.out.println(Trail.longest(List.of(ChMapPublic.ALL_ROUTES.get(15),
                ChMapPublic.ALL_ROUTES.get(49), ChMapPublic.ALL_ROUTES.get(17), ChMapPublic.ALL_ROUTES.get(64), ChMapPublic.ALL_ROUTES.get(23),
                ChMapPublic.ALL_ROUTES.get(24), ChMapPublic.ALL_ROUTES.get(78))));
        System.out.println(Trail.longest(List.of(ChMapPublic.ALL_ROUTES.get(78),
                ChMapPublic.ALL_ROUTES.get(24), ChMapPublic.ALL_ROUTES.get(17), ChMapPublic.ALL_ROUTES.get(64), ChMapPublic.ALL_ROUTES.get(23),
                ChMapPublic.ALL_ROUTES.get(49), ChMapPublic.ALL_ROUTES.get(15), ChMapPublic.ALL_ROUTES.get(20))));

//        System.out.println(Trail.longest(List.of(
//                ChMapPublic.ALL_ROUTES.get(0),ChMapPublic.ALL_ROUTES.get(1),ChMapPublic.ALL_ROUTES.get(2),ChMapPublic.ALL_ROUTES.get(3),ChMapPublic.ALL_ROUTES.get(4),
//                ChMapPublic.ALL_ROUTES.get(5),ChMapPublic.ALL_ROUTES.get(6),ChMapPublic.ALL_ROUTES.get(7),ChMapPublic.ALL_ROUTES.get(8),ChMapPublic.ALL_ROUTES.get(9),
//                ChMapPublic.ALL_ROUTES.get(10),ChMapPublic.ALL_ROUTES.get(11),ChMapPublic.ALL_ROUTES.get(12),ChMapPublic.ALL_ROUTES.get(13),ChMapPublic.ALL_ROUTES.get(14),
//                ChMapPublic.ALL_ROUTES.get(15),ChMapPublic.ALL_ROUTES.get(16),ChMapPublic.ALL_ROUTES.get(17),ChMapPublic.ALL_ROUTES.get(18),ChMapPublic.ALL_ROUTES.get(19),
//                ChMapPublic.ALL_ROUTES.get(20),ChMapPublic.ALL_ROUTES.get(21),ChMapPublic.ALL_ROUTES.get(22),ChMapPublic.ALL_ROUTES.get(23),ChMapPublic.ALL_ROUTES.get(24),
//                ChMapPublic.ALL_ROUTES.get(25),ChMapPublic.ALL_ROUTES.get(26),ChMapPublic.ALL_ROUTES.get(27),ChMapPublic.ALL_ROUTES.get(28),ChMapPublic.ALL_ROUTES.get(29),
//                ChMapPublic.ALL_ROUTES.get(30),ChMapPublic.ALL_ROUTES.get(31),ChMapPublic.ALL_ROUTES.get(32),ChMapPublic.ALL_ROUTES.get(33),ChMapPublic.ALL_ROUTES.get(34),
//                ChMapPublic.ALL_ROUTES.get(35),ChMapPublic.ALL_ROUTES.get(36),ChMapPublic.ALL_ROUTES.get(37),ChMapPublic.ALL_ROUTES.get(38),ChMapPublic.ALL_ROUTES.get(39),
//                ChMapPublic.ALL_ROUTES.get(40),ChMapPublic.ALL_ROUTES.get(41),ChMapPublic.ALL_ROUTES.get(42),ChMapPublic.ALL_ROUTES.get(43),ChMapPublic.ALL_ROUTES.get(44),
//                ChMapPublic.ALL_ROUTES.get(45),ChMapPublic.ALL_ROUTES.get(46),ChMapPublic.ALL_ROUTES.get(47),ChMapPublic.ALL_ROUTES.get(48),ChMapPublic.ALL_ROUTES.get(49),
//                ChMapPublic.ALL_ROUTES.get(50),ChMapPublic.ALL_ROUTES.get(51),ChMapPublic.ALL_ROUTES.get(52),ChMapPublic.ALL_ROUTES.get(53),ChMapPublic.ALL_ROUTES.get(54),
//                ChMapPublic.ALL_ROUTES.get(55),ChMapPublic.ALL_ROUTES.get(56),ChMapPublic.ALL_ROUTES.get(57),ChMapPublic.ALL_ROUTES.get(58),ChMapPublic.ALL_ROUTES.get(59),
//                ChMapPublic.ALL_ROUTES.get(60),ChMapPublic.ALL_ROUTES.get(61),ChMapPublic.ALL_ROUTES.get(62),ChMapPublic.ALL_ROUTES.get(63),ChMapPublic.ALL_ROUTES.get(64),
//                ChMapPublic.ALL_ROUTES.get(65),ChMapPublic.ALL_ROUTES.get(66),ChMapPublic.ALL_ROUTES.get(67),ChMapPublic.ALL_ROUTES.get(68),ChMapPublic.ALL_ROUTES.get(69),
//                ChMapPublic.ALL_ROUTES.get(70),ChMapPublic.ALL_ROUTES.get(71),ChMapPublic.ALL_ROUTES.get(72),ChMapPublic.ALL_ROUTES.get(73),ChMapPublic.ALL_ROUTES.get(74),
//                ChMapPublic.ALL_ROUTES.get(75),ChMapPublic.ALL_ROUTES.get(76),ChMapPublic.ALL_ROUTES.get(77),ChMapPublic.ALL_ROUTES.get(78),ChMapPublic.ALL_ROUTES.get(79),
//                ChMapPublic.ALL_ROUTES.get(80),ChMapPublic.ALL_ROUTES.get(81),ChMapPublic.ALL_ROUTES.get(82),ChMapPublic.ALL_ROUTES.get(83),ChMapPublic.ALL_ROUTES.get(84),
//                ChMapPublic.ALL_ROUTES.get(85),ChMapPublic.ALL_ROUTES.get(86),ChMapPublic.ALL_ROUTES.get(87))));

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