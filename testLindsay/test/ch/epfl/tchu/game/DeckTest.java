package ch.epfl.tchu.game;


//finale, immuable

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Un tas de cartes.
 *
 * @author Nur Ertug
 * @author Lindsay Bordier
 */
public final class Deck<C extends Comparable<C>> {

    private final List<C> cards;
    /**
     * le nombre des cartes 
     */
    public final int size;


    /**
    
     * @param cards 
     *          multiensembles de cartes
     * @param rng 
     *         générateur de nombres aléatoire
     * @return un tas de cartes ayant les mêmes cartes que le multiensemble cards,
     *                mélangées au moyen du générateurs de nombres aléatoires rng 
     * Retourne un tas de cartes ayant les mêmes cartes que le multiensemble cards,
     *              mélangées au moyen du générateurs de nombres aléatoires rng
     */
    public static <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng) {
        List<C> cardList = cards.toList();
        Collections.shuffle(cardList);
        return new Deck<>(cardList);
    }

   
    private Deck(List<C> cards) {
        this.cards = List.copyOf(cards);
        this.size = cards.size();
    }

    /**
     * Retourne le nombre de cartes du tas
     * @return le nombre de cartes du tas
     */
    public int size() {
        return cards.size();
    }

    /**
     * Retourne vrai si et seulement le tas est vide.
     * @return vrai si et seulement le tas est vide
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
 
     * @throws IllegalArgumentException 
     *                      si le tas est vide    
     * Retourne la carte au sommet du tas.
     * @return la carte au sommet du tas.
     */
    public C topCard() {
        Preconditions.checkArgument(!isEmpty());
        return cards.get(0);
    }

    /**
     *  @throws IllegalArgumentException
     *                      si le tas est vide
     * Retourne un tas identique à this mais sans la carte au sommet
     * @return un tas identique à this mais sans la carte au sommet
     * 
     */
    public Deck<C> withoutTopCard() {
        Preconditions.checkArgument(!isEmpty());
        return new Deck<>(cards.subList(0,size-1));
    }



    /** 
     * @param count 
     *          nombre de cartes au sommet du tas à renvoyer
     *@throws IllegalArgumentException 
     *                  si le tas est vide ou si count n'est pas compris 
     *                  entre 0 (inclus) et la taille du tas (exclus)
     * Retourne le multiensemble des count cartes au sommet du tas        
     * @return le multiensemble des count cartes au sommet du tas
     * 
     */
    public SortedBag<C> topCards(int count) {
        Preconditions.checkArgument(!isEmpty());
        Preconditions.checkArgument(count>=0 && count<=size);
        SortedBag<C> topCards = SortedBag.of(cards.subList(size-count,size));
        return topCards;
    }

    /**
     * @throws IllegalArgumentException 
     *          si le tas est vide
     *          ou si count n'est pas compris entre 0 (inclus) et la taille du tas (exclus)
     * Retourne un tas identique à this mais sans la carte au sommet
     * @return un tas identique à this mais sans la carte au sommet
     */
    public Deck<C> withoutTopCards(int count) {
        Preconditions.checkArgument(count>=0 && count<=size);
        return new Deck<>(cards.subList(0,size-count));
    }



}
