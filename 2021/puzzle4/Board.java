class Board {
    private int[][] contents;
    private boolean[][] markedSquares;
    private boolean alreadyWon;
    
    Board() {
        contents = new int[5][5];
        markedSquares = new boolean[5][5];
        alreadyWon = false;
    }

    int getSquareNumber(int row, int col) { return contents[row][col]; }
    
    boolean squareMarked(int row, int col) { return markedSquares[row][col]; }

    boolean alreadyWon() { return alreadyWon; }

    void setSquareNumber(int row, int col, int value) { contents[row][col] = value; }
    
    void markSquare(int row, int col, boolean value) { markedSquares[row][col] = value; }

    void setAlreadyWon(boolean value) { alreadyWon = value; }
    
    void markIfHasNumber(int drawnNum) {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (getSquareNumber(row, col) == drawnNum) {
                    markSquare(row, col, true);
                    break;
                }
            }
        }
    }

    boolean won() {
        for (int row = 0; row < 5; row++) {
            if (rowComplete(row)) {
                return true;
            }
        }
        for (int col = 0; col < 5; col++) {
            if (colComplete(col)) {
                return true;
            }
        }
        return false;
    }

    int calcScore(int finalNumDrawn) {
        int score = 0;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (!squareMarked(row, col)) {
                    score += getSquareNumber(row, col);
                }
            }
        }
        score *= finalNumDrawn;
        return score;
    }
    
    boolean rowComplete(int row) {
        for (int col = 0; col < 5; col++) {
            if (!squareMarked(row, col)) {
                return false;
            }
        }
        return true;
    }
    
    boolean colComplete(int col) {
        for (int row = 0; row < 5; row++) {
            if (!squareMarked(row, col)) {
                return false;
            }
        }
        return true;
    }

    void printContents() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                System.out.print(getSquareNumber(row, col) + " ");
            }
            System.out.println();
        }
    }
    
    void printStatus() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (squareMarked(row, col)) {
                    System.out.print("1 ");
                }
                else {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
    }
}
