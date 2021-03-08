package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

public final class CardState extends PublicCardState{

    SortedBag<Card> discard;

    private CardState(Deck<Card> deck, int deckSize, int discardSize) {
        super(deck.size(), deckSize, discardSize);
    }

    public CardState of(Deck<Card> deck){
         faceUpCards
    }




}
