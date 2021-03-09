package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * final immutable class that represents all the informations and cards of the board
 * author Jérémy Barghorn (328403)
 */
public final class CardState extends PublicCardState{

    private final Deck<Card> deck; //TODO final ?
    private final SortedBag<Card> discard;


    private CardState(List<Card> faceUpCards, Deck<Card> deck, SortedBag<Card> discard, int deckSize, int discardSize) {  // TODO decksize et discardsize nécessaires ?
        super(faceUpCards, deckSize, discardSize);
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
        Preconditions.checkArgument(deck.size()>=Constants.FACE_UP_CARDS_COUNT);
        return new CardState(deck.topCards(Constants.FACE_UP_CARDS_COUNT).toList(),
                            deck.withoutTopCards(5),
                            SortedBag.of(),
                    deck.size()-Constants.FACE_UP_CARDS_COUNT,
                    0);
    }

    /**
     * replace a given card of the faceUpCards by the top card of the deck
     * @param slot position of the card that is changed
     * @return a CardState where a given card of the faceUpCards is replaced by the top card of the deck
     * @throws IndexOutOfBoundsException if the slot is out of bound 0,5
     */
    public CardState withDrawnFaceUpCard(int slot){
        Objects.checkIndex(slot, 5);
        List.copyOf(faceUpCards).set(slot, deck.topCard()); // TODO Copy nécessaire ?
        deck = deck.withoutTopCard();
        // deckSize = deckSize() - 1;
        // TODO faut il enlever 1 à la pioche ?
        return this;
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
        deck = deck.withoutTopCard();
        // TODO faut il ajouter 1 à la pioche ?
        return this;
    }

    /**
     * return a new CardState where the discard deck is mixed and put to the deck
     * @param rng random to mix the discard
     * @return a new CardState with a new deck regenerated from the discard that was mixed
     * @throws IllegalArgumentException if the deck isn't empty
     */
    public CardState withDeckRecreatedFromDiscard(Random rng){
        Preconditions.checkArgument(deck.isEmpty());
        deck = Deck.of(discard, rng);
        discard = SortedBag.of();
        // TODO faut il ajouter 1 à la pioche ?
        return this;
    }

    /**
     * return a CardState where the SortedBag of cards is added to the discard
     * @param additionalDiscards SortedBag of Cards that will be added to the discard
     * @return a CardState where the SortedBag of cards is added to the discard
     */
    public CardState withMoreDiscardCards(SortedBag<Card> additionalDiscards){
        // discard = SortedBag.of(additionalDiscards);
        discard.union(additionalDiscards); // TODO union ?
        return this;
    }

}
