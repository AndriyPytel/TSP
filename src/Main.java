import ga.Calculation;
import ga.Generation;
import ga.Reproduction;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Calculation calculation = new Calculation();
        Generation firstGeneration = calculation.getFirstGeneration();
        calculation.execute(firstGeneration, Reproduction.SIMPLE);
        calculation.execute(firstGeneration, Reproduction.CYCLE);
        calculation.execute(firstGeneration, Reproduction.NEAREST);
    }
}
