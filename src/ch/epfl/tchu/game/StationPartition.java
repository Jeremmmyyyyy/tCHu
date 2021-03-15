package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.Map;

/**
 * Represents a partition of the stations given a player's connectivity
 *
 * @author Yann Ennassih (329978)
 */
public final class StationPartition implements StationConnectivity {

    private final Map<Integer, Integer> stationPartition;

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

    /**
     * Enables to build an instance of StationPartition.
     *
     * @author Yann Ennassih (329978)
     */
    public final static class Builder {

        private Map<Integer, Integer> stationPartition;

        /**
         * Public constructor of the class Builder
         * @param stationCount number of stations to partition
         * @throws IllegalArgumentException if stationCount > 0
         */
        public  Builder(int stationCount) {
            Preconditions.checkArgument(stationCount >= 0);
            for (int i = 0; i < stationCount; ++i) {
                stationPartition.put(i, i);
            }
        }

        /**
         * Returns the lead-station of a given station
         * @param s station whose representative is to return
         * @return the lead-station (representative) of the station s
         */
        private Integer representative(Station s) {
            return stationPartition.get(s.id());
        }

        /**
         * Connects to stations together
         * @param s1 station to connect
         * @param s2 station to connect
         * @return the current instance
         */
        public Builder connect(Station s1, Station s2) {
            //the lead-station of s1 becomes the lead-station for s2 and thus connects the two stations
            stationPartition.put(s2.id(), representative(s1));
            return this;
        }

        /**
         * Builds an instance of StationPartition
         * @return a new instance of StationPartition
         */
        public StationPartition build() {
            return new StationPartition(stationPartition);
        }
    }

}
