package ga;

import com.google.gson.Gson;
import javafx.util.Pair;
import org.junit.rules.Stopwatch;
import tsp.CityMap;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Calculation {

    private static int maxIteration = 50;
    private static int population = 100;
    private static double reproductionRate = 0.9;
    private static double maxEpsilon = 0.005;
    private static Reproduction reproductionMethod = Reproduction.SIMPLE;

    private int cityNumber = 10;

    private Generation firstGeneration;

    public Calculation() throws IOException {
        Generation.setSize(getPopulation());
        Generation.setToReproduction((int) (getReproductionRate()*getPopulation()));
        Reproduction.setCurrentMethod(getReproductionMethod());
        CityMap.load();
        firstGeneration = Generation.generateFirstGeneration();
    }

    public Calculation(String method, int cityNumber) throws IOException {
        setReproductionMethod(method);
        setCityNumber(cityNumber);
        Generation.setToReproduction((int) (getReproductionRate()*getPopulation()));

        Reproduction.setCurrentMethod(Reproduction.SIMPLE);
        CityMap.randomCityMap(cityNumber);
        firstGeneration = Generation.generateFirstGeneration();
    }
    public Calculation(int cityNumber) throws IOException {
        setCityNumber(cityNumber);
        Generation.setToReproduction((int) (getReproductionRate()*getPopulation()));

        Reproduction.setCurrentMethod(Reproduction.SIMPLE);
        CityMap.randomCityMap(cityNumber);
        firstGeneration = Generation.generateFirstGeneration();
    }

    public static double getReproductionRate() {
        return reproductionRate;
    }

    public static void setReproductionRate(double reproductionRate) {
        Calculation.reproductionRate = reproductionRate;
    }

    public static int getPopulation() {
        return population;
    }

    public static void setPopulation(int population) {
        Calculation.population = population;
    }

    public void execute(Generation generation, Reproduction method) throws IOException {
        long startTime = System.currentTimeMillis();
        int iteration = 0;
        double epsilon = maxEpsilon * 2;
        double minPath = Double.MAX_VALUE;

        Reproduction.setCurrentMethod(method);

        ArrayList<Pair<String, Double>> bestOfGen = new ArrayList<>();

        while (++iteration < getMaxIteration() || epsilon > getMaxEpsilon()) {
            Generation newGeneration = generation.nextGeneration();
            epsilon = 1 - minPath / Person.getMinPath();
            minPath = Person.getMinPath();
            generation = newGeneration;
            bestOfGen.add(generation.getBest().getPath());
            saveResult(generation.getPaths(),"generations/"+method.name()+iteration);
        }

        long time = System.currentTimeMillis() - startTime;
        saveResult(new Pair<>(time, generation.getPaths()),method.name()+iteration);
        saveResult(bestOfGen,"bests_"+method.name());
    }

    public void execute() throws IOException {
        execute(getFirstGeneration(), getReproductionMethod());
    }


    public void saveResult(Object object, String name) throws IOException {
        FileWriter file;
        SimpleDateFormat formatter= new SimpleDateFormat("_yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        file = new FileWriter("src/data/" + name + formatter.format(date)+".json");


        Gson gson = new Gson();

        file.write(gson.toJson(object));

        file.flush();
        file.close();
    }


    public static int getMaxIteration() {
        return maxIteration;
    }

    public static void setMaxIteration(int maxIteration) {
        Calculation.maxIteration = maxIteration;
    }

    public static double getMaxEpsilon() {
        return maxEpsilon;
    }

    public static void setMaxEpsilon(double maxEpsilon) {
        Calculation.maxEpsilon = maxEpsilon;
    }

    public static Reproduction getReproductionMethod() {
        return reproductionMethod;
    }

    public static void setReproductionMethod(Reproduction reproductionMethod) {
        Calculation.reproductionMethod = reproductionMethod;
    }

    public static void setReproductionMethod(String reproductionMethod) {
        Calculation.reproductionMethod = Reproduction.valueOf(reproductionMethod);
    }

    public int getCityNumber() {
        return cityNumber;
    }

    public void setCityNumber(int cityNumber) {
        this.cityNumber = cityNumber;
    }

    public Generation getFirstGeneration() {
        return firstGeneration;
    }
}
