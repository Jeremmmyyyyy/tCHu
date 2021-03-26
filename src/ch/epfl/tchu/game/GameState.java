package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.lang.ref.SoftReference;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

/**
 * Represents a state of the game
 *
 * @author Jérémy Barghorn (328403)
 * @author Yann Ennassih (329978)
 */
public final class GameState extends PublicGameState {

    private final Map<PlayerId, PlayerState> playerState;
    private final CardState cardState;
    private final Deck<Ticket> tickets;

    /**
     * Private constructor for the class GameState
     * @param tickets not played yet
     * @param cardState card state of the game
     * @param currentPlayerId id of the current player
     * @param playerState each player state of the game
     * @param lastPlayerId id of the last player
     */
    private GameState(Deck<Ticket> tickets, CardState cardState, PlayerId currentPlayerId, Map<PlayerId,
            PlayerState> playerState, PlayerId lastPlayerId) {
        super(tickets.size(), cardState, currentPlayerId, Map.copyOf(playerState), lastPlayerId); //TODO revoir la copie de map
        this.playerState = Map.copyOf(playerState);
        this.cardState = cardState;
        this.tickets = tickets;
    }

    /**
     * Builds the first game state
     * @param tickets to be shuffled and put as a deck
     * @param rng used to shuffle the decks
     * @return a new GameState corresponding to the first state of the game
     */
    public static GameState initial(SortedBag<Ticket> tickets, Random rng) {
        Deck<Card> initialDeck = Deck.of(Constants.ALL_CARDS, rng);
        CardState initialCardState = CardState.of(initialDeck.withoutTopCards(2*Constants.INITIAL_CARDS_COUNT));
        Deck<Ticket> initialTickets = Deck.of(tickets, rng);

        SortedBag<Card> initialPlayer1Cards =
                initialDeck.topCards(Constants.INITIAL_CARDS_COUNT);
        SortedBag<Card> initialPlayer2Cards =
                initialDeck.withoutTopCards(Constants.INITIAL_CARDS_COUNT).topCards(Constants.INITIAL_CARDS_COUNT);// TODO marche pas renvoie deux fois les meme cartes
        Map<PlayerId, PlayerState> initialPlayerState = new EnumMap<>(PlayerId.class);
        initialPlayerState.put(PlayerId.PLAYER_1, PlayerState.initial(initialPlayer1Cards)); //liste
        initialPlayerState.put(PlayerId.PLAYER_2, PlayerState.initial(initialPlayer2Cards));

        PlayerId initialPlayer = PlayerId.ALL.get(rng.nextInt(2));

        return new GameState(initialTickets, initialCardState, initialPlayer, initialPlayerState, null);

    }

    /**
     * Getter for the state of a given player
     * @param playerId Id of the player
     * @return the PlayerState corresponding to the given id
     */
    @Override
    public PlayerState playerState(PlayerId playerId) {
        return playerState.get(playerId);
    }

    /**
     * Getter for the state of the current player
     * @return the state of the current player
     */
    @Override
    public PlayerState currentPlayerState() {
        return playerState.get(currentPlayerId());
    }

    /**
     * Returns a given amount of tickets from the deck
     * @param count number of top tickets to return
     * @return the first count tickets of the attribute tickets
     * @throws IllegalArgumentException if count isn't in [0,tickets.size()]
     */
    public SortedBag<Ticket> topTickets(int count) {
        Preconditions.checkArgument( count >= 0 && count <= tickets.size());
        return tickets.topCards(count);
    }

    /**
     * Returns a similar state to this without a given amount of top tickets
     * @param count number of tickets to remove
     * @return a new GameState similar to this, except the first count tickets of this.tickets
     * @throws IllegalArgumentException if count isn't in [0,tickets.size()]
     */
    public GameState withoutTopTickets(int count) {
        Preconditions.checkArgument(count >= 0 && count <= tickets.size());
        return new GameState(tickets.withoutTopCards(count), cardState, currentPlayerId(), playerState, lastPlayer());
    }

    /**
     * Getter for the top card of the current deck
     * @return the top card of the current deck
     * @throws IllegalArgumentException if the deck of card is empty
     */
    public Card topCard() {
        Preconditions.checkArgument(!cardState.isDeckEmpty());
        return cardState.topDeckCard();
    }

    /**
     * Returns a similar state to this without the top card of the deck
     * @return a new GameState similar to this without the top card of the deck
     * @throws IllegalArgumentException if the deck of card is empty
     */
    public GameState withoutTopCard() {
        Preconditions.checkArgument(!cardState.isDeckEmpty());
        return new GameState(tickets, cardState.withoutTopDeckCard(), currentPlayerId(), playerState, lastPlayer());
    }

