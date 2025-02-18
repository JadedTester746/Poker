import java.util.*;
public class Card implements Comparable
{
    int value;
    String face = "";
    String suite;
    public static HashMap<Integer, String>Names;
    public Card(int value, String suite){
        Card.setUpMap();
        this.face = Names.get(value);
        this.value = value;
        this.suite=suite;
        
    }
    public String toString(){
        return Names.get(value) + suite;
    }
    public static void setUpMap(){
        Names = new HashMap<Integer, String>();
        Names.put(11, "Jack of "); Names.put(12, "Queen of "); Names.put(13, "King of "); Names.put(14, "Ace of");
        for(int i = 2; i <= 10; i++){
            Names.put(i, ""+i);
        }
    }
    
    public int compareTo(Object temp){
        Card c = (Card)temp;
        return this.value - c.value;
    }
    
    public boolean equals(Card c){
        return c.suite.equals(suite) && c.value == value;
    }
}
