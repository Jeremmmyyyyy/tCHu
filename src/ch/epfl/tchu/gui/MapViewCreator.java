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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.List;

/**
 * Abstract class that creates the view of the graphical map (package private)
 *
 * @author Yann Ennassih (329978)
 */
final class MapViewCreator {

    private static final int RECTANGLE_WIDTH = 36;
    private static final int RECTANGLE_HEIGHT = 12;
    private static final int CIRCLE_RADIUS = 3;
    private static final int CIRCLE_OFFSET = 6;

    public static final String NEUTRAL = "NEUTRAL";

    //In order to make the class non instantiable
    private MapViewCreator(){
        throw new UnsupportedOperationException();}

    /**
     * Creates the view of the map
     * @param observableGameState considered
     * @param claimRouteHandler considered
     * @param cardChooser considered
     * @return the view of the map as a new JavaFX node
     */
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
                List<SortedBag<Card>> possibleClaimCards = observableGameState.possibleClaimCards(r);

                if (possibleClaimCards.size() == 1) {
                    claimRouteHandler.get().onClaimRoute(r, possibleClaimCards.get(0));
                } else {
                    cardChooser.chooseCards(observableGameState.possibleClaimCards(r),
                            chosenCards -> claimRouteHandler.get().onClaimRoute(r, chosenCards));
                }});

            //When a player claims or attempts to claim a route
            observableGameState.routes(r).addListener((o, oV, nV) -> {
                //route.setStyle("-colorPlayer1 : " + Color.RED);
                //route.setStyle("-colorPlayer2 : " + Color.BLUE);
//                route.getStylesheets().add("launcher.css");
                //route.setStyle("-my-background1:" + "red"); //TODO
                //route.setStyle("-my-background2:" + "blue");
                //route.getStyleClass().add(nV.name() + "red");
                route.getStyleClass().add(nV.name());
            });
            route.disableProperty().bind(claimRouteHandler.isNull().or(observableGameState.claimableRoute(r).not()));

            mapView.getChildren().add(route);
        }
        return mapView;

    }

    /**
     * Called by createMapView(...), creates a node attached to a given route
     * @param r the given route
     * @return a new JavaFX group for the given route
     */
    private static Group createRouteGroup(Route r) {
        Group route = new Group();
        route.setId(r.id());
        route.getStyleClass().addAll(
                "route", r.level().toString(), r.color() != null ? r.color().toString() : NEUTRAL);

        return route;
    }

    /**
     * Called by createMapView(...), initializes the main mapView pane
     * @return a new JavaFX pane for the mapView
     */
    private static Pane createMapViewPane() {
        Pane mapView = new Pane();
        mapView.getStylesheets().addAll("map.css", "colors.css");
        mapView.getChildren().add(new ImageView());

        return mapView;
    }

    /**
     * Called by createMapView(...), constructs a new route cell
     * @return a new JavaFX group for a new route cell
     */
    private static Group newCell() {
        Rectangle rectangle = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        rectangle.getStyleClass().add("filled");
        Circle circle1 = new Circle(RECTANGLE_WIDTH / 2. - CIRCLE_OFFSET, CIRCLE_OFFSET, CIRCLE_RADIUS);
        Circle circle2 = new Circle(RECTANGLE_WIDTH / 2. + CIRCLE_OFFSET, CIRCLE_OFFSET, CIRCLE_RADIUS);

        Group car = new Group(rectangle, circle1, circle2);
        car.getStyleClass().add("car");
        Rectangle way = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        way.getStyleClass().addAll("track", "filled");

        return new Group(way, car);
    }

    /**
     * Interface for the player's card-chose strategy as chooseCards(...) function
     */
    @FunctionalInterface
    interface CardChooser {
        void chooseCards(List<SortedBag<Card>> options, ChooseCardsHandler handler);
    }


}
