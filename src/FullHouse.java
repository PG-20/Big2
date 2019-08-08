/**
 * A class to emulate a FullHouse Hand.
 * This hand consists of five cards, with two having
 * the same rank and three having another same rank
 *
 * @author Pranav
 */
public class FullHouse extends Hand {

    /**
     * a constructor for building a hand
     * with the specified player and list of cards.
     * simply calls the constructor of the Hand class
     *
     * @param player - current player
     * @param cards - list of cards to build hand with
     */
    public FullHouse(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    /**
     * Returns the validity of a hand.
     *
     * @return boolean whether the hand constructed is a valid instance of the FullHouse class
     */
    @Override
    public boolean isValid() {
        int rankLow = this.getCard(0).getRank(), rankHigh = this.getCard(4).getRank();
        int lowCount = 1 , highCount = 1;
        for (int i = 1; i < 4; i++){
            if (this.getCard(i).getRank() == rankHigh) highCount++;
            else if (this.getCard(i).getRank() == rankLow) lowCount++;
            else return false;
        }
        return ((highCount == 2 || lowCount == 2) && (highCount + lowCount == 5));
    }

    /**
     * a method for retrieving the top card of this hand.
     *
     * @return the topCard of the specific hand
     */
    @Override
    public Card getTopCard() {
        return this.getCard(2).getRank() == this.getCard(0).getRank() ? this.getCard(2) : this.getCard(4);
    }

    /**
     * Used to get the type of Hand in String format
     *
     * @return type of Hand
     */
    @Override
    public String getType() {
        return "FullHouse";
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
        if (hand instanceof FullHouse) return super.beats(hand);
        else return ((hand instanceof Straight) || (hand instanceof Flush));
    }
}
