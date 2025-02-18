import java.util.*;
public class Deck
{
    Stack<Card> deck;
    
    public Deck(){
        String[] suites = {"♠", "♥", "♦", "♣"};
        deck = new Stack<Card>();
        for(int i = 0; i < suites.length; i++){
            for(int j = 2; j <=14; j++ ){
                deck.push(new Card(j, suites[i]));
            }
        }
        
    }
    public void shuffleHelper(){
        Random rand = new Random();
        Stack<Card>left = new Stack<Card>();
        Stack<Card>right = new Stack<Card>();
        ArrayList<Card>list = new ArrayList<Card>(deck);
        int size = deck.size();
        for(int i = 0; i < size/2; i++){
            left.push(deck.pop());
        }
        while(deck.size() != 0){
            right.push(deck.pop());
        }
        for(int i = 0; i < size/2; i++){
            int amountLeft = rand.nextInt(3) + 1;
            int amountRight = rand.nextInt(3) + 1;
            int j = 0;
            while(j < amountLeft && !left.isEmpty()){
                deck.push(left.pop());
                j++;
            }
            j = 0;
            while(j < amountRight && !right.isEmpty()){
                deck.push(right.pop());
                j++;
            }
           
        }
        
        
    }
    public Card draw(){
        return deck.pop();
    }
    public void shuffle(int times){
        for(int i = 0; i < times; i++){
            shuffleHelper();
        }

        //while(!deck.isEmpty()){
            //System.out.println((deck.pop()));
        //}
    }
    public static void main(String[] args){
        Card.setUpMap();
    }
}
