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
        Preconditions.checkArgument( count >= 0 && count <= tickets.size());
        return tickets.topCards(count);
    }

    public GameState withoutTopTickets(int count) {
        Preconditions.checkArgument(count >= 0 && count <= tickets.size());
        return new GameState(tickets.withoutTopCards(count), cardState, currentPlayerId(), playerState, lastPlayer());
    }

    public Card topCard() {
        Preconditions.checkArgument(cardState.isDeckEmpty());
        return cardState.topDeckCard();
    }

    public GameState withoutTopCard() {
        Preconditions.checkArgument(cardState.isDeckEmpty());
        return new GameState(tickets, cardState.withoutTopDeckCard(), currentPlayerId(), playerState, lastPlayer());
    }

    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards) {
        return new GameState(tickets, cardState.withMoreDiscardedCards(discardedCards),
                currentPlayerId(), playerState, lastPlayer());
    }

    public GameState withCardsDeckRecreatedIfNeeded(Random rng) {
        if (cardState.isDeckEmpty()) {
            return new GameState(tickets, cardState, currentPlayerId(), playerState, lastPlayer());
        }
        return new GameState(tickets, cardState.withDeckRecreatedFromDiscards(rng),
                currentPlayerId(), playerState, lastPlayer());
    }


}
