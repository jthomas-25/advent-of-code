class Submarine {
    private int xpos;
    private int depth;
    private int aim;

    Submarine() {
        xpos = 0;
        depth = 0;
        aim = 0;
    }

    int getXPos() { return xpos; }
    
    int getDepth() { return depth; }

    int getAim() { return aim; }

    void setXPos(int value) { this.xpos = value; }
    
    void setDepth(int value) { this.depth = value; }

    void moveForward(int delta) {
        xpos += delta;
    }

    void moveDown(int delta) {
        depth += delta;
    }

    void moveUp(int delta) {
        depth -= delta;
    }

    void increaseAim(int delta) {
        aim += delta;
    }
    
    void decreaseAim(int delta) {
        aim -= delta;
    }
}
