public class Card 
{
    private Value value;
    private Col color;

    public Card(Value val, Col col)
    {
        if(col == Col.black)
        {
            if(val != Value.plus4 && val != Value.pickColor)
                throw new IllegalArgumentException("Card can't be created, Illegal value / color combination!");
        }
        else
        {
            if(val == Value.plus4 || val == Value.pickColor)
                throw new IllegalArgumentException("Card can't be created, Illegal value / color combination!");
        }
        value = val;
        color = col;
    }

    public Value getValue()
    {
        return value;
    }

    public Col getColor()
    {
        return color;
    }

    public static boolean isStackable(Card a, Card b)
    {
        boolean tmp = false;
        if(a.value == b.value)
            tmp = true;
        if(a.color == b.color)
            tmp = true;
        if(a.color == Col.black)
            tmp = true;
        return tmp;
    }

    public static boolean equals(Card a, Card b)
    {
        return a.value == b.value && a.color == b.color;
    }

    public String toString()
    {
        String tmp = "";
        tmp = "val : " + value + "  col : " + color;
        return tmp;
    }
}