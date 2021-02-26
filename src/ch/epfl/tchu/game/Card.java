package ch.epfl.tchu.game;

import java.util.List;

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

    public static final List<Card> ALL = List.of(Card.values());
    public static final int COUNT = ALL.size();
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
