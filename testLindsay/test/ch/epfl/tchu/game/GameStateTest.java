package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    private final Random random = new Random();

    @Test
    void initial() {
        var initialGS = GameState.initial(someTickets, random);
        assertEquals(true, initialGS.canDrawCards());
        assertEquals(4, initialGS.ticketsCount());
        assertEquals(Constants.ALL_CARDS.size()-8, initialGS.cardState().totalSize());

        var p1 = initialGS.currentPlayerId();

        assertEquals(4, initialGS.currentPlayerState().cardCount());
        assertEquals(0, initialGS.claimedRoutes().size());
        assertEquals(null, initialGS.lastPlayer());


    }

    @Test
    void topTicketsFailsWithWrongCountOfTickets() {
        var initialGS = GameState.initial(someTickets, random);
        assertThrows(IllegalArgumentException.class, () -> {
            initialGS.topTickets(5);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            initialGS.topTickets(-1);
        });
    }

    @Test
    void withoutTopTickets() {
        var initialGS = GameState.initial(someTickets, random);
        var v2 = initialGS.withoutTopTickets(2);
        assertEquals(2, v2.ticketsCount());
        assertThrows(IllegalArgumentException.class, () -> {
            initialGS.withoutTopTickets(5);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            initialGS.withoutTopTickets(-1);
        });
    }

    @Test
    void topCard() {
        var initialGS = GameState.initial(someTickets, random);
        var v2 = initialGS.topCard();
    }

    @Test
    void withoutTopCard() {
        var initialGS = GameState.initial(someTickets, random);
        var v2 = initialGS.withoutTopCard();
        assertEquals(initialGS.cardState().totalSize()-1, v2.cardState().totalSize());
    }

    @Test
    void withMoreDiscardedCards() {
        var initialGS = GameState.initial(someTickets, random);
        var card = SortedBag.of(Card.BLUE);
        var v2 = initialGS.withMoreDiscardedCards(card);
        assertEquals(initialGS.cardState().discardsSize()+1, v2.cardState().discardsSize());
    }

    @Test
    void withCardsDeckRecreatedIfNeeded() {
        var initialGS = GameState.initial(someTickets, random);
        assertEquals(initialGS.cardState(), initialGS.withCardsDeckRecreatedIfNeeded(random).cardState());
    }

    @Test
    void withInitiallyChosenTickets() {
        var initialGS = GameState.initial(someTickets1, random);
        var v2 = initialGS.withInitiallyChosenTickets(PlayerId.PLAYER_2, someTickets2);
        assertTrue(v2.playerState(PlayerId.PLAYER_2).tickets().contains(someTickets2));
        assertThrows(IllegalArgumentException.class, () -> {
            v2.withInitiallyChosenTickets(PlayerId.PLAYER_2, SortedBag.of(ChMap.tickets().get(8)));
        });
        var v3 = v2.withInitiallyChosenTickets(PlayerId.PLAYER_1, SortedBag.of(ChMap.tickets().get(8)));
        assertTrue(v3.playerState(PlayerId.PLAYER_1).tickets().contains(ChMap.tickets().get(8)));
    }

    @Test
    void withChosenAdditionalTickets() {

    }

    @Test
    void withDrawnFaceUpCard() {
        var initialGS = GameState.initial(someTickets, random);
        var slot = 2;
        var cur = initialGS.currentPlayerId();
        var card = initialGS.cardState().faceUpCard(slot);
        var v2 = initialGS.withDrawnFaceUpCard(2);
        assertTrue(v2.playerState(cur).cards().contains(card));
    }

    @Test
    void withBlindlyDrawnCard() {
        var initialGS = GameState.initial(someTickets, random);
        var cur = initialGS.currentPlayerId();
        var card = initialGS.topCard();
        var v2 = initialGS.withBlindlyDrawnCard();
        assertTrue(v2.playerState(cur).cards().contains(card));
    }

    @Test
    void drawnCardsMethodsFailWhenCannotDrawCard() {
        var gs = GameState.initial(someTickets, random);
        var nb = gs.cardState().deckSize();
        System.out.println(nb);
        var nbOfDrawnCards = 0;
        for (int i = 0; i < nb-5; i++) {
            gs = gs.withBlindlyDrawnCard();
            nbOfDrawnCards++;
        }
        System.out.println(nbOfDrawnCards);
        System.out.println(gs.cardState().deckSize());
        assertTrue(gs.canDrawCards());
    }

    @Test
    void withClaimedRoute() {
        var initialGS = GameState.initial(someTickets, random);
        var cur = initialGS.currentPlayerId();
        var rte = getRoute("LAU_NEU_1");
        var cards = SortedBag.of(Card.ORANGE);
        var v2 = initialGS.withClaimedRoute(rte, cards);
        assertEquals(1, v2.claimedRoutes().size());
        assertTrue(v2.currentPlayerState().routes().contains(rte));
        System.out.println(v2.claimedRoutes());
        System.out.println(v2.claimedRoutes().size());
    }

    @Test
    void lastTurnBegins() {
        var gs = GameState.initial(someTickets, random);
        var rte = new Route("AT1_STG_1", AT1, STG, 1, Route.Level.UNDERGROUND, null);
        for (int i = 0; i < 38; i++) {
            gs = gs.withClaimedRoute(rte, SortedBag.of(1, Card.LOCOMOTIVE));
        }
        assertTrue(gs.lastTurnBegins());
        var gs2 = gs.forNextTurn();
        assertTrue(!gs2.lastTurnBegins());
        assertTrue(gs2.lastPlayer()!=null);
    }

    @Test
    void forNextTurn() {
        var initialGS = GameState.initial(someTickets, random);
        var v2 = initialGS.forNextTurn();
        assertEquals(initialGS.currentPlayerId().next(), v2.currentPlayerId());
    }

    private Route getRoute(String id) {
        for (Route route : ChMap.routes()) {
            if(route.id()==id) return route;
        }
        System.out.println("No Route with this id was found");
        return null;
    }

    // Stations - cities
    public final Station BER = new Station(0, "Berne");
    public final Station LAU = new Station(1, "Lausanne");
    public final Station STG = new Station(2, "Saint-Gall");
    public final Station SIO = new Station(25, "Sion");
    private final Station GEN = new Station(10, "Genève");


    // Stations - countries
    public final Station DE1 = new Station(3, "Allemagne");
    public final Station DE2 = new Station(4, "Allemagne");
    public final Station DE3 = new Station(5, "Allemagne");
    public final Station AT1 = new Station(6, "Autriche");
    public final Station AT2 = new Station(7, "Autriche");
    public final Station IT1 = new Station(8, "Italie");
    public final Station IT2 = new Station(9, "Italie");
    public final Station IT3 = new Station(10, "Italie");
    public final Station FR1 = new Station(11, "France");
    public final Station FR2 = new Station(12, "France");

    // Countries
    public final List<Station> DE = List.of(DE1, DE2, DE3);
    public final List<Station> AT = List.of(AT1, AT2);
    public final List<Station> IT = List.of(IT1, IT2, IT3);
    public final List<Station> FR = List.of(FR1, FR2);

    public final Ticket LAU_STG = new Ticket(LAU, STG, 13);
    public final Ticket LAU_BER = new Ticket(LAU, BER, 2);
    public final Ticket GEN_BER = new Ticket(GEN, BER, 8);
    public final Ticket GEN_SIO = new Ticket(GEN, SIO, 10);

    public final List<Ticket> fewTickets = List.of(LAU_STG, LAU_BER, GEN_BER, GEN_SIO);
    public final SortedBag<Ticket> someTickets = SortedBag.of(fewTickets);
    public final SortedBag<Ticket> allTickets = SortedBag.of(ChMap.tickets());

    public final List<Ticket> fewTickets1 = List.of(LAU_STG, LAU_BER);
    public final SortedBag<Ticket> someTickets1 = SortedBag.of(fewTickets1);

    public final List<Ticket> fewTickets2 = List.of(GEN_BER, GEN_SIO);
    public final SortedBag<Ticket> someTickets2 = SortedBag.of(fewTickets2);

    public final Ticket BER_NEIGHBORS = ticketToNeighbors(List.of(BER), 6, 11, 8, 5);
    public final Ticket FR_NEIGHBORS = ticketToNeighbors(FR, 5, 14, 11, 0);

    private Ticket ticketToNeighbors(List<Station> from, int de, int at, int it, int fr) {
        var trips = new ArrayList<Trip>();
        if (de != 0) trips.addAll(Trip.all(from, DE, de));
        if (at != 0) trips.addAll(Trip.all(from, AT, at));
        if (it != 0) trips.addAll(Trip.all(from, IT, it));
        if (fr != 0) trips.addAll(Trip.all(from, FR, fr));
        return new Ticket(trips);
    }
}