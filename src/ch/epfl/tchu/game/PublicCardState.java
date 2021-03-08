package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;

public final class PublicCardState {

    public List<Card> faceUpCards;
    public int deckSize, discardSize; // TODO PUBLIQUE ?

    public PublicCardState(List<Card> faceUpCards, int deckSize, int discardSize){
        Preconditions.checkArgument(faceUpCards.size() > 5);
        Preconditions.checkArgument(deckSize > 0);
        Preconditions.checkArgument(discardSize > 0);
        this.faceUpCards = faceUpCards;
        this.deckSize = deckSize;
        this.discardSize = discardSize;
    }

    public int totalSize(){
        return faceUpCards.size() + deckSize + discardSize;
    }

    public List<Card> faceUpCards(){
        return faceUpCards;
    }

    public Card faceUpCard(int slot){
        if(slot < 0 || slot >= 5){
            throw new IndexOutOfBoundsException("!");
        }
        return faceUpCards.get(slot);
    }

    public int deckSize(){
        return deckSize;
    }

    public boolean isDeckEmpty(){
        return deckSize == 0;
    }

    public int disCardSize(){
        return discardSize;
    }
}
