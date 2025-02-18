import java.util.*;
public class Player implements Comparable
{
    String name;
    Card[] cards;
    int money;
    int bet;
    boolean folded;
    public Player(String name){
        this.name = name;
        cards = new Card[5];
        money = 1000;
        bet = 0;
    }

    public void setCards(Card... cards){
        if(cards.length == 5){
            this.cards = cards;
            Arrays.sort(cards);
        }
    }

    public static void main(String[] args){
        Player p = new Player("asdf");
        p.cards = new Card[5];
        p.cards[0] = new Card(2, "♦");
        p.cards[1] = new Card(2, "♠");
        p.cards[2] = new Card(2, "♠");
        p.cards[3] = new Card(2, "♠");
        p.cards[4] = new Card(14, "♠");
        System.out.println(p.getHand());
    }

    public Card swap(Card other, int i){
        Card out = cards[i];
        cards[i] = other;
        Arrays.sort(cards);
        return out;
    }

    public void printDeck(){
        System.out.println(Arrays.toString(cards));
    }

    public Hand getHand(){ //hee hee
        HashMap<Integer, Integer> freqMap = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> freqMapFreqMap = new HashMap<Integer, Integer>();
        for(int i = 2; i < 15; i++){
            freqMap.put(i, 0);
        }
        for(int i = 0; i < 5; i++){
            freqMapFreqMap.put(i, 0);
        }
        for(Card c : cards){
            freqMap.put(c.value, freqMap.get(c.value) + 1);    
        }
        //This is where the fun begins

        for(Map.Entry<Integer, Integer> entry : freqMap.entrySet()){
            freqMapFreqMap.put(entry.getValue(), freqMapFreqMap.get(entry.getValue())+1);    
        }

        if(cards[0].value == 10){
            boolean flag = true;
            for(int i =  1; i < 5; i++){
                if(cards[i].value != 10 + i && !cards[i].suite.equals(cards[i-1])){
                    flag = false;
                }
            }
            if(flag){
                return Hand.ROYAL_FLUSH;
            }
        }
        boolean flag = true;
        for(int i = 1; i < 5; i++){
            if(!cards[i].suite.equals(cards[i-1].suite)){
                flag = false;
            }
        }
        if(flag){
            if(freqMap.get(14) == 0){
                flag = true;
                for(int i = 1; i < 5; i++){
                    if(cards[i].value != cards[i-1].value + 1){
                        flag = false;
                    }
                }
                if(flag){
                    return Hand.STRAIGHT_FLUSH;
                }
            }
            if(freqMap.get(14) == 1){
                flag = true;
                if(cards[0].value == 2){
                    for(int i = 1; i < 4; i++){
                        if(cards[i].value != cards[i-1].value + 1){
                            flag = false;
                        }
                    }
                }
                else{
                    for(int i = 1; i < 5; i++){
                        if(cards[i].value != cards[i-1].value + 1){
                            flag = false;
                        }
                    }
                }
                if(flag){
                    return Hand.STRAIGHT_FLUSH;
                }
            }
        }

        if(freqMapFreqMap.get(4) != 0){
            return Hand.FOUR;
        }
        if(freqMapFreqMap.get(2) == 1 && freqMapFreqMap.get(3) == 1){
            return Hand.FULL_HOUSE;
        }

        flag = true;
        for(int i = 1; i < 5; i++){
            if(!cards[i].suite.equals(cards[i-1].suite)){
                flag = false;
            }
        }
        if(flag){
            return Hand.FLUSH;
        }

        if(freqMap.get(14) == 0){
            flag = true;
            for(int i = 1; i < 5; i++){
                if(cards[i].value != cards[i-1].value + 1){
                    flag = false;
                }
            }
            if(flag){
                return Hand.STRAIGHT;
            }
        }
        if(freqMap.get(14) == 1){
            flag = true;
            if(cards[0].value == 2){
                for(int i = 1; i < 4; i++){
                    if(cards[i].value != cards[i-1].value + 1){
                        flag = false;
                    }
                }
            }
            else{
                for(int i = 1; i < 5; i++){
                    if(cards[i].value != cards[i-1].value + 1){
                        flag = false;
                    }
                }
            }
            if(flag){
                return Hand.STRAIGHT;
            }
        }

        if(freqMapFreqMap.get(3) == 1){
            return Hand.THREE;
        }
        if(freqMapFreqMap.get(2) == 2){
            return Hand.TWO_PAIR;
        }
        if(freqMapFreqMap.get(2) == 1){
            return Hand.PAIR;
        }

        return Hand.HIGH;
    }

    public int max(){
        int max = 0;
        for(Card c: cards){
            if(c.value > max){
                max = c.value;
            }
        }
        return max;
    }

    public int compareTo(Object other){
        Player p = (Player)other;

        if(this.getHand() != p.getHand()){
            return -1 * (this.getHand().ordinal() - p.getHand().ordinal()); // times -1 because I put the hands in the wrong order and don't have time to change it
        }
        else{
            HashMap<Integer, Integer> thisfreqMap = new HashMap<Integer, Integer>();
            for(int i = 2; i < 15; i++){
                thisfreqMap.put(i, 0);
            }
            for(Card c : cards){
                thisfreqMap.put(c.value, thisfreqMap.get(c.value) + 1);
            }
            //This is where the fun begins
            HashMap<Integer, Integer> otherfreqMap = new HashMap<Integer, Integer>();
            for(int i = 2; i < 15; i++){
                otherfreqMap.put(i, 0);
            }
            for(Card c : cards){
                otherfreqMap.put(c.value, otherfreqMap.get(c.value) + 1);
            }

            if(this.getHand() == Hand.FLUSH || this.getHand() == Hand.HIGH){
                return this.max() - p.max();
            }
            if(this.getHand() == Hand.FULL_HOUSE){// get highest three pair
                int thisValue = 0;
                for(Map.Entry<Integer, Integer> entry : thisfreqMap.entrySet()){
                    if(entry.getValue() == 3){
                        thisValue = entry.getValue();
                    }
                }

                int otherValue = 0;
                for(Map.Entry<Integer, Integer> entry : otherfreqMap.entrySet()){
                    if(entry.getValue() == 3){
                        otherValue = entry.getValue();
                    }
                }

                return thisValue - otherValue;
            }

        }
        return 0;
    }

    public int raise(int amount){
        if(amount <= money){
            bet += amount;
            money -= amount;
            return amount;
        }
        else{
            int temp = money;
            bet += money;
            money = 0;
            return temp;
        }

    }
    
    @Override
    public Player clone(){
        Player p = new Player(name);
        p.cards = cards.clone();
        return p;
    }
}
