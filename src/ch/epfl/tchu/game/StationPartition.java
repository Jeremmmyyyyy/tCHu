package ch.epfl.tchu.game;

import java.util.Map;

/**
 * Represents a partition of the stations given a player's connectivity
 *
 * @author Yann Ennassih (329978)
 */
public final class StationPartition implements StationConnectivity {

    private Map<Integer, Integer> stationPartition;

    /**
     * Private constructor for StationPartition
     * @param stationPartition map where each station id (value) is associated to one lead-station id (key)
     */
    private StationPartition(Map<Integer, Integer> stationPartition) {
        this.stationPartition = stationPartition;
    }

    /**
     * Tests if two stations are connected in the current station partition
     * @param s1 Station number 1
     * @param s2 Station number 2
     * @return true if s1 and s2 are connected
     * true else if stationPartition doesn't contain s1 or s2 and s1 == s2
     * false else
     */
    @Override
    public boolean connected(Station s1, Station s2) {
        return stationPartition.getOrDefault(s1.id(), s1.id()) == stationPartition.getOrDefault(s2.id(), s2.id());
    }
}
