package ch.epfl.tchu.game;

import java.util.List;

/**
 * enum for the colors
 *
 * @author Jérémy Barghorn (328403)
 */
public enum Color {

    BLACK,
    VIOLET,
    BLUE,
    GREEN,
    YELLOW,
    ORANGE,
    RED,
    WHITE;

    /**
     * List of all the colors of the enum
     */
    public static final List<Color> ALL = List.of(Color.values());
    /**
     * size of the ALL list : number of all the colors
     */
    public static final int COUNT = ALL.size();

}
