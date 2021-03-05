package ch.epfl.test;

import ch.epfl.tchu.game.*;
import ch.epfl.tchu.game.Route.Level;

import java.util.ArrayList;
import java.util.List;

public final class ChMapPublic {
    private ChMapPublic() { }

    public static List<Station> stations() {
        return ALL_STATIONS;
    }

    public static List<Route> routes() {
        return ALL_ROUTES;
    }

    public static List<Ticket> tickets() {
        return ALL_TICKETS;
    }

    // Stations - cities
    public static final Station BAD = new Station(0, "Baden");
    public static final Station BAL = new Station(1, "Bâle");
    public static final Station BEL = new Station(2, "Bellinzone");
    public static final Station BER = new Station(3, "Berne");
    public static final Station BRI = new Station(4, "Brigue");
    public static final Station BRU = new Station(5, "Brusio");
    public static final Station COI = new Station(6, "Coire");
    public static final Station DAV = new Station(7, "Davos");
    public static final Station DEL = new Station(8, "Delémont");
    public static final Station FRI = new Station(9, "Fribourg");
    public static final Station GEN = new Station(10, "Genève");
    public static final Station INT = new Station(11, "Interlaken");
    public static final Station KRE = new Station(12, "Kreuzlingen");
    public static final Station LAU = new Station(13, "Lausanne");
    public static final Station LCF = new Station(14, "La Chaux-de-Fonds");
    public static final Station LOC = new Station(15, "Locarno");
    public static final Station LUC = new Station(16, "Lucerne");
    public static final Station LUG = new Station(17, "Lugano");
    public static final Station MAR = new Station(18, "Martigny");
    public static final Station NEU = new Station(19, "Neuchâtel");
    public static final Station OLT = new Station(20, "Olten");
    public static final Station PFA = new Station(21, "Pfäffikon");
    public static final Station SAR = new Station(22, "Sargans");
    public static final Station SCE = new Station(23, "Schaffhouse");
    public static final Station SCZ = new Station(24, "Schwyz");
    public static final Station SIO = new Station(25, "Sion");
    public static final Station SOL = new Station(26, "Soleure");
    public static final Station STG = new Station(27, "Saint-Gall");
    public static final Station VAD = new Station(28, "Vaduz");
    public static final Station WAS = new Station(29, "Wassen");
    public static final Station WIN = new Station(30, "Winterthour");
    public static final Station YVE = new Station(31, "Yverdon");
    public static final Station ZOU = new Station(32, "Zoug");
    public static final Station ZUR = new Station(33, "Zürich");

    // Stations - countries
    public static final Station DE1 = new Station(34, "Allemagne");
    public static final Station DE2 = new Station(35, "Allemagne");
    public static final Station DE3 = new Station(36, "Allemagne");
    public static final Station DE4 = new Station(37, "Allemagne");
    public static final Station DE5 = new Station(38, "Allemagne");
    public static final Station AT1 = new Station(39, "Autriche");
    public static final Station AT2 = new Station(40, "Autriche");
    public static final Station AT3 = new Station(41, "Autriche");
    public static final Station IT1 = new Station(42, "Italie");
    public static final Station IT2 = new Station(43, "Italie");
    public static final Station IT3 = new Station(44, "Italie");
    public static final Station IT4 = new Station(45, "Italie");
    public static final Station IT5 = new Station(46, "Italie");
    public static final Station FR1 = new Station(47, "France");
    public static final Station FR2 = new Station(48, "France");
    public static final Station FR3 = new Station(49, "France");
    public static final Station FR4 = new Station(50, "France");

    // Countries
    public static final List<Station> DE = List.of(DE1, DE2, DE3, DE4, DE5);
    public static final List<Station> AT = List.of(AT1, AT2, AT3);
    public static final List<Station> IT = List.of(IT1, IT2, IT3, IT4, IT5);
    public static final List<Station> FR = List.of(FR1, FR2, FR3, FR4);

    public static final List<Station> ALL_STATIONS = List.of(
            BAD, BAL, BEL, BER, BRI, BRU, COI, DAV, DEL, FRI, GEN, INT, KRE, LAU, LCF, LOC, LUC,
            LUG, MAR, NEU, OLT, PFA, SAR, SCE, SCZ, SIO, SOL, STG, VAD, WAS, WIN, YVE, ZOU, ZUR,
            DE1, DE2, DE3, DE4, DE5, AT1, AT2, AT3, IT1, IT2, IT3, IT4, IT5, FR1, FR2, FR3, FR4);