    /**
     * Returns a similar state to this with cards added to the discard deck
     * @param discardedCards to add to the discard deck
     * @return a new GameState similar to this with more discarded cards
     */
    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards) {
        return new GameState(tickets, cardState.withMoreDiscardedCards(discardedCards),
                currentPlayerId(), playerState, lastPlayer());
    }

    /**
     * Returns a similar state to this where the discard deck becomes the card deck
     * @param rng to shuffle the discard deck
     * @return a new GameState similar to this where the discarded cards becomes the new deck of cards
     */
    public GameState withCardsDeckRecreatedIfNeeded(Random rng) {
        return new GameState(tickets, cardState.isDeckEmpty() ? cardState.withDeckRecreatedFromDiscards(rng): cardState,
                currentPlayerId(), playerState, lastPlayer());
    }

    /**
     * Returns a similar state to this where the given player has chosen the given tickets
     * @param playerId of the player who choosed the tickets
     * @param chosenTickets to add to the given player
     * @return  a new GameState similar to this with the chosenTickets given to the player of playerId
     * @throws IllegalArgumentException if the player has already one of the chosen tickets
     */
    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets) {
        for (Ticket chosenTicket: chosenTickets) {
            Preconditions.checkArgument(!playerState(playerId).tickets().contains(chosenTicket));
        }
        Map<PlayerId, PlayerState> newPlayerState = new EnumMap<>(playerState);
        newPlayerState.replace(currentPlayerId(), playerState(playerId).withAddedTickets(chosenTickets));
        return new GameState(tickets, cardState, currentPlayerId(), newPlayerState, lastPlayer());
    }


    /**
     * Checks if the last turn begins or not
     * @return true if the last turn begins (which occurs when the current player has at most 2 cars left to play)
     * false else
     */
    public boolean lastTurnBegins() {
        return lastPlayer() == null && currentPlayerState().carCount() <= 2;
    }

    /**
     * Returns the next state of the current state this
     * @return the new GameState that follows the current one (this)
     */
    public GameState forNextTurn() {
        return new GameState(tickets, cardState, currentPlayerId().next(), playerState,
                lastTurnBegins() ? currentPlayerId(): lastPlayer());
    }

    /**
     * Returns a similar state to this where the current player has chosen the given tickets out of the drawn tickets
     * @param drawnTickets drawn tickets by the current player
     * @param chosenTickets chosen tickets by the current player
     * @return a new GameState similar to this with the chosen tickets given to the current player
     * @throws IllegalArgumentException if chosenTickets is a subset of drawnTickets
     */
    public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets, SortedBag<Ticket> chosenTickets){
        Preconditions.checkArgument(drawnTickets.contains(chosenTickets));
        Map<PlayerId, PlayerState> newPlayerState = new EnumMap<>(playerState);
        newPlayerState.replace(currentPlayerId(), currentPlayerState().withAddedTickets(chosenTickets));
        return new GameState(tickets.withoutTopCards(drawnTickets.size()), cardState, currentPlayerId(),
                newPlayerState, lastPlayer() );
    }

    /**
     * Returns a similar state to this where the given face up card is picked by the current player and replaced by the
     * top card of the deck
     * @param slot of the face up card drawn by the player
     * @return a new GameState similar to this where the face up card of the given slot is drawn by the current player
     * and replaced by the top card of the deck
     * @throws IllegalArgumentException if deckSize + discardsSize < 5
     */
    public GameState withDrawnFaceUpCard(int slot){
        Preconditions.checkArgument(canDrawCards());
        Map<PlayerId, PlayerState> newPlayerState = new EnumMap<>(playerState);
        newPlayerState.replace(currentPlayerId(),
                currentPlayerState().withAddedCard(cardState.faceUpCard(slot)));
        return new GameState(tickets, cardState.withDrawnFaceUpCard(slot), currentPlayerId(), newPlayerState, lastPlayer());
    }

    /**
     * Returns a similar state to this where the top card of the deck is placed is the hand of the player
     * @return a new GameState similar to this where the top card of the deck is placed is the hand of the player
     * @throws IllegalArgumentException if deckSize + discardsSize < 5
     */
    public GameState withBlindlyDrawnCard(){
        Preconditions.checkArgument(canDrawCards());
        Map<PlayerId, PlayerState> newPlayerState = new EnumMap<>(playerState);
        newPlayerState.replace(currentPlayerId(),
                currentPlayerState().withAddedCard(cardState.topDeckCard()));
        return new GameState(tickets, cardState.withoutTopDeckCard(), currentPlayerId(), newPlayerState, lastPlayer());
    }

    /**
     * Returns a similar state to this where the current player claimed the given route thanks to the given cards
     * @param route
     * @param cards
     * @return a new GameState similar to this where the current player
     * claimed the given route thanks to the given cards
     */
    public GameState withClaimedRoute(Route route, SortedBag<Card> cards){
        Map<PlayerId, PlayerState> newPlayerState = new EnumMap<>(playerState);
        newPlayerState.replace(currentPlayerId(),
                currentPlayerState().withClaimedRoute(route, cards));
        return new GameState(tickets, cardState.withoutTopDeckCard(), currentPlayerId(), newPlayerState, lastPlayer());
        //TODO repetition de code sur les 3 méthodes précedentes à vérif
    }
}
