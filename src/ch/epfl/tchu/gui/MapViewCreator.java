package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.gui.ActionHandlers.ChooseCardsHandler;
import ch.epfl.tchu.gui.ActionHandlers.ClaimRouteHandler;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

class MapViewCreator {

    public static Node createMapView(ObservableGameState observableGameState,
                                     ObjectProperty<ClaimRouteHandler> claimRouteHandler,
                                     CardChooser cardChooser) {
        Pane mapView = new Pane();
        mapView.getStylesheets().add("map.css");
        mapView.getStylesheets().add("colors.css");
        mapView.getChildren().add(new ImageView());

        List<Group> groups = new ArrayList<>();

        for (Route r : ChMap.routes()) {
            Rectangle rectangle = new Rectangle(36, 12);
            rectangle.getStyleClass().add("filled");
            Circle c1 = new Circle(12,6,3);
            Circle c2 = new Circle(24,6,3);
            Group car = new Group(rectangle, c1, c2);
            car.getStyleClass().add("car");
            Rectangle way = new Rectangle(36, 12);
            way.getStyleClass().addAll("track", "filled");
            Group g2 = new Group(way, car);
            g2.setId(r.id());
            Group route = new Group(g2);
            route.setId(r.id());
            System.out.println(r.id() + " " + r.level().toString() + " " + r.color().toString());
            route.getStyleClass().addAll("route", "UNDERGROUND", "NEUTRAL");
            groups.add(route);
        }
        mapView.getChildren().addAll(groups);

        return mapView;





    }

    @FunctionalInterface
    interface CardChooser {
        void chooseCards(List<SortedBag<Card>> options, ChooseCardsHandler handler);
    }


}
