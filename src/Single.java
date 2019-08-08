/**
 * A class to emulate the Single hand.
 * This hand consists of a single card.
 *
 * @author Pranav
 */
public class Single extends Hand{

    /**
     * a constructor for building a hand
     * with the specified player and list of cards.
     * simply calls the constructor of the Hand class
     *
     * @param player - current player
     * @param cards - list of cards to build hand with
     */
    public Single(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    /**
     * Returns the validity of a hand.
     *
     * @return boolean whether the hand constructed is a valid instance of the Single class
     */
    @Override
    public boolean isValid() {
        return true;
    }

    /**
     * Used to get the type of Hand in String format
     *
     * @return type of Hand
     */
    @Override
    public String getType() {
        return "Single";
    }
}