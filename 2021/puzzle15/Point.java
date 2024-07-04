class Point {
    private int row;
    private int col;
    private int riskLevel;
    private static Point[][] grid;
    private static int gridWidth, gridHeight;
    private Point[] neighbors;

    Point(int row, int col, int riskLevel, Point[][] grid) {
        this.row = row;
        this.col = col;
        this.riskLevel = riskLevel;
        this.grid = grid;
        gridWidth = grid[0].length;
        gridHeight = grid.length;
        neighbors = null;
    }

    public String toString() {
        return String.format("(%d, %d)", row, col);
    }

    int getRow() { return row; }

    int getCol() { return col; }

    int getIndex() { return row * gridHeight + col; }

    int getRiskLevel() { return riskLevel; }

    void setGrid(Point[][] grid) {
        this.grid = grid;
        gridWidth = grid[0].length;
        gridHeight = grid.length;
    }

    Point[] getNeighbors() {
        if (neighbors == null) {
            if (isCorner()) {
                neighbors = new Point[2];
                if (isTopLeft()) {
                    neighbors[0] = grid[0][1];
                    neighbors[1] = grid[1][0];
                }
                else if (isTopRight()) {
                    neighbors[0] = grid[0][col-1];
                    neighbors[1] = grid[1][col];
                }
                else if (isBottomLeft()) {
                    neighbors[0] = grid[row][1];
                    neighbors[1] = grid[row-1][0];
                }
                else if (isBottomRight()) {
                    neighbors[0] = grid[row][col-1];
                    neighbors[1] = grid[row-1][col];
                }
            }
            else if (isEdge()) {
                neighbors = new Point[3];
                if (isTopEdge()) {
                    neighbors[0] = grid[0][col+1];
                    neighbors[1] = grid[1][col];
                    neighbors[2] = grid[0][col-1];
                }
                else if (isBottomEdge()) {
                    neighbors[0] = grid[row][col-1];
                    neighbors[1] = grid[row-1][col];
                    neighbors[2] = grid[row][col+1];
                }
                else if (isLeftEdge()) {
                    neighbors[0] = grid[row-1][col];
                    neighbors[1] = grid[row][col+1];
                    neighbors[2] = grid[row+1][col];
                }
                else if (isRightEdge()) {
                    neighbors[0] = grid[row-1][col];
                    neighbors[1] = grid[row][col-1];
                    neighbors[2] = grid[row+1][col];
                }
            }
            else {
                neighbors = new Point[4];
                neighbors[0] = grid[row-1][col];
                neighbors[1] = grid[row][col+1];
                neighbors[2] = grid[row+1][col];
                neighbors[3] = grid[row][col-1];
            }
        }
        return neighbors;
    }
    
    boolean isCorner() {
        return (isTopLeft() ||
                isTopRight() ||
                isBottomLeft() ||
                isBottomRight());
    }
    
    boolean isEdge() {
        return (isTopEdge() ||
                isBottomEdge() ||
                isLeftEdge() ||
                isRightEdge());
    }

    boolean isTopLeft() {
        return row == 0 && col == 0;
    }
    
    boolean isTopRight() {
        return row == 0 && col == gridWidth-1;
    }
    
    boolean isBottomLeft() {
        return row == gridHeight-1 && col == 0;
    }
    
    boolean isBottomRight() {
        return row == gridHeight-1 && col == gridWidth-1;
    }

    boolean isTopEdge() {
        return row == 0 && (col > 0 && col < gridWidth-1);
    }

    boolean isBottomEdge() {
        return row == gridHeight-1 && (col > 0 && col < gridWidth-1);
    }

    boolean isLeftEdge() {
        return col == 0 && (row > 0 && row < gridHeight-1);
    }

    boolean isRightEdge() {
        return col == gridWidth-1 && (row > 0 && row < gridHeight-1);
    }
}
