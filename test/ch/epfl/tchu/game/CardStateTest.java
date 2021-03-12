package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.Random;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardStateTest {


    public Deck<Card> deckTooSmallBuilder(){
        SortedBag<Card> cards0 = SortedBag.of(1, Card.BLACK, 1, Card.VIOLET);
        SortedBag<Card> cards1 = SortedBag.of(1, Card.BLUE, 1, Card.GREEN);
        SortedBag<Card> cards2 = SortedBag.of(0, Card.YELLOW, 0, Card.ORANGE);
        SortedBag<Card> cards3 = SortedBag.of(0, Card.RED, 0, Card.WHITE);
        SortedBag<Card> cards4 = SortedBag.of(0, Card.LOCOMOTIVE);
        SortedBag<Card> deckInBag =  cards0.union(cards1.union(cards2.union(cards3.union(cards4))));
        System.out.println(deckInBag);
        Deck<Card> deck = Deck.of(deckInBag, new Random());
        return deck;
    }

    public Deck<Card> deckExactBuilder(){
        SortedBag<Card> cards0 = SortedBag.of(1, Card.BLACK, 1, Card.VIOLET);
        SortedBag<Card> cards1 = SortedBag.of(1, Card.BLUE, 1, Card.GREEN);
        SortedBag<Card> cards2 = SortedBag.of(1, Card.YELLOW, 0, Card.ORANGE);
        SortedBag<Card> cards3 = SortedBag.of(0, Card.RED, 0, Card.WHITE);
        SortedBag<Card> cards4 = SortedBag.of(0, Card.LOCOMOTIVE);
        SortedBag<Card> deckInBag =  cards0.union(cards1.union(cards2.union(cards3.union(cards4))));
        System.out.println(deckInBag);
        Deck<Card> deck = Deck.of(deckInBag, new Random());
        return deck;
    }

    public Deck<Card> deckOkBuilder(){
        SortedBag<Card> cards0 = SortedBag.of(10, Card.BLACK, 10, Card.VIOLET);
        SortedBag<Card> cards1 = SortedBag.of(10, Card.BLUE, 10, Card.GREEN);
        SortedBag<Card> cards2 = SortedBag.of(10, Card.YELLOW, 10, Card.ORANGE);
        SortedBag<Card> cards3 = SortedBag.of(10, Card.RED, 10, Card.WHITE);
        SortedBag<Card> cards4 = SortedBag.of(10, Card.LOCOMOTIVE);
        SortedBag<Card> deckInBag =  cards0.union(cards1.union(cards2.union(cards3.union(cards4))));
        System.out.println(deckInBag);
        Deck<Card> deck = Deck.of(deckInBag, new Random());
        return deck;
    }

    @Test
    public void CardStateOfTestWithTooSmallDeck(){
        Deck<Card> deck = deckTooSmallBuilder();
        assertThrows(IllegalArgumentException.class, ()->{
            CardState test = CardState.of(deck);
        });

    }

    @Test
    public void CardStateOfTestWithNormalDeck(){
        Deck<Card> deck = deckExactBuilder();
        CardState test = CardState.of(deck);
        assertThrows(IllegalArgumentException.class, () ->{
            for (int i = 0; i < 5; i++) {
                test.withDrawnFaceUpCard(i);
            }
        });
        assertThrows(IndexOutOfBoundsException.class, () ->{
            test.withDrawnFaceUpCard(-1);
        });
        assertThrows(IndexOutOfBoundsException.class, () ->{
            test.withDrawnFaceUpCard(6);
        });
    }

    @Test
    public void withoutTopCardAFailsWithToSmallDeck(){
        Deck<Card> deck = deckExactBuilder();
        CardState cardState = CardState.of(deck);
        assertThrows(IllegalArgumentException.class, ()->{
            cardState.withoutTopDeckCard();
        });
    }

    @Test
    public void topDeckCardsWork(){
        Deck<Card> deck = deckExactBuilder();
        Deck<Card> deckOk = deckOkBuilder();
        CardState cardState = CardState.of(deck);
        CardState cardStateOk = CardState.of(deckOk);
        assertThrows(IllegalArgumentException.class, ()->{
            cardState.topDeckCard();
        });
//        assertEquals(Card.BLUE, cardStateOk.topDeckCard()); //TODO comment tester vu que c'est random ?
    }

    @Test
    public void withDeckRecreatedFailsWithEmptyDeck(){
        Deck<Card> deck = deckExactBuilder();
        CardState cardState = CardState.of(deck);
        assertThrows(IllegalArgumentException.class, ()->{
            cardState.withDeckRecreatedFromDiscards(new Random());
        });
    }

    // TODO comment testerr que les bonnes choses sont renvoyées
}
