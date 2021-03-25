package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.test.ChMapPublic;
import org.junit.jupiter.api.Test;

import javax.management.relation.RelationNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Random;

public class GameStateTest {

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
    void withoutTopCard() {
        GameState gameStateWithoutTopCard = gameState.withoutTopCard();
        assertEquals(gameStateWithoutTopCard.currentPlayerId(), gameStateWithoutTopCard.currentPlayerId());
        assertEquals(gameStateWithoutTopCard.currentPlayerState(), gameStateWithoutTopCard.currentPlayerState());
        assertNotEquals(gameState.cardState(), gameStateWithoutTopCard.cardState());
        assertEquals(gameState.lastPlayer(), gameStateWithoutTopCard.lastPlayer());
        assertEquals(gameState.cardState().deckSize() - 1, gameStateWithoutTopCard.cardState().deckSize());
        assertEquals(gameState.cardState().discardsSize(), gameStateWithoutTopCard.cardState().deckSize());

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
                gameState.cardState().discardsSize());
        assertEquals(gameState.cardState().deckSize(), gameStateWithMoreDiscardedCards.cardState().deckSize());
    }

    @Test
    void withCardsDeckRecreatedIfNeeded() {
        GameState gameStateWithCardsDeckRecreatedIfNeeded= gameState.withCardsDeckRecreatedIfNeeded(new Random());
        assertEquals(gameStateWithCardsDeckRecreatedIfNeeded.currentPlayerId(),
                gameStateWithCardsDeckRecreatedIfNeeded.currentPlayerId());
        assertEquals(gameStateWithCardsDeckRecreatedIfNeeded.currentPlayerState(),
                gameStateWithCardsDeckRecreatedIfNeeded.currentPlayerState());
        assertNotEquals(gameState.cardState(), gameStateWithCardsDeckRecreatedIfNeeded.cardState());
        assertEquals(gameState.lastPlayer(), gameStateWithCardsDeckRecreatedIfNeeded.lastPlayer());
        assertEquals(gameState.cardState().discardsSize(),
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



}
