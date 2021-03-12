package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

//    @Test
//    void ofAndSizeCheck() {
//        SortedBag<Card> cards = SortedBag.of(2, Card.ORANGE, 3, Card.LOCOMOTIVE);
//        Random rng = new Random(343497398);
//        Deck<Card> deck1 = Deck.of(cards, rng);
//        Deck<Card> deck2 = Deck.of(cards,rng);
//        assertFalse(deck1.equals(deck2));
//        assertEquals(5, deck1.size());
//        assertEquals(5, deck2.size());
//        for (Card card : deck1.cards) {
//            System.out.println(card);
//        }
//        System.out.println();
//        for (Card card : deck2.cards) {
//            System.out.println(card + ", ");
//        }
//    }
//
//    @Test
//    void isEmptyCheck() {
//        SortedBag<Card> cards = SortedBag.of();
//        Random rng = new Random(343497398);
//        Deck<Card> deck = Deck.of(cards, rng);
//        assertTrue(deck.isEmpty());
//    }
//
//    @Test
//    void topCard() {
//        SortedBag<Card> cards = SortedBag.of(2, Card.BLUE, 3, Card.GREEN);
//        Random rng = new Random(24973458);
//        Deck<Card> deck = Deck.of(cards, rng);
//        for (Card card : deck.cards) {
//            System.out.println(card + ", ");
//        }
//        System.out.println();
//        System.out.println(deck.topCard());
//    }
//
//    @Test
//    void topCardFailsWithEmptyList() {
//        SortedBag<Card> emptyCards = SortedBag.of();
//        Random rng = new Random(3987);
//        Deck<Card> deck = Deck.of(emptyCards, rng);
//        assertThrows(IllegalArgumentException.class, () -> {deck.topCard();});
//    }
//
//    @Test
//    void withoutTopCard() {
//        SortedBag<Card> cards = SortedBag.of(3, Card.YELLOW, 4, Card.BLUE);
//        Deck<Card> deck = Deck.of(cards, new Random(398474));
//        int l = deck.size() - 1;
//        Deck<Card> deckWithoutTopCard = deck.withoutTopCard();
//        deck.cards.remove(l);
//        for (int i = 0; i < deck.size(); ++i) {
//            assertEquals(deck.cards.get(i), deckWithoutTopCard.cards.get(i));
//        }
//    }
//
//    @Test
//    void withoutTopCardFailsWithEmptyDeck() {
//        SortedBag<Card> cards = SortedBag.of();
//        Deck<Card> deck = Deck.of(cards, new Random(398474));
//        assertThrows(IllegalArgumentException.class, () -> {deck.withoutTopCard();});
//    }
//
//    @Test
//    void withoutTopCardsWith0AsInput() {
//        SortedBag<Card> cards = SortedBag.of(3, Card.YELLOW, 4, Card.BLUE);
//        Deck<Card> deck = Deck.of(cards, new Random(398474));
//        Deck<Card> deckWithoutTopCards = deck.withoutTopCards(0);
//        for (int i = 0; i < deck.size(); ++i) {
//            assertEquals(deck.cards.get(i), deckWithoutTopCards.cards.get(i));
//        }
//    }
//
//    @Test
//    void withoutTopCards() {
//        SortedBag<Card> cards = SortedBag.of(3, Card.YELLOW, 4, Card.BLUE);
//        Deck<Card> deck = Deck.of(cards, new Random(398474));
//        int l = deck.cards.size();
//        Deck<Card> deckWithoutTopCards = deck.withoutTopCards(3);
//        for (int i = 1; i <= 3; ++i) {
//            deck.cards.remove(l - i);
//        }
//        for (int i = 0; i < deck.size(); ++i) {
//            assertEquals(deck.cards.get(i), deckWithoutTopCards.cards.get(i));
//        }
//    }
//
//    @Test
//    void withoutTopCardsWithFullInput() {
//        SortedBag<Card> cards = SortedBag.of(3, Card.YELLOW, 4, Card.BLUE);
//        Deck<Card> deck = Deck.of(cards, new Random(398474));
//        int l = deck.size();
//        Deck<Card> deckWithoutTopCards = deck.withoutTopCards(l);
//        deck.cards.clear();
//        assertTrue(deck.isEmpty());
//        for (Card card : deckWithoutTopCards.cards) {
//            System.out.print(card + "/ ");
//        }
//        assertEquals(0, deckWithoutTopCards.size());
//    }
//
//    @Test
//    void withoutTopCardsFailsWithOutOfBoundInput() {
//        SortedBag<Card> cards = SortedBag.of();
//        Deck<Card> deck = Deck.of(cards, new Random(398474));
//        assertThrows(IllegalArgumentException.class, () -> {deck.withoutTopCards(-1);});
//        assertThrows(IllegalArgumentException.class, () -> {deck.withoutTopCards(deck.size() + 1);});
//    }
//
//    @Test
//    void topCards() {
//        SortedBag<Card> cards = SortedBag.of(3, Card.YELLOW, 4, Card.BLUE);
//        Deck<Card> deck = Deck.of(cards, new Random(398474));
//        for (Card card : deck.cards) {
//            System.out.print(card + ", ");
//        }
//        System.out.println("============");
//        SortedBag<Card> topCards = deck.topCards(3);
//        for (Card card : topCards) {
//            System.out.print(card + ", ");
//        }
//    }
//
//    @Test
//    void topCardsFailsWithOutOfBoundAndWorksWithEdgeInput() {
//        SortedBag<Card> emptyCards = SortedBag.of(3, Card.YELLOW, 4, Card.BLUE);
//        Random rng = new Random(3987);
//        Deck<Card> deck = Deck.of(emptyCards, rng);
//        assertThrows(IllegalArgumentException.class, () -> {deck.topCards(-1);});
//        assertThrows(IllegalArgumentException.class, () -> {deck.topCards(deck.size() + 1);});
//        SortedBag<Card> sameCards = deck.topCards(deck.size());
//        assertEquals(deck.size(), sameCards.size());
//        SortedBag<Card> noCards = deck.topCards(0);
//        assertTrue(noCards.isEmpty());
//    }


}