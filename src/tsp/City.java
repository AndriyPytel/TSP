package tsp;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;


public class City {
    private String name;
    private Coordinates coordinates;

    public City(String name, Coordinates coordinates){
        setName(name);
        setCoordinates(coordinates);
    }


    public City(String name, Number x, Number y){
        setName(name);
        setCoordinates(x, y);
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCoordinates(Number x, Number y) {
        this.coordinates = new Coordinates(x, y);
    }

    public double distanceTo(Coordinates c) {
        return distance(this.getCoordinates(), c);
    }

    public double distanceTo(City c) {
        return distance(this.getCoordinates(), c.getCoordinates());
    }

    public static double distance(City city1, City city2) {
        return distance(city1.getCoordinates(), city2.getCoordinates());
    }

    public static double distance(Coordinates c1, Coordinates c2) {
        return sqrt(pow((c1.getX() - c2.getX()),2) + pow((c1.getY() - c2.getY()),2));
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) || ((City) obj).getName().equals(this.getName());
    }

    @Override
    public String toString() {
        return "City: " + getName()
                +"\nCoordinates: " + getCoordinates().toString() ;
    }
}
