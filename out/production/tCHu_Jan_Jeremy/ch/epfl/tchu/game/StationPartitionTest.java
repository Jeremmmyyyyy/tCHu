package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StationPartitionTest {
    Station s1 = new Station(2, "ZUR");
    Station s2 = new Station(2, "LSN");
    Station s3 = new Station(1, "LUC");
    int [] partition1= new int[3];
    int [] partitionB;
    StationPartition sp;

    //@Test
    /*void BuilderWorksFine() {
        List<Route> routes = List.of(
                getRoute("BAD_OLT_1"), getRoute("LAU_NEU_1"),
                getRoute("NEU_SOL_1"), getRoute("OLT_SOL_1"),
                getRoute("BAD_ZUR_1"), getRoute("STG_ZUR_1"));

        public int ticketPoints() {
            int maxId = 0;
            for (Route route : routes()) {
                int tempMax = Math.max(route.station1().id(), route.station2().id());
                if(tempMax > maxId) maxId = tempMax;
            }
            StationPartition.Builder builder = new StationPartition.Builder(maxId+1);
            for (Route route : routes()) {
                builder.connect(route.station1(), route.station2());
            }
            StationPartition partition = builder.build();
            int points = 0;
            for (Ticket ticket : tickets) {
                points += ticket.points(partition);
            }
            return points;
        }
    }*/

    @Test
    void ConnectedWorks(){
        assertEquals(true, partition1[s1.id()]== partition1[s2.id()]); }

    @Test
    void BuilderWorksWhen(){
        //  this.partitionB = new int[stationCount];
        int [] partitionB= new int [5];
        // StationPartition.Builder build =new StationPartition.Builder(4);
        int stationCount =5;
        assertEquals(stationCount, partitionB.length);
    }

    @Test
    void BuilderFailsWhen(){
        assertThrows(IllegalArgumentException.class, ()-> { new StationPartition.Builder(-1);});
    }


    @Test
    void BuilderBuildWorksWhen(){

        int [] rep = new int[8];
        int [] partitionC = new int[8];
        for(int i=0; i<partitionC.length; i++){
            partitionC[i]=rep[i];
        }
        //   StationPartition sp= new StationPartition(partition1);

    }




    @Test
    void representativeWorksWhen(){
        int [] partitionB = new int [s1.id()];
        int id =2;
        int [] table = new int [id];
        assertEquals(table.length, partitionB.length);

        int id1=5;
        int rep=id;
        while(rep!= partition1[rep]){
            rep=partition1[rep];
        }
        assertEquals(rep, partition1[rep]);

    }




    private Route getRoute(String id) {
        for (Route route : ChMap.routes()) {
            if(route.id()==id) return route;
        }
        System.out.println("No Route with this id was found");
        return null;
    }

    // Stations - cities
    public final Station BER = new Station(0, "Berne");
    public final Station LAU = new Station(1, "Lausanne");
    public final Station STG = new Station(2, "Saint-Gall");
    public final Station SIO = new Station(25, "Sion");
    private final Station GEN = new Station(10, "Genève");


    // Stations - countries
    public final Station DE1 = new Station(3, "Allemagne");
    public final Station DE2 = new Station(4, "Allemagne");
    public final Station DE3 = new Station(5, "Allemagne");
    public final Station AT1 = new Station(6, "Autriche");
    public final Station AT2 = new Station(7, "Autriche");
    public final Station IT1 = new Station(8, "Italie");
    public final Station IT2 = new Station(9, "Italie");
    public final Station IT3 = new Station(10, "Italie");
    public final Station FR1 = new Station(11, "France");
    public final Station FR2 = new Station(12, "France");

    // Countries
    public final List<Station> DE = List.of(DE1, DE2, DE3);
    public final List<Station> AT = List.of(AT1, AT2);
    public final List<Station> IT = List.of(IT1, IT2, IT3);
    public final List<Station> FR = List.of(FR1, FR2);

    public final Ticket LAU_STG = new Ticket(LAU, STG, 13);
    public final Ticket LAU_BER = new Ticket(LAU, BER, 2);
    public final Ticket GEN_BER = new Ticket(GEN, BER, 8);
    public final Ticket GEN_SIO = new Ticket(GEN, SIO, 10);

    public final Ticket BER_NEIGHBORS = ticketToNeighbors(List.of(BER), 6, 11, 8, 5);
    public final Ticket FR_NEIGHBORS = ticketToNeighbors(FR, 5, 14, 11, 0);

    private Ticket ticketToNeighbors(List<Station> from, int de, int at, int it, int fr) {
        var trips = new ArrayList<Trip>();
        if (de != 0) trips.addAll(Trip.all(from, DE, de));
        if (at != 0) trips.addAll(Trip.all(from, AT, at));
        if (it != 0) trips.addAll(Trip.all(from, IT, it));
        if (fr != 0) trips.addAll(Trip.all(from, FR, fr));
        return new Ticket(trips);
    }
}