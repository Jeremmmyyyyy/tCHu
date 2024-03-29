package ch.epfl.tchu.game;

/**
 * Interface StationConnectivity
 *
 * @author Jérémy Barghorn (328403)
 */
public interface StationConnectivity {
    /**
     * @param s1 Station number 1
     * @param s2 Station number 2
     * @return true if stations are connected by one player
     */
    boolean connected(Station s1, Station s2);

}
