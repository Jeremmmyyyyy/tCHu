package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.test.ChMapPublic;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InfoTest {

    private final String player = "Luc";
    private final Info gui = new Info(player);

//    @Test
//    void routeName() {
//        assertEquals("Autriche – Saint-Gall", gui.routeName(ChMapPublic.ALL_ROUTES.get(0)));
//    }

//    @Test
//    void cardsName() {
//        SortedBag<Card> cards = SortedBag.of(1, Card.YELLOW, 4, Card.ORANGE);
//        assertEquals("1 jaune et 4 oranges", gui.cardsName(cards));
//    }

    @Test
    void cardName() {
        assertEquals("noire", Info.cardName(Card.BLACK, 1));
        assertEquals("noires", Info.cardName(Card.BLACK, 4));
        assertEquals("bleues", Info.cardName(Card.BLUE, -34));
    }

    @Test
    void draw() {
        List<String> playerNames = new ArrayList<>();
        playerNames.add(player);
        playerNames.add("Jose");
        assertEquals("\nLuc et Jose sont ex æqo avec 350 points !\n", Info.draw(playerNames, 350));
    }

    @Test
    void willPlayerFirst() {
        assertEquals("Luc jouera en premier.\n\n", gui.willPlayFirst());
    }






}