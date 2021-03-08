package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public final class CardState extends PublicCardState{

    Deck<Card> deck;
    SortedBag<Card> discard;


    private CardState(List<Card> faceUpCards, Deck<Card> deck, SortedBag<Card> discard, int deckSize, int discardSize) {  // TODO decksize et discardsize nécessaires ?
        super(faceUpCards, deckSize, discardSize);
        this.deck = deck;
        this.discard = discard;
    }


    public static CardState of(Deck<Card> deck){
        Preconditions.checkArgument(deck.size()>=Constants.FACE_UP_CARDS_COUNT);
        return new CardState(deck.topCards(Constants.FACE_UP_CARDS_COUNT).toList(), deck.withoutTopCards(5),
                                SortedBag.of(), deck.size()-Constants.FACE_UP_CARDS_COUNT, 0);
    }

    public CardState withDrawnFaceUpCard(int slot){
        Objects.checkIndex(slot, 5);
        List.copyOf(faceUpCards).set(slot, deck.topCard()); // TODO Copy nécessaire
        deck = deck.withoutTopCard();
        // TODO faut il ajouter 1 à la pioche ?
        return this;
    }

    public Card topDeckCard(){
        Preconditions.checkArgument(!deck.isEmpty());
        return deck.topCard();
    }

    public CardState withoutTopDeckCard(){
        Preconditions.checkArgument(!deck.isEmpty());
        deck = deck.withoutTopCard();
        // TODO faut il ajouter 1 à la pioche ?

        return this;
    }

    public CardState withDeckRecreatedFromDiscard(Random rng){
        deck = Deck.of(discard, rng);
        Preconditions.checkArgument(discard.isEmpty());
        // discard.clear // TODO effacer la deiscar ?
        // TODO faut il ajouter 1 à la pioche ?
        return this;
    }

    public CardState withMoreDiscardCards(SortedBag<Card> additionalDiscards){
        // discard = SortedBag.of(additionalDiscards);
        discard.union(additionalDiscards);
        return this;
    }

}
