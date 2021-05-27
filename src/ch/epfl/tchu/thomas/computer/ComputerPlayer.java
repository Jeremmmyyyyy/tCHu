package ch.epfl.tchu.thomas.computer;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class ComputerPlayer implements Player {

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    private String tab[];
    private static final int TURN_LIMIT = 1000;

    private final Random rng;
    // Toutes les routes de la carte
    private final List<Route> allRoutes;

    private int turnCounter;
    private PlayerState ownState;
    private PublicGameState publicGameState;

    // Lorsque nextTurn retourne CLAIM_ROUTE
    private Route routeToClaim;
    private SortedBag<Card> initialClaimCards;
    private Map<PlayerId, String> playerNames;
    private PlayerId ownId;
    private SortedBag<Ticket> tickets;
    private int numberOfInfoReceived = 0;
    private boolean hasDrawnTickets = false;

    public ComputerPlayer(long randomSeed, List<Route> allRoutes) {
        System.out.println("Computer Started");
        this.rng = new Random(randomSeed);
        this.allRoutes = List.copyOf(allRoutes);
        this.turnCounter = 0;
    }

    public ArrayList<String> getAllInfosFromGameState(){
        ArrayList<String> infos = new ArrayList<>();
//        infos.addAll(player());
//        infos.addAll(ticketAmount());
        infos.addAll(tickets());
        return infos;
    }

    public ArrayList<String> player(){
        Preconditions.checkArgument(publicGameState != null);
        ArrayList<String> player = new ArrayList<>();
        if (publicGameState.currentPlayerId() == ownId) {
            player.add("1.0|0.0");
        } else {
            player.add("0.0|1.0");
        }

        return player;
    }

    public ArrayList<String> ticketAmount(){
        Preconditions.checkArgument(publicGameState != null);
        ArrayList<String> tickets = new ArrayList<>();

        tickets.add(Double.toString(publicGameState.playerState(ownId).ticketCount()));
        tickets.add(Double.toString(publicGameState.playerState(ownId.next()).ticketCount()));

        return tickets;
    }

    public ArrayList<String> tickets(){
        ArrayList<String> tickets = new ArrayList<>();
        List<Ticket> allTickets = ChMap.tickets();
        int count = 0;
        System.out.println(ownState.tickets());
        if (ownState.tickets().size() != 0){
            for (Ticket ticket : allTickets) {

                if (ticket.compareTo(ownState.tickets().get(count)) == 0 && count < ownState.tickets().size()){
                    System.out.println("count : " + count + " " + ownState.tickets().get(count));
                    tickets.add("1.0");
                    count += 1;

                }else{
                    tickets.add("0.0");
                }
            }
        }

//        for (int i = 0; i < allTickets.size(); i++) {
//            System.out.printf("|%3d", i);
//        }
//        System.out.println();

        return tickets;
    }


    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        this.ownId = ownId;
        this.playerNames = playerNames;
    }

    @Override
    public void receiveInfo(String info) {
//        ++numberOfInfoReceived;
//        PrintToTxt.writeToFile(info);
//            System.out.println(numberOfInfoReceived + " | " + info);
//        if(info.contains("remporte la victoire avec")){
//            tab = info.split(" ");
//        }
//        getAllInfosFromGameState();
    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        this.publicGameState = newState;
        this.ownState = ownState;

        ArrayList<String> oldInfos = getAllInfosFromGameState();
        if (oldInfos != getAllInfosFromGameState()){
            System.out.println("|" + (String.join("|", getAllInfosFromGameState())));
            System.out.println();
        }

    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        List<Ticket> chosenTickets = new ArrayList<>();
        for (int i = 0; i < getRandomNumber(3, 5); ++i) {
            chosenTickets.add(tickets.get(i));
        }
        return SortedBag.of(chosenTickets);
    }


    @Override
    public TurnKind nextTurn() {
        turnCounter += 1;
        if (turnCounter > TURN_LIMIT)
            throw new Error("Trop de tours joués !");

        // Détermine les routes dont ce joueur peut s'emparer
        List<Route> claimableRoutes = new ArrayList<>();
        for (Route r : allRoutes) {
            if (ownState.canClaimRoute(r)) {
                claimableRoutes.add(r);
            }
        }
        if (claimableRoutes.isEmpty()) {
//                System.out.println("DRAW_CARDS ");
            if (!publicGameState.canDrawTickets()) {
//                PrintToTxt.writeToFile("DRAW_CARDS "+ "\n");
                return TurnKind.DRAW_CARDS;
            }
            else {
//                PrintToTxt.writeToFile("DRAW_TICKETS "+ "\n");
                return TurnKind.DRAW_TICKETS;
            }
        } else {
            int routeIndex = rng.nextInt(claimableRoutes.size());
            Route route = claimableRoutes.get(routeIndex);
            List<SortedBag<Card>> cards = ownState.possibleClaimCards(route);

            routeToClaim = route;
            initialClaimCards = cards.get(0);

//            PrintToTxt.writeToFile("CLAIM_ROUTE = Route : " + route.id() + " | Level : "
//                    + route.level() + " | InitialClaimCards : " + initialClaimCards+ "\n");
//            PrintToTxt.writeToFile("Deck: " + publicGameState.cardState().deckSize()+
//                    "| Discard: " + publicGameState.cardState().discardsSize() +
//                    "| Cards: " + ownState.cards()+
//                    "| AvailableCars: " + publicGameState.playerState(ownId).carCount() +
//                    " | faceUpCards " + publicGameState.cardState().faceUpCards()+ "\n");
//            publicGameState.claimedRoutes().forEach((s)-> PrintToTxt.writeToFile(s.id() + "|"));
//            PrintToTxt.writeToFile("\n");


            return TurnKind.CLAIM_ROUTE;
        }
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        List<Ticket> chosenTickets = new ArrayList<>();
        for (int i = 0; i < getRandomNumber(1, 3); ++i) {
            chosenTickets.add(options.get(i));
        }
        return SortedBag.of(chosenTickets);
    }

    @Override
    public int drawSlot() {
//            return -1;
        return getRandomNumber(-1, 4);
    }

    @Override
    public Route claimedRoute() {
        return routeToClaim;
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        return initialClaimCards;
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        SortedBag<Card> chosenAdditionalCards = options.get(getRandomNumber(0, options.size() - 1));
//            System.out.println("ChosenAdditionalCards : " + chosenAdditionalCards);
//        PrintToTxt.writeToFile("ChosenAdditionalCards : " + chosenAdditionalCards+ "\n");
        return chosenAdditionalCards;
    }

    @Override
    public String toString() {
        return playerNames.get(ownId);
    }

    public String[] tab() {
        return tab;
    }
}