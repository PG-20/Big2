/**
 * A class to emulate the Straight hand.
 * This hand consists of five cards with consecutive ranks. For the sake of
 * simplicity, 2 and A can only form a straight with K but not with 3
 *
 * @author Pranav
 */
public class Straight extends Hand {

    /**
     * a constructor for building a hand
     * with the specified player and list of cards.
     * simply calls the constructor of the Hand class
     *
     * @param player - current player
     * @param cards - list of cards to build hand with
     */
    public Straight(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    /**
     * Returns the validity of a hand.
     *
     * @return boolean whether the hand constructed is a valid instance of the Straight class
     */

    @Override
    public boolean isValid() {
        for (int i = 1; i < 5 ; i++) {
            if (((BigTwoCard) this.getCard(i)).getBigTwoRank() - ((BigTwoCard) this.getCard(i-1)).getBigTwoRank() != 1){
                return false;
            }
        }
        return true;
    }

    /**
     * Used to get the type of Hand in String format
     *
     * @return type of Hand
     */
    @Override
    public String getType() {
        return "Straight";
    }

    /**
     * A method for checking if this hand beats a specified hand.
     *
     * @param hand
     *              the hand to be compared with
     *
     * @return boolean - whether current hands beats given hand
     */
    @Override
    public boolean beats(Hand hand) {
        if (hand instanceof Straight) return super.beats(hand);
        else return false;
    }
}
