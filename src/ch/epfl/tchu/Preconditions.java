package ch.epfl.tchu;

/**
 * Utility class for exception-checks
 *
 * @author Jérémy Barghorn (328403)
 */
public final class Preconditions{
    // not instantiable class
    private Preconditions() {}

    public static void checkArgument (boolean shouldBeTrue) {
        if(!shouldBeTrue){
            throw new IllegalArgumentException();
        }
    }
}