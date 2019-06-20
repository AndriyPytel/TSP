package tsp;

import static java.lang.Math.sqrt;
import static java.lang.Math.pow;


public class Coordinates {

    private double x;
    private double y;

    public Coordinates(Number x, Number y) {
        setCoordinates(x, y);
    }

    public void setX(Number x) {
        this.x = x.doubleValue();
    }

    public void setY(Number y) {
        this.y = y.doubleValue();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setCoordinates(Number x, Number y){
        setX(x);
        setY(y);
    }

    @Override
    public String toString() {
        return "(" + this.getX() + ", " + this.getY() + ")";
    }

}
