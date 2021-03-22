package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.lang.ref.SoftReference;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public final class GameState extends PublicGameState {

    private final Map<PlayerId, PlayerState> playerState;
    private final CardState cardState;
    private final Deck<Ticket> tickets;

    private GameState(Deck<Ticket> tickets, CardState cardState, PlayerId currentPlayerId, Map<PlayerId,
            PlayerState> playerState, PlayerId lastPlayer) {
        super(tickets.size(), cardState, currentPlayerId, Map.copyOf(playerState), lastPlayer); //TODO revoir la copie de map
        this.playerState = playerState;
        this.cardState = cardState;
        this.tickets = tickets; //TODO revoir rng = null
    }

    public static GameState initial(SortedBag<Ticket> tickets, Random rng) {
        Deck<Card> initialDeck = Deck.of(Constants.ALL_CARDS, rng);
        SortedBag<Card> initialPlayer1Cards = initialDeck.topCards(4);
        //TODO A CHECK COMMENT DISTRIBUER LES PREMIERES CARTES ET DONC INITIALISER LE PLAYERSTATE
        SortedBag<Card> initialPlayer2Cards = initialDeck.withoutTopCards(4).topCards(4);
        CardState initialCardState = CardState.of(initialDeck.withoutTopCards(8));
        Deck<Ticket> initialTickets = Deck.of(tickets, rng);
        Map<PlayerId, PlayerState> initialPlayerState = new EnumMap<>(PlayerId.class);
        initialPlayerState.put(PlayerId.PLAYER_1, PlayerState.initial(initialPlayer1Cards));
        initialPlayerState.put(PlayerId.PLAYER_2, PlayerState.initial(initialPlayer2Cards));
        PlayerId initialPlayer = PlayerId.ALL.get(rng.nextInt(2));
        return new GameState(initialTickets, initialCardState, initialPlayer, initialPlayerState, initialPlayer); //TODO A REVOIR

    }

    @Override
    public PlayerState playerState(PlayerId playerId) {
        return playerState.get(playerId);
    }

    @Override
    public PlayerState currentPlayerState() {
        return playerState.get(currentPlayerId());
    }

    public SortedBag<Ticket> topTickets(int count) {
        Preconditions.checkArgument( count >= 0 && count <= tickets.size()); //TODO NECESSAIRE ??????
        return tickets.topCards(count);
    }

    public GameState withoutTopTickets(int count) {
        Preconditions.checkArgument(count >= 0 && count <= tickets.size()); //TODO NECESSAIRE ??????
        return new GameState(tickets.withoutTopCards(count), cardState, currentPlayerId(), playerState, lastPlayer());
    }

    public Card topCard() {
        Preconditions.checkArgument(!cardState.isDeckEmpty());  //TODO NECESSAIRE ??????
        return cardState.topDeckCard();
    }

    public GameState withoutTopCard() {
        Preconditions.checkArgument(!cardState.isDeckEmpty()); //TODO NECESSAIRE ??????
        return new GameState(tickets, cardState.withoutTopDeckCard(), currentPlayerId(), playerState, lastPlayer());
    }

    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards) {
        return new GameState(tickets, cardState.withMoreDiscardedCards(discardedCards),
                currentPlayerId(), playerState, lastPlayer());
    }

    public GameState withCardsDeckRecreatedIfNeeded(Random rng) {
        //TODO VOIR LEXCEPTION THROWN PAR LA METHODE DE CARDSTATE
        if (cardState.isDeckEmpty()) {
            return new GameState(tickets, cardState, currentPlayerId(), playerState, lastPlayer());
        }
        return new GameState(tickets, cardState.withDeckRecreatedFromDiscards(rng),
                currentPlayerId(), playerState, lastPlayer());
    }

    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets) {
        for (Ticket chosenTicket: chosenTickets) {
            Preconditions.checkArgument(playerState.get(playerId).tickets().contains(chosenTicket));
        }
        Map<PlayerId, PlayerState> newPlayerState = new EnumMap<>(playerState); //TODO CONSTRUCTEUR DE COPIE ?????
        newPlayerState.replace(currentPlayerId(), playerState.get(playerId).withAddedTickets(chosenTickets));
        return new GameState(tickets, cardState, currentPlayerId(), newPlayerState, lastPlayer());
    }

    public boolean lastTurnBegins() {
        return false; //TODO a faire
    }

    public GameState forNextTurn() {
        return new GameState(tickets, cardState, lastPlayer(), playerState, lastPlayer());
    }


    public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets, SortedBag<Ticket> chosenTickets){
        Preconditions.checkArgument(drawnTickets.contains(chosenTickets));
        Map<PlayerId, PlayerState> newPlayerState = new EnumMap<>(playerState);
        newPlayerState.replace(currentPlayerId(), playerState.get(currentPlayerId()).withAddedTickets(chosenTickets));
        return new GameState(tickets.withoutTopCards(drawnTickets.size()), cardState, currentPlayerId(),
                newPlayerState, lastPlayer() );
    }

    public GameState withDrawnFaceUpCard(int slot){
        Preconditions.checkArgument(canDrawCards());
        Map<PlayerId, PlayerState> newPlayerState = new EnumMap<>(playerState);
        newPlayerState.replace(currentPlayerId(),
                playerState.get(currentPlayerId()).withAddedCard(cardState.faceUpCard(slot)));
        return new GameState(tickets, cardState.withDrawnFaceUpCard(slot), currentPlayerId(), newPlayerState, lastPlayer());
    }

    public GameState withBlindlyDrawnCard(){
        Preconditions.checkArgument(canDrawCards());
        Map<PlayerId, PlayerState> newPlayerState = new EnumMap<>(playerState);
        newPlayerState.replace(currentPlayerId(),
                playerState.get(currentPlayerId()).withAddedCard(cardState.topDeckCard()));
        return new GameState(tickets, cardState.withoutTopDeckCard(), currentPlayerId(), newPlayerState, lastPlayer());
    }

    public GameState withClaimedRoute(Route route, SortedBag<Card> cards){
        Map<PlayerId, PlayerState> newPlayerState = new EnumMap<>(playerState);
        newPlayerState.replace(currentPlayerId(),
                playerState.get(currentPlayerId()).withClaimedRoute(route, cards));
        return new GameState(tickets, cardState.withoutTopDeckCard(), currentPlayerId(), newPlayerState, lastPlayer());
        //TODO repetition de code sur les 3 méthodes précedentes à vérif
    }
}
