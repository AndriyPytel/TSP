package test;

import ga.Generation;
import ga.Person;
import ga.Reproduction;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import tsp.CityMap;

import java.io.IOException;

public class GenerationTest {


    static Generation generation;

    @BeforeClass
    public static void setup() throws IOException {
        CityMap.load();

        generation = Generation.generateFirstGeneration();
    }

    @Test
    public void sizeTest() {
        Assert.assertEquals(10, Generation.getSize());
    }

    @Test
    public void populationTest() {
        Assert.assertNotNull(generation.getPopulation());
    }

    // @Ignore
    @Test
    public void nextGeneration() {
        for (Reproduction method: Reproduction.values()) {
            Reproduction.setCurrentMethod(Reproduction.SIMPLE);

            Generation newGeneration = generation.nextGeneration();
            Assert.assertNotSame(newGeneration, generation);
            Assert.assertNotNull(newGeneration);
            System.out.println(newGeneration.getPopulation().get(0).getPathLength());
            generation = newGeneration;
        }
    }

    @Test
    public void crossover() {
        Person father1 = Person.generate();
        Person father2 = Person.generate();

        System.out.println(father1.getPath().toString());
        System.out.println(father1.getAdjustment());
        System.out.println(father2.getPath().toString());
        System.out.println(father2.getAdjustment());

        for (Reproduction method: Reproduction.values()) {
            System.out.println(method.toString());
            Person child = method.crossover(father1, father2);
            System.out.println(child.getPath().toString());
            System.out.println(child.getAdjustment());
        }

    }
}
