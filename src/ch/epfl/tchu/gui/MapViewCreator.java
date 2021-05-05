package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.PlayerId;
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

import java.util.List;

abstract class MapViewCreator {

    public static final int RECTANGLE_WIDTH = 36;
    public static final int RECTANGLE_HEIGHT = 12;
    public static final int CIRCLE_RADIUS = 3;
    public static final int CIRCLE_OFFSET = 6;

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

            mapView.getChildren().add(route);

//            route.setOnMouseClicked(e -> {
//                List<SortedBag<Card>> possibleClaimCards = observableGameState.possibleClaimCards(r);
//                ClaimRouteHandler claimRouteH = …;
//                ChooseCardsHandler chooseCardsH =
//                        chosenCards -> claimRouteH.onClaimRoute(route, chosenCards);
//                cardChooser.chooseCards(possibleClaimCards, chooseCardsH);
//            });
            observableGameState.routes(r).addListener((o, oV, nV) -> route.getStyleClass().add(nV.name()));
            route.disableProperty().bind(
                    claimRouteHandler.isNull().or(observableGameState.claimableRoutes(r).not()));
        }

//        claimRouteHandler.get().onClaimRoute();

        return mapView;

    }

    private static Group newCell() {
        Rectangle rectangle = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        rectangle.getStyleClass().add("filled");

        Circle circle1 = new Circle(RECTANGLE_WIDTH / 2 - CIRCLE_OFFSET, CIRCLE_OFFSET, CIRCLE_RADIUS);
        Circle circle2 = new Circle(RECTANGLE_WIDTH / 2 + CIRCLE_OFFSET, CIRCLE_OFFSET, CIRCLE_RADIUS);
        Group car = new Group(rectangle, circle1, circle2);
        car.getStyleClass().add("car");
        Rectangle way = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        way.getStyleClass().addAll("track", "filled");

        return new Group(way, car);
    }

    @FunctionalInterface
    interface CardChooser {
        void chooseCards(List<SortedBag<Card>> options, ChooseCardsHandler handler);
    }


}
