package ch.epfl.tchu.game;

import ch.epfl.test.ChMapPublic;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class PublicPLayerStateTest {
    private List<Route> routes = List.of(new Route("AT1_STG_1", ChMapPublic.AT1, ChMapPublic.STG, 4, Route.Level.UNDERGROUND, null),
            new Route("BAD_OLT_1", ChMapPublic.BAD, ChMapPublic.OLT, 2, Route.Level.OVERGROUND, Color.VIOLET),
            new Route("BER_INT_1", ChMapPublic.BER, ChMapPublic.INT, 3, Route.Level.OVERGROUND, Color.BLUE),
            new Route("BER_LUC_1", ChMapPublic.BER, ChMapPublic.LUC, 4, Route.Level.OVERGROUND, null));

    private List<Route> routes2 = List.of();

    private List<Route> routes40 = List.of(new Route("BRI_LOC_1", ChMapPublic.BRI, ChMapPublic.LOC, 6, Route.Level.UNDERGROUND, null),
            new Route("COI_WAS_1", ChMapPublic.COI, ChMapPublic.WAS, 5, Route.Level.UNDERGROUND, null),
            new Route("GEN_YVE_1", ChMapPublic.GEN, ChMapPublic.YVE, 6, Route.Level.OVERGROUND, null),
            new Route("SCE_WIN_2", ChMapPublic.SCE, ChMapPublic.WIN, 1, Route.Level.OVERGROUND, Color.WHITE),
            new Route("SCE_ZUR_1", ChMapPublic.SCE, ChMapPublic.ZUR, 3, Route.Level.OVERGROUND, Color.ORANGE),
            new Route("SCZ_WAS_1", ChMapPublic.SCZ, ChMapPublic.WAS, 2, Route.Level.UNDERGROUND, Color.GREEN),
            new Route("SCZ_WAS_2", ChMapPublic.SCZ, ChMapPublic.WAS, 2, Route.Level.UNDERGROUND, Color.YELLOW),
            new Route("SCZ_ZOU_1", ChMapPublic.SCZ, ChMapPublic.ZOU, 1, Route.Level.OVERGROUND, Color.BLACK),
            new Route("SCZ_ZOU_2", ChMapPublic.SCZ, ChMapPublic.ZOU, 1, Route.Level.OVERGROUND, Color.WHITE),
            new Route("AT1_STG_1", ChMapPublic.AT1, ChMapPublic.STG, 4, Route.Level.UNDERGROUND, null),
            new Route("BAD_OLT_1", ChMapPublic.BAD, ChMapPublic.OLT, 2, Route.Level.OVERGROUND, Color.VIOLET),
            new Route("BER_INT_1", ChMapPublic.BER, ChMapPublic.INT, 3, Route.Level.OVERGROUND, Color.BLUE),
            new Route("BER_LUC_1", ChMapPublic.BER, ChMapPublic.LUC, 4, Route.Level.OVERGROUND, null));

    private List<Route> routesTooLong = List.of(new Route("BRI_LOC_1", ChMapPublic.BRI, ChMapPublic.LOC, 6, Route.Level.UNDERGROUND, null),
            new Route("COI_WAS_1", ChMapPublic.COI, ChMapPublic.WAS, 5, Route.Level.UNDERGROUND, null),
            new Route("GEN_YVE_1", ChMapPublic.GEN, ChMapPublic.YVE, 6, Route.Level.OVERGROUND, null),
            new Route("SCE_WIN_2", ChMapPublic.SCE, ChMapPublic.WIN, 1, Route.Level.OVERGROUND, Color.WHITE),
            new Route("SCE_ZUR_1", ChMapPublic.SCE, ChMapPublic.ZUR, 3, Route.Level.OVERGROUND, Color.ORANGE),
            new Route("SCZ_WAS_1", ChMapPublic.SCZ, ChMapPublic.WAS, 2, Route.Level.UNDERGROUND, Color.GREEN),
            new Route("SCZ_WAS_2", ChMapPublic.SCZ, ChMapPublic.WAS, 2, Route.Level.UNDERGROUND, Color.YELLOW),
            new Route("SCZ_ZOU_1", ChMapPublic.SCZ, ChMapPublic.ZOU, 1, Route.Level.OVERGROUND, Color.BLACK),
            new Route("SCZ_ZOU_2", ChMapPublic.SCZ, ChMapPublic.ZOU, 1, Route.Level.OVERGROUND, Color.WHITE),
            new Route("AT1_STG_1", ChMapPublic.AT1, ChMapPublic.STG, 4, Route.Level.UNDERGROUND, null),
            new Route("BAD_OLT_1", ChMapPublic.BAD, ChMapPublic.OLT, 2, Route.Level.OVERGROUND, Color.VIOLET),
            new Route("BER_INT_1", ChMapPublic.BER, ChMapPublic.INT, 3, Route.Level.OVERGROUND, Color.BLUE),
            new Route("BER_LUC_1", ChMapPublic.BER, ChMapPublic.LUC, 4, Route.Level.OVERGROUND, null),
            new Route("STG_VAD_1", ChMapPublic.STG, ChMapPublic.VAD, 2,Route.Level.UNDERGROUND, Color.BLUE),
            new Route("STG_WIN_1", ChMapPublic.STG, ChMapPublic.WIN, 3,Route.Level.OVERGROUND, Color.RED),
            new Route("STG_ZUR_1", ChMapPublic.STG, ChMapPublic.ZUR, 4,Route.Level.OVERGROUND, Color.BLACK));

    @Test
    public void constructorWorksWell(){
        PublicPlayerState playerState = new PublicPlayerState(5,5, routes);
        PublicPlayerState playerState2 = new PublicPlayerState(5,5, routes2);
        PublicPlayerState playerState40 = new PublicPlayerState(5,5, routes40);
        PublicPlayerState playerStateTooLong = new PublicPlayerState(5,5, routesTooLong);

        assertThrows(IllegalArgumentException.class, ()->{
            PublicPlayerState playerState5 = new PublicPlayerState(-1,5, routes);
        });
        assertThrows(IllegalArgumentException.class, ()->{
            PublicPlayerState playerState6 = new PublicPlayerState(5,-1, routes);
        });
        assertThrows(IllegalArgumentException.class, ()->{
            PublicPlayerState playerState7 = new PublicPlayerState(-5,-1, routes);
        });
        assertEquals(27, playerState.carCount());
        assertEquals(40, playerState2.carCount());
        assertEquals(0, playerState40.carCount());
//        assertEquals(-9, playerStateTooLong.carCount());

        assertEquals(20, playerState.claimPoints());
        assertEquals(0, playerState2.claimPoints());
        assertEquals(71, playerState40.claimPoints());
        assertEquals(84, playerStateTooLong.claimPoints());
    }

    @Test
    public void allGettersAreOk(){
        PublicPlayerState playerState = new PublicPlayerState(5,5, List.of(
                new Route("AT1_STG_1", ChMapPublic.AT1, ChMapPublic.STG, 4, Route.Level.UNDERGROUND, null),
                new Route("BAD_OLT_1", ChMapPublic.BAD, ChMapPublic.OLT, 2, Route.Level.OVERGROUND, Color.VIOLET),
                new Route("BER_INT_1", ChMapPublic.BER, ChMapPublic.INT, 3, Route.Level.OVERGROUND, Color.BLUE),
                new Route("BER_LUC_1", ChMapPublic.BER, ChMapPublic.LUC, 4, Route.Level.OVERGROUND, null)));

        assertEquals(5, playerState.ticketCount());
        assertEquals(5, playerState.cardCount());
        for (int i = 0; i < routes.size(); i++) {
            assertEquals(routes.get(i).id(), playerState.routes().get(i).id());
        }
    }
}
