
public enum Hand
{
    ROYAL_FLUSH, 
    STRAIGHT_FLUSH, 
    FOUR, 
    FULL_HOUSE, 
    FLUSH, 
    STRAIGHT, 
    THREE, 
    TWO_PAIR, 
    PAIR, 
    HIGH;
    
    public static double winProbability(Hand hand){
        return (9 - hand.ordinal()) * 10;
    }
}
