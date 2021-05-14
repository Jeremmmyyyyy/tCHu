package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.gui.ActionHandlers.ClaimRouteHandler;
import ch.epfl.tchu.gui.ActionHandlers.DrawCardHandler;
import ch.epfl.tchu.gui.ActionHandlers.DrawTicketsHandler;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import javax.swing.text.html.ListView;

import java.util.Map;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;
import static org.junit.jupiter.api.Assertions.*;

class GraphicalPlayerTest extends Application {

    @Test
    void toStringWorks() {
        GraphicalPlayer.CardBagStringConverter c = new GraphicalPlayer.CardBagStringConverter();
        SortedBag<Card> a = SortedBag.of(1, Card.VIOLET, 3, Card.RED);
        SortedBag<Card> b = SortedBag.of(2, Card.BLUE, 1, Card.LOCOMOTIVE);
        SortedBag<Card> d = SortedBag.of(1, Card.BLACK, 1, Card.GREEN);
        SortedBag<Card> e = SortedBag.of(1, Card.VIOLET);
        SortedBag<Card> f = SortedBag.of(3, Card.VIOLET);
        assertEquals("1 violette et 3 rouges", c.toString(a));
        assertEquals("2 bleues et 1 locomotive", c.toString(b));
        assertEquals("1 noire et 1 verte", c.toString(d));
        assertEquals("1 violette", c.toString(e));
        assertEquals("3 violettes", c.toString(f));
    }

    private void setState(GraphicalPlayer player) {
        // … construit exactement les mêmes états que la méthode setState
        // du test de l'étape 9
        player.setState(publicGameState, p1State);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Map<PlayerId, String> playerNames =
                Map.of(PLAYER_1, "Ada", PLAYER_2, "Charles");
        GraphicalPlayer p = new GraphicalPlayer(PLAYER_1, playerNames);
        setState(p);

        DrawTicketsHandler drawTicketsH =
                () -> p.receiveInfo("Je tire des billets !");
        DrawCardHandler drawCardH =
                s -> p.receiveInfo(String.format("Je tire une carte de %s !", s));
        ClaimRouteHandler claimRouteH =
                (r, cs) -> {
                    String rn = r.station1() + " - " + r.station2();
                    p.receiveInfo(String.format("Je m'empare de %s avec %s", rn, cs));
                };

        p.startTurn(drawTicketsH, drawCardH, claimRouteH);
    }
}