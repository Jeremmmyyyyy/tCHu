package ch.epfl.tchu.thomas.computer;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import java.util.Map;
import java.util.Random;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public class ComputerVSComputer {
    public static void main(String[] args) {

        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());

        Map<PlayerId, String> names = Map.of(PLAYER_1, "Computer", PLAYER_2, "Computer");


        for (int i = 0; i < 100; i++) {
            Random rng = new Random();
            Map<PlayerId, Player> players =
                    Map.of(PLAYER_1, new ComputerPlayer(rng.nextLong()), PLAYER_2, new ComputerPlayer(rng.nextLong()));
            Game.play(players, names, tickets, rng);
        }
    }
}
