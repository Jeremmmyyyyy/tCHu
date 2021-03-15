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
//            System.out.print(card + ", ");
//        }
//        System.out.println();
//        System.out.println("TopCard :c" + deck.topCard());
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
//        System.out.println("Deck normal :");
//        for (Card card : deck.cards) {
//            System.out.print(card + ", ");
//        }
//        System.out.println("\nDeckWithoutTopCard :");
//        for (Card card : deckWithoutTopCard.cards) {
//            System.out.print(card + ", ");
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
//        Deck<Card> deckWithout3TopCards = deck.withoutTopCards(3);
//        System.out.println("Deck de depart : ");
//        for (Card card : deck.cards) {
//            System.out.print(card + ", ");
//        }
//        System.out.println("\nDeckWithout3TopCards :");
//        for (Card card : deckWithout3TopCards.cards) {
//            System.out.print(card + ", ");
//        }
//    }
//
//    @Test
//    void withoutTopCardsWithFullInput() {
//        SortedBag<Card> cards = SortedBag.of(3, Card.YELLOW, 4, Card.BLUE);
//        Deck<Card> deck = Deck.of(cards, new Random(398474));
//        int l = deck.size();
//        Deck<Card> deckWithoutCards = deck.withoutTopCards(l);
//        System.out.println("Deck normal :");
//        for (Card card : deck.cards) {
//            System.out.print(card + ", ");
//        }
//        System.out.println("\nDeckWithoutAllCards :");
//        for (Card card : deckWithoutCards.cards) {
//            System.out.print(card + ", ");
//        }
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
//        System.out.println();
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
//
//    @Test
//    void hugeTest() {
//        System.out.println("==================== Huge test ====================");
//        SortedBag<Card> cards1 = SortedBag.of(3, Card.BLUE, 2, Card.LOCOMOTIVE);
//        SortedBag<Card> cards2 = SortedBag.of(1, Card.BLACK, 5, Card.BLUE);
//        SortedBag<Card> cards3 = SortedBag.of(1, Card.YELLOW, 1, Card.WHITE);
//        SortedBag<Card> cards4 = SortedBag.of(3, Card.ORANGE, 1, Card.RED);
//        SortedBag<Card> cards5 = SortedBag.of(2, Card.LOCOMOTIVE, 2, Card.GREEN);
//        Random rng = new Random();
//        SortedBag<Card> cards = cards1.union(cards2.union(cards3.union(cards4.union(cards5))));
//        Deck<Card> deck = Deck.of(cards, rng);
//        System.out.println("Initial deck : ");
//        for (Card card : deck.cards) {
//            System.out.print(card + ", ");
//        }
//        System.out.println("\nTop card : " + deck.topCard());
//        System.out.println("5 top cards : " + deck.topCards(5));
//        System.out.println("deck.size() top cards : " + deck.topCards(deck.size()));
//        System.out.println("0 top card : " + deck.topCards(0));
//
//        Deck<Card> deckWithoutTopCard = deck.withoutTopCard();
//        System.out.println("DeckWithoutTopCard : ");
//        for (Card card : deckWithoutTopCard.cards) {
//            System.out.print(card + ", ");
//        }
//        Deck<Card> deckWithout7TopCards = deck.withoutTopCards(7);
//        System.out.println("\nDeckWithout7TopCards : ");
//        for (Card card : deckWithout7TopCards.cards) {
//            System.out.print(card + ", ");
//        }
//        Deck<Card> deckWithout0TopCards = deck.withoutTopCards(0);
//        System.out.println("\nDeckWithout0TopCards : ");
//        for (Card card : deckWithout0TopCards.cards) {
//            System.out.print(card + ", ");
//        }
//        Deck<Card> deckWithoutAllTopCards = deck.withoutTopCards(deck.size());
//        System.out.println("\nDeckWithoutAllTopCards : ");
//        for (Card card : deckWithoutAllTopCards.cards) {
//            System.out.print(card + ", ");
//        }
//    }

}