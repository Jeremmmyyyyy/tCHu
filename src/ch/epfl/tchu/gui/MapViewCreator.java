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

        for (Route r : ChMap.routes()) {
            Group route = new Group();
            route.setId(r.id());
            route.getStyleClass().addAll("route", r.level().toString(), r.color() != null ? r.color().toString() : "NEUTRAL");

            for (int i = 1; i <= r.length(); i++) {
                Group cell = newCell();
                cell.setId(String.format("%s_%s", r.id(), i));
                route.getChildren().add(cell);
            }

            System.out.printf("\n%s %s %s", r.id(), r.level().toString(), r.color() != null ? r.color().toString() : "NEUTRAL");
            mapView.getChildren().add(route);
        }

        return mapView;





    }

    private static Group newCell() {
        Rectangle rectangle = new Rectangle(36, 12);
        rectangle.getStyleClass().add("filled");

        Circle c1 = new Circle(12,6,3);
        Circle c2 = new Circle(24,6,3);
        Group car = new Group(rectangle, c1, c2);
        car.getStyleClass().add("car");
        Rectangle way = new Rectangle(36, 12);
        way.getStyleClass().addAll("track", "filled");
        return new Group(way, car);
    }
    @FunctionalInterface
    interface CardChooser {
        void chooseCards(List<SortedBag<Card>> options, ChooseCardsHandler handler);
    }


}
