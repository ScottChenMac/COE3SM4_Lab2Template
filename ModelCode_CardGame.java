package model1;

import java.util.*;

public class ModelCode_CardGame {

    public static final int POCKETSIZE = 25;
    public static Scanner myInputScanner;          
    
    public static void main(String args[]) throws Exception
    {        
        CardPool myCardPool;
        Card[] aiCards, myCards, tempCards;        
        Hands aiHand, myHand;

        // HandsMaxHeap aiMaxHeap;
        HandsBST aiBST;

        HandsRBT myRBT;
                
        int aiPocketSize = POCKETSIZE, myPocketSize = POCKETSIZE;
        int aiScore = 0, playerScore = 0; 
        
        myCardPool = new CardPool();        
        //myCardPool.printPool();        

        aiCards = new Card[aiPocketSize];
        aiCards = myCardPool.getRandomCards(POCKETSIZE);
        sortCards(aiCards, POCKETSIZE);
        
            
        // Repeat until out of cards
        // Lab 1 Demo: Must Always Generate the Best 5-card Set sequences - Aggressive Max Approach
        /*
        for(int i = 0; aiPocketSize > 4; i++)
        {            
            System.out.println("AI Pocket Cards:");
            for(int j = 0; j < aiPocketSize; j++)
            {            
                aiCards[j].printCard();
            }
            System.out.println();

            myMaxHeap = new HandsMaxHeap(2*aiPocketSize * aiPocketSize);
            generateHandsIntoHeap(aiCards, aiPocketSize);               
            //myMaxHeap.printHeap();     
            
            System.out.printf("%dst Hand: ", (i+1));
            
            if(myMaxHeap.getSize() > 0)
            {
                // Put out the Max Hand
                aiHand = myMaxHeap.removeMax();                            
            }
            else // If Heap Size == 0 after recomputation, put together a random hand
            {
                aiHand = new Hands(aiCards[0], aiCards[1], aiCards[2], aiCards[3], aiCards[4]);
            }
            aiHand.printMyHand();

            // Remove the Cards used in my Move, Recompute Max Heap
            tempCards = new Card[aiPocketSize - 5];
            for(int j = 0, k = 0; j < aiPocketSize; j++)
            {
                if( !aiCards[j].isMyCardEqual(aiHand.getCard(0)) &&
                    !aiCards[j].isMyCardEqual(aiHand.getCard(1)) &&
                    !aiCards[j].isMyCardEqual(aiHand.getCard(2)) &&
                    !aiCards[j].isMyCardEqual(aiHand.getCard(3)) &&
                    !aiCards[j].isMyCardEqual(aiHand.getCard(4)) )
                {
                    tempCards[k++] = aiCards[j];
                }
            }
            aiCards = tempCards;
            aiPocketSize -= 5;
        }
        */
        

        // Lab 2 - Turn-base AI (Aggresive) vs Player

        myCards = new Card[POCKETSIZE];        
        myCards = myCardPool.getRandomCards(POCKETSIZE);     
        sortCards(myCards, POCKETSIZE);        

        myRBT = new HandsRBT();
        generateHandsIntoTree(myCards, myPocketSize, myRBT);    

        // next, confirm that you can complete consume all the hands in RBT
        for(int round = 0; round < 5; round++)
        {
            // Print Both AI and Player Pocket Cards for Strategy Analysis
            System.out.println("AI Pocket Cards:");
            for(int j = 0; j < aiPocketSize; j++)
            {            
                aiCards[j].printCard();
            }
            System.out.println();
            
            System.out.printf("\nMy Pocket Cards (Count: %d)\n", myPocketSize);
            for(int i = 0; i < myPocketSize; i++)
            {            
                System.out.printf("[%d]", i+1);
                myCards[i].printCard();                                 
            }
            if(myRBT.getRoot() == null) System.out.println("\n OUT OF HANDS!");      
            
            
            // Get User to Generate Hand First
            while(true)
            {
                myHand = getUserHand(myCards, myPocketSize);
                if((myRBT.findNode(myHand) != null) || (myRBT.getRoot() == null)) break;
                System.out.println("Cannot Pass! You still have valid 5-card hands to make a move.");
            }
            
            System.out.printf("\nMy Hand: ");
            myHand.printMyHand();            
            myRBT.deleteInvalidHands(myHand);
           
            // Remove the Cards used in my Move, Remove from Tree
            tempCards = new Card[myPocketSize - 5];
            for(int j = 0, k = 0; j < myPocketSize; j++)
            {
                if( !myCards[j].isMyCardEqual(myHand.getCard(0)) &&
                    !myCards[j].isMyCardEqual(myHand.getCard(1)) &&
                    !myCards[j].isMyCardEqual(myHand.getCard(2)) &&
                    !myCards[j].isMyCardEqual(myHand.getCard(3)) &&
                    !myCards[j].isMyCardEqual(myHand.getCard(4)) )
                {
                    tempCards[k++] = myCards[j];
                }
            }

            myCards = tempCards;
            myPocketSize -= 5;


            // Then, AI generates its aggresive hand : Heap Version           
            // aiMaxHeap = new HandsMaxHeap(2 * aiPocketSize * aiPocketSize);
            // generateHandsIntoHeap(aiCards, aiPocketSize, aiMaxHeap);  

            // System.out.printf("\nAI Hand: ");
            
            // if(aiMaxHeap.getSize() > 0)
            // {
            //     // Put out the Max Hand
            //     aiHand = aiMaxHeap.removeMax();                            
            // }
            // else // If Heap Size == 0 after recomputation, put together a random hand
            // {
            //     aiHand = new Hands(aiCards[0], aiCards[1], aiCards[2], aiCards[3], aiCards[4]);
            // }
            // aiHand.printMyHand();

            // // Remove the Cards used in my Move, Recompute Max Heap
            // tempCards = new Card[aiPocketSize - 5];
            // for(int j = 0, k = 0; j < aiPocketSize; j++)
            // {
            //     if( !aiCards[j].isMyCardEqual(aiHand.getCard(0)) &&
            //         !aiCards[j].isMyCardEqual(aiHand.getCard(1)) &&
            //         !aiCards[j].isMyCardEqual(aiHand.getCard(2)) &&
            //         !aiCards[j].isMyCardEqual(aiHand.getCard(3)) &&
            //         !aiCards[j].isMyCardEqual(aiHand.getCard(4)) )
            //     {
            //         tempCards[k++] = aiCards[j];
            //     }
            // }
            // aiCards = tempCards;
            // aiPocketSize -= 5;

            // // Compare Hands
            // if(aiHand.isMyHandSmaller(myHand))
            // {
            //     System.out.println("\n [RESULT] Player Wins This Round!");
            //     playerScore++;
            // }
            // else if(aiHand.isMyHandLarger(myHand))
            // {
            //     System.out.println("\n [RESULT] AI Wins This Round!");
            //     aiScore++;
            // }
            // else
            // {
            //     System.out.println("\n [RESULT] Draw");
            // }

            // Then, AI generates its aggresive hand : BST Version           
            aiBST = new HandsBST();
            generateHandsIntoBST(aiCards, aiPocketSize, aiBST);  

            System.out.printf("\nAI Hand: ");
            
            if(!aiBST.isEmpty())
            {
                // Put out the Max Hand
                aiHand = aiBST.removeMaxHand();                            
            }
            else // If Heap Size == 0 after recomputation, put together a random hand
            {
                aiHand = new Hands(aiCards[0], aiCards[1], aiCards[2], aiCards[3], aiCards[4]);
            }
            aiHand.printMyHand();

            // Remove the Cards used in my Move, Recompute Max Heap
            tempCards = new Card[aiPocketSize - 5];
            for(int j = 0, k = 0; j < aiPocketSize; j++)
            {
                if( !aiCards[j].isMyCardEqual(aiHand.getCard(0)) &&
                    !aiCards[j].isMyCardEqual(aiHand.getCard(1)) &&
                    !aiCards[j].isMyCardEqual(aiHand.getCard(2)) &&
                    !aiCards[j].isMyCardEqual(aiHand.getCard(3)) &&
                    !aiCards[j].isMyCardEqual(aiHand.getCard(4)) )
                {
                    tempCards[k++] = aiCards[j];
                }
            }
            aiCards = tempCards;
            aiPocketSize -= 5;

            // Compare Hands
            if(aiHand.isMyHandSmaller(myHand))
            {
                System.out.println("\n [RESULT] Player Wins This Round!");
                playerScore++;
            }
            else if(aiHand.isMyHandLarger(myHand))
            {
                System.out.println("\n [RESULT] AI Wins This Round!");
                aiScore++;
            }
            else
            {
                System.out.println("\n [RESULT] Draw");
            }
        }

        // finally, build a turn-based game
        // AI and Player gets 25 cards each, 5 turns.  Player and AI Score init to zero.
        // In each turn
        //      Print AI Cards, then Numbered My Cards
        //      Player makes a choice - proceed when valid
        //      AI makes a move, compare Player's hand vs. AI's hand
        //      Update score
        //      Players and AI cannot make INVALID moves until they are out of valid hands
        // At the end of the game, report winner with score

        System.out.printf("\n ==== Game Result ====\n");
        System.out.printf("Player Score: %d\n", playerScore);
        System.out.printf("AI Score: %d\n", aiScore);
        
        if(aiScore > playerScore) System.out.printf("\n<< AI Won >>\n");
        else if(aiScore < playerScore) System.out.printf("\n<< Player Won >>\n");
        else System.out.printf("\n<< Draw >>\n");   
        System.out.printf("\n\n =====================\n\n");     

        myInputScanner.close();
    }

