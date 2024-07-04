import java.awt.geom.Line2D;
import java.awt.Point;

// horizontal, vertical, or diagonal (at 45 degrees) line
class Line extends Line2D.Double {
    private java.lang.Double slope;

    Line(Point p1, Point p2) {
        super(p1, p2);
        calcSlope();
    }

    private void calcSlope() {
        if (getX2() == getX1()) {
            slope = null;
        }
        else if (getY2() == getY1()) {
            slope = 0.0;
        }
        else {
            double dy = getY2() - getY1();
            double dx = getX2() - getX1();
            slope = dy/dx;
        }
    }
    
    java.lang.Double getSlope() { return slope; }

    boolean isVertical() { return slope == null; }
    
    boolean isHorizontal() {
        return !this.isVertical() && slope == 0;
    }

    // diagonal here means the angle is exactly 45 degrees
    boolean isDiagonal() {
        return !this.isVertical() && (slope == 1 || slope == -1);
    }
    
    boolean equals(Line l) {
        return this.getP1().equals(l.getP1()) && this.getP2().equals(l.getP2());
    }
    
    boolean containsPoint(double x, double y) {
        return ptSegDist(x, y) == 0;
    }
}
