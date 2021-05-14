package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import org.junit.jupiter.api.Test;

import javax.swing.text.html.ListView;

import static org.junit.jupiter.api.Assertions.*;

class GraphicalPlayerTest {

    @Test
    void toStringWorks() {
        GraphicalPlayer.CardBagStringConverter c = new GraphicalPlayer.CardBagStringConverter();
        SortedBag<Card> a = SortedBag.of(1, Card.VIOLET, 3, Card.RED);
        SortedBag<Card> b = SortedBag.of(2, Card.BLUE, 1, Card.LOCOMOTIVE);
        SortedBag<Card> d = SortedBag.of(1, Card.BLACK, 1, Card.GREEN);
        SortedBag<Card> e = SortedBag.of(1, Card.VIOLET);
        SortedBag<Card> f = SortedBag.of(3, Card.VIOLET);
        assertEquals("1 violette et 3 rouges", c.toString(a));
        assertEquals("2 bleues et 1 locomotive", c.toString(b));
        assertEquals("1 noire et 1 verte", c.toString(d));
        assertEquals("1 violette", c.toString(e));
        assertEquals("3 violettes", c.toString(f));
    }

}