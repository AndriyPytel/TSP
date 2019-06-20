package tsp;

import com.google.gson.Gson;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CityMap implements Serializable {

    private static final transient String fileName = "src/data/map.json";
    private static CityMap instance = null;

    private HashMap<String, City> cityList;
    private HashMap<String, Double> cityReferences;
    private int cityNumber;

    private CityMap() {
        cityList = new HashMap<>();
        cityReferences = new HashMap<>();
    }
    
    private CityMap(City ... cities) {
        this();
        for (City city: cities) {
            addCity(city);
        }
    }

    private CityMap(List<City> cities) {
        this();
        for (City city: cities) {
            addCity(city);
        }
    }

    public static CityMap getInstance() {
        if (instance == null){
            instance = new CityMap();
        }
        return instance;
    }

    public static void setInstance(City ... cities) {
        if (instance == null) {
            instance = new CityMap(cities);
        }
    }
    public static void setInstance(CityMap map) {
        if (instance == null) {
            instance = map;
        }
    }

    public static void setInstance(List<City> cities) {
        if (instance == null) {
            instance = new CityMap(cities);
        }
    }

    public double getDistance(City c1, City c2) {
        String reference = c1.getName() + " " + c2.getName();
        if (!cityReferences.containsKey(reference)) {
            System.out.println(reference);
        }
        return cityReferences.get(reference);
    }

    public HashMap<String, City> getCityList() {
        return new HashMap<>(cityList);
    }

    public int getCityNumber() {
        return cityNumber;
    }

    public void setCityNumber() {
        this.cityNumber = this.getCityList().size();
    }
    public void setCityNumber(int size) {
        this.cityNumber = size;
    }

    public void addCity(City city) {
        setCityNumber();
        cityList.put(city.getName(), city);
        addReferences(city);
    }
    
    private void addReferences(City newCity) {
        for (City city: cityList.values()) {
            if(!city.equals(newCity)) {
                double distance = newCity.distanceTo(city);
                String reference = newCity.getName() + " " + city.getName();
                String reverseReference = city.getName() + " " + newCity.getName();
                cityReferences.put(reference, distance);
                cityReferences.put(reverseReference, distance);
            }
        }
    }


    public void save(String ... prop) throws IOException {
        FileWriter file;
        if (prop.length == 0 ) {
            file = new FileWriter(fileName);
        } else {
            file = new FileWriter(prop[0]);
        }

        Gson gson = new Gson();

        file.write(gson.toJson(this));

        file.flush();
        file.close();
    }


    public static void load(String ... prop) throws IOException {
        FileReader file;
        if (prop.length == 0 ) {
            file = new FileReader(fileName);
        } else {
            file = new FileReader(prop[0]);
        }

        Gson gson = new Gson();

        setInstance(gson.fromJson(file, CityMap.class));

        file.close();
    }

    public static void randomCityMap(int size) throws IOException {
        Random r = new Random();
        CityMap.setInstance();
        CityMap map = CityMap.getInstance();
        map.setCityNumber(size);

        for (int i = 0; i < size; i++) {
            String cityName = String.valueOf((char) ('A' + i));
            int x = r.nextInt(size);
            int y = r.nextInt(size);
            City city = new City(cityName, x, y);
            map.addCity(city);
        }
        map.setCityNumber();
        map.save();

    }
}