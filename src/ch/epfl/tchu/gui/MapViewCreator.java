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

import java.util.List;

/*

 */
abstract class MapViewCreator {

    public static final int RECTANGLE_WIDTH = 36;
    public static final int RECTANGLE_HEIGHT = 12;
    public static final int CIRCLE_RADIUS = 3;
    public static final int CIRCLE_OFFSET = 6;

    public static Node createMapView(ObservableGameState observableGameState,
                                     ObjectProperty<ClaimRouteHandler> claimRouteHandler,
                                     CardChooser cardChooser) {

        Pane mapView = createMapViewPane();

        for (Route r : ChMap.routes()) {

            Group route = createRouteGroup(r);

            //Loop for each cell of the route
            for (int i = 1; i <= r.length(); i++) {
                Group cell = newCell();
                cell.setId(String.format("%s_%s", r.id(), i));
                route.getChildren().add(cell);
            }

            //Handles a mouse click event
            route.setOnMouseClicked(e -> {
                cardChooser.chooseCards(observableGameState.possibleClaimCards(r),
                        chosenCards -> claimRouteHandler.get().onClaimRoute(r, chosenCards));
            });

            //When a player claims or attempts to claim a route
            observableGameState.routes(r).addListener((o, oV, nV) -> route.getStyleClass().add(nV.name()));
            route.disableProperty().bind(claimRouteHandler.isNull().or(observableGameState.claimableRoutes(r).not()));

            mapView.getChildren().add(route);
        }

        return mapView;

    }

    private static Group createRouteGroup(Route r) {
        Group route = new Group();
        route.setId(r.id());
        route.getStyleClass().addAll("route",
                r.level().toString(),
                r.color() != null ? r.color().toString() : "NEUTRAL");

        return route;
    }

    private static Pane createMapViewPane() {
        Pane mapView = new Pane();
        mapView.getStylesheets().add("map.css");
        mapView.getStylesheets().add("colors.css");
        mapView.getChildren().add(new ImageView());

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
