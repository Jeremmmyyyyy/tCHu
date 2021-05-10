package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

/**
 * Represents a partition of the stations given a player's connectivity
 *
 * @author Yann Ennassih (329978)
 */
public final class StationPartition implements StationConnectivity {

    private final int[] stationPartition;

    /**
     * Private constructor for StationPartition
     * @param stationPartition map where each station id (value) is associated to one lead-station id (key)
     */
    private StationPartition(int[] stationPartition) {
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
        if (s1.id() >= stationPartition.length || s2.id() >= stationPartition.length) {
            return s1.id() == s2.id();
        }
        return stationPartition[s1.id()] == stationPartition[s2.id()];
    }

    /**
     * Enables to build an instance of StationPartition.
     *
     * @author Yann Ennassih (329978)
     */
    public final static class Builder {

        private final int[] stationPartition;

        /**
         * Public constructor of the class Builder
         * @param stationCount number of stations to partition
         * @throws IllegalArgumentException if stationCount > 0
         */
        public Builder(int stationCount) {
            Preconditions.checkArgument(stationCount >= 0);
            stationPartition = new int[stationCount];

            for (int i = 0; i < stationCount; ++i) {
                stationPartition[i] = i;
            }
        }

        /**
         * Returns the lead-station of a given station
         * @param stationId id of the station whose representative is to return
         * @return the id of the lead-station (representative) of the station s
         */
        private int representative(int stationId) {
            int representative = stationId;

            while (representative != stationPartition[representative]) {
                representative = stationPartition[representative];
            }

            return representative;
        }

        /**
         * Connects to stations together
         * @param s1 station to connect
         * @param s2 station to connect
         * @return the current instance
         */
        public Builder connect(Station s1, Station s2) {
            stationPartition[representative(s1.id())] = representative(s2.id());
            return this;
        }

        /**
         * Builds an instance of StationPartition
         * @return a new instance of StationPartition
         */
        public StationPartition build() {

            for (int i = 0; i < stationPartition.length; ++i) {
                stationPartition[i] = representative(i);
            }
            return new StationPartition(stationPartition);
        }
    }

}