    // public static void generateHandsIntoHeap(Card[] thisHand, int pocketSize, HandsMaxHeap thisHeap)
    // {
    //     if(pocketSize <= 5) return;
        
    //     Hands currentHand;
    //     int i1 = 0, i2 = i1 + 1, i3 = i2 + 1, i4 = i3 + 1, i5 = i4 + 1;
        
    //     for(i1 = 0; i1 < i2; i1++)
    //         for(i2 = i1 + 1; i2 < i3; i2++)
    //             for(i3 = i2 + 1; i3 < i4; i3++)
    //                 for(i4 = i3 + 1; i4 < i5; i4++)
    //                     for(i5 = i4 + 1; i5 < pocketSize; i5++)
    //                     {
    //                         currentHand = new Hands(thisHand[i1], thisHand[i2], thisHand[i3], thisHand[i4], thisHand[i5]);
    //                         if(currentHand.isAValidHand())
    //                         {
    //                             thisHeap.insert(currentHand);
    //                         }                         
    //                     }
    // }

    public static void generateHandsIntoBST(Card[] thisHand, int pocketSize, HandsBST thisBST)
    {
        if(pocketSize <= 5) return;
        
        Hands currentHand;
        int i1 = 0, i2 = i1 + 1, i3 = i2 + 1, i4 = i3 + 1, i5 = i4 + 1;
        
        for(i1 = 0; i1 < i2; i1++)
            for(i2 = i1 + 1; i2 < i3; i2++)
                for(i3 = i2 + 1; i3 < i4; i3++)
                    for(i4 = i3 + 1; i4 < i5; i4++)
                        for(i5 = i4 + 1; i5 < pocketSize; i5++)
                        {
                            currentHand = new Hands(thisHand[i1], thisHand[i2], thisHand[i3], thisHand[i4], thisHand[i5]);
                            if(currentHand.isAValidHand())
                            {
                                thisBST.insert(currentHand);
                            }                         
                        }
    }

