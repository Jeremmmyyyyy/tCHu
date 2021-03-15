package ch.epfl.tchu.game;

import java.util.List;

/**
 * Enum of the Players
 *
 * @author Jérémy Barghorn (328403)
 */
public enum PlayerId {
    PLAYER_1,
    PLAYER_2;

    /**
     * List that contains all the players
     */
    public static final List<PlayerId> ALL = List.of(PlayerId.values());
    /**
     * Amount of players in PlayerId
     */
    public static final int COUNT = ALL.size();

    /**
     * When applied to a player returns the next one
     * @return the opposite of the actual player
     */
    public PlayerId next(){
        return this.equals(PLAYER_1) ? PLAYER_1 : PLAYER_2;
    }
}
