package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CardStateTest {

    private final Random random = new Random();

    private final List<Card> cards = Card.CARS;
    private final SortedBag<Card> cardSortedBag = SortedBag.of(Card.ALL);

    @Test
    void withDrawnFaceUpCardworksNormally() {
        var faceUpCardList = cards.subList(0, 5);
        var s = SortedBag.of(cards);
        //System.out.println(cards);
        var initialDeck = Deck.of(s, random);
        var initialCardState = CardState.of(initialDeck);
        //System.out.println("Initial FaceUpCards : " + initialCardState.faceUpCards());
        var secondCardState = initialCardState.withDrawnFaceUpCard(4);
        //System.out.println("SecondFaceUpCard : " + secondCardState.faceUpCards());
        assertEquals(initialCardState.topDeckCard(), secondCardState.faceUpCard(4)); //Marche bien
    }


    @Test
    void TotalSizeWorksNormally() {
        var cardList = cards.subList(0, 5);
        PublicCardState pcs1 = new PublicCardState(cardList, 2, 3);

        assertEquals(cardList.get(4), pcs1.faceUpCard(4));
    }

    @Test
    void of() {
    }

    @Test
    void withDrownfaceUpCard() {
    }

    @Test
    void topDeckCard() {
    }

    @Test
    void withoutTopDeckCard() {
    }

    @Test
    void withDeckRecreatedFromDiscards() {
    }

    @Test
    void withMoreDiscardedCards() {
    }
}