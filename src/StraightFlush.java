/**
 * A class to emulate the StraightFlush hand.
 * This hand consists of five cards with consecutive ranks and the same
 * suit. For the sake of simplicity, 2 and A can only form a straight flush with K but not
 * with 3
 *
 * @author Pranav
 */
public class StraightFlush extends Hand {

    /**
     * a constructor for building a hand
     * with the specified player and list of cards.
     * simply calls the constructor of the Hand class
     *
     * @param player - current player
     * @param cards - list of cards to build hand with
     */
    public StraightFlush(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    /**
     * Returns the validity of a hand.
     *
     * @return boolean whether the hand constructed is a valid instance of the StraightFlush class
     */
    @Override
    public boolean isValid() {
        int suit = this.getCard(0).getSuit();
        for (int i=1 ; i < 5 ; i++ ){
            if ((this.getCard(i).getSuit() != suit)
                    || (((BigTwoCard) this.getCard(i)).getBigTwoRank() - ((BigTwoCard) this.getCard(i-1)).getBigTwoRank() != 1))
            {
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
        return "StraightFlush";
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
        if (hand instanceof StraightFlush) return super.beats(hand);
        else return true;
    }
}
