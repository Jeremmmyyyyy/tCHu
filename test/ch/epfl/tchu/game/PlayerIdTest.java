package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerIdTest {
    @Test
    public void PlayerIdOk(){
        PlayerId p1 = PlayerId.PLAYER_1;
        PlayerId p2 = PlayerId.PLAYER_2;
        System.out.println(PlayerId.ALL);
        assertEquals(PlayerId.ALL.size(), PlayerId.COUNT);
        assertEquals(PlayerId.PLAYER_2, p1.next());
        assertEquals(PlayerId.PLAYER_1, p2.next());
    }
}
