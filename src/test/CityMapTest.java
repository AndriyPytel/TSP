package test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import tsp.City;
import tsp.CityMap;

import java.io.IOException;

public class CityMapTest {
    static City cityA;
    static City cityB;
    static City cityC;

    @BeforeClass
    public static void setup() {
        cityA = new City("A", 0,0);
        cityB = new City("B", 0,2);
        cityC = new City("C", 3,1);

        CityMap.setInstance(cityA, cityB, cityC);
    }

    @Test
    public void instanceTest() {
        Assert.assertNotNull(CityMap.getInstance());
    }

    @Test
    public void addCityTest() {
        City cityD = new City("D", 1, 3);
        CityMap map = CityMap.getInstance();
        map.addCity(cityD);
    }

    @Test
    public void distanceTest() {
        CityMap map = CityMap.getInstance();
        double distance = map.getDistance(cityB, cityA);
        System.out.println(distance);
        Assert.assertEquals(2, distance, 0.1);
    }

    @Test
    public void cityNumberTest() {
        CityMap map = CityMap.getInstance();
        Assert.assertEquals(map.getCityNumber(), 3, 1);
    }

    // @Ignore
    @Test
    public void serializationTest() throws IOException {
        CityMap.randomCityMap(15);
        CityMap map = CityMap.getInstance();
        map.save("src/data/testMap.json");
    }
}
