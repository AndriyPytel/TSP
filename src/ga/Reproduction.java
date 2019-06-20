package ga;

import tsp.City;
import tsp.CityMap;

import java.util.*;

public enum  Reproduction {

    SIMPLE {
        @Override
        public Person crossover(Person father1, Person father2){
            int chromosomeSize = Person.getChromosomeSize();
            int pointDivision = chromosomeSize / 2;

            ArrayList<City> newChromosome = new ArrayList<>(father1.getChromosome().subList(0,pointDivision));
            newChromosome.addAll(father2.getChromosome().subList(pointDivision, chromosomeSize));

            return new Person(correction(newChromosome));
        }

        private ArrayList<City> correction(ArrayList<City> chromosome) {
            HashMap<String, City> cityPull = new HashMap<>();
            HashMap<String, City> cityPush = CityMap.getInstance().getCityList();
            ArrayList<City> newChromosome = new ArrayList<>();
            for (City gene: chromosome) {
                if(cityPull.containsKey(gene.getName())) {
                    String key = String.valueOf(cityPush.keySet().toArray()[0]);
                    newChromosome.add(cityPush.get(key));

                    cityPush.remove(key);
                    cityPull.put(key,cityPush.get(key));
                }
                else {
                    newChromosome.add(gene);

                    cityPush.remove(gene.getName());
                    cityPull.put(gene.getName(),gene);
                }
            }
            return newChromosome;
        }


        @Override
        public Person mutation(Person person) {
            Random random = new Random();
            int index1 = random.nextInt(Person.getChromosomeSize());
            int index2 = random.nextInt(Person.getChromosomeSize());
            while (index1 == index2) {
                index2 = random.nextInt(Person.getChromosomeSize());
            }
            ArrayList<City> chromosome = person.getChromosome();
            Collections.swap(chromosome, index1, index2);
            return new Person(chromosome);
        }
    },
    CYCLE {
        private HashMap<String, City> cityPull;
        private  int maxParentalSequence;

        @Override
        public Person crossover(Person father1, Person father2) {
            maxParentalSequence = Person.getChromosomeSize() / 4;
            cityPull = new HashMap<>();
            ArrayList<City> newChromosome = new ArrayList<>();

            ArrayList<City> father1Chromosome = father1.getChromosome();
            ArrayList<City> father2Chromosome = father2.getChromosome();

            City lastGene = father1Chromosome.get(0);


            while (newChromosome.size() < Person.getChromosomeSize() ) {

                newChromosome.addAll(chromosomePart(father1Chromosome, lastGene));
                lastGene = newChromosome.get(newChromosome.size() - 1);

                if(newChromosome.size() < Person.getChromosomeSize()) {
                    newChromosome.addAll(chromosomePart(father2Chromosome, lastGene));
                    lastGene = newChromosome.get(newChromosome.size() - 1);
                }
            }

            return new Person(newChromosome);
        }

        private ArrayList<City> chromosomePart(ArrayList<City> father,City lastGene) {
            ArrayList<City> part = geneLink(lastGene, father);
            return part.size() == 0 ? addRest(cityPull) : part;
        }

        private ArrayList<City> geneLink(City fistGene, ArrayList<City> father) {
            ArrayList<City> link = new ArrayList<>();
            int index = father.indexOf(fistGene);
            for (int i = 0; i < maxParentalSequence; i++) {

                City nextGene = father.get(nextIndex(index));
                City priorGene = father.get(priorIndex(index));

                if(!cityPull.containsKey(nextGene.getName())) {
                    index = nextIndex(index);
                    link.add(nextGene);
                    cityPull.put(nextGene.getName(), nextGene);
                }
                else if(!cityPull.containsKey(priorGene.getName())) {
                    index = priorIndex(index);
                    link.add(priorGene);
                    cityPull.put(priorGene.getName(), priorGene);
                }
                else {
                    break;
                }
            }
            return link;
        }

        @Override
        public Person mutation(Person person) {
            return SIMPLE.mutation(person);
        }
    },
    NEAREST {

        @Override
        public Person crossover(Person father1, Person father2) {
            ArrayList<City> newChromosome = merge(father1.getChromosome(), father2.getChromosome());
            Person child = new Person(newChromosome);
            if (child.getPathLength() == father1.getPathLength()  || child.getPathLength() == father2.getPathLength()) {
                child = mutation(child);
            }

            return child;

        }

        private ArrayList<City> merge(ArrayList<City> chromosome1, ArrayList<City> chromosome2) {

            HashMap<String, City> cityPull = new HashMap<>();
            ArrayList<City> mergeChromosome = new ArrayList<>();

            int index1 = 0;
            City currentGene = chromosome1.get(index1);
            int index2 = chromosome2.indexOf(currentGene);

            mergeChromosome.add(currentGene);
            cityPull.put(currentGene.getName(), currentGene);


            while (mergeChromosome.size() < Person.getChromosomeSize()) {

                ArrayList<City> directions = new ArrayList<>();

                directions.add(chromosome1.get(nextIndex(index1)));
                directions.add(chromosome1.get(priorIndex(index1)));
                directions.add(chromosome2.get(nextIndex(index2)));
                directions.add(chromosome2.get(priorIndex(index2)));

                double minDistance = Double.MAX_VALUE;

                City nextGene = null;

                for (City gene: directions) {
                    double distance = currentGene.distanceTo(gene);
                    if(!cityPull.containsKey(gene.getName()) && distance < minDistance) {
                        minDistance = distance;
                        nextGene = gene;
                    }
                }
                index1 = chromosome1.indexOf(nextGene);
                index2 = chromosome2.indexOf(nextGene);

                currentGene = nextGene;

                if (currentGene != null) {
                    mergeChromosome.add(currentGene);
                    cityPull.put(currentGene.getName(), currentGene);
                } else {
                    mergeChromosome.addAll(addRest(cityPull));
                }
            }

            return mergeChromosome;
        }


        @Override
        public Person mutation(Person person) {
            return SIMPLE.mutation(person);
        }
    };

