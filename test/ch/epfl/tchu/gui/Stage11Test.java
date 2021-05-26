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

    //TODO quand decide de ne pas choisir de cartes additionnelles peut encore jouer et produit une erreur et bloque l'interface
    //TODO c'est bon ne crash plus mais bloque le jeu
    //reglé fallait rajouter un true dans le builder des chooseAdditionalCards du GraphicalPlayer mais ducoup true ???
    //Non faut laisser le bouton choisir actif, le joueur peut choisir de ne pas jouer de cartes supplementaires meme s'il en a la possibilite

    //TODO normal que quand toutes les cartes soient prises, on ne puisse plus prendre de faceUpCards ???
    //je pense que c'est normal mais quand meme petit doute

    //TODO points affiches au cours de la partie : seulement les claim points des routes et non des billes deja remplis ?
    //c'est normal il compte que à la fin en meme temps que les bonus

    //TODO 2.2.4 etape Interface graphique : le jeu doit proposer une selection par defaut ??

    //TODO on peut selectionner deux choix d'additional Cards pour initialClaimCards aussi : normal ??

    //TODO egalité bonus aux deux ??



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