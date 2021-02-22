package ch.epfl.tchu.game;

import java.util.List;

public enum Card {

    BLACK("BLACK"),
    VIOLET("VIOLET"),
    BLUE("BLUE"),
    GREEN("GREEN"),
    YELLOW("YELLOW"),
    ORANGE("ORANGE"),
    RED("RED"),
    WHITE("WHITE"),
    LOCOMOTIVE("null");
    
    //blablabla

    public static final List<Card> ALL = List.of(Card.values());
    public static final int COUNT = ALL.size();
    public final static List<Card> CARS = ALL.subList(0, ALL.size()-1);

    Card(Object color) {
//        this.toString();
    }
}