    // Routes
    public static final List<Route> ALL_ROUTES = List.of(
            new Route("AT1_STG_1", AT1, STG, 4, Level.UNDERGROUND, null), //0
            new Route("AT2_VAD_1", AT2, VAD, 1, Level.UNDERGROUND, Color.RED), //1
            new Route("BAD_BAL_1", BAD, BAL, 3, Level.UNDERGROUND, Color.RED), //2
            new Route("BAD_OLT_1", BAD, OLT, 2, Level.OVERGROUND, Color.VIOLET), //3
            new Route("BAD_ZUR_1", BAD, ZUR, 1, Level.OVERGROUND, Color.YELLOW), //4
            new Route("BAL_DE1_1", BAL, DE1, 1, Level.UNDERGROUND, Color.BLUE), //5
            new Route("BAL_DEL_1", BAL, DEL, 2, Level.UNDERGROUND, Color.YELLOW), //6
            new Route("BAL_OLT_1", BAL, OLT, 2, Level.UNDERGROUND, Color.ORANGE), //7
            new Route("BEL_LOC_1", BEL, LOC, 1, Level.UNDERGROUND, Color.BLACK), //8
            new Route("BEL_LUG_1", BEL, LUG, 1, Level.UNDERGROUND, Color.RED), //9
            new Route("BEL_LUG_2", BEL, LUG, 1, Level.UNDERGROUND, Color.YELLOW), //10
            new Route("BEL_WAS_1", BEL, WAS, 4, Level.UNDERGROUND, null), //11
            new Route("BEL_WAS_2", BEL, WAS, 4, Level.UNDERGROUND, null), //12
            new Route("BER_FRI_1", BER, FRI, 1, Level.OVERGROUND, Color.ORANGE), //13
            new Route("BER_FRI_2", BER, FRI, 1, Level.OVERGROUND, Color.YELLOW), //14
            new Route("BER_INT_1", BER, INT, 3, Level.OVERGROUND, Color.BLUE), //15
            new Route("BER_LUC_1", BER, LUC, 4, Level.OVERGROUND, null), //16
            new Route("BER_LUC_2", BER, LUC, 4, Level.OVERGROUND, null), //17
            new Route("BER_NEU_1", BER, NEU, 2, Level.OVERGROUND, Color.RED), // 18
            new Route("BER_SOL_1", BER, SOL, 2, Level.OVERGROUND, Color.BLACK), //19
            new Route("BRI_INT_1", BRI, INT, 2, Level.UNDERGROUND, Color.WHITE), //20
            new Route("BRI_IT5_1", BRI, IT5, 3, Level.UNDERGROUND, Color.GREEN), //21
            new Route("BRI_LOC_1", BRI, LOC, 6, Level.UNDERGROUND, null), //22
            new Route("BRI_SIO_1", BRI, SIO, 3, Level.UNDERGROUND, Color.BLACK), //23
            new Route("BRI_WAS_1", BRI, WAS, 4, Level.UNDERGROUND, Color.RED), //24
            new Route("BRU_COI_1", BRU, COI, 5, Level.UNDERGROUND, null), //25
            new Route("BRU_DAV_1", BRU, DAV, 4, Level.UNDERGROUND, Color.BLUE), //26
            new Route("BRU_IT2_1", BRU, IT2, 2, Level.UNDERGROUND, Color.GREEN), //27
            new Route("COI_DAV_1", COI, DAV, 2, Level.UNDERGROUND, Color.VIOLET), //28
            new Route("COI_SAR_1", COI, SAR, 1, Level.UNDERGROUND, Color.WHITE), //29
            new Route("COI_WAS_1", COI, WAS, 5, Level.UNDERGROUND, null), //30
            new Route("DAV_AT3_1", DAV, AT3, 3, Level.UNDERGROUND, null), //31
            new Route("DAV_IT1_1", DAV, IT1, 3, Level.UNDERGROUND, null), //32
            new Route("DAV_SAR_1", DAV, SAR, 3, Level.UNDERGROUND, Color.BLACK), //33
            new Route("DE2_SCE_1", DE2, SCE, 1, Level.OVERGROUND, Color.YELLOW), //34
            new Route("DE3_KRE_1", DE3, KRE, 1, Level.OVERGROUND, Color.ORANGE), //35
            new Route("DE4_KRE_1", DE4, KRE, 1, Level.OVERGROUND, Color.WHITE), //36
            new Route("DE5_STG_1", DE5, STG, 2, Level.OVERGROUND, null), //37
            new Route("DEL_FR4_1", DEL, FR4, 2, Level.UNDERGROUND, Color.BLACK), //38
            new Route("DEL_LCF_1", DEL, LCF, 3, Level.UNDERGROUND, Color.WHITE), //39
            new Route("DEL_SOL_1", DEL, SOL, 1, Level.UNDERGROUND, Color.VIOLET), //40
            new Route("FR1_MAR_1", FR1, MAR, 2, Level.UNDERGROUND, null), //41
            new Route("FR2_GEN_1", FR2, GEN, 1, Level.OVERGROUND, Color.YELLOW), //42
            new Route("FR3_LCF_1", FR3, LCF, 2, Level.UNDERGROUND, Color.GREEN), //43
            new Route("FRI_LAU_1", FRI, LAU, 3, Level.OVERGROUND, Color.RED), //44
            new Route("FRI_LAU_2", FRI, LAU, 3, Level.OVERGROUND, Color.VIOLET), //45
            new Route("GEN_LAU_1", GEN, LAU, 4, Level.OVERGROUND, Color.BLUE), //46
            new Route("GEN_LAU_2", GEN, LAU, 4, Level.OVERGROUND, Color.WHITE), //47
            new Route("GEN_YVE_1", GEN, YVE, 6, Level.OVERGROUND, null), //48
            new Route("INT_LUC_1", INT, LUC, 4, Level.OVERGROUND, Color.VIOLET), //49
            new Route("IT3_LUG_1", IT3, LUG, 2, Level.UNDERGROUND, Color.WHITE), //50
            new Route("IT4_LOC_1", IT4, LOC, 2, Level.UNDERGROUND, Color.ORANGE), //51
            new Route("KRE_SCE_1", KRE, SCE, 3, Level.OVERGROUND, Color.VIOLET), //52
            new Route("KRE_STG_1", KRE, STG, 1, Level.OVERGROUND, Color.GREEN), //53
            new Route("KRE_WIN_1", KRE, WIN, 2, Level.OVERGROUND, Color.YELLOW), //54
            new Route("LAU_MAR_1", LAU, MAR, 4, Level.UNDERGROUND, Color.ORANGE), // 55
            new Route("LAU_NEU_1", LAU, NEU, 4, Level.OVERGROUND, null), //56
            new Route("LCF_NEU_1", LCF, NEU, 1, Level.UNDERGROUND, Color.ORANGE), //57
            new Route("LCF_YVE_1", LCF, YVE, 3, Level.UNDERGROUND, Color.YELLOW), //58
            new Route("LOC_LUG_1", LOC, LUG, 1, Level.UNDERGROUND, Color.VIOLET), //59
            new Route("LUC_OLT_1", LUC, OLT, 3, Level.OVERGROUND, Color.GREEN), //60
            new Route("LUC_SCZ_1", LUC, SCZ, 1, Level.OVERGROUND, Color.BLUE), //61
            new Route("LUC_ZOU_1", LUC, ZOU, 1, Level.OVERGROUND, Color.ORANGE), //62
            new Route("LUC_ZOU_2", LUC, ZOU, 1, Level.OVERGROUND, Color.YELLOW), //63
            new Route("MAR_SIO_1", MAR, SIO, 2, Level.UNDERGROUND, Color.GREEN), //64
            new Route("NEU_SOL_1", NEU, SOL, 4, Level.OVERGROUND, Color.GREEN), //65
            new Route("NEU_YVE_1", NEU, YVE, 2, Level.OVERGROUND, Color.BLACK), //66
            new Route("OLT_SOL_1", OLT, SOL, 1, Level.OVERGROUND, Color.BLUE), //67
            new Route("OLT_ZUR_1", OLT, ZUR, 3, Level.OVERGROUND, Color .WHITE), //68
            new Route("PFA_SAR_1", PFA, SAR, 3, Level.UNDERGROUND, Color.YELLOW), //69
            new Route("PFA_SCZ_1", PFA, SCZ, 1, Level.OVERGROUND, Color.VIOLET), //70
            new Route("PFA_STG_1", PFA, STG, 3, Level.OVERGROUND, Color.ORANGE), //71
            new Route("PFA_ZUR_1", PFA, ZUR, 2, Level.OVERGROUND, Color.BLUE), //72
            new Route("SAR_VAD_1", SAR, VAD, 1, Level.UNDERGROUND, Color.ORANGE), //73
            new Route("SCE_WIN_1", SCE, WIN, 1, Level.OVERGROUND, Color.BLACK), //74
            new Route("SCE_WIN_2", SCE, WIN, 1, Level.OVERGROUND, Color.WHITE), //75
            new Route("SCE_ZUR_1", SCE, ZUR, 3, Level.OVERGROUND, Color.ORANGE), //76
            new Route("SCZ_WAS_1", SCZ, WAS, 2, Level.UNDERGROUND, Color.GREEN), //77
            new Route("SCZ_WAS_2", SCZ, WAS, 2, Level.UNDERGROUND, Color.YELLOW), //78
            new Route("SCZ_ZOU_1", SCZ, ZOU, 1, Level.OVERGROUND, Color.BLACK), //79
            new Route("SCZ_ZOU_2", SCZ, ZOU, 1, Level.OVERGROUND, Color.WHITE), // 80
            new Route("STG_VAD_1", STG, VAD, 2, Level.UNDERGROUND, Color.BLUE), //81
            new Route("STG_WIN_1", STG, WIN, 3, Level.OVERGROUND, Color.RED), //82
            new Route("STG_ZUR_1", STG, ZUR, 4, Level.OVERGROUND, Color.BLACK), //83
            new Route("WIN_ZUR_1", WIN, ZUR, 1, Level.OVERGROUND, Color.BLUE), //84
            new Route("WIN_ZUR_2", WIN, ZUR, 1, Level.OVERGROUND, Color.VIOLET), //85
            new Route("ZOU_ZUR_1", ZOU, ZUR, 1, Level.OVERGROUND, Color.GREEN), //86
            new Route("ZOU_ZUR_2", ZOU, ZUR, 1, Level.OVERGROUND, Color.RED)); //87

