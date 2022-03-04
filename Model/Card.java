/**
 * Class representing a standard UNO card with value and color
 */

package Model;

import java.io.Serializable;

public class Card implements Serializable {
    private Value value;
    private Col color;

    /**
     * Creates a new card with specified value and color
     * @author Dag Brynildsen Tholander
     * @param val
     * @param col
     */
    public Card(Value val, Col col) {
        // Check for illegal color/value combos
        if (col == Col.black) {
            if (val != Value.plus4 && val != Value.pickColor)
                throw new IllegalArgumentException("Card can't be created, Illegal value / color combination!");
        } else {
            if (val == Value.plus4 || val == Value.pickColor)
                throw new IllegalArgumentException("Card can't be created, Illegal value / color combination!");
        }
        value = val;
        color = col;
    }

    public Value getValue() {
        return value;
    }

    public Col getColor() {
        return color;
    }

    public void setColor(Col c) {
        color = c;
    }

    /**
     * Checks if one card is stackable on another card¨
     * @author Dag Brynildsen Tholander
     * @param a
     * @param b
     * @return Returns true if a is stackable on b and vice versa
     */
    public static boolean isStackable(Card a, Card b) {
        boolean tmp = false;
        if (a.value == b.value)
            tmp = true;
        if (a.color == b.color)
            tmp = true;
        if (a.color == Col.black || b.color == Col.black)
            tmp = true;
        return tmp;
    }

    /**
     * checks if one card equals another card
     * @author Dag Brynildsen Tholander
     * @param a
     * @param b
     * @return true if a and b are equal value and color
     */
    public static boolean equals(Card a, Card b) {
        if (a.color == Col.black || b.color == Col.black) {
            return a.value == b.value;
        }
        return a.value == b.value && a.color == b.color;
    }

    public String toString() {
        String tmp = "";
        tmp = "val : " + value + "  col : " + color;
        return tmp;
    }
}