    public static Reproduction currentMethod;
    public static double mutationRate = 0.05;

    public abstract Person crossover(Person father1, Person father2);
    public abstract Person mutation(Person person);

    public static ArrayList<Person> posterity(ArrayList<Person> population) {
        Random r = new Random();
        Reproduction method = Reproduction.getCurrentMethod();
        ArrayList<Person> children = new ArrayList<>(population.size()/2);

        Collections.shuffle(population);

        for (int i = 0; i < population.size() - 1; i+=2) {

            Person father1 = population.get(i);
            Person father2 = population.get(i + 1);

            System.out.println();

            System.out.println(father1.getPartners());
            System.out.println(father2.getPartners());

            Person child = method.crossover(father1, father2);

            if (r.nextDouble() <= mutationRate || !father1.getPartners().contains(father2.getPath().getValue())
                    || !father2.getPartners().contains(father1.getPath().getValue())) {
                child = method.mutation(child);
            }

            System.out.println(children.size());
            father1.addPartner(father2);
            father2.addPartner(father1);
            child.addPartner(father1);
            child.addPartner(father2);
            
            children.add(child);
        }
        return children;
    }

    public static Person checkEquals(Person child, Person father1, Person father2) {

        if (child.equals(father1) || child.equals(father2) || father1.equals(father2)){
            return Person.generate();
        } else {
            return child;
        }
    }

    private static ArrayList<City> addRest(HashMap<String, City> cityPull) {
        ArrayList<City> allCity = new ArrayList<>(CityMap.getInstance().getCityList().values());
        for (City city: cityPull.values()) {
            allCity.remove(city);
        }
        return allCity;
    }

    public static Reproduction getCurrentMethod() {
        return currentMethod;
    }

    public static void setCurrentMethod(Reproduction currentMethod) {
        Reproduction.currentMethod = currentMethod;
    }

    private static int nextIndex(int current) {
        if (current >= Person.getChromosomeSize() - 1) {
            return 0;
        }
        else {
            return ++current;
        }
    }

    private static int priorIndex(int current) {
        if (current <= 0) {
            return Person.getChromosomeSize() - 1;
        }
        else {
            return --current;
        }
    }
}
