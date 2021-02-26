package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

public final class Station {

    private final int id;
    private final String name;

    Station(int id, String name) {
        Preconditions.checkArgument(id >= 0);
        this.id = id;
        this.name = name;
    }

    public int id(){
        return id;
    }

    public String name(){
        return name;
    }

    @Override
    public String toString(){
        return name;
    }
}
