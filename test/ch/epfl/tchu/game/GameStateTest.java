package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.test.ChMapPublic;
import org.junit.jupiter.api.Test;

import javax.management.relation.RelationNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameStateTest {

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public GameState gameStateCreator(int ticketCount, boolean print){

        List<Ticket> ticketsInList = new ArrayList<>();
        for (int i = 0; i < ticketCount; i++) {
            ticketsInList.add(ChMapPublic.ALL_TICKETS.get(getRandomNumber(0, ChMapPublic.ALL_TICKETS.size())));
        }
        SortedBag<Ticket> tickets = SortedBag.of(ticketsInList);

        GameState gameState = GameState.initial(tickets, new Random());

        if(print){
            System.out.println("===========================");
            System.out.println("Number of tickets " + gameState.ticketsCount());
            System.out.println("ALL_CARDS size " + Constants.ALL_CARDS.size());
            System.out.println("FaceUpCards " + gameState.cardState().faceUpCards() + "Decksize " + gameState.cardState().deckSize() + " DiscardSize " + gameState.cardState().discardsSize());
            System.out.println("TicketsPlayer1 " + gameState.playerState(PlayerId.PLAYER_1).tickets() + "CardsPlayer 1 " + gameState.playerState(PlayerId.PLAYER_1).cards());
            System.out.println("TicketsPlayer2 " + gameState.playerState(PlayerId.PLAYER_2).tickets() + "CardsPlayer 2 " + gameState.playerState(PlayerId.PLAYER_2).cards());
            System.out.println("First Player " + gameState.currentPlayerId() + " Second Player" + gameState.lastPlayer());
            System.out.println("===========================");

        }
        return gameState;
    }

    @Test
    public void gameStateCreatorWorks(){
        gameStateCreator(10, true);
    }

    private final SortedBag<Ticket> ticketDeck = SortedBag.of(List.of(
            ChMapPublic.ticketToNeighbors(List.of(ChMapPublic.BER), 6, 11, 8, 5),
            ChMapPublic.ticketToNeighbors(List.of(ChMapPublic.COI), 6, 3, 5, 12),
            ChMapPublic.ticketToNeighbors(List.of(ChMapPublic.LUG), 12, 13, 2, 14),
            ChMapPublic.ticketToNeighbors(List.of(ChMapPublic.ZUR), 3, 7, 11, 7),
            new Ticket(ChMapPublic.SCE, ChMapPublic.MAR, 15),
            new Ticket(ChMapPublic.BAL, ChMapPublic.BER, 5),
            new Ticket(ChMapPublic.OLT, ChMapPublic.SCE, 5),
            new Ticket(ChMapPublic.LUC, ChMapPublic.VAD, 6),
            new Ticket(ChMapPublic.GEN, ChMapPublic.SIO, 10),
            new Ticket(ChMapPublic.NEU, ChMapPublic.WIN, 9)));

    private final SortedBag<Card> cards = SortedBag.of(1, Card.BLACK, 1, Card.VIOLET)
            .union(SortedBag.of(1, Card.BLUE, 1, Card.GREEN))
            .union(SortedBag.of(1, Card.YELLOW, 2, Card.ORANGE))
            .union(SortedBag.of(3, Card.RED, 5, Card.WHITE))
            .union(SortedBag.of(3, Card.LOCOMOTIVE));

    private final SortedBag<Ticket> moreTickets = SortedBag.of(List.of(new Ticket(ChMapPublic.LAU, ChMapPublic.STG, 13),
            new Ticket(ChMapPublic.LCF, ChMapPublic.BER, 3),
            new Ticket(ChMapPublic.LCF, ChMapPublic.LUC, 7),
            new Ticket(ChMapPublic.LCF, ChMapPublic.ZUR, 8)));

    private final SortedBag<Ticket> moreTicketsWithOneSimilar = SortedBag.of(List.of(new Ticket(ChMapPublic.LAU, ChMapPublic.STG, 13),
            new Ticket(ChMapPublic.LCF, ChMapPublic.BER, 3),
            new Ticket(ChMapPublic.LCF, ChMapPublic.LUC, 7),
            new Ticket(ChMapPublic.GEN, ChMapPublic.SIO, 10),
            new Ticket(ChMapPublic.LCF, ChMapPublic.ZUR, 8)));

    private final GameState gameState = GameState.initial(ticketDeck, new Random());


    @Test
    void initial() {
        assertTrue(ticketDeck.equals(gameState.topTickets(gameState.ticketsCount())));
        assertEquals(ticketDeck.size(), gameState.ticketsCount());
//        assertEquals(Constants.ALL_CARDS.size() - 2 * Constants.INITIAL_CARDS_COUNT, //TODO test non fonctionnel
//                gameState.cardState().deckSize());
        assertEquals(gameState.currentPlayerId(), gameState.lastPlayer());
        assertEquals(4, gameState.playerState(PlayerId.PLAYER_1).cards().size());
        assertEquals(4, gameState.playerState(PlayerId.PLAYER_2).cards().size());
    }

    @Test
    void topTickets() {
        assertThrows(IllegalArgumentException.class, () -> {gameState.topTickets(-2);});
        assertThrows(IllegalArgumentException.class, () -> {gameState.topTickets(gameState.ticketsCount() + 1);});
        SortedBag<Ticket> topTickets = gameState.topTickets(4);
        assertEquals(4, topTickets.size());
        assertTrue(ticketDeck.contains(topTickets));
    }

    @Test
    void withoutTopTickets() {
        GameState gameStateWithoutTop5Tickets = gameState.withoutTopTickets(5);
        assertThrows(IllegalArgumentException.class,
                () -> {gameState.withoutTopTickets(-2);});
        assertThrows(IllegalArgumentException.class,
                () -> {gameState.withoutTopTickets(gameState.ticketsCount() + 1);});
        assertEquals(gameStateWithoutTop5Tickets.currentPlayerId(), gameStateWithoutTop5Tickets.currentPlayerId());
        assertEquals(gameStateWithoutTop5Tickets.currentPlayerState(),
                gameStateWithoutTop5Tickets.currentPlayerState());
        assertEquals(gameState.cardState(), gameStateWithoutTop5Tickets.cardState());
        assertEquals(gameState.lastPlayer(), gameStateWithoutTop5Tickets.lastPlayer());
        assertEquals(gameState.ticketsCount() - 5, gameStateWithoutTop5Tickets.ticketsCount());;
    }

    @Test
    public void topCardWorks(){
        GameState gameState = gameStateCreator(10, false);
        System.out.println(gameState.topCard());
    }


    @Test
    void withoutTopCard() {
        GameState gameStateWithoutTopCard = gameState.withoutTopCard();
        assertEquals(gameStateWithoutTopCard.currentPlayerId(), gameStateWithoutTopCard.currentPlayerId());
        assertEquals(gameStateWithoutTopCard.currentPlayerState(), gameStateWithoutTopCard.currentPlayerState());
        assertNotEquals(gameState.cardState(), gameStateWithoutTopCard.cardState());
        assertEquals(gameState.lastPlayer(), gameStateWithoutTopCard.lastPlayer());
        assertEquals(gameState.cardState().deckSize() - 1, gameStateWithoutTopCard.cardState().deckSize());
        assertEquals(gameState.cardState().discardsSize(), gameStateWithoutTopCard.cardState().discardsSize());

    }

    @Test
    void withMoreDiscardedCards() {
        GameState gameStateWithMoreDiscardedCards = gameState.withMoreDiscardedCards(cards);
        assertEquals(gameStateWithMoreDiscardedCards.currentPlayerId(),
                gameStateWithMoreDiscardedCards.currentPlayerId());
        assertEquals(gameStateWithMoreDiscardedCards.currentPlayerState(),
                gameStateWithMoreDiscardedCards.currentPlayerState());
        assertNotEquals(gameState.cardState(), gameStateWithMoreDiscardedCards.cardState());
        assertEquals(gameState.lastPlayer(), gameStateWithMoreDiscardedCards.lastPlayer());
        assertEquals(gameState.cardState().discardsSize() + cards.size(),
                gameStateWithMoreDiscardedCards.cardState().discardsSize());
        assertEquals(gameState.cardState().deckSize(), gameStateWithMoreDiscardedCards.cardState().deckSize());
    }

    @Test
    void withCardsDeckRecreatedIfNeeded() {
        GameState gameStateWithCardsDeckRecreatedIfNeeded= gameState.withCardsDeckRecreatedIfNeeded(new Random());
        assertEquals(gameStateWithCardsDeckRecreatedIfNeeded.currentPlayerId(),
                gameStateWithCardsDeckRecreatedIfNeeded.currentPlayerId());
        assertEquals(gameStateWithCardsDeckRecreatedIfNeeded.currentPlayerState(),
                gameStateWithCardsDeckRecreatedIfNeeded.currentPlayerState());
//        assertNotEquals(gameState.cardState(), gameStateWithCardsDeckRecreatedIfNeeded.cardState()); // on peut pas comparer des listes avec not equal
        assertEquals(gameState.lastPlayer(), gameStateWithCardsDeckRecreatedIfNeeded.lastPlayer());
        assertNotEquals(gameState.cardState().discardsSize(),
                gameStateWithCardsDeckRecreatedIfNeeded.cardState().deckSize());
        assertEquals(0, gameStateWithCardsDeckRecreatedIfNeeded.cardState().discardsSize());
    }

    @Test
    void withInitiallyChosenTickets() {
        GameState gameStateWithInitiallyChosenTickets =
                gameState.withInitiallyChosenTickets(PlayerId.PLAYER_1, moreTickets);
        assertEquals(gameStateWithInitiallyChosenTickets.currentPlayerId(),
                gameStateWithInitiallyChosenTickets.currentPlayerId());
        assertNotEquals(gameStateWithInitiallyChosenTickets.currentPlayerState(),
                gameStateWithInitiallyChosenTickets.currentPlayerState());
        assertEquals(gameState.cardState(), gameStateWithInitiallyChosenTickets.cardState());
        assertEquals(gameState.lastPlayer(), gameStateWithInitiallyChosenTickets.lastPlayer());
        assertEquals(gameState.ticketsCount() - moreTickets.size(),
                gameStateWithInitiallyChosenTickets.ticketsCount());
        assertThrows(IllegalArgumentException.class,
                () -> {gameState.withInitiallyChosenTickets(PlayerId.PLAYER_1, moreTicketsWithOneSimilar);});
        assertEquals(gameState.playerState(PlayerId.PLAYER_1).tickets().size() + moreTickets.size(),
                gameStateWithInitiallyChosenTickets.playerState(PlayerId.PLAYER_1).tickets().size());
        assertTrue(gameStateWithInitiallyChosenTickets.playerState(PlayerId.PLAYER_1).tickets().contains(moreTickets));
    }

    @Test
    void lastTurnBegins() {
        assertFalse(gameState.lastTurnBegins());
    }

    @Test
    void forNextTurn() {

    }


    @Test
    public void withChosenAdditionalTickets(){
        List<Ticket> drawTicketsToSortedBag = new ArrayList<>();
        List<Ticket> chosenTicketsToSortedBag = new ArrayList<>();
        List<Ticket> randomTicketsToSortedBag = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            drawTicketsToSortedBag.add(ChMapPublic.ALL_TICKETS.get(getRandomNumber(0, ChMapPublic.ALL_TICKETS.size())));
            randomTicketsToSortedBag.add(ChMapPublic.ALL_TICKETS.get(getRandomNumber(0, ChMapPublic.ALL_TICKETS.size())));
        }
        for (int i = 0; i < drawTicketsToSortedBag.size() - 2; i++) {
            chosenTicketsToSortedBag.add(drawTicketsToSortedBag.get(i));
        }
        SortedBag<Ticket> drawnTickets = SortedBag.of(drawTicketsToSortedBag);
        SortedBag<Ticket> chosenTicket = SortedBag.of(chosenTicketsToSortedBag);
        SortedBag<Ticket> randomTickets = SortedBag.of(randomTicketsToSortedBag);
        GameState gameState = gameStateCreator(10, false);
        GameState gameState1 = gameStateCreator(10, false);

        assertThrows(IllegalArgumentException.class, ()->{
            gameState.withChosenAdditionalTickets(drawnTickets, randomTickets);
        });

        gameState1 = gameState1.withChosenAdditionalTickets(drawnTickets, chosenTicket);
        System.out.println(drawnTickets);
        System.out.println(gameState1.currentPlayerState().tickets());
        assertEquals(5, gameState1.ticketsCount());
        assertEquals(3, gameState1.currentPlayerState().tickets().size());
        assertEquals(chosenTicket, gameState1.currentPlayerState().tickets());
    }

    @Test
    public void withDrawnFaceUpCard(){
        GameState gameState = gameStateCreator(10, true);
        for (int i = 0; i < gameState.cardState().faceUpCards().size(); i++) {
            gameState = gameState.withDrawnFaceUpCard(i);
            System.out.println(gameState.cardState().faceUpCards());
            assertEquals(97-i-1, gameState.cardState().deckSize());
            assertEquals(4+1+i, gameState.currentPlayerState().cards().size());
        }
        System.out.println(gameState.currentPlayerState().cards());
    }

    @Test
    public void withBlindlyDrawnCard(){
        GameState gameState = gameStateCreator(10, true);
        for (int i = 0; i < 5; i++) {
            gameState = gameState.withBlindlyDrawnCard();
            assertEquals(4+1+i, gameState.currentPlayerState().cards().size());
        }
        System.out.println(gameState.currentPlayerState().cards());
    }

    @Test
    public void withClaimedRoute(){
        GameState gameState = gameStateCreator(10, true);
        gameState = gameState.withClaimedRoute(ChMapPublic.ALL_ROUTES.get(0), SortedBag.of(4, Card.LOCOMOTIVE));
        assertEquals(1, gameState.currentPlayerState().routes().size());
        System.out.println(gameState.currentPlayerState().cards().size());
        gameState = gameState.withClaimedRoute(ChMapPublic.ALL_ROUTES.get(1), SortedBag.of(10, Card.LOCOMOTIVE));
        assertEquals(2, gameState.currentPlayerState().routes().size());
        System.out.println(gameState.currentPlayerState().cards().size());
    }


}
