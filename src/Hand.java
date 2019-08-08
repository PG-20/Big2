/**
 * The Hand class is a subclass of the CardList class,
 * and is used to model a hand of cards.
 *
 * @author Pranav
 */
public abstract class Hand extends CardList {
    private CardGamePlayer player;

    /**
     * a constructor for building a hand
     * with the specified player and list of cards.
     *
     * @param player - current player
     * @param cards - list of cards to build hand with
     */
    public Hand(CardGamePlayer player, CardList cards){
        this.player = player;
        for (int i = 0; i < cards.size(); i++){
            this.addCard(cards.getCard(i));
        }
        this.sort();
    }

    /**
     * abstract function implemented in child classes
     * Returns the validity of a specific Hand
     *
     * @return validity of Hand
     */
    public abstract boolean isValid();

    /**
     * abstract function implemented in child classes
     * Used to get the type of Hand in String format
     *
     * @return type of Hand
     */
    public abstract String getType();

    /**
     * A method for retrieving the player of this hand.
     *
     * @return the player which the hand belongs to
     */
    public CardGamePlayer getPlayer() {
        return player;
    }

    /**
     * a method for retrieving the top card of this hand.
     *
     * @return the topCard of the specific hand
     */
    public Card getTopCard(){
        return this.getCard(this.size()-1);
    }

    /**
     * A method for checking if this hand beats a specified hand.
     *
     * @param hand
     *              the hand to be compared with
     *
     * @return boolean - whether current hands beats given hand
     */
    public boolean beats(Hand hand) {
        return this.getTopCard().compareTo(hand.getTopCard()) > 0;
    }
}
