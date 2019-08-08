/**
 * A class to emulate the Pair hand.
 * This hand consists of two cards with the same rank.
 *
 * @author Pranav
 */
public class Pair extends Hand {

    /**
     * a constructor for building a hand
     * with the specified player and list of cards.
     * simply calls the constructor of the Hand class
     *
     * @param player - current player
     * @param cards - list of cards to build hand with
     */
    public Pair(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    /**
     * Returns the validity of a hand.
     *
     * @return boolean whether the hand constructed is a valid instance of the Pair class
     */
    @Override
    public boolean isValid() {
        return (this.getCard(0).getRank() == this.getCard(1).getRank());
    }

    /**
     * Used to get the type of Hand in String format
     *
     * @return type of Hand
     */
    @Override
    public String getType() {
        return "Pair";
    }
}
