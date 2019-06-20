package ga;

import javafx.util.Pair;
import tsp.City;
import tsp.CityMap;

import java.util.*;

public class Person implements Comparable<Person>{

    private static double minPath = Double.MAX_VALUE;
    private static int chromosomeSize;
    private static CityMap map;

    private ArrayList<City> chromosome;
    private HashSet<Double> partners;
    private double pathLength;
    private double adjustment;


    public Person(City ... cities) {
        chromosome = new ArrayList<>(Arrays.asList(cities));

        setup();
    }

    public Person(List<City> cities) {
        chromosome = new ArrayList<>(cities);

        setup();
    }

    private void setup() {
        map = getMap();
        setChromosomeSize();
        calculatePathLength();
        setMinPath();
        partners = new HashSet<>();
        partners.add(this.getPath().getValue());
    }

    private void calculatePathLength() {
        double len = 0;
        for (int i = 0; i < chromosome.size() - 1; i++) {
            len += map.getDistance(chromosome.get(i),chromosome.get(i + 1));
        }
        len += map.getDistance(chromosome.get(0),chromosome.get(chromosomeSize - 1));
        pathLength = len;
        setMinPath();
    }

    public double getPathLength() {
        return pathLength;
    }

    private void calculateAdjustment() {
        adjustment = minPath / pathLength;
    }

    public double getAdjustment() {
        calculateAdjustment();
        return adjustment;
    }

    public static void setMap() {
        Person.map = CityMap.getInstance();
    }

    public static void setMap(CityMap map) {
        Person.map = map;
    }

    public static CityMap getMap() {
        if (map == null) {
            setMap();
        }
        return map;
    }

    private void setMinPath() {
        minPath = minPath > pathLength ? pathLength : minPath;
    }

    public static double getMinPath() {
        return minPath;
    }

    public static int getChromosomeSize() {
        return chromosomeSize;
    }

    public static void setChromosomeSize() {
        Person.chromosomeSize = map.getCityNumber();
    }

    public ArrayList<City> getChromosome() {
            return chromosome;
    }

    public static Person generate() {

        ArrayList<City> cities = new  ArrayList<>(getMap().getCityList().values());
        Collections.shuffle(cities);
        return new Person(cities);
    }

    public Pair<String, Double> getPath() {
        StringBuffer path = new StringBuffer();

        for (City gene: getChromosome()) {
            path.append(gene.getName());
            if (getChromosome().indexOf(gene) < getChromosomeSize()-1) {
                path.append("-");
            }
        }

        return new Pair<>(path.toString(), getPathLength());
    }

    public HashSet<Double> getPartners() {
        return partners;
    }

    public void addPartner(Person partner) {
        this.partners.add(partner.getPath().getValue());
    }

    @Override
    public int compareTo(Person o) {
            return Double.compare(o.getAdjustment(), this.getAdjustment());
    }

    @Override
    public boolean equals(Object obj) {
        return Math.abs(this.getPathLength() - ((Person)obj).getPathLength()) < 0.1;
    }


}
