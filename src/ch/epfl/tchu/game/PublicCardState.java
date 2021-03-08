package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;
import java.util.Objects;

public class PublicCardState { //TODO une classe immuable a des attributs finaux mais peut ne pas être finale

    public final List<Card> faceUpCards;
    public final int deckSize, discardSize; // TODO PUBLIQUE ?

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
        return List.copyOf(faceUpCards);
    }

    public Card faceUpCard(int slot){
        Objects.checkIndex(slot, 5);
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
