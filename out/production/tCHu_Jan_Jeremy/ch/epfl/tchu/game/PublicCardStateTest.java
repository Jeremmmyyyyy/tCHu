package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ch.epfl.tchu.game.Color.ALL;
import static ch.epfl.tchu.game.Color.COUNT;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

class PublicCardStateTest {

    private final Random random = new Random();

    private final List<Card> cards = Card.CARS;
    private final SortedBag<Card> cardSortedBag = SortedBag.of(Card.ALL);


    @Test
    void worksNormally() {
        var cardList = cards.subList(0, 5);
        PublicCardState pcs1 = new PublicCardState(cardList, 2, 3);
        //System.out.println(pcs1.faceUpCards());

        assertThrows(IndexOutOfBoundsException.class, () -> {
            pcs1.faceUpCard(5);
        });
    }

    @Test
    void TotalSizeWorksNormally() {
        var cardList = cards.subList(0, 5);
        PublicCardState pcs1 = new PublicCardState(cardList, 2, 3);

        assertEquals(cardList.get(4), pcs1.faceUpCard(4));
    }



    @Test
    void faceUpCardWorksNormally() {
        var cardList = cards.subList(0, 5);
        PublicCardState pcs1 = new PublicCardState(cardList, 2, 3);

        assertEquals(10, pcs1.totalSize());
    }

    @Test
    void faceUpCardFailsWhenIndexNotValid() {
        var cardList = cards.subList(0, 5);
        PublicCardState pcs1 = new PublicCardState(cardList, 2, 3);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            pcs1.faceUpCard(5);
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            pcs1.faceUpCard(-1);
        });
    }







}