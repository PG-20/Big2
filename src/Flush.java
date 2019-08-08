/**
 * A class to emulate a Flush hand.
 * This hand consists of five cards with the same suit.
 *
 * @author Pranav
 */
public class Flush extends Hand {

    /**
     * a constructor for building a hand
     * with the specified player and list of cards.
     * simply calls the constructor of the Hand class
     *
     * @param player - current player
     * @param cards - list of cards to build hand with
     */
    public Flush(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    /**
     * Returns the validity of a hand.
     *
     * @return boolean whether the hand constructed is a valid instance of the Flush class
     */
    @Override
    public boolean isValid() {
        int suit = this.getCard(0).getSuit();
        for (int i=1 ; i < 5 ; i++ ){
            if (this.getCard(i).getSuit() != suit){
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
        return "Flush";
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
        if (hand instanceof Flush)
            return this.getTopCard().suit == hand.getTopCard().suit
                ? super.beats(hand)
                : this.getTopCard().suit > hand.getTopCard().suit;
        else return (hand instanceof Straight);
    }
}
