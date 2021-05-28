package ch.epfl.tchu.thomas.computer;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ComputerPlayer implements Player {

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    private static final int TURN_LIMIT = 1000;

    private final Random rng;
    // Toutes les routes de la carte
    private final List<Route> allRoutes = ChMap.routes();

    private int turnCounter;
    private PlayerState ownState;
    private PublicGameState publicGameState;
    private long randomSeed;

    // Lorsque nextTurn retourne CLAIM_ROUTE
    private Route routeToClaim;
    private SortedBag<Card> initialClaimCards;
    private Map<PlayerId, String> playerNames;
    private PlayerId ownId;
    private SortedBag<Ticket> tickets;

    //infoList
    private final ArrayList<String> infos = new ArrayList<>();

    public ComputerPlayer(Long randomSeed) {
        this.randomSeed = randomSeed;
        System.out.println("Computer Started");
        this.rng = new Random(randomSeed);
        this.turnCounter = 0;
    }

    public void getAllInfosFromGameState(){
        infos.clear();
        player();
        ticketAmount();
        tickets();
        cardAmount();
        cards();
        routes();
        carAmount();
        points();
        faceUpCardsDeckSize();
    }

    public void player(){
        Preconditions.checkArgument(publicGameState != null);
        if (publicGameState.currentPlayerId() == ownId) {
            infos.add("1.0|0.0");
        } else {
            infos.add("0.0|1.0");
        }
    }

    public void ticketAmount(){
        Preconditions.checkArgument(publicGameState != null);
        infos.add(Double.toString(publicGameState.playerState(ownId).ticketCount()));
        infos.add(Double.toString(publicGameState.playerState(ownId.next()).ticketCount()));
    }

    public void tickets(){
        List<Ticket> allTickets = ChMap.tickets();
        List<Ticket> test = ownState.tickets().toList();
        if (ownState.tickets().size() != 0){
//            for (Ticket ticket : allTickets) {
//                infos.add(ownState.tickets().contains(ticket)  ? "1.0" : "0.0");
//            }
            for (Ticket ticket : allTickets) {
                if (test.contains(ticket)){
                    infos.add("1.0");
                    test.remove(ticket);
                }
                else {
                    infos.add("0.0");
                }
            }
//            for (int i = 0; i<allTickets.size() - 8; ++i) {
//                infos.add(ownState.tickets().contains(allTickets.get(i))  ? "1.0" : "0.0");
//            }
//            System.out.println(ownState.tickets());
//            int count = 0;
//            for (Ticket ticket : allTickets) {
//                if (count != ownState.tickets().size()-1){
//                    infos.add((ownState.tickets().get(count).compareTo(ticket) == 0) ?  "1.0" : "0.0");
//                    ++count;
//                }
//            }
        }
    }

    public void cardAmount(){
        infos.add(Double.toString(publicGameState.playerState(ownId).cardCount()));
        infos.add(Double.toString(publicGameState.playerState(ownId.next()).cardCount()));
    }

    public void cards(){
        for (Card card : Card.ALL) {
            if (ownState.cards().contains(card)){
                infos.add(Double.toString(ownState.cards().countOf(card)));
            }else{
                infos.add("0.0");
            }
        }
    }

    public void routes(){
        for (Route route : allRoutes) {
            if (publicGameState.playerState(ownId).routes().contains(route)){
                infos.add("1.0");
            }else if(publicGameState.playerState(ownId.next()).routes().contains(route)){
                infos.add("2.0");
            }else{
                infos.add("0.0");
            }
        }
    }

    public void carAmount(){
        infos.add(Double.toString(publicGameState.playerState(ownId).carCount()));
        infos.add(Double.toString(publicGameState.playerState(ownId.next()).carCount()));
    }

    public void points(){
        infos.add(Double.toString(publicGameState.playerState(ownId).claimPoints()));
        infos.add(Double.toString(publicGameState.playerState(ownId.next()).claimPoints()));
        infos.add(Double.toString(ownState.finalPoints()));
    }

    public void faceUpCardsDeckSize(){
        SortedBag<Card>  test = SortedBag.of(publicGameState.cardState().faceUpCards());
        for (Card card : Card.ALL) {
            if (test.contains(card)){
                infos.add(Double.toString(test.countOf(card)));
            }else{
                infos.add("0.0");
            }
        }
        infos.add(Double.toString(publicGameState.cardState().deckSize()));
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

        getAllInfosFromGameState();
        if (infos.size() == 163){
            System.out.println(String.join( "|", infos));
        }
//        for (int i = 0; i < infos.size(); i++) {
//            System.out.printf("%3d|", i);
//        }

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
}