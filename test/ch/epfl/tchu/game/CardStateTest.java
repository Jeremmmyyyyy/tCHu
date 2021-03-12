package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    public void CardStateOfWorksWithNormalDeck(){
        Deck<Card> deck = deckOkBuilder();
        CardState test = CardState.of(deck);
        System.out.println(test.faceUpCards());
        assertEquals(5, test.faceUpCards().size());
        assertEquals(85, test.deckSize());
        assertEquals(0, test.discardsSize());
    }

    @Test
    public void CardStateOfAndDrawnTestWithExactDeck(){
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
        assertEquals(5, test.faceUpCards().size());
        assertEquals(0, test.deckSize());
        assertEquals(0, test.discardsSize());
    }

    @Test
    public void withDrawnFaceUpCard(){  //Teste aussi topDeckCard par la même occasion
        Deck<Card> deck = deckOkBuilder();
        CardState test = CardState.of(deck);
        for (int i = 0; i < 5; i++) {
            Card topCard = test.topDeckCard();
            test = test.withDrawnFaceUpCard(i);
            assertEquals(topCard, test.faceUpCards().get(i));
            System.out.println(test.faceUpCards() + " " + topCard);
        }
    }

    @Test
    public void withoutTopDeckCardFailsWithToSmallDeck(){
        Deck<Card> deck = deckExactBuilder();
        CardState cardState = CardState.of(deck);
        assertThrows(IllegalArgumentException.class, ()->{
            cardState.withoutTopDeckCard();
        });
    }

    @Test
    public void withoutTopDeckCardWorksWell(){
        Deck<Card> deck = deckOkBuilder();
        CardState cardState = CardState.of(deck);
        cardState = cardState.withoutTopDeckCard();
        assertEquals(5, cardState.faceUpCards().size());
        assertEquals(84, cardState.deckSize());
        assertEquals(0, cardState.discardsSize());
    }



    @Test
    public void topDeckCardsFailsWithEmptyDeck(){
        Deck<Card> deck = deckExactBuilder();
        CardState cardState = CardState.of(deck);
        assertThrows(IllegalArgumentException.class, ()->{
            cardState.topDeckCard();
        });
    }

    @Test
    public void withDeckRecreatedFailsWithEmptyDeck(){
        Deck<Card> deck = deckExactBuilder();
        CardState cardState = CardState.of(deck);
        assertThrows(IllegalArgumentException.class, ()->{
            cardState.withDeckRecreatedFromDiscards(new Random());
        });
    }

    @Test
    public void withMoreDiscardedCardsWorksWell(){
        Deck<Card> deck = deckOkBuilder();
        CardState cardState = CardState.of(deck);
        SortedBag<Card> cards0 = SortedBag.of(10, Card.BLACK, 10, Card.VIOLET);
        SortedBag<Card> cards1 = SortedBag.of(10, Card.BLUE, 10, Card.GREEN);
        SortedBag<Card> cards2 = SortedBag.of(10, Card.YELLOW, 10, Card.ORANGE);
        SortedBag<Card> cards3 = SortedBag.of(10, Card.RED, 10, Card.WHITE);
        SortedBag<Card> cards4 = SortedBag.of(10, Card.LOCOMOTIVE);
        SortedBag<Card> deckInBag =  cards0.union(cards1.union(cards2.union(cards3.union(cards4))));

        cardState =  cardState.withMoreDiscardedCards(deckInBag);
        System.out.println(deckInBag +" "+ deckInBag.size());
        assertEquals(5, cardState.faceUpCards().size());
        assertEquals(85, cardState.deckSize());
        assertEquals(90, cardState.discardsSize());

//        Deck<Card> deck1 = deckExactBuilder();
//        CardState cardState1 = CardState.of(deck1);
//        cardState1 = cardState1.withMoreDiscardedCards(deckInBag);
//        assertEquals(5, cardState1.faceUpCards().size());
//        assertEquals(0, cardState1.deckSize());
//        assertEquals(90, cardState1.discardsSize());

//        cardState1 = cardState1.withDeckRecreatedFromDiscards(new Random());
//        assertEquals(5, cardState1.faceUpCards().size());
//        assertEquals(90, cardState1.deckSize());
//        assertEquals(0, cardState1.discardsSize());

    }


}
