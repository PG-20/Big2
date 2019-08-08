import javax.swing.*;
import java.awt.*;

/**
 * A class to model an Image of the cards
 *
 * @author Pranav
 */
public class CardIcon {
    private ImageIcon image;
    private int x;
    private int y;
    private boolean clicked;
    private int position;

    /**
     * Constructor to build an instance of the class
     *
     * @param image
     *              image of the card
     * @param position
     *              position of card in deck
     */
    public CardIcon(ImageIcon image, int position) {
        this.image = image;
        this.clicked = false;
        this.position = position;
    }

    /**
     * Returns position of card in deck
     *
     * @return the position of card in deck
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets position of card in deck
     *
     * @param position
     *              position of card in deck
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Sets the coordinates of the card
     *
     * @param x
     *          x co-ordinate
     * @param y
     *          y co-ordinate
     */
    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Checks if card is clicked
     *
     * @return
     *        clicked state
     */
    public boolean isClicked() {
        return clicked;
    }

    /**
     * Sets the clicked state of the card
     *
     * @param clicked
     *              clicked state
     */
    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    /**
     * Checks whether given point is inside bounds of a card
     *
     * @param x
     *          x co-ordinate
     * @param y
     *          y co-ordinate
     *
     * @return if point is inside or not
     */
    public boolean contains(int x, int y) {
        return (x > this.x && x < (this.x + image.getIconWidth()) &&
                y > this.y && y < (this.y + image.getIconHeight()));
    }

    /**
     * Returns x co-ordinate of card
     *
     * @return x co-ordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns y co-ordinate of card
     *
     * @return y co-ordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Draws the card image
     *
     * @param g
     *          graphics used to draw image
     * @param c
     *          component to draw the image on
     */
    public void draw(Graphics g, Component c) {
        image.paintIcon(c, g, x, y);
    }
}