package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * final immutable class that represents all the informations and cards of the board
 * author Jérémy Barghorn (328403)
 */
public final class CardState extends PublicCardState{

    private final Deck<Card> deck;
    private final SortedBag<Card> discard;


    private CardState(List<Card> faceUpCards, Deck<Card> deck, SortedBag<Card> discard) {
        super(faceUpCards, deck.size(), discard.size());
        this.deck = deck;
        this.discard = discard;
    }

    /**
     * return a Card state with a list of faceUpCards, the whole deck without the 5 first cards, an empty SortedBag, a deck size and the discrad size
     * @param deck deck that is used to initialise the Card State
     * @return a complete CardState
     * @throws IllegalArgumentException if the size of the deck is not grater equal than 5
     */
    public static CardState of(Deck<Card> deck){
        Preconditions.checkArgument(deck.size() >= Constants.FACE_UP_CARDS_COUNT);
        List<Card> faceUpCard = new ArrayList<>();
        for(int slot : Constants.FACE_UP_CARD_SLOTS){
            faceUpCard.add(deck.topCard());
            deck = deck.withoutTopCard();
        }
        return new CardState(faceUpCard, deck, SortedBag.of());
    }

    /**
     * replace a given card of the faceUpCards by the top card of the deck
     * @param slot position of the card that is changed
     * @return a CardState where a given card of the faceUpCards is replaced by the top card of the deck
     * @throws IndexOutOfBoundsException if the slot is out of bound 0,5
     */
    public CardState withDrawnFaceUpCard(int slot){
        Objects.checkIndex(slot, 5);
        Preconditions.checkArgument(deckSize() > 0);
        List<Card> faceUpCard = new ArrayList<>(faceUpCards());
        faceUpCard.set(slot, deck.topCard());
        return new CardState(faceUpCard, deck.withoutTopCard(), SortedBag.of());
    }

    /**
     * return the first card of the deck
     * @return the first card of the deck
     */
    public Card topDeckCard(){
        Preconditions.checkArgument(!deck.isEmpty());
        return deck.topCard();
    }

    /**
     * return the deck without it's first card
     * @return the deck without it's first card
     * @throws IllegalArgumentException if the deck is empty
     */
    public CardState withoutTopDeckCard(){
        Preconditions.checkArgument(!deck.isEmpty());
        return new CardState(faceUpCards(), deck.withoutTopCard(), SortedBag.of());
    }

    /**
     * return a new CardState where the discard deck is mixed and put to the deck
     * @param rng random to mix the discard
     * @return a new CardState with a new deck regenerated from the discard that was mixed
     * @throws IllegalArgumentException if the deck isn't empty
     */
    public CardState withDeckRecreatedFromDiscards(Random rng){
        Preconditions.checkArgument(!deck.isEmpty());
        return new CardState(faceUpCards(), Deck.of(discard, rng), SortedBag.of());
    }

    /**
     * return a CardState where the SortedBag of cards is added to the discard
     * @param additionalDiscards SortedBag of Cards that will be added to the discard
     * @return a CardState where the SortedBag of cards is added to the discard
     */
    public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards){
        discard.union(additionalDiscards);
        return new CardState(faceUpCards(), deck, discard.union(additionalDiscards));
    }
}