    // Tickets
    public static final Ticket deToNeighbors = ticketToNeighbors(DE, 0, 5, 13, 5);
    public static final Ticket atToNeighbors = ticketToNeighbors(AT, 5, 0, 6, 14);
    public static final Ticket itToNeighbors = ticketToNeighbors(IT, 13, 6, 0, 11);
    public static final Ticket frToNeighbors = ticketToNeighbors(FR, 5, 14, 11, 0);

    public static final List<Ticket> ALL_TICKETS = List.of(
            // City-to-city tickets
            new Ticket(BAL, BER, 5),
            new Ticket(BAL, BRI, 10),
            new Ticket(BAL, STG, 8),
            new Ticket(BER, COI, 10),
            new Ticket(BER, LUG, 12),
            new Ticket(BER, SCZ, 5),
            new Ticket(BER, ZUR, 6),
            new Ticket(FRI, LUC, 5),
            new Ticket(GEN, BAL, 13),
            new Ticket(GEN, BER, 8),
            new Ticket(GEN, SIO, 10),
            new Ticket(GEN, ZUR, 14),
            new Ticket(INT, WIN, 7),
            new Ticket(KRE, ZUR, 3),
            new Ticket(LAU, INT, 7),
            new Ticket(LAU, LUC, 8),
            new Ticket(LAU, STG, 13),
            new Ticket(LCF, BER, 3),
            new Ticket(LCF, LUC, 7),
            new Ticket(LCF, ZUR, 8),
            new Ticket(LUC, VAD, 6),
            new Ticket(LUC, ZUR, 2),
            new Ticket(LUG, COI, 10),
            new Ticket(NEU, WIN, 9),
            new Ticket(OLT, SCE, 5),
            new Ticket(SCE, MAR, 15),
            new Ticket(SCE, STG, 4),
            new Ticket(SCE, ZOU, 3),
            new Ticket(STG, BRU, 9),
            new Ticket(WIN, SCZ, 3),
            new Ticket(ZUR, BAL, 4),
            new Ticket(ZUR, BRU, 11),
            new Ticket(ZUR, LUG, 9),
            new Ticket(ZUR, VAD, 6),

            // City to country tickets
            ticketToNeighbors(List.of(BER), 6, 11, 8, 5),
            ticketToNeighbors(List.of(COI), 6, 3, 5, 12),
            ticketToNeighbors(List.of(LUG), 12, 13, 2, 14),
            ticketToNeighbors(List.of(ZUR), 3, 7, 11, 7),

            // Country to country tickets (two of each)
            deToNeighbors, deToNeighbors,
            atToNeighbors, atToNeighbors,
            itToNeighbors, itToNeighbors,
            frToNeighbors, frToNeighbors);

    public static Ticket ticketToNeighbors(List<Station> from, int de, int at, int it, int fr) {
        var trips = new ArrayList<Trip>();
        if (de != 0) trips.addAll(Trip.all(from, DE, de));
        if (at != 0) trips.addAll(Trip.all(from, AT, at));
        if (it != 0) trips.addAll(Trip.all(from, IT, it));
        if (fr != 0) trips.addAll(Trip.all(from, FR, fr));
        return new Ticket(trips);
    }
}