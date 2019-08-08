/**
 * A class to emulate the Pair hand.
 * This hand consists all four cards of the same rank
 * and one card with different rank
 *
 * @author Pranav
 */
public class Quad extends Hand {

    /**
     * a constructor for building a hand
     * with the specified player and list of cards.
     * simply calls the constructor of the Hand class
     *
     * @param player - current player
     * @param cards - list of cards to build hand with
     */
    public Quad(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    /**
     * a method for retrieving the top card of this hand.
     *
     * @return the topCard of the specific hand
     */
    @Override
    public Card getTopCard() {
        return this.getCard(0).getRank() == this.getCard(1).getRank() ? this.getCard(3) : this.getCard(4);
    }

    /**
     * Returns the validity of a hand.
     *
     * @return boolean whether the hand constructed is a valid instance of the Quad class
     */
    @Override
    public boolean isValid() {
        int quadRank = this.getCard(2).getRank();
        int equalityCount = 0;
        for (int i = 0; i < 5; i++){
            if (this.getCard(i).getRank() == quadRank) equalityCount++;
        }
        return (equalityCount == 4);
    }

    /**
     * Used to get the type of Hand in String format
     *
     * @return type of Hand
     */
    @Override
    public String getType() {
        return "Quad";
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
        if (hand instanceof Quad) return super.beats(hand);
        else return !(hand instanceof StraightFlush);
    }
}
