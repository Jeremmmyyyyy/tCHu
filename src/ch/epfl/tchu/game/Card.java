
package ch.epfl.tchu.game;

import java.util.List;

/**
 * Enum of all the cards and their color
 *
 * @author Jérémy Barghorn (328403)
 */
public enum Card {

    BLACK(Color.BLACK),
    VIOLET(Color.VIOLET),
    BLUE(Color.BLUE),
    GREEN(Color.GREEN),
    YELLOW(Color.YELLOW),
    ORANGE(Color.ORANGE),
    RED(Color.RED),
    WHITE(Color.WHITE),
    LOCOMOTIVE(null);

    private final Color color;
    /**
     * List containing all the colors of the Cards and null if it's a locomotive
     */
    public static final List<Card> ALL = List.of(Card.values());
    /**
     * size of the ALL list : number of all the Cards
     */
    public static final int COUNT = ALL.size();
    /**
     * List of all the Cards by color that are not a locomotive
     */
    public final static List<Card> CARS = ALL.subList(0, COUNT-1);


    Card(Color color) {
        this.color = color;
    }

    /**
     * The type of the train card BLACK (from de enum CARD) matches with the color BLACK (from enum type Color)
     * If null returns null
     * @param color Color of the card
     * @return The type of the Card
     *
     */
    public static Card of (Color color){
        for (Card card : CARS) {
            if (color.equals(card.color)) {
                return card;
            }
        }
        return null;
    }


    /**
     * @return Color
     * Card.BLACK.color() returns Color.BLACK
     * Card.LOCOMOTIVE.color() returns null
     */
    public Color color(){
        return this.color;
    }
}
