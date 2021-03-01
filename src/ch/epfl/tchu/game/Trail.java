package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;
/**
 * A Trail composed by one or more routes
 *
 * @author Yann Ennassih (329978)
 * @author Jérémy Barghorn (328403)
 */
public final class  Trail {
    private int length;
    private Station station1;
    private Station station2;

    public static Trail longest(List<Route> routes){
        while(!routes.isEmpty()){

        }
        return null;
    }

    public int length(){
        return length;
    }

    public Station station1(){
        if(station1.equals(null)){
            return null;
        }
        return station1;
    }

    public Station station2(){
        if(station2.equals(null)){
            return null;
        }
        return station2;
    }
}
