package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * immutable class that represents all the informations and cards of the board that are known by the players
 * author Jérémy Barghorn (328403)
 */
public class PublicCardState {
    /**
     * List of 5 Cards that are shown on the board, final because we want an immutable class
     */
    private final List<Card> faceUpCards;
    /**
     * final integer values for the size of the deck and discard
     */
    private final int deckSize, discardSize;

    /**
     * create the board infos and set the cards that are known by the player
     * @param faceUpCards List of Cards that represent the 5 cards that are shown on the board
     * @param deckSize size of the deck
     * @param discardSize size of the discard
     * @throws IllegalArgumentException if faceUpCards do not contain 5 cards
     * @throws IllegalArgumentException if the size of the deck is negative
     * @throws IllegalArgumentException if the size of the discard is negative
     */
    public PublicCardState(List<Card> faceUpCards, int deckSize, int discardSize){
        Preconditions.checkArgument(faceUpCards.size() == 5);
        Preconditions.checkArgument(deckSize >= 0);
        Preconditions.checkArgument(discardSize >= 0);
        this.faceUpCards = List.copyOf(faceUpCards);
        this.deckSize = deckSize;
        this.discardSize = discardSize;
    }

    /**
     * return the total size of all the cards that are given to PublicCardState
     * @return the sum of the faceUpCards, the deck size and the discard size
     */
    public int totalSize(){
        return faceUpCards.size() + deckSize + discardSize;
    }

    /**
     * return a copy of of the faceUpCards
     * @return a copy of the faceUpCards
     */
    public List<Card> faceUpCards(){
        return faceUpCards;
    }

    /**
     * return the Card in faceUpCard that is given by the slot
     * @param slot position of the faceUpCard that is asked
     * @return the Card in faceUpCard that is given by the slot
     * @throws IndexOutOfBoundsException if the slot is out of bound à 0,5
     */
    public Card faceUpCard(int slot){
        Objects.checkIndex(slot, 5);
        return faceUpCards.get(slot);
    }

    /**
     * return the size of the Deck
     * @return the size of the Deck
     */
    public int deckSize(){
        return deckSize;
    }

    /**
     * return true if the deck is empty
     * @return tru if the deck is empty
     */
    public boolean isDeckEmpty(){
        return deckSize == 0;
    }

    /**
     * return the size of the discard
     * @return the size of the discard
     */
    public int disCardSize(){
        return discardSize;
    }
}
