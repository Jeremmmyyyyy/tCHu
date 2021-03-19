package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.test.ChMapPublic;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TicketPointsTest {

    private List<Ticket> listOfTickets = List.of(
            new Ticket(ChMapPublic.BAL, ChMapPublic.BER, 5),
            new Ticket(ChMapPublic.BAL, ChMapPublic.BRI, 10),
            new Ticket(ChMapPublic.BAL, ChMapPublic.STG, 8),
            new Ticket(ChMapPublic.BER, ChMapPublic.LUG, 12));

    private List<Ticket> listOfTicketsToAdd = List.of(
            new Ticket(ChMapPublic.GEN, ChMapPublic.SIO, 10),
            new Ticket(ChMapPublic.GEN, ChMapPublic.ZUR, 14),
            new Ticket(ChMapPublic.INT, ChMapPublic.WIN, 7),
            new Ticket(ChMapPublic.KRE, ChMapPublic.ZUR, 3),
            new Ticket(ChMapPublic.LAU, ChMapPublic.INT, 7));

    private List<Ticket> unionOfTickets = List.of(
            new Ticket(ChMapPublic.BAL, ChMapPublic.BER, 5),
            new Ticket(ChMapPublic.BAL, ChMapPublic.BRI, 10),
            new Ticket(ChMapPublic.BAL, ChMapPublic.STG, 8),
            new Ticket(ChMapPublic.BER, ChMapPublic.LUG, 12),
            new Ticket(ChMapPublic.GEN, ChMapPublic.SIO, 10),
            new Ticket(ChMapPublic.GEN, ChMapPublic.ZUR, 14),
            new Ticket(ChMapPublic.INT, ChMapPublic.WIN, 7),
            new Ticket(ChMapPublic.KRE, ChMapPublic.ZUR, 3),
            new Ticket(ChMapPublic.LAU, ChMapPublic.INT, 7));


    private List<Route> routes = List.of(new Route("AT1_STG_1", ChMapPublic.AT1, ChMapPublic.STG, 4, Route.Level.UNDERGROUND, null),
            new Route("BAD_OLT_1", ChMapPublic.BAD, ChMapPublic.OLT, 2, Route.Level.OVERGROUND, Color.VIOLET),
            new Route("BER_INT_1", ChMapPublic.BER, ChMapPublic.INT, 3, Route.Level.OVERGROUND, Color.BLUE),
            new Route("BER_LUC_1", ChMapPublic.BER, ChMapPublic.LUC, 4, Route.Level.OVERGROUND, null));

    private List<Route> routes2 = List.of();

    private List<Route> routes40 = List.of(new Route("BRI_LOC_1", ChMapPublic.BRI, ChMapPublic.LOC, 6, Route.Level.UNDERGROUND, null),
            new Route("COI_WAS_1", ChMapPublic.COI, ChMapPublic.WAS, 5, Route.Level.UNDERGROUND, null),
            new Route("GEN_YVE_1", ChMapPublic.GEN, ChMapPublic.YVE, 6, Route.Level.OVERGROUND, null),
            new Route("SCE_WIN_2", ChMapPublic.SCE, ChMapPublic.WIN, 1, Route.Level.OVERGROUND, Color.WHITE),
            new Route("SCE_ZUR_1", ChMapPublic.SCE, ChMapPublic.ZUR, 3, Route.Level.OVERGROUND, Color.ORANGE),
            new Route("SCZ_WAS_1", ChMapPublic.SCZ, ChMapPublic.WAS, 2, Route.Level.UNDERGROUND, Color.GREEN),
            new Route("SCZ_WAS_2", ChMapPublic.SCZ, ChMapPublic.WAS, 2, Route.Level.UNDERGROUND, Color.YELLOW),
            new Route("SCZ_ZOU_1", ChMapPublic.SCZ, ChMapPublic.ZOU, 1, Route.Level.OVERGROUND, Color.BLACK),
            new Route("SCZ_ZOU_2", ChMapPublic.SCZ, ChMapPublic.ZOU, 1, Route.Level.OVERGROUND, Color.WHITE),
            new Route("AT1_STG_1", ChMapPublic.AT1, ChMapPublic.STG, 4, Route.Level.UNDERGROUND, null),
            new Route("BAD_OLT_1", ChMapPublic.BAD, ChMapPublic.OLT, 2, Route.Level.OVERGROUND, Color.VIOLET),
            new Route("BER_INT_1", ChMapPublic.BER, ChMapPublic.INT, 3, Route.Level.OVERGROUND, Color.BLUE),
            new Route("BER_LUC_1", ChMapPublic.BER, ChMapPublic.LUC, 4, Route.Level.OVERGROUND, null));

    private List<Route> routesTooLong = List.of(new Route("BRI_LOC_1", ChMapPublic.BRI, ChMapPublic.LOC, 6, Route.Level.UNDERGROUND, null),
            new Route("COI_WAS_1", ChMapPublic.COI, ChMapPublic.WAS, 5, Route.Level.UNDERGROUND, null),
            new Route("GEN_YVE_1", ChMapPublic.GEN, ChMapPublic.YVE, 6, Route.Level.OVERGROUND, null),
            new Route("SCE_WIN_2", ChMapPublic.SCE, ChMapPublic.WIN, 1, Route.Level.OVERGROUND, Color.WHITE),
            new Route("SCE_ZUR_1", ChMapPublic.SCE, ChMapPublic.ZUR, 3, Route.Level.OVERGROUND, Color.ORANGE),
            new Route("SCZ_WAS_1", ChMapPublic.SCZ, ChMapPublic.WAS, 2, Route.Level.UNDERGROUND, Color.GREEN),
            new Route("SCZ_WAS_2", ChMapPublic.SCZ, ChMapPublic.WAS, 2, Route.Level.UNDERGROUND, Color.YELLOW),
            new Route("SCZ_ZOU_1", ChMapPublic.SCZ, ChMapPublic.ZOU, 1, Route.Level.OVERGROUND, Color.BLACK),
            new Route("SCZ_ZOU_2", ChMapPublic.SCZ, ChMapPublic.ZOU, 1, Route.Level.OVERGROUND, Color.WHITE),
            new Route("AT1_STG_1", ChMapPublic.AT1, ChMapPublic.STG, 4, Route.Level.UNDERGROUND, null),
            new Route("BAD_OLT_1", ChMapPublic.BAD, ChMapPublic.OLT, 2, Route.Level.OVERGROUND, Color.VIOLET),
            new Route("BER_INT_1", ChMapPublic.BER, ChMapPublic.INT, 3, Route.Level.OVERGROUND, Color.BLUE),
            new Route("BER_LUC_1", ChMapPublic.BER, ChMapPublic.LUC, 4, Route.Level.OVERGROUND, null),
            new Route("STG_VAD_1", ChMapPublic.STG, ChMapPublic.VAD, 2,Route.Level.UNDERGROUND, Color.BLUE),
            new Route("STG_WIN_1", ChMapPublic.STG, ChMapPublic.WIN, 3,Route.Level.OVERGROUND, Color.RED),
            new Route("STG_ZUR_1", ChMapPublic.STG, ChMapPublic.ZUR, 4,Route.Level.OVERGROUND, Color.BLACK));

    private SortedBag<Ticket> tickets = SortedBag.of(listOfTickets);

    private SortedBag<Ticket> ticketsToAdd = SortedBag.of(listOfTicketsToAdd);


    public SortedBag<Card> bagTooSmallBuilder(){
        SortedBag<Card> cards0 = SortedBag.of(1, Card.BLACK, 1, Card.VIOLET);
        SortedBag<Card> cards1 = SortedBag.of(0, Card.BLUE, 0, Card.GREEN);
        SortedBag<Card> cards2 = SortedBag.of(0, Card.YELLOW, 0, Card.ORANGE);
        SortedBag<Card> cards3 = SortedBag.of(0, Card.RED, 0, Card.WHITE);
        SortedBag<Card> cards4 = SortedBag.of(0, Card.LOCOMOTIVE);
        SortedBag<Card> deckInBag =  cards0.union(cards1.union(cards2.union(cards3.union(cards4))));
        return deckInBag;
    }

    public SortedBag<Card> bagExactBuilder(){
        SortedBag<Card> cards0 = SortedBag.of(1, Card.BLACK, 1, Card.VIOLET);
        SortedBag<Card> cards1 = SortedBag.of(1, Card.BLUE, 1, Card.GREEN);
        SortedBag<Card> cards2 = SortedBag.of(0, Card.YELLOW, 0, Card.ORANGE);
        SortedBag<Card> cards3 = SortedBag.of(0, Card.RED, 0, Card.WHITE);
        SortedBag<Card> cards4 = SortedBag.of(0, Card.LOCOMOTIVE);
        SortedBag<Card> deckInBag =  cards0.union(cards1.union(cards2.union(cards3.union(cards4))));
        return deckInBag;
    }

    public SortedBag<Card>  bagOkBuilder(){
        SortedBag<Card> cards0 = SortedBag.of(10, Card.BLACK, 10, Card.VIOLET);
        SortedBag<Card> cards1 = SortedBag.of(10, Card.BLUE, 10, Card.GREEN);
        SortedBag<Card> cards2 = SortedBag.of(10, Card.YELLOW, 10, Card.ORANGE);
        SortedBag<Card> cards3 = SortedBag.of(10, Card.RED, 10, Card.WHITE);
        SortedBag<Card> cards4 = SortedBag.of(10, Card.LOCOMOTIVE);
        SortedBag<Card> deckInBag =  cards0.union(cards1.union(cards2.union(cards3.union(cards4))));
        return deckInBag;
    }

    private SortedBag<Ticket> tickets_1 = SortedBag.of(List.of(
            new Ticket(ChMapPublic.BAL, ChMapPublic.BER, 5),
            new Ticket(ChMapPublic.OLT, ChMapPublic.SCE, 5)));

    private SortedBag<Ticket> tickets_2 = SortedBag.of(List.of(
            new Ticket(ChMapPublic.LUC, ChMapPublic.VAD, 6),
            new Ticket(ChMapPublic.GEN, ChMapPublic.SIO, 10),
            new Ticket(ChMapPublic.NEU, ChMapPublic.WIN, 9)));

    private SortedBag<Ticket> tickets_3 = SortedBag.of(List.of(
            new Ticket(ChMapPublic.SCE, ChMapPublic.MAR, 15)));

    private SortedBag<Ticket> tickets_4 = SortedBag.of(List.of(
            ChMapPublic.ticketToNeighbors(List.of(ChMapPublic.BER), 6, 11, 8, 5),
            ChMapPublic.ticketToNeighbors(List.of(ChMapPublic.COI), 6, 3, 5, 12),
            ChMapPublic.ticketToNeighbors(List.of(ChMapPublic.LUG), 12, 13, 2, 14),
            ChMapPublic.ticketToNeighbors(List.of(ChMapPublic.ZUR), 3, 7, 11, 7)));

    private SortedBag<Ticket> tickets_5 = SortedBag.of(List.of(
            new Ticket(ChMapPublic.SCE, ChMapPublic.MAR, 15)));


    private List<Route> routes_1_oneConnected =  List.of(
            ChMapPublic.ALL_ROUTES.get(6),
            ChMapPublic.ALL_ROUTES.get(40),
            ChMapPublic.ALL_ROUTES.get(19));

    private List<Route> routes_1_notConnected = List.of(
            ChMapPublic.ALL_ROUTES.get(50),
            ChMapPublic.ALL_ROUTES.get(28));

    private List<Route> routes_1_bothConnected = List.of(
            ChMapPublic.ALL_ROUTES.get(7),
            ChMapPublic.ALL_ROUTES.get(60),
            ChMapPublic.ALL_ROUTES.get(17),
            ChMapPublic.ALL_ROUTES.get(3),
            ChMapPublic.ALL_ROUTES.get(4),
            ChMapPublic.ALL_ROUTES.get(76));

    private List<Route> routes_2_oneConnected = List.of(
            ChMapPublic.ALL_ROUTES.get(61),
            ChMapPublic.ALL_ROUTES.get(73),
            ChMapPublic.ALL_ROUTES.get(69),
            ChMapPublic.ALL_ROUTES.get(70));

    private List<Route> routes_2_allConnected = List.of(
            ChMapPublic.ALL_ROUTES.get(61),
            ChMapPublic.ALL_ROUTES.get(73),
            ChMapPublic.ALL_ROUTES.get(69),
            ChMapPublic.ALL_ROUTES.get(70),
            ChMapPublic.ALL_ROUTES.get(47),
            ChMapPublic.ALL_ROUTES.get(44),
            ChMapPublic.ALL_ROUTES.get(13),
            ChMapPublic.ALL_ROUTES.get(15),
            ChMapPublic.ALL_ROUTES.get(20),
            ChMapPublic.ALL_ROUTES.get(24),
            ChMapPublic.ALL_ROUTES.get(18),
            ChMapPublic.ALL_ROUTES.get(19),
            ChMapPublic.ALL_ROUTES.get(67),
            ChMapPublic.ALL_ROUTES.get(68),
            ChMapPublic.ALL_ROUTES.get(84),
            ChMapPublic.ALL_ROUTES.get(74),
            ChMapPublic.ALL_ROUTES.get(52));

    private List<Route> routes_3_1 = List.of(
            ChMapPublic.ALL_ROUTES.get(74),
            ChMapPublic.ALL_ROUTES.get(84),
            ChMapPublic.ALL_ROUTES.get(87),
            ChMapPublic.ALL_ROUTES.get(80),
            ChMapPublic.ALL_ROUTES.get(78),
            ChMapPublic.ALL_ROUTES.get(24),
            ChMapPublic.ALL_ROUTES.get(23),
            ChMapPublic.ALL_ROUTES.get(64));

    private List<Route> routes_3_2 = List.of(
            ChMapPublic.ALL_ROUTES.get(76),
            ChMapPublic.ALL_ROUTES.get(4),
            ChMapPublic.ALL_ROUTES.get(3),
            ChMapPublic.ALL_ROUTES.get(60),
            ChMapPublic.ALL_ROUTES.get(17),
            ChMapPublic.ALL_ROUTES.get(13),
            ChMapPublic.ALL_ROUTES.get(44),
            ChMapPublic.ALL_ROUTES.get(55));

    private List<Route> routes_3_3 = List.of(
            ChMapPublic.ALL_ROUTES.get(52),
            ChMapPublic.ALL_ROUTES.get(54),
            ChMapPublic.ALL_ROUTES.get(82),
            ChMapPublic.ALL_ROUTES.get(81),
            ChMapPublic.ALL_ROUTES.get(0),
            ChMapPublic.ALL_ROUTES.get(1),
            ChMapPublic.ALL_ROUTES.get(73),
            ChMapPublic.ALL_ROUTES.get(29),
            ChMapPublic.ALL_ROUTES.get(69),
            ChMapPublic.ALL_ROUTES.get(30),
            ChMapPublic.ALL_ROUTES.get(12),
            ChMapPublic.ALL_ROUTES.get(8),
            ChMapPublic.ALL_ROUTES.get(9),
            ChMapPublic.ALL_ROUTES.get(59),
            ChMapPublic.ALL_ROUTES.get(22),
            ChMapPublic.ALL_ROUTES.get(6),
            ChMapPublic.ALL_ROUTES.get(7),
            ChMapPublic.ALL_ROUTES.get(20),
            ChMapPublic.ALL_ROUTES.get(15),
            ChMapPublic.ALL_ROUTES.get(13),
            ChMapPublic.ALL_ROUTES.get(45),
            ChMapPublic.ALL_ROUTES.get(47),
            ChMapPublic.ALL_ROUTES.get(48),
            ChMapPublic.ALL_ROUTES.get(58),
            ChMapPublic.ALL_ROUTES.get(42),
            ChMapPublic.ALL_ROUTES.get(55));

    private List<Route> routes_3_4 = List.of(
            ChMapPublic.ALL_ROUTES.get(52),
            ChMapPublic.ALL_ROUTES.get(54),
            ChMapPublic.ALL_ROUTES.get(82),
            ChMapPublic.ALL_ROUTES.get(81),
            ChMapPublic.ALL_ROUTES.get(0),
            ChMapPublic.ALL_ROUTES.get(1),
            ChMapPublic.ALL_ROUTES.get(73),
            ChMapPublic.ALL_ROUTES.get(29),
            ChMapPublic.ALL_ROUTES.get(69),
            ChMapPublic.ALL_ROUTES.get(30),
            ChMapPublic.ALL_ROUTES.get(12),
            ChMapPublic.ALL_ROUTES.get(9),
            ChMapPublic.ALL_ROUTES.get(22),
            ChMapPublic.ALL_ROUTES.get(6),
            ChMapPublic.ALL_ROUTES.get(7),
            ChMapPublic.ALL_ROUTES.get(20),
            ChMapPublic.ALL_ROUTES.get(15),
            ChMapPublic.ALL_ROUTES.get(13),
            ChMapPublic.ALL_ROUTES.get(45),
            ChMapPublic.ALL_ROUTES.get(47),
            ChMapPublic.ALL_ROUTES.get(48),
            ChMapPublic.ALL_ROUTES.get(58),
            ChMapPublic.ALL_ROUTES.get(42),
            ChMapPublic.ALL_ROUTES.get(55));


    private List<Route> routes_3_5 = List.of(
            ChMapPublic.ALL_ROUTES.get(76),
            ChMapPublic.ALL_ROUTES.get(4),
            ChMapPublic.ALL_ROUTES.get(2),
            ChMapPublic.ALL_ROUTES.get(5),
            ChMapPublic.ALL_ROUTES.get(6),
            ChMapPublic.ALL_ROUTES.get(40),
            ChMapPublic.ALL_ROUTES.get(19),
            ChMapPublic.ALL_ROUTES.get(18),
            ChMapPublic.ALL_ROUTES.get(66),
            ChMapPublic.ALL_ROUTES.get(48),
            ChMapPublic.ALL_ROUTES.get(46),
            ChMapPublic.ALL_ROUTES.get(47),
            ChMapPublic.ALL_ROUTES.get(55));

    private List<Route> routes_4_1 = List.of(
            ChMapPublic.ALL_ROUTES.get(14),
            ChMapPublic.ALL_ROUTES.get(44),
            ChMapPublic.ALL_ROUTES.get(56),
            ChMapPublic.ALL_ROUTES.get(57),
            ChMapPublic.ALL_ROUTES.get(42));

    private List<Route> routes_4_2 = List.of(
            ChMapPublic.ALL_ROUTES.get(14),
            ChMapPublic.ALL_ROUTES.get(44),
            ChMapPublic.ALL_ROUTES.get(56),
            ChMapPublic.ALL_ROUTES.get(57),
            ChMapPublic.ALL_ROUTES.get(42),
            ChMapPublic.ALL_ROUTES.get(30),
            ChMapPublic.ALL_ROUTES.get(12),
            ChMapPublic.ALL_ROUTES.get(8),
            ChMapPublic.ALL_ROUTES.get(51));

    private List<Route> routes_4_3 = List.of();

    private List<Route> routes_4_4 = List.of(
            ChMapPublic.ALL_ROUTES.get(63),
            ChMapPublic.ALL_ROUTES.get(62),
            ChMapPublic.ALL_ROUTES.get(80),
            ChMapPublic.ALL_ROUTES.get(13),
            ChMapPublic.ALL_ROUTES.get(20),
            ChMapPublic.ALL_ROUTES.get(22),
            ChMapPublic.ALL_ROUTES.get(0));

    @Test
    void ticketPointsWorksWithNoTicketsFulfilled() {
        PlayerState p = new PlayerState(tickets, bagOkBuilder(), routes);
        assertEquals(-35  , p.ticketPoints());
    }

    @Test
    void ticketPointsWorksWithNoRouteClaimed() {
        PlayerState playerStateWithNoRoute= new PlayerState(tickets, bagOkBuilder(), routes2);
        assertEquals( -35, playerStateWithNoRoute.ticketPoints());
    }

    @Test
    void basicTicketPoints() {
        PlayerState p1notConnected = new PlayerState(tickets_1, bagOkBuilder(), routes_1_notConnected);
        assertEquals( -10, p1notConnected.ticketPoints());

        PlayerState p1oneConnected = new PlayerState(tickets_1, bagOkBuilder(), routes_1_oneConnected);
        assertEquals( 0, p1oneConnected.ticketPoints());

        PlayerState p1bothConnected = new PlayerState(tickets_1, bagOkBuilder(), routes_1_bothConnected);
        assertEquals( 10, p1bothConnected.ticketPoints());
    }

    @Test
    void advancedTicketPoints() {
        PlayerState p2oneConnected = new PlayerState(tickets_2, bagOkBuilder(), routes_2_oneConnected);
        assertEquals(-13, p2oneConnected.ticketPoints());

        PlayerState p2allConnected = new PlayerState(tickets_2, bagOkBuilder(), routes_2_allConnected);
        assertEquals(25, p2allConnected.ticketPoints());
    }

    @Test
    void multipleWaysTicketPoints() {
        PlayerState p3_1 = new PlayerState(tickets_3, bagOkBuilder(), routes_3_1);
        assertEquals(15, p3_1.ticketPoints());

        PlayerState p3_2 = new PlayerState(tickets_3, bagOkBuilder(), routes_3_2);
        assertEquals(15, p3_2.ticketPoints());

        PlayerState p3_3 = new PlayerState(tickets_3, bagOkBuilder(), routes_3_3);
        assertEquals(15, p3_3.ticketPoints());

        PlayerState p3_4 = new PlayerState(tickets_3, bagOkBuilder(), routes_3_4);
        assertEquals(-15, p3_4.ticketPoints());

        PlayerState p3_5 = new PlayerState(tickets_3, bagOkBuilder(), routes_3_5);
        assertEquals(15, p3_5.ticketPoints());
    }


    @Test
    void ticketPointsOnCountryCity() {
//        PlayerState p4_1 = new PlayerState(tickets_4, bagOkBuilder(), routes_4_1); //TODO PAS BON
//        assertEquals(-3, p4_1.ticketPoints());

        PlayerState p4_1 = new PlayerState(tickets_4, bagOkBuilder(), routes_4_1);
        assertEquals(-3, p4_1.ticketPoints());

        PlayerState p4_2 = new PlayerState(tickets_4, bagOkBuilder(), routes_4_2);
        assertEquals(5, p4_2.ticketPoints());

        PlayerState p4_3 = new PlayerState(tickets_4, bagOkBuilder(), routes_4_3);
        assertEquals(-13, p4_3.ticketPoints());

        PlayerState p4_4 = new PlayerState(tickets_4, bagOkBuilder(), routes_4_4);
        assertEquals(-13, p4_4.ticketPoints());

    }

    @Test
    void ticketPointsOnCountryCountry() {

    }
}