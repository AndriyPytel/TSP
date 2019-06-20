package ga;

import javafx.util.Pair;
import tsp.CityMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Generation {

    private static int size = 100;
    private static int toReproduction;

    private ArrayList<Person> population;

    private Generation(ArrayList<Person> population) {
        if (population.size() > size) {
            setPopulation(reject(population, size));
        }
        else {
            setPopulation(population);
        }
    }

    public static Generation generateFirstGeneration() {
        if (Generation.getSize() == 0) {
            setSize(CityMap.getInstance().getCityNumber());
        }
        ArrayList<Person> population = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Person person = Person.generate();
            population.add(person);
        }
        return new Generation(population);
    }

    public Generation nextGeneration() {
        return Generation.nextGeneration(this);
    }


    public static Generation nextGeneration(Generation g) {
        ArrayList<Person> parents = g.getPopulation();
        Collections.sort(parents);
        ArrayList<Person> newPersons = Reproduction.posterity(g.reject(parents, getToReproduction()));

        newPersons.addAll(g.getPopulation());
        Collections.sort(newPersons);

        return new Generation(newPersons);
    }

    private ArrayList<Person> reject (ArrayList<Person> population, int size) {
        Collections.sort(population);
        return new ArrayList<>(population.subList(0,size));
    }


    public static int getSize() {
        return size;
    }

    public static void setSize(int size) {
        Generation.size = size;
    }

    private void setPopulation(ArrayList<Person> population) {
        this.population = population;
    }

    public ArrayList<Person> getPopulation() {
        return population;
    }

    public static int getToReproduction() {
        if (toReproduction == 0) {
            setToReproduction(size);
        }
        return toReproduction;
    }

    public static void setToReproduction(int toReproduction) {
        Generation.toReproduction = toReproduction;
    }

    public Person getBest() {

        ArrayList<Person> population = this.getPopulation();
        Collections.sort(population);

        return population.get(0);
    }

    public ArrayList<Pair<String, Double>> getPaths(String ... prop) throws IOException {
        ArrayList<Pair<String, Double>> paths = new ArrayList<>();
        for (Person p : this.getPopulation()) {
            if(p.getChromosome().size() == Person.getChromosomeSize()) {
                paths.add(p.getPath());
            }
        }

        return paths;
    }
}
