package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.test.ChMapPublic;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class PlayerStateTest {

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

    private List<Route> routesTooLong = List.of(
            new Route("BRI_LOC_1", ChMapPublic.BRI, ChMapPublic.LOC, 6, Route.Level.UNDERGROUND, null),
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
        assertThrows(IllegalArgumentException.class, ()->{
            PlayerState playerState1 = PlayerState.initial(bagOkBuilder());
        });
        assertThrows(IllegalArgumentException.class, ()->{
            PlayerState playerState2 = PlayerState.initial(bagTooSmallBuilder());
        });
        assertEquals(SortedBag.of(), playerState.tickets());
        assertEquals(bagExactBuilder(), playerState.cards());
        assertEquals(List.of(), playerState.routes());
    }

    @Test
    public void withAddedTicketsWork(){
        PlayerState playerState1 = new PlayerState(tickets, bagOkBuilder(), routes);
        playerState1 = playerState1.withAddedTickets(ticketsToAdd);
        assertEquals(9, playerState1.tickets().size());
    }

    @Test
    public void withAddedCardsWork(){
        PlayerState playerState1 = new PlayerState(tickets, bagOkBuilder(), routes);
        playerState1 = playerState1.withAddedCards(bagOkBuilder());
        PlayerState playerState2 = new PlayerState(tickets, bagOkBuilder(), routes);
        playerState2 = playerState2.withAddedCards(bagExactBuilder());

        assertEquals(180, playerState1.cards().size());
        assertEquals(94, playerState2.cards().size());

    }

    @Test
    public void withAddedCardWork(){
        PlayerState playerState1 = new PlayerState(tickets, bagOkBuilder(), routes);
        playerState1 = playerState1.withAddedCard(Card.BLACK);
        assertEquals(91, playerState1.cards().size());
    }

    @Test
    public void canClaimRouteWorks(){
        //Cas le plus simple avec Overground et couleur unique
        PlayerState playerState = new PlayerState(tickets, SortedBag.of(2, Card.VIOLET), routes);
        Route route1 = new Route("BAD_OLT_1", ChMapPublic.BAD, ChMapPublic.OLT, 2, Route.Level.OVERGROUND, Color.VIOLET);
        assertTrue(playerState.canClaimRoute(route1));

        //Cas où il n'y a plus assez de wagons
        assertThrows(IllegalArgumentException.class,()->{
            PlayerState playerState1 = new PlayerState(tickets, SortedBag.of(2, Card.VIOLET), routes40);
            assertFalse(playerState1.canClaimRoute(route1));
        });

        //Cas ou le joueur possède déjà la route
        PlayerState playerState3 = new PlayerState(tickets, SortedBag.of(3, Card.BLUE), routes);
        Route route2 = new Route("BER_INT_1", ChMapPublic.BER, ChMapPublic.INT, 3, Route.Level.OVERGROUND, Color.BLUE);
        assertTrue(playerState3.canClaimRoute(route2));

        //Cas route de couleur null
        PlayerState playerState4 = new PlayerState(tickets, bagOkBuilder(), routes);
        Route route3 = new Route("BER_LUC_1", ChMapPublic.BER, ChMapPublic.LUC, 4, Route.Level.OVERGROUND, null);
        assertTrue(playerState4.canClaimRoute(route3));

        //Cas route de couleur null mais pas assez de cartes
        PlayerState playerState5 = new PlayerState(tickets, bagTooSmallBuilder(), routes);
        Route route4 = new Route("BER_LUC_1", ChMapPublic.BER, ChMapPublic.LUC, 4, Route.Level.OVERGROUND, null);
        assertFalse(playerState5.canClaimRoute(route4));

        //Test Général
        PlayerState playerState6 = new PlayerState(tickets, bagOkBuilder(), routes2);
        for(Route route : ChMapPublic.ALL_ROUTES){
            assertTrue(playerState6.canClaimRoute(route));
        }

        //Cas route tunnel avec trop de cartes diff mais le bon nombre
        List<Card> cardsList = List.of(Card.LOCOMOTIVE,Card.LOCOMOTIVE,Card.RED,Card.RED,Card.ORANGE);
        PlayerState playerState7 = new PlayerState(tickets, SortedBag.of(cardsList), routes);
        Route route5 = new Route("COI_WAS_1", ChMapPublic.COI, ChMapPublic.WAS, 5, Route.Level.UNDERGROUND, null);
        assertFalse(playerState7.canClaimRoute(route5));
    }

    @Test
    public void possibleClaimCardsWorksGenerally(){
        //Test global prints
        PlayerState playerState1 = new PlayerState(tickets, bagOkBuilder(), routes2);
        for(Route route : ChMapPublic.ALL_ROUTES){
            System.out.println(playerState1.possibleClaimCards(route));
        }
    }

    @Test
    public void possibleClaimCardsWorksWithSpecificCases(){
        //Test spécifique avec cartes limitées
        Route route = new Route("AT1_STG_1",ChMapPublic.AT1, ChMapPublic.STG, 4, Route.Level.UNDERGROUND, null);
        List<Card> cardsList = List.of(Card.LOCOMOTIVE,Card.LOCOMOTIVE,Card.LOCOMOTIVE,Card.LOCOMOTIVE,Card.RED,Card.RED,Card.ORANGE,Card.ORANGE,Card.ORANGE,Card.ORANGE);
        PlayerState playerState2 = new PlayerState(tickets, SortedBag.of(cardsList), routes2);
        System.out.println(playerState2.possibleClaimCards(route));

        //Test spécifique avec cartes limitées
        Route route2 = new Route("AT1_STG_1",ChMapPublic.AT1, ChMapPublic.STG, 4, Route.Level.UNDERGROUND, null);
        List<Card> cardsList2 = List.of(Card.RED,Card.RED,Card.ORANGE,Card.ORANGE,Card.ORANGE,Card.ORANGE,Card.BLUE,Card.BLUE,Card.BLUE,Card.BLUE,Card.BLUE,Card.LOCOMOTIVE);
        PlayerState playerState3 = new PlayerState(tickets, SortedBag.of(cardsList2), routes2);
        System.out.println(playerState3.possibleClaimCards(route2));

        //Test spécifique avec cartes limitées
        Route route3 = new Route("AT1_STG_1",ChMapPublic.AT1, ChMapPublic.STG, 4, Route.Level.UNDERGROUND, null);
        List<Card> cardsList3 = List.of(Card.RED);
        PlayerState playerState4 = new PlayerState(tickets, SortedBag.of(cardsList3), routes2);
        System.out.println(playerState4.possibleClaimCards(route3));
    }

    @Test
    public void possibleAdditionalCardsWithBigBag(){
        PlayerState playerState = new PlayerState(tickets, bagOkBuilder(), routes2);
        List<Card> moreThanTwoTypesOfCards = List.of(Card.BLUE, Card.BLUE,Card.RED,Card.WHITE);
        playerState.possibleAdditionalCards(1,SortedBag.of(2, Card.BLUE, 1,Card.RED), SortedBag.of(2, Card.BLUE, 1, Card.RED));
        playerState.possibleAdditionalCards(2,SortedBag.of(2, Card.BLUE, 1,Card.RED), SortedBag.of(2, Card.BLUE, 1, Card.RED));
        playerState.possibleAdditionalCards(3,SortedBag.of(2, Card.BLUE, 1,Card.RED), SortedBag.of(2, Card.BLUE, 1, Card.RED));
        assertThrows(IllegalArgumentException.class, ()-> {
            playerState.possibleAdditionalCards(0,SortedBag.of(2, Card.BLUE, 1,Card.RED), SortedBag.of(2, Card.BLUE, 1, Card.RED));
        });
        assertThrows(IllegalArgumentException.class, ()-> {
            playerState.possibleAdditionalCards(-1,SortedBag.of(2, Card.BLUE, 1,Card.RED), SortedBag.of(2, Card.BLUE, 1, Card.RED));
        });
        assertThrows(IllegalArgumentException.class, ()-> {
            playerState.possibleAdditionalCards(4,SortedBag.of(2, Card.BLUE, 1,Card.RED), SortedBag.of(2, Card.BLUE, 1, Card.RED));
        });
        assertThrows(IllegalArgumentException.class, ()-> {
            playerState.possibleAdditionalCards(2,SortedBag.of(2, Card.BLUE, 1,Card.RED), SortedBag.of(2, Card.BLUE));
        });
        assertThrows(IllegalArgumentException.class, ()-> {
            playerState.possibleAdditionalCards(2,SortedBag.of(), SortedBag.of(2, Card.BLUE, 1, Card.RED));
        });
        assertThrows(IllegalArgumentException.class, ()-> {
            playerState.possibleAdditionalCards(1,SortedBag.of(moreThanTwoTypesOfCards), SortedBag.of(2, Card.BLUE, 1, Card.RED));
        });
        System.out.println(playerState.possibleAdditionalCards(1,SortedBag.of(2, Card.BLUE, 1,Card.RED), SortedBag.of(2, Card.BLUE, 1, Card.RED)));
        System.out.println(playerState.possibleAdditionalCards(2,SortedBag.of(2, Card.BLUE, 1,Card.RED), SortedBag.of(2, Card.BLUE, 1, Card.RED)));
        System.out.println(playerState.possibleAdditionalCards(3,SortedBag.of(2, Card.BLUE, 1,Card.RED), SortedBag.of(2, Card.BLUE, 1, Card.RED)));
    }

    @Test
    public void possibleAdditionalCardsWithLimitedBag(){
        List<Card> listOfCards = List.of(Card.BLUE);
        PlayerState playerState = new PlayerState(tickets, SortedBag.of(listOfCards), routes2);
        System.out.println(playerState.possibleAdditionalCards(1, SortedBag.of(2, Card.BLUE, 1,Card.RED), SortedBag.of(3, Card.BLUE)));
    }




}
