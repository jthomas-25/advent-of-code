class Octopus {
    private int row, col;
    private int energyLevel;
    private static Octopus[][] grid;
    private static int gridWidth, gridHeight;
    private boolean flashed;
    private int numFlashes;
    private Octopus[] neighbors;

    Octopus(int row, int col, int energyLevel, Octopus[][] grid) {
        this.row = row;
        this.col = col;
        this.energyLevel = energyLevel;
        this.grid = grid;
        gridWidth = grid[0].length;
        gridHeight = grid.length;
        flashed = false;
        numFlashes = 0;
        neighbors = null;
    }

    int getEnergyLevel() { return energyLevel; }

    void setEnergyLevel(int value) { energyLevel = value; }

    int getNumFlashes() { return numFlashes; }
    
    void setNumFlashes(int value) { numFlashes = value; }

    void step() {
        if (!flashed) {
            energyLevel++;
            if (energyLevel > 9) {
                flash();
            }
        }
    }

    void flash() {
        flashed = true;
        numFlashes++;
        energyLevel = 0;
        // chain reaction
        neighbors = getNeighbors();
        for (Octopus o : neighbors) {
            o.step();
        }
    }

    void reset() {
        flashed = false;
    }
    
    void printNeighbors() {
        if (neighbors == null) {
            neighbors = getNeighbors();
        }
        for (Octopus o : neighbors) {
            System.out.println(o.row + ", " + o.col);
        }
    }

    private Octopus[] getNeighbors() {
        if (neighbors == null) {
            if (isCorner(row, col)) {
                neighbors = new Octopus[3];
                if (isTopLeft(row, col)) {
                    neighbors[0] = grid[0][1];
                    neighbors[1] = grid[1][1];
                    neighbors[2] = grid[1][0];
                }
                else if (isTopRight(row, col)) {
                    neighbors[0] = grid[0][col-1];
                    neighbors[1] = grid[1][col-1];
                    neighbors[2] = grid[1][col];
                }
                else if (isBottomLeft(row, col)) {
                    neighbors[0] = grid[row][1];
                    neighbors[1] = grid[row-1][1];
                    neighbors[2] = grid[row-1][0];
                }
                else if (isBottomRight(row, col)) {
                    neighbors[0] = grid[row][col-1];
                    neighbors[1] = grid[row-1][col-1];
                    neighbors[2] = grid[row-1][col];
                }
            }
            else if (isEdge(row, col)) {
                neighbors = new Octopus[5];
                if (isTopEdge(row, col)) {
                    neighbors[0] = grid[0][col+1];
                    neighbors[1] = grid[1][col+1];
                    neighbors[2] = grid[1][col];
                    neighbors[3] = grid[1][col-1];
                    neighbors[4] = grid[0][col-1];
                }
                else if (isBottomEdge(row, col)) {
                    neighbors[0] = grid[row][col-1];
                    neighbors[1] = grid[row-1][col-1];
                    neighbors[2] = grid[row-1][col];
                    neighbors[3] = grid[row-1][col+1];
                    neighbors[4] = grid[row][col+1];
                }
                else if (isLeftEdge(row, col)) {
                    neighbors[0] = grid[row-1][col];
                    neighbors[1] = grid[row-1][col+1];
                    neighbors[2] = grid[row][col+1];
                    neighbors[3] = grid[row+1][col+1];
                    neighbors[4] = grid[row+1][col];
                }
                else if (isRightEdge(row, col)) {
                    neighbors[0] = grid[row-1][col];
                    neighbors[1] = grid[row-1][col-1];
                    neighbors[2] = grid[row][col-1];
                    neighbors[3] = grid[row+1][col-1];
                    neighbors[4] = grid[row+1][col];
                }
            }
            else {
                neighbors = new Octopus[8];
                neighbors[0] = grid[row-1][col-1];
                neighbors[1] = grid[row-1][col];
                neighbors[2] = grid[row-1][col+1];
                neighbors[3] = grid[row][col+1];
                neighbors[4] = grid[row+1][col+1];
                neighbors[5] = grid[row+1][col];
                neighbors[6] = grid[row+1][col-1];
                neighbors[7] = grid[row][col-1];
            }
        }
        return neighbors;
    }
    
    private boolean isCorner(int row, int col) {
        return (isTopLeft(row, col) ||
                isTopRight(row, col) ||
                isBottomLeft(row, col) ||
                isBottomRight(row, col));
    }
    
    private boolean isEdge(int row, int col) {
        return (isTopEdge(row, col) ||
                isBottomEdge(row, col) ||
                isLeftEdge(row, col) ||
                isRightEdge(row, col));
    }

    private boolean isTopLeft(int row, int col) {
        return row == 0 && col == 0;
    }
    
    private boolean isTopRight(int row, int col) {
        return row == 0 && col == gridWidth-1;
    }
    
    private boolean isBottomLeft(int row, int col) {
        return row == gridHeight-1 && col == 0;
    }
    
    private boolean isBottomRight(int row, int col) {
        return row == gridHeight-1 && col == gridWidth-1;
    }

    private boolean isTopEdge(int row, int col) {
        return row == 0 && (col > 0 && col < gridWidth-1);
    }

    private boolean isBottomEdge(int row, int col) {
        return row == gridHeight-1 && (col > 0 && col < gridWidth-1);
    }

    private boolean isLeftEdge(int row, int col) {
        return col == 0 && (row > 0 && row < gridHeight-1);
    }

    private boolean isRightEdge(int row, int col) {
        return col == gridWidth-1 && (row > 0 && row < gridHeight-1);
    }
}
