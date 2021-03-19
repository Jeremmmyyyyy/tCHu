package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.test.ChMapPublic;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class PlayerStateTest {

    private List<Ticket> listOfTickets = List.of(
            new Ticket(ChMapPublic.BAL, ChMapPublic.BER, 5),
            new Ticket(ChMapPublic.BAL, ChMapPublic.BRI, 10),
            new Ticket(ChMapPublic.BAL, ChMapPublic.STG, 8),
            new Ticket(ChMapPublic.BER, ChMapPublic.LUG, 12));

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

    public SortedBag<Card> bagTooSmallBuilder(){
        SortedBag<Card> cards0 = SortedBag.of(1, Card.BLACK, 1, Card.VIOLET);
        SortedBag<Card> cards1 = SortedBag.of(1, Card.BLUE, 1, Card.GREEN);
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

    @Test
    public void gettersAreOk(){
        PlayerState playerState1 = new PlayerState(SortedBag.of(List.of(
                new Ticket(ChMapPublic.BAL, ChMapPublic.BER, 5),
                new Ticket(ChMapPublic.BAL, ChMapPublic.BRI, 10),
                new Ticket(ChMapPublic.BAL, ChMapPublic.STG, 8),
                new Ticket(ChMapPublic.BER, ChMapPublic.LUG, 12))),
                bagOkBuilder(),
                routes);

        assertEquals(tickets, playerState1.tickets());
        assertEquals(bagOkBuilder(), playerState1.cards());
    }

    @Test
    public void initialWorks(){
        PlayerState playerState = PlayerState.initial(bagExactBuilder());
        PlayerState playerState1 = PlayerState.initial(bagOkBuilder());


    }

}
