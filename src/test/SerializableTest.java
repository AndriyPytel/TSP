package test;

import org.junit.*;
import tsp.City;
import tsp.CityMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SerializableTest {
    private static String testMap = "src/data/testMap.json";

    @Ignore
    @Test
    public void deserializationTest() throws IOException {
        CityMap.load(testMap);
        CityMap map = CityMap.getInstance();
        Assert.assertNotNull(map);
    }

    @Test
    public void serializationTest() throws IOException {
        CityMap map = CityMap.getInstance();
        map.save(testMap);
        Assert.assertNotNull(map);
    }

    @Test
    public void randomCityMap() throws IOException {
        CityMap.randomCityMap(10);
        CityMap map = CityMap.getInstance();
        Assert.assertNotNull(map);
    }

    @Ignore
    @Test
    public void getDistance() {
        CityMap map = CityMap.getInstance();
        ArrayList<City> cities = new ArrayList<>(map.getCityList().values());

        System.out.println(map.getDistance(cities.get(0), cities.get(1)));
    }
}
