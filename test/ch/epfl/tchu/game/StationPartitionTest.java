package ch.epfl.tchu.game;

import ch.epfl.test.ChMapPublic;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StationPartitionTest {

    @Test
    void builder() {
        StationPartition.Builder builder1 = new StationPartition.Builder(8);
        builder1.connect(ChMapPublic.BAD, ChMapPublic.BRU); //0-5
        builder1.connect(ChMapPublic.COI, ChMapPublic.DAV); //6-7
        builder1.connect(ChMapPublic.BAD, ChMapPublic.COI); //0-6
        builder1.connect(ChMapPublic.BRI, ChMapPublic.BER); //4-3
        builder1.connect(ChMapPublic.BER, ChMapPublic.BEL); //3-2
        System.out.println("Expected map : 0-5 1-1 2-2 3-2 4-3 5-7 6-7 7-7");
        System.out.print("Actual map :   ");
//        for (int i = 0; i < 8; ++i) {
//            System.out.print(i + "-" + builder1.stationPartition[i] + " ");
//        }
        StationPartition part1 = builder1.build();
        System.out.println("\nExpected map : 0-7 1-1 2-2 3-2 4-2 5-7 6-7 7-7");
        System.out.print("Actual map :   ");
//        for (int i = 0; i < 8; ++i) {
//            System.out.print(i + "-" + part1.stationPartition[i] + " ");
//        }
    }

    @Test
    void connected() {

        StationPartition.Builder builder1 = new StationPartition.Builder(8);
        builder1.connect(ChMapPublic.BAD, ChMapPublic.BRU); //0-5
        builder1.connect(ChMapPublic.COI, ChMapPublic.DAV); //6-7
        builder1.connect(ChMapPublic.BAD, ChMapPublic.COI); //0-6
        builder1.connect(ChMapPublic.BRI, ChMapPublic.BER); //4-3
        builder1.connect(ChMapPublic.BER, ChMapPublic.BEL); //3-2
        StationPartition part1 = builder1.build();
        assertTrue(part1.connected(ChMapPublic.BAD, ChMapPublic.DAV));
        assertTrue(part1.connected(ChMapPublic.COI, ChMapPublic.DAV));
        assertTrue(part1.connected(ChMapPublic.DAV, ChMapPublic.DAV));
        assertFalse(part1.connected(ChMapPublic.BRI, ChMapPublic.BRU));
        assertFalse(part1.connected(ChMapPublic.BAL, ChMapPublic.BRU));
        assertFalse(part1.connected(ChMapPublic.COI, ChMapPublic.BEL));
        assertTrue(part1.connected(ChMapPublic.BEL, ChMapPublic.BRI));

    }

    @Test
    void connectedOutOfBounds() {

        StationPartition.Builder builder1 = new StationPartition.Builder(8);
        builder1.connect(ChMapPublic.BAD, ChMapPublic.BRU); //0-5
        builder1.connect(ChMapPublic.COI, ChMapPublic.DAV); //6-7
        builder1.connect(ChMapPublic.BAD, ChMapPublic.COI); //0-6
        builder1.connect(ChMapPublic.BRI, ChMapPublic.BER); //4-3
        builder1.connect(ChMapPublic.BER, ChMapPublic.BEL); //3-2
        StationPartition part1 = builder1.build();
        assertTrue(part1.connected(ChMapPublic.GEN, ChMapPublic.GEN));
        assertTrue(part1.connected(ChMapPublic.FR4, ChMapPublic.FR4));
        assertFalse(part1.connected(ChMapPublic.FR4, ChMapPublic.BAD));
        assertFalse(part1.connected(ChMapPublic.GEN, ChMapPublic.BRU));
        assertFalse(part1.connected(ChMapPublic.GEN, ChMapPublic.LUC));

    }
}
