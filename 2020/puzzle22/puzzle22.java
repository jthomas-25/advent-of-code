import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

public class puzzle22 {
    static LinkedList<Integer> p1StartDeck;
    static LinkedList<Integer> p2StartDeck;
    static final int PLAYER1 = 1;
    static final int PLAYER2 = 2;
    static int numGames;
    static LinkedList<Integer> unfinishedGames;

    public static void main(String[] args) {
        try {
            String filename = args[0];
            File inputFile = new File(filename);
            Scanner stdin = new Scanner(inputFile);
            readStartDecks(stdin);
            stdin.close();
            LinkedList<Integer> p1Deck = new LinkedList<>();
            LinkedList<Integer> p2Deck = new LinkedList<>();
            setupDecks(p1Deck, p2Deck);
            playCombat(p1Deck, p2Deck);
            resetDecks(p1Deck, p2Deck);
            playRecursiveCombat(p1Deck, p2Deck);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void playCombat(LinkedList<Integer> p1Deck, LinkedList<Integer> p2Deck) {
        System.out.print("REGULAR COMBAT\n");
        int round = 1;
        int winner = 0;
        boolean gameOver = false;
        while (!gameOver) {
            if (p1Deck.isEmpty()) {
                winner = PLAYER2;
            }
            else if (p2Deck.isEmpty()) {
                winner = PLAYER1;
            }
            else {
                // play round
                System.out.print(String.format("\n-- Round %d --\n", round));
                printDecks(p1Deck, p2Deck);
                // draw cards
                int p1TopCard = p1Deck.removeFirst();
                int p2TopCard = p2Deck.removeFirst();
                System.out.print(String.format("Player 1 plays: %d\n", p1TopCard));
                System.out.print(String.format("Player 2 plays: %d\n", p2TopCard));
                // winner takes cards
                if (p1TopCard > p2TopCard) {
                    System.out.print(String.format("Player 1 wins round %d!\n", round));
                    p1Deck.addLast(p1TopCard);
                    p1Deck.addLast(p2TopCard);
                }
                else {
                    System.out.print(String.format("Player 2 wins round %d!\n", round));
                    p2Deck.addLast(p2TopCard);
                    p2Deck.addLast(p1TopCard);
                }
                // go to the next round
                round++;
            }
            gameOver = (winner == PLAYER1) || (winner == PLAYER2);
        }
        printPostgameResults(winner, p1Deck, p2Deck);
    }

    private static void playRecursiveCombat(LinkedList<Integer> p1Deck, LinkedList<Integer> p2Deck) {
        System.out.print("\nRECURSIVE COMBAT\n");
        numGames = 0;
        unfinishedGames = new LinkedList<>();
        int winner = playGame(p1Deck, p2Deck);
        printPostgameResults(winner, p1Deck, p2Deck);
    }

    private static int playGame(LinkedList<Integer> p1Deck, LinkedList<Integer> p2Deck) {
        numGames++;
        unfinishedGames.push(numGames);
        int game = unfinishedGames.peek();
        System.out.print(String.format("\n=== Game %d ===\n", game));
        ArrayList<LinkedList<Integer>> prevP1Decks = new ArrayList<>();
        ArrayList<LinkedList<Integer>> prevP2Decks = new ArrayList<>();
        int round = 1;
        int gameWinner = 0;
        boolean gameOver = false;
        while (!gameOver) {
            if (p1Deck.isEmpty()) {
                gameWinner = PLAYER2;
            }
            else if (p2Deck.isEmpty()) {
                gameWinner = PLAYER1;
            }
            else {
                System.out.print(String.format("\n-- Round %d (Game %d) --\n", round, game));
                // check previous rounds for this game
                boolean somePrevRoundHasSameConfiguration = somePrevDeckHasSameConfiguration(p1Deck, prevP1Decks) && somePrevDeckHasSameConfiguration(p2Deck, prevP2Decks);
                if (somePrevRoundHasSameConfiguration) {
                    gameWinner = PLAYER1;
                }
                else {
                    prevP1Decks.add(new LinkedList<Integer>(p1Deck));
                    prevP2Decks.add(new LinkedList<Integer>(p2Deck));
                    // play round as normal
                    int roundWinner = playRound(p1Deck, p2Deck);
                    System.out.print(String.format("Player %d wins round %d of game %d!\n", roundWinner, round, game));
                    // go to the next round
                    round++;
                }
            }
            gameOver = (gameWinner == PLAYER1) || (gameWinner == PLAYER2);
        }
        System.out.print(String.format("The winner of game %d is player %d!\n\n", game, gameWinner));
        unfinishedGames.pop();
        return gameWinner;
    }

    private static int playRound(LinkedList<Integer> p1Deck, LinkedList<Integer> p2Deck) {
        int roundWinner = 0;
        printDecks(p1Deck, p2Deck);
        // draw cards
        int p1TopCard = p1Deck.removeFirst();
        int p2TopCard = p2Deck.removeFirst();
        System.out.print(String.format("Player 1 plays: %d\n", p1TopCard));
        System.out.print(String.format("Player 2 plays: %d\n", p2TopCard));
        if (p1Deck.size() >= p1TopCard && p2Deck.size() >= p2TopCard) {
            //List<Integer> p1NewDeck = p1Deck.subList(0, p1TopCard);
            LinkedList<Integer> p1NewDeck = new LinkedList<>();
            Iterator<Integer> itr = p1Deck.iterator();
            for (int i = 0; i < p1TopCard; i++) {
                int card = itr.next();
                p1NewDeck.add(card);
            }
            //List<Integer> p2NewDeck = p2Deck.subList(0, p2TopCard);
            LinkedList<Integer> p2NewDeck = new LinkedList<>();
            itr = p2Deck.iterator();
            for (int i = 0; i < p2TopCard; i++) {
                int card = itr.next();
                p2NewDeck.add(card);
            }
            // play sub-game to determine the winner of this round
            System.out.print("Playing a subgame to determine the winner...\n");
            roundWinner = playGame(p1NewDeck, p2NewDeck);
            int game = unfinishedGames.peek();
            System.out.print(String.format("...anyway, back to game %d.\n", game));
        }
        else {
            // no sub-game; winner of the round has the higher-valued card
            roundWinner = (p1TopCard > p2TopCard) ? PLAYER1 : PLAYER2;
        }
        // winner takes cards
        if (roundWinner == PLAYER1) {
            p1Deck.addLast(p1TopCard);
            p1Deck.addLast(p2TopCard);
        }
        else {
            p2Deck.addLast(p2TopCard);
            p2Deck.addLast(p1TopCard);
        }
        return roundWinner;
    }
    
    private static boolean somePrevDeckHasSameConfiguration(LinkedList<Integer> playerDeck, ArrayList<LinkedList<Integer>> prevPlayerDecks) {
        for (LinkedList<Integer> prevDeck : prevPlayerDecks) {
            if (prevDeck.equals(playerDeck)) {
                return true;
            }
        }
        return false;
    }

    private static void printPostgameResults(int winner, LinkedList<Integer> p1Deck, LinkedList<Integer> p2Deck) {
        System.out.print("\n== Post-game results ==\n");
        printDecks(p1Deck, p2Deck);
        if (winner == PLAYER1) {
            System.out.print("Player 1 wins!\n");
            int p1Score = calcScore(p1Deck);
            System.out.print(String.format("Player 1's Score: %d\n", p1Score));
        }
        else {
            System.out.print("Player 2 wins!\n");
            int p2Score = calcScore(p2Deck);
            System.out.print(String.format("Player 2's Score: %d\n", p2Score));
        }
    }

    private static int calcScore(LinkedList<Integer> deck) {
        int score = 0;
        int cardValueMultiplier = deck.size();
        for (int card : deck) {
            int cardValue = card * cardValueMultiplier;
            score += cardValue;
            cardValueMultiplier--;
        }
        return score;
    }
    
    private static void printDecks(LinkedList<Integer> p1Deck, LinkedList<Integer> p2Deck) {
        System.out.print("Player 1's deck: " );
        printDeck(p1Deck);
        System.out.print("Player 2's deck: " );
        printDeck(p2Deck);
    }

    private static void printDeck(LinkedList<Integer> deck) {
        Iterator<Integer> itr = deck.iterator();
        for (int i = 0; i < deck.size(); i++) {
            int card = itr.next();
            System.out.print(card);
            if (i != deck.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }
    
    private static void setupDecks(LinkedList<Integer> p1Deck, LinkedList<Integer> p2Deck) {
        p1Deck.addAll(p1StartDeck);
        p2Deck.addAll(p2StartDeck);
    }
    
    private static void resetDecks(LinkedList<Integer> p1Deck, LinkedList<Integer> p2Deck) {
        p1Deck.clear();
        p2Deck.clear();
        setupDecks(p1Deck, p2Deck);
    }
    
    private static void readStartDecks(Scanner stdin) {
        stdin.nextLine();   //read past 'Player 1:' line
        p1StartDeck = new LinkedList<>();
        String line = stdin.nextLine();
        while (!line.isEmpty()) {
            int card = Integer.parseInt(line);
            p1StartDeck.add(card);
            line = stdin.nextLine();
        }
        stdin.nextLine();   //read past 'Player 2:' line
        p2StartDeck = new LinkedList<>();
        while (stdin.hasNextLine()) {
            int card = Integer.parseInt(stdin.nextLine());
            p2StartDeck.add(card);
        }
    }
}
