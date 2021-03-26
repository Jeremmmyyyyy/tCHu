package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PlayerStateTest {
    private final Random random = new Random();

    private final List<Card> cards = Card.CARS;
    private final SortedBag<Card> cardSortedBag = SortedBag.of(Card.ALL);



    @Test
    void ticketPointsWorks() {
        SortedBag.Builder<Card> playerCardsB = new SortedBag.Builder<>();
        playerCardsB.add(3, Card.GREEN);
        playerCardsB.add(1, Card.BLUE);
        SortedBag<Card> playerCards = playerCardsB.build();

        var tickets = SortedBag.of(LAU_STG);

        List<Route> routes = List.of(
                getRoute("BAD_OLT_1"), getRoute("LAU_NEU_1"),
                getRoute("NEU_SOL_1"), getRoute("OLT_SOL_1"),
                getRoute("BAD_ZUR_1"), getRoute("STG_ZUR_1"));

        var playerState = new PlayerState(tickets, playerCards, routes);

        //assertEquals(13, playerState.ticketPoints());

    }


    @Test
    void initialWorks() {
        SortedBag.Builder<Card> playerCardsB = new SortedBag.Builder<>();
        playerCardsB.add(3, Card.GREEN);
        playerCardsB.add(1, Card.BLUE);
        SortedBag<Card> playerCards = playerCardsB.build();

        PlayerState playerState = PlayerState.initial(playerCards);

        assertEquals(4, playerState.cardCount());
    }

    @Test
    void initialFailsWithWrongCards() {

        SortedBag.Builder<Card> playerCardsB = new SortedBag.Builder<>();
        playerCardsB.add(3, Card.GREEN);
        playerCardsB.add(2, Card.BLUE);
        playerCardsB.add(2, Card.LOCOMOTIVE);
        SortedBag<Card> playerCards = playerCardsB.build();


        assertThrows(IllegalArgumentException.class, () -> {
            PlayerState p = PlayerState.initial(playerCards);
        });
    }



    @Test
    void BasicMethodsWork() {
        SortedBag.Builder<Card> playerCardsB = new SortedBag.Builder<>();
        playerCardsB.add(3, Card.GREEN);
        playerCardsB.add(1, Card.BLUE);
        SortedBag<Card> playerCards = playerCardsB.build();

        var p1 = PlayerState.initial(playerCards);
        var p2 = p1.withAddedTickets(SortedBag.of(LAU_STG));

        assertEquals(SortedBag.of(LAU_STG), p2.tickets());
        //assertEquals(-13, p2.ticketPoints());

        var p3 = p2.withAddedTickets(SortedBag.of(LAU_BER));

        assertEquals(SortedBag.of(1, LAU_STG, 1, LAU_BER), p3.tickets());
        //assertEquals(15, p2.ticketPoints());

        var addedCards = SortedBag.of(1, GEN_BER, 1, GEN_SIO);
        var p4  = p3.withAddedTickets(addedCards);  //Ici on a

        assertEquals(SortedBag.of(List.of(LAU_STG, LAU_BER, GEN_BER, GEN_SIO)), p4.tickets());
        //assertEquals(15, p2.ticketPoints());

    }


    @Test
    void possibleAdditionalCardsFailsWithWrongAdditionalCardsCount() {
        SortedBag.Builder<Card> playerCardsB = new SortedBag.Builder<>();
        playerCardsB.add(3, Card.GREEN);
        playerCardsB.add(2, Card.BLUE);
        playerCardsB.add(2, Card.LOCOMOTIVE);
        SortedBag<Card> playerCards = playerCardsB.build();

        SortedBag.Builder<Card> drawnCardsB = new SortedBag.Builder<>();
        drawnCardsB.add(1, Card.GREEN);
        drawnCardsB.add(1, Card.LOCOMOTIVE);
        drawnCardsB.add(1, Card.RED);
        SortedBag<Card> drawnCards = playerCardsB.build();

        PlayerState playerState = new PlayerState(SortedBag.of(), playerCards, List.of());

        int add1 = 0;
        int add2 = 4;

        SortedBag<Card> initialCards = SortedBag.of(Card.GREEN);

        assertThrows(IllegalArgumentException.class, () -> {
            playerState.possibleAdditionalCards(add1, initialCards, drawnCards);;
        });

        assertThrows(IllegalArgumentException.class, () -> {
            playerState.possibleAdditionalCards(add2, initialCards, drawnCards);;
        });
    }

    @Test
    void possibleAdditionalCardsFailsWithEmptyInitialCards() {
        SortedBag.Builder<Card> playerCardsB = new SortedBag.Builder<>();
        playerCardsB.add(3, Card.GREEN);
        playerCardsB.add(2, Card.BLUE);
        playerCardsB.add(2, Card.LOCOMOTIVE);
        SortedBag<Card> playerCards = playerCardsB.build();

        SortedBag.Builder<Card> drawnCardsB = new SortedBag.Builder<>();
        drawnCardsB.add(1, Card.GREEN);
        drawnCardsB.add(1, Card.LOCOMOTIVE);
        drawnCardsB.add(1, Card.RED);
        SortedBag<Card> drawnCards = drawnCardsB.build();

        PlayerState playerState = new PlayerState(SortedBag.of(), playerCards, List.of());

        int add1 = 2;

        SortedBag<Card> initialCards = SortedBag.of();

        assertThrows(IllegalArgumentException.class, () -> {
            playerState.possibleAdditionalCards(add1, initialCards, drawnCards);;
        });
    }

    @Test
    void possibleAdditionalCardsFailsWithMoreCardTypes() {
        SortedBag.Builder<Card> playerCardsB = new SortedBag.Builder<>();
        playerCardsB.add(3, Card.GREEN);
        playerCardsB.add(2, Card.BLUE);
        playerCardsB.add(2, Card.LOCOMOTIVE);
        SortedBag<Card> playerCards = playerCardsB.build();

        SortedBag.Builder<Card> drawnCardsB = new SortedBag.Builder<>();
        drawnCardsB.add(1, Card.GREEN);
        drawnCardsB.add(1, Card.LOCOMOTIVE);
        drawnCardsB.add(1, Card.RED);
        SortedBag<Card> drawnCards = drawnCardsB.build();

        PlayerState playerState = new PlayerState(SortedBag.of(), playerCards, List.of());

        int add1 = 2;

        SortedBag.Builder<Card> initialCardsB = new SortedBag.Builder<>();
        initialCardsB.add(1, Card.GREEN);
        initialCardsB.add(1, Card.LOCOMOTIVE);
        initialCardsB.add(1, Card.RED);
        SortedBag<Card> initialCards = initialCardsB.build();

        assertThrows(IllegalArgumentException.class, () -> {
            playerState.possibleAdditionalCards(add1, initialCards, drawnCards);;
        });
    }

    @Test
    void possibleAdditionalCardsFailsWithWrongDrawnCards() {
        SortedBag.Builder<Card> playerCardsB = new SortedBag.Builder<>();
        playerCardsB.add(3, Card.GREEN);
        playerCardsB.add(2, Card.BLUE);
        playerCardsB.add(2, Card.LOCOMOTIVE);
        SortedBag<Card> playerCards = playerCardsB.build();

        SortedBag.Builder<Card> drawnCardsB = new SortedBag.Builder<>();
        drawnCardsB.add(2, Card.GREEN);
        drawnCardsB.add(2, Card.LOCOMOTIVE);
        SortedBag<Card> drawnCards = drawnCardsB.build();

        PlayerState playerState = new PlayerState(SortedBag.of(), playerCards, List.of());

        int add1 = 2;

        SortedBag.Builder<Card> initialCardsB = new SortedBag.Builder<>();
        initialCardsB.add(1, Card.GREEN);
        SortedBag<Card> initialCards = initialCardsB.build();

        System.out.print("Taille" + drawnCards.size());

        assertThrows(IllegalArgumentException.class, () -> {
            playerState.possibleAdditionalCards(add1, initialCards, drawnCards);;
        });
    }



    @Test
    void possibleAdditionalCarsWorksOnGivenExample() {
        SortedBag.Builder<Card> playerCardsB = new SortedBag.Builder<>();
        playerCardsB.add(3, Card.GREEN);
        playerCardsB.add(2, Card.BLUE);
        playerCardsB.add(2, Card.LOCOMOTIVE);
        SortedBag<Card> playerCards = playerCardsB.build();

        SortedBag.Builder<Card> drawnCardsB = new SortedBag.Builder<>();
        drawnCardsB.add(1, Card.GREEN);
        drawnCardsB.add(1, Card.LOCOMOTIVE);
        drawnCardsB.add(1, Card.RED);
        SortedBag<Card> drawnCards = drawnCardsB.build();

        PlayerState playerState = new PlayerState(SortedBag.of(), playerCards, List.of());

        int add = 2;

        SortedBag<Card> initialCards = SortedBag.of(Card.GREEN);

        List<SortedBag<Card>> value = playerState.possibleAdditionalCards(add, initialCards, drawnCards);

        List<SortedBag<Card>> expected = new ArrayList<>();
        expected.add(SortedBag.of(2, Card.GREEN));
        expected.add(SortedBag.of(1, Card.GREEN, 1, Card.LOCOMOTIVE));
        expected.add(SortedBag.of(2, Card.LOCOMOTIVE));

        assertEquals(expected, value);
    }

    @Test
    void possibleAdditionalCarsWorksReturningEmptyList() {
        SortedBag.Builder<Card> playerCardsB = new SortedBag.Builder<>();
        playerCardsB.add(3, Card.GREEN);
        playerCardsB.add(2, Card.BLUE);
        playerCardsB.add(2, Card.LOCOMOTIVE);
        playerCardsB.add(4, Card.ORANGE);
        SortedBag<Card> playerCards = playerCardsB.build();

        SortedBag.Builder<Card> drawnCardsB = new SortedBag.Builder<>();
        drawnCardsB.add(1, Card.GREEN);
        drawnCardsB.add(2, Card.LOCOMOTIVE);
        SortedBag<Card> drawnCards = drawnCardsB.build();

        PlayerState playerState = new PlayerState(SortedBag.of(), playerCards, List.of());

        int add = 2;

        SortedBag.Builder<Card> initialCardsB = new SortedBag.Builder<>();
        initialCardsB.add(3, Card.GREEN);
        initialCardsB.add(2, Card.LOCOMOTIVE);
        SortedBag<Card> initialCards = initialCardsB.build();

        System.out.println(initialCards);

        List<SortedBag<Card>> value = playerState.possibleAdditionalCards(add, initialCards, drawnCards);

        List<SortedBag<Card>> expected = List.of();

        assertEquals(expected, value);
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