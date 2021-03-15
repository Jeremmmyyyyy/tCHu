package ch.epfl.tchu.game;

import java.util.HashMap;
import java.util.Map;

public final class StationPartition implements StationConnectivity {

    private Map<Integer, Integer> stationPartition;

    private StationPartition(Map<Integer, Integer> stationPartition) {
        this.stationPartition = stationPartition;
    }

    @Override
    public boolean connected(Station s1, Station s2) {

        return false;
    }
}
