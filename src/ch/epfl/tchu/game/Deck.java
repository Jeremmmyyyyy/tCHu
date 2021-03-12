package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.Collections;
import java.util.Random;
import java.util.List;

/**
 * A deck containing cards of type C.
 *
 * @author Yann Ennassih (329978)
 */
public final class Deck<C extends Comparable<C>> {

    private final List<C> cards;

    /**
     * Private constructor of the class Deck
     * @param cards list of cards composing the deck
     */
    private Deck(List<C> cards){
        this.cards = cards;
    }

    /**
     * Builds a shuffled Deck out of a list of cards
     * @param cards SortedBag of cards to add to the deck
     * @param rng random number generator for the shuffling
     * @param <C> type parameter for the type of cards
     * @return a new shuffled deck
     */
    public static <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng){
        List<C> cardsOf = cards.toList();
        Collections.shuffle(cardsOf, rng);
        return new Deck<C>(cardsOf);
    }

    /**
     * Returns the size of the deck
     * @return the size of the deck
     */
    public int size() {
        return cards.size();
    }

    /**
     * Returns if the deck is empty or not
     * @return true if the deck isEmpty, false else
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Returns the top card of the deck
     * @return the top card of the deck, last here in case of a stack
     * @throws IllegalArgumentException if the deck is empty
     */
    public C topCard() {
        Preconditions.checkArgument(!isEmpty());
        return cards.get(0);
    }

    /**
     * Return the deck this without the top card
     * @return the same deck as this without the top card
     * @throws IllegalArgumentException if the deck is empty
     */
    public Deck<C> withoutTopCard() {
        Preconditions.checkArgument(!isEmpty());
        return new Deck<>(cards.subList(1, size()));
    }

    /**
     * Returns a given number of top cards of the deck
     * @param count number of top cards to return
     * @return a SortedBag of the <count> top card of the deck
     * @throws IllegalArgumentException if count isn't in [0, size()]
     */
    public SortedBag<C> topCards(int count) {
        Preconditions.checkArgument(0 <= count && count <= size());
        List<C> topCardsList = cards.subList(0, count);
        SortedBag.Builder<C> topCards = new SortedBag.Builder<>();
        for (C card : topCardsList) {
            topCards.add(card);
        }
        return topCards.build();
    }

    /**
     * Returns the deck this without a given number of top cards
     * @param count number of top cards to put aside
     * @return the same deck as this without its <count> top cards
     * @throws IllegalArgumentException if count isn't in [0, size()]
     */
    public Deck<C> withoutTopCards(int count) {
        Preconditions.checkArgument(0 <= count && count <= size());
        return new Deck<>(cards.subList(count, size()));

    }
}
