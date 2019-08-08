/**
 * The BigTwoCard class is a subclass of the Card class, and is used to model a card used in a
 * Big Two card game
 *
 * @author Pranav
 */
public class BigTwoCard extends Card{
    /**
     * Creates and returns an instance of the BigTwoCard class.
     *
     * @param suit
     *            an int value between 0 and 3 representing the suit of a card:
     *            <p>
     *            0 = Diamond, 1 = Club, 2 = Heart, 3 = Spade
     * @param rank
     *            an int value between 0 and 12 representing the rank of a card:
     *            <p>
     *            0 = 'A', 1 = '2', 2 = '3', ..., 8 = '9', 9 = '0', 10 = 'J', 11
     *            = 'Q', 12 = 'K'
     */
    public BigTwoCard(int suit, int rank){
        super(suit, rank);
    }

    /**
     * a method for comparing the order of this card with the
     * specified card.
     *
     * @param card
     *            the card to be compared
     *
     * @return Returns a negative integer, zero, or a positive integer as this card is less
     * than, equal to, or greater than the specified card.
     */
    @Override
    public int compareTo(Card card) {
        if (this.rank == 1 && card.rank != 1){
            return 1;
        }
        else if (this.rank != 1 && card.rank == 1){
            return -1;
        }
        else if (this.rank == 0 && card.rank != 0){
            return 1;
        }
        else if (this.rank != 0 && card.rank == 0){
            return -1;
        }
        else {
            return super.compareTo(card);
        }
    }

    /**
     * A function to get the modified rank of the cards
     * according to the bigTwo game logic
     *
     * @return int object with rank according to the bigTwo game logic
     */
    public int getBigTwoRank(){
        return this.getRank() < 2 ? this.getRank() + 13 : this.getRank();
    }
}
