import java.util.*;
public class Poker
{
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        System.out.println("Hello gambler! Lets play some poker");
        System.out.println("Whats your name?");
        Player humanDummy = new Player(scan.next());
        System.out.printf("Hello %s how many AI would you like to play against? (max of 8)", humanDummy.name);
        int numChatGPTs = scan.nextInt(); scan.nextLine();
        String[] names = {"ChatGPT", "LLama", "Claude", "Gemini", "DeepSeek", "Jenson Huang", "O3", "Clippy"};
        Player[] chatGPTs = new Player[numChatGPTs];
        for(int i = 0; i < numChatGPTs; i++){
            chatGPTs[i] = new Player(names[i]);
        }
        boolean gameFlag = false;
        do{
            for(Player p : chatGPTs){
                if(p.money < 1000){
                    p.money = 1000;
                }
            }
            System.out.println("dealing cards");
            Deck deck = new Deck();
            deck.shuffle(500);
            humanDummy.setCards(deck.draw(), deck.draw(), deck.draw(), deck.draw(), deck.draw());
            for(Player p : chatGPTs){
                p.setCards(deck.draw(), deck.draw(), deck.draw(), deck.draw(), deck.draw());
            }
            humanDummy.printDeck();
            System.out.println("Cards have been dealt! Look at your deck(sorted for your ease) and select which indexs you want to switch");
            System.out.println("Just give a comma seperated list 1-5, you do up to 4 or type NONE");
            String in = scan.nextLine();
            if(!in.equals("NONE")){
                String[] nums = in.replace(" ", "").split(",");
                HashSet<Integer> seen = new HashSet<Integer>();
                for(String s : nums){
                    int num = Integer.parseInt(s);
                    if(!seen.contains(num) && num > 0 && num < 6 && seen.size()  < 4){
                        Card old = humanDummy.swap(deck.draw(), num - 1);
                        deck.deck.push(old);
                        deck.shuffle(500);
                    }
                    else{
                        System.out.println("You gave us bad numbers, we'll ignore it this time, but try harder");
                    }
                }    
            }
            int pool = 0;
            pool += 50 * (numChatGPTs + 1);
            humanDummy.money -= 50;
            humanDummy.folded = false;
            Player lastToRaise = null;
            boolean gamblingFlag = false;
            int currentBet = 50;
            for(Player p : chatGPTs){
                if(Hand.winProbability(p.getHand()) < 50){
                    Player copy = p.clone();
                    ArrayList<Card> cards = new ArrayList<Card>();
                    String[] suites = {"♠", "♥", "♦", "♣"};
                    for(int i = 0; i < suites.length; i++){
                        for(int j = 2; j <=14; j++ ){
                            Card c = new Card(j, suites[i]);
                            boolean flag = false;
                            for(Card card : p.cards){
                                if(card.equals(c)){
                                    flag = true;
                                }
                            }
                            if(!flag){
                                cards.add(c);                            
                            }

                        }
                    }
                    ArrayList<Integer> indexsToSwap1 = new ArrayList<>();
                    Hand baseHand = p.getHand();
                    ArrayList<Integer> finalSwaps = new ArrayList<>();
                    int max1 = 0;
                    boolean done = false;
                    for(int i = 0; i < 5; i++){
                        copy.cards = p.cards.clone();
                        for(Card c : cards){
                            copy.cards[i] = c;
                            int num = (int)Hand.winProbability(copy.getHand());
                            if(num > max1){
                                max1 = num; 
                                indexsToSwap1.clear(); 
                                indexsToSwap1.add(i);
                            }
                        }
                    }
                    int max2 = 0;
                    ArrayList<Integer> indexsToSwap2 = new ArrayList<Integer>();
                    for(int i = 0; i < 5; i++){
                        for(Card c : cards){
                            copy.cards[i] = c;
                            ArrayList<Card> cardsCopy = (ArrayList<Card>)cards.clone();
                            cardsCopy.remove(c);
                            for(int j = i+1; j < 5; j++){
                                for(Card c1 : cardsCopy){
                                    copy.cards[j] = c1;
                                    int num = (int)Hand.winProbability(copy.getHand());
                                    if(num > max2){
                                        max2 = num;
                                        indexsToSwap2.clear();
                                        indexsToSwap2.add(i);
                                        indexsToSwap2.add(j);
                                    }
                                }
                                copy.cards[j] = p.cards[j];
                            }    
                        }
                        copy.cards[i] = p.cards[i];

                    }
                    if(Hand.winProbability(baseHand) < max1 && Hand.winProbability(baseHand) < max2){
                        if(max1 + 20 > max2){
                            finalSwaps = indexsToSwap1;
                        }
                        else{
                            finalSwaps = indexsToSwap2;
                        }
                    }

                    ArrayList<Card> removedCards =new ArrayList<>();
                    for(int i : finalSwaps){
                        Card old = p.swap(deck.draw(), i);
                        removedCards.add(old);
                    }
                    for(Card c : removedCards){
                        deck.deck.push(c);
                    }
                    deck.shuffle(500);
                }

                p.money -= 50;
                p.folded = false;
            }
            humanDummy.printDeck();
            System.out.println("Ok, heres your new deck. Buy in was automatically deducted from your balance, its time to go gambling");
            System.out.println("Heres how this will work:\nyou can raise, call, fold, check or ALL IN.  If you go all in, then you don't need to worry about calling ... you have bigger problems\nraise needs to be followed by a number, if its greater than what you have its assumed you're going all in. Gambling ends when no one raises in a turn");
            while(!gamblingFlag){
                if(lastToRaise == humanDummy){
                    gamblingFlag = true; break;
                }
                if(!humanDummy.folded && humanDummy.money > 0){

                    System.out.printf("Current Pool: %d Current Money: %d Current Bet: %d What would you like to do \n", pool, humanDummy.money, currentBet);
                    String choice = scan.nextLine();

                    if(choice.toLowerCase().matches("raise [0-9]+")){
                        int amount = Integer.parseInt(choice.substring(choice.indexOf(" ") + 1));
                        if(humanDummy.money > 0){
                            lastToRaise = humanDummy;
                            int temp = humanDummy.raise(amount);  
                            currentBet += temp;
                            pool += temp;
                        }
                        else{
                            System.out.println("Sorry, you can't do that. We're skipping you");
                        }

                    }
                    if(choice.toLowerCase().equals("all in")){
                        if(humanDummy.money > 0){
                            lastToRaise = humanDummy;
                            int temp = humanDummy.raise(humanDummy.money);  
                            currentBet += temp;
                            pool += temp;
                        }
                        else{
                            System.out.println("Sorry, you can't do that. We're skipping you");
                        }

                    }
                    if(choice.toLowerCase().equals("call")){
                        int temp = humanDummy.raise(currentBet - humanDummy.bet);
                        pool += temp;
                    }
                    if(choice.toLowerCase().equals("fold")){
                        humanDummy.folded = true;
                    }
                    if(lastToRaise == null){lastToRaise = humanDummy;}
                }
                for(Player p : chatGPTs){
                    //Ai Decisions
                    if(lastToRaise == p){
                        gamblingFlag = true;
                        break;
                    }
                    Hand hand = p.getHand();
                    if(!p.folded && p.money > 0){
                        double potOdds = (((double)currentBet)/(pool + currentBet)) * 100;
                        double winProb = Hand.winProbability(hand);
                        int temp = 0;
                        if(winProb < 20 && winProb < potOdds){
                            //fold
                            p.folded = true;
                            System.out.println(p.name + " folded");
                        }
                        else if(winProb >= 20 && winProb <= 40 && potOdds > winProb){
                            //call
                            temp = p.raise(currentBet - p.bet);
                            pool += temp;
                            System.out.println(p.name + " called");
                        }
                        else if(winProb >= 60){
                            lastToRaise = p;
                            temp = p.raise(p.money);  
                            currentBet += temp;
                            pool += temp;
                            System.out.println(p.name + " went all in!");
                        }
                        else {
                            int baseRaise = (int)(((potOdds)/100) * 0.75 * pool);

                            if(winProb >= 50){
                                temp = p.raise(baseRaise);
                                currentBet += temp;
                                pool += temp;
                                System.out.println(p.name + " raised " + temp);
                                lastToRaise = p;
                            }
                            else if(winProb >= 30 && p.bet <= p.money - 200){
                                temp = p.raise(baseRaise/2);
                                currentBet += temp;
                                pool += temp;
                                System.out.println(p.name + " raised " + temp);
                                lastToRaise = p;
                            }
                            else if(p.bet <= p.money){
                                temp = p.raise(baseRaise/3);
                                currentBet += temp;
                                pool += temp;
                                System.out.println(p.name + " raised " + temp);
                                lastToRaise = p;
                            }
                            else{
                                temp = p.raise(currentBet - p.bet);
                                pool += temp;
                                System.out.println(p.name + " called");
                            }

                        }
                    }
                }
            }

            System.out.printf("Current Pool: %d calculating the winner\n", pool);
            Player[] players = new Player[numChatGPTs + 1];
            for(int i = 0; i < numChatGPTs; i++){
                players[i] = chatGPTs[i];
            }
            players[numChatGPTs] = humanDummy;
            Arrays.sort(players);
            Player winner = null;
            for(int i = players.length - 1; i >= 0; i--){
                if(winner == null && !players[i].folded){
                    winner = players[i];
                }
            }

            winner.money += pool;
            System.out.println(winner.name + " Won!!!!!(but the house really won)");
            for(Player p : players){
                p.printDeck();
                System.out.println("" + p.name + " " + p.getHand());
            }
            if(humanDummy.money == 0){
                System.out.println("Sorry gambler, you can't win them all. But don't forget, the house always wins unless you do! Go to the bank and get some more money!");
                gameFlag = true;
            }
            else{
                System.out.println("Would you like to play again? y/n");
        
                String answer = scan.nextLine();
                
                if(!answer.equals("y")){
                    gameFlag = true;
                }
            }
        }while(!gameFlag);
    }
}

