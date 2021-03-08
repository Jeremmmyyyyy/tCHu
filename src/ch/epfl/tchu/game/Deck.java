package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.List;

public final class Deck<C extends Comparable<C>> {

    private List<C> cards;

    private Deck(List<C> cards){
        this.cards = cards;
    }

    public static <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng){
        List<C> cardsOf = cards.toList();
        Collections.shuffle(cardsOf, rng);
        return new Deck<C>(cardsOf);
    }

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public C topCard() {
        Preconditions.checkArgument(!isEmpty());
        return cards.get(size() - 1);
    }

    public Deck<C> withoutTopCard() {
        Preconditions.checkArgument(!isEmpty());
        return new Deck<>(cards.subList(0, size()));
    }

    public SortedBag<C> topCards(int count) {
        Preconditions.checkArgument(0 <= count && count <= size());
        return null;
    }


}
