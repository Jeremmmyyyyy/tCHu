// Rigel stage 1

package ch.epfl.test;

import ch.epfl.tchu.Preconditions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PreconditionsTest {
    @Test
    void checkArgumentSucceedsForTrue() {
        Preconditions.checkArgument(true);
    }

    @Test
    void checkArgumentFailsForFalse() {
        assertThrows(IllegalArgumentException.class, () -> {
            Preconditions.checkArgument(false);
        });
    }
}
