package test;

import ga.Person;
import ga.Reproduction;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import tsp.CityMap;

import java.io.IOException;

public class ReproductionTest {

    private Person father1;
    private Person father2;
    private static CityMap map;
    int iteration = 100;

    @BeforeClass
    public static void setMap() throws IOException {
        CityMap.load();
        map = CityMap.getInstance();
        map.save("src/data/testMap.json");
    }

    @Ignore
    @Test
    public void simpleCrossover() throws IndexOutOfBoundsException, NullPointerException {
        father1 = Person.generate();
        father2 = Person.generate();
        Person child = Reproduction.SIMPLE.crossover(father1, father2);
        System.out.println(child.getPath());
    }

    @Test
    public void simpleCrossoverCycle() throws IndexOutOfBoundsException, NullPointerException {
        for (int i = 0; i < iteration; i++) {
            simpleCrossover();
        }
    }

    @Ignore
    @Test
    public void cycleCrossover() throws IndexOutOfBoundsException, NullPointerException {
        father1 = Person.generate();
        father2 = Person.generate();
        Person child = Reproduction.CYCLE.crossover(father1, father2);
        System.out.println(child.getPath());
    }

    @Test
    public void cycleCrossoverCycle() throws IndexOutOfBoundsException, NullPointerException {
        for (int i = 0; i < iteration; i++) {
            cycleCrossover();
        }
    }

    @Ignore
    @Test
    public void nearestCrossover() throws IndexOutOfBoundsException, NullPointerException {
        father1 = Person.generate();
        father2 = Person.generate();
        Person child = Reproduction.NEAREST.crossover(father1, father2);
        System.out.println(child.getPath());
    }

    @Test
    public void nearestCrossoverCycle() throws IndexOutOfBoundsException, NullPointerException {
        for (int i = 0; i < iteration; i++) {
            nearestCrossover();
        }
    }
}
