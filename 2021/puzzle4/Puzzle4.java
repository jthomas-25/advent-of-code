import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

public class Puzzle4 {
    static int finalNumDrawn;
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            File file = new File(filename);
            Scanner stdin = new Scanner(file);
            String[] order = stdin.nextLine().split(",");
            ArrayList<Board> boards = new ArrayList<>();
            while (stdin.hasNextLine()) {
                if (stdin.hasNextLine()) {
                    stdin.nextLine();
                    if (!stdin.hasNextLine()) {
                        break;
                    }
                }
                Board board = new Board();
                for (int row = 0; row < 5; row++) {
                    for (int col = 0; col < 5; col++) {
                        board.setSquareNumber(row, col, stdin.nextInt());
                        board.markSquare(row, col, false);
                    }
                }
                boards.add(board);
            }
            stdin.close();
            Board winner = part1(order, boards);
            printWinner(winner);
            Board lastWinner = part2(order, boards);
            printWinner(lastWinner);
        }
        catch (Exception e) {
        }
    }

    private static Board part1(String[] order, ArrayList<Board> boards) {
        for (int i = 0; i < order.length; i++) {
            int drawnNum = Integer.parseInt(order[i]);
            finalNumDrawn = drawnNum;
            for (Board b : boards) {
                b.markIfHasNumber(drawnNum);
                if (i >= 4 && b.won()) {
                    return b;
                }
            }
        }
        return null;
    }
    
    private static Board part2(String[] order, ArrayList<Board> boards) {
        ArrayList<Board> winners = new ArrayList<>();
        for (int i = 0; i < order.length; i++) {
            int drawnNum = Integer.parseInt(order[i]);
            finalNumDrawn = drawnNum;
            for (Board b : boards) {
                if (!b.alreadyWon()) {
                    b.markIfHasNumber(drawnNum);
                    if (i >= 4 && b.won()) {
                        winners.add(b);
                        b.setAlreadyWon(true);
                        if (winners.size() == boards.size()) {
                            return winners.get(winners.size()-1);
                        }
                    }
                }
            }
        }
        return null;
    }

    private static void printWinner(Board winner) {
        if (winner == null) {
            System.out.println("No winner");
        }
        else {
            //winner.printContents();
            //winner.printStatus();
            //System.out.println(finalNumDrawn);
            System.out.println(winner.calcScore(finalNumDrawn));
        }
    }
}
