package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class PublicCardStateTest {

    List<Card> faceUpCards6 = List.of(Card.BLUE, Card.RED, Card.BLACK, Card.LOCOMOTIVE, Card.ORANGE, Card.BLUE);
    List<Card> faceUpCards5 = List.of(Card.BLUE, Card.RED, Card.ORANGE, Card.LOCOMOTIVE, Card.ORANGE);
//    List<Card> faceUpCardsProblem = List.of(null, Card.RED, Card.ORANGE, Card.LOCOMOTIVE, Card.ORANGE);
    List<Card> faceUpCards4 = List.of(Card.BLUE, Card.RED, Card.BLACK, Card.LOCOMOTIVE);
    List<Card> faceUpCardsEmpty = List.of();

    @Test
    public void constructorFailsWithWrongArguments(){
        assertThrows(IllegalArgumentException.class, () ->{
            PublicCardState test1 = new PublicCardState(faceUpCards4, 5,5);
        });
        assertThrows(IllegalArgumentException.class, () ->{
            PublicCardState test1 = new PublicCardState(faceUpCards6, 5,5);
        });
        assertThrows(IllegalArgumentException.class, () ->{
            PublicCardState test1 = new PublicCardState(faceUpCardsEmpty, 5,5);
        });
        assertThrows(IllegalArgumentException.class, () ->{
            PublicCardState test1 = new PublicCardState(faceUpCards5, 0,0);
        });
        assertThrows(IllegalArgumentException.class, () ->{
            PublicCardState test1 = new PublicCardState(faceUpCards5, -5,-5);
        });
//        assertThrows(IllegalArgumentException.class, () ->{
//            PublicCardState test1 = new PublicCardState(faceUpCardsProblem, -5,-5);
//        });
//        assertThrows(IllegalArgumentException.class, () ->{
//            PublicCardState test1 = new PublicCardState(null, 5,5);
//        }); //TODO si faceUpCards null ?
    }

    @Test
    public void allGettersAreOk(){
        PublicCardState test = new PublicCardState(faceUpCards5, 5 , 12);
        assertEquals(22, test.totalSize());
        assertEquals(faceUpCards5, test.faceUpCards());
        for (int i = 0; i < faceUpCards5.size(); i++) {
            assertEquals(faceUpCards5.get(i), test.faceUpCard(i));
        }
        assertThrows(IndexOutOfBoundsException.class, () -> {
            test.faceUpCard(-1);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            test.faceUpCard(6);
        });
        assertEquals(5, test.deckSize());
        assertEquals(12, test.discardsSize());
        assertFalse(test.isDeckEmpty());
    }

    //TODO faire un test si on modifie faceUpCards -> copie est modif ?
}
