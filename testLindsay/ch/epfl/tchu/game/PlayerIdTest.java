package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerIdTest {
    @Test
    void PlayerIdValuesAreDefinedInTheRightOrder() {
        var expectedValues = new PlayerId[]{
                PlayerId.PLAYER_1, PlayerId.PLAYER_2
        };
        assertArrayEquals(expectedValues, PlayerId.values());
    }

    @Test
    void PlayerIdIsDefinedCorrectly() {
        assertEquals(List.of(PlayerId.values()), PlayerId.ALL);
    }

    @Test
    void PlayerIdCountIsDefinedCorrectly() {
        assertEquals(2, PlayerId.COUNT);
    }
}