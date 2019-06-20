package test;

import ga.Person;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import tsp.CityMap;

import java.io.IOException;

public class PersonTest {
    static CityMap map;

    Person person = null;

    @BeforeClass
    public static void setup() throws IOException {
        CityMap.randomCityMap(10);
        map = CityMap.getInstance();
    }

    @Test
    public void generate() {
        person = Person.generate();
        Assert.assertNotNull(person);
    }

    @Test
    public void chromosomeSize() {
        if (person == null) {
            generate();
        }
        Assert.assertEquals(Person.getChromosomeSize(), map.getCityNumber());
    }

    @Test
    public void adjustment() {
        if (person == null) {
            generate();
        }
        double adj = person.getAdjustment();
        Assert.assertEquals(1, adj, 0.1);
    }
}
