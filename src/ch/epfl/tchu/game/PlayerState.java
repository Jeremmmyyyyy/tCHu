package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;

public final class PlayerState extends PublicPlayerState{

    private final SortedBag<Ticket> tickets;
    private final SortedBag<Card> cards;

    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes) {
        super(tickets.size(), cards.size(), routes);
        this.tickets = tickets;
        this.cards  = cards;
    }

    public static PlayerState initial(SortedBag<Card> initialCards){
        Preconditions.checkArgument(initialCards.size() >= 4);
        return new PlayerState(SortedBag.of(), initialCards, List.of());
    }

    public SortedBag<Ticket> tickets(){
        return tickets;
    }

    public PlayerState withAddedTickets(SortedBag<Ticket> newTickets){
        return new PlayerState(tickets.union(newTickets), cards, routes());
    }

    public SortedBag<Card> cards(){
        return cards;
    }

    public PlayerState withAddedCards(SortedBag<Card> additionalCards){
        return new PlayerState(tickets, cards.union(additionalCards), routes());
    }

    public boolean canClaimRoute(Route route){
        for(SortedBag<Card> possibleCards : route.possibleClaimCards()){
            if(route.length() <= carCount() && cards.contains(possibleCards)){ // TODO vérif cette méthode
                return true;
            }
        }
        return false;
    }

    public List<SortedBag<Card>> possibleClaimCards(Route route){
        Preconditions.checkArgument(carCount() >= route.length());
        List<SortedBag<Card>> possibleClaimCards = new ArrayList<>();
        for(SortedBag<Card> possibleCards : route.possibleClaimCards()){
            if (cards.contains(possibleCards)){
                possibleClaimCards.add(possibleCards);
            }
        }
        return possibleClaimCards;
    }

    public List<SortedBag<Card>> possibleAdditionalCards(int additionalCardsCount, SortedBag<Card> initialCards, SortedBag<Card> drawnCards){




        return null;
    }
}