    public static void generateHandsIntoTree(Card[] thisHand, int pocketSize, HandsRBT thisRBT)
    {
        if(pocketSize <= 5) return;
        
        Hands currentHand;
        int i1 = 0, i2 = i1 + 1, i3 = i2 + 1, i4 = i3 + 1, i5 = i4 + 1;
        
        for(i1 = 0; i1 < i2; i1++)
            for(i2 = i1 + 1; i2 < i3; i2++)
                for(i3 = i2 + 1; i3 < i4; i3++)
                    for(i4 = i3 + 1; i4 < i5; i4++)
                        for(i5 = i4 + 1; i5 < pocketSize; i5++)
                        {
                            currentHand = new Hands(thisHand[i1], thisHand[i2], thisHand[i3], thisHand[i4], thisHand[i5]);                            
                            if(currentHand.isAValidHand())
                            {                                
                                thisRBT.insert(currentHand);                                
                            }                            
                        }
    }

    public static void sortCards(Card[] cards, int size)
    {
        int j;
        Card temp;        

        for(int i = 1; i < size; i++) 
        { 
            temp = cards[i];		
            for(j = i; j > 0 && cards[j-1].isMyCardLarger(temp); j--) 
                cards[j] = cards[j-1]; 
            cards[j] = temp;
        }  
    }

    public static Hands getUserHand(Card[] myCards, int size)
    {
        int[] mySelection = new int[5];  
        myInputScanner = new Scanner(System.in);

        System.out.println();
        for(int i = 0; i < 5; i++)
        {            
            System.out.printf("Card Choice #%d: ", i + 1);
            mySelection[i] = myInputScanner.nextInt() - 1;
            if(mySelection[i] > size) mySelection[i] = size - 1;
            if(mySelection[i] < 0) mySelection[i] = 0;            
        }
        
        Hands newHand = new Hands(  myCards[mySelection[0]], 
                                    myCards[mySelection[1]], 
                                    myCards[mySelection[2]], 
                                    myCards[mySelection[3]], 
                                    myCards[mySelection[4]]);

        return newHand;
    }

}
