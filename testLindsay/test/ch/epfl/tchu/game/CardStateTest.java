package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CardStateTest {
    private final Random random = new Random();
    private final List<Card> cards = Card.CARS;
    private final List<Card> cardss = null;
    private final SortedBag<Card> cardSortedBag = SortedBag.of(Card.ALL);

    @Test
    void withDrawnFaceUpCardworksNormally() {
        var faceUpCardList = cards.subList(0, 5);
        var s = SortedBag.of(cards);
        System.out.println(cards);
        var initialDeck = Deck.of(s, random);
        var initialCardState = CardState.of(initialDeck);
        System.out.println("Initial FaceUpCards : " + initialCardState.faceUpCards());
        var secondCardState = initialCardState.withDrawnFaceUpCard(4);
        System.out.println("SecondFaceUpCard : " + secondCardState.faceUpCards());
        assertEquals(initialCardState.topDeckCard(), secondCardState.faceUpCard(4)); //Marche bien
    }
    @Test
    void withDrawnFaceUpCardsFailsWhen(){
        var faceUpCardList = cards.subList(0, 5);
        var s = SortedBag.of(cards);
        System.out.println(cards);
        var initialDeck = Deck.of(s, random);
        var initialCardState = CardState.of(initialDeck);
        var secondCardState = initialCardState.withDrawnFaceUpCard(4);
        assertThrows(IndexOutOfBoundsException.class, ()-> {initialCardState.withDrawnFaceUpCard(5);});

    }
    @Test
    void topDeckCard() {

        var faceUpCardList = cards.subList(0, 5);
        var s = SortedBag.of(cards);

        var initialDeck = Deck.of(s,random);
        Card c =initialDeck.topCard();
        assertEquals(c, initialDeck.topCard());


        // how i need to do assertEqual to this when the arrangement of cards is random?
    }

    @Test
    void topDeckCardFailsWhen(){
        List<Card> faceUpCardList = null;
        var s = SortedBag.of(cards);

        var initialDeck = Deck.of(s, random);
        //  @throws IllegalArgumentException si la pioche est vide
        assertThrows(NullPointerException.class, ()->{faceUpCardList.isEmpty();} );
    }
    @Test
    void TotalSizeWorksNormally() {
        var cardList = cards.subList(0, 5);
        PublicCardState pcs1 = new PublicCardState(cardList, 2, 3);
        assertEquals(cardList.get(4), pcs1.faceUpCard(4));
    }
   /* @Test
    void withoutTopDeckCard() {


        var faceUpCardList = cards.subList(0, 5);
        var s = SortedBag.of(cards);
        System.out.println(cards);
        var initialDeck = Deck.of(s, random);
        var HJ = List.of (Card.YELLOW, Card.ORANGE);
        var discards= SortedBag.of(HJ);
        var initialCardState = CardState.of(initialDeck);
        List<Card> newL=null;

        for(int i =0; i< initialDeck.size(); i++){
                if(s.toList().get(i)!=initialDeck.topCard()){
                    newL.add(s.toList().get(i));
            }
        }

        CardState ct1= new CardState(faceUpCardList, newL, discards, newL.size(), discards.size());
       CardState ct2 = new CardState(faceUpCardList, initialDeck.withoutTopCard(), discards, initialDeck.size(), discards.size());

        assertEquals(,);
    }
*/
    @Test
    void withoutTopDeckCardFailsWhen(){
        List<Card> faceUpCardList = null;
        var s = SortedBag.of(cards);

        var initialDeck = Deck.of(s, random);
        //  @throws IllegalArgumentException si la pioche est vide
        assertThrows(NullPointerException.class, ()->{faceUpCardList.isEmpty();} );
    }

    @Test
    void withDeckRecreatedFromDiscardsWorksWhen() {
    }

    @Test
    void withDeckRecreatedFromDiscardsFailsWhen() {
        List<Card> faceUpCardList = null;
        var s = SortedBag.of(cards);

        var initialDeck = Deck.of(s, random);
        //  @throws IllegalArgumentException si la pioche est vide
        assertThrows(NullPointerException.class, ()->{faceUpCardList.isEmpty();} );
    }
 /*   @Test
   void withMoreDiscardedCardsWorksWhen() {
        List<Card> faceUpCards= List.of(Card.YELLOW, Card.BLUE, Card.RED);
        Deck<Card> deck= Deck.of(faceUpCards, Random);
        SortedBag<Card> discards= SortedBag.of(cards); 
        SortedBag<Card> additionalDiscards;
      


    } */


    }


