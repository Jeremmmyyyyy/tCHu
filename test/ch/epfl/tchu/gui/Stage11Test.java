package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Map;
import java.util.Random;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public final class Stage11Test extends Application {
    public static void main(String[] args) { launch(args); }

    //TODO carte sans couleur dans un des deck des joueurs : du coup carte sans couleur permet de chopper n'importe quelle route PEUT ETRE REGLER MANQUAIT UN NEUTRAL DANS DECKVIEW
    //TODO COULEUR NOIRE : SE COMPORTE COMME UNE LOCOMOTIVE VOIR SCREEN BUGCOULEURNOIRE

    //TODO SAUT DE LIGNE APRES "C'est a Kevin de jouer" ??

    //TODO quand decide de ne pas choisir de cartes additionnelles peut encore jouer et produit une erreur

    //TODO points avec les billets pas pris en compte au cours du jeu quand on remplit l'objectif d'un billet....

    //TODO points casse quand on prend une route de taille 4 fait + 5 au lieu de + 4


    @Override
    public void start(Stage primaryStage) {
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        Map<PlayerId, String> names = Map.of(PLAYER_1, "Kevin", PLAYER_2, "Jordan");
        Map<PlayerId, Player> players =
                Map.of(PLAYER_1, new GraphicalPlayerAdapter(), PLAYER_2, new GraphicalPlayerAdapter());
        Random rng = new Random();
        new Thread(() -> Game.play(players, names, tickets, rng)).start();
    }
}