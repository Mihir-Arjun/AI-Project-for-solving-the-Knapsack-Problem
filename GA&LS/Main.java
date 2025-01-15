import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/*These are the file names of each problem instance as well as their seed values
    f1_l-d_kp_10_269        seed value: 1714210631751L
    f2_l-d_kp_20_878        seed value: 1714210698173L
    f3_l-d_kp_4_20          seed value: 1714210729761L
    f4_l-d_kp_4_11          seed value: 1714210761531L
    f5_l-d_kp_15_375        seed value: 1714210802747L
    f6_l-d_kp_10_60         seed value: 1714210841104L
    f7_l-d_kp_7_50          seed value: 1714210890982L
    f8_l-d_kp_23_10000      seed value: 1714210959527L
    f9_l-d_kp_5_80          seed value: 1714210994357L
    f10_l-d_kp_20_879       seed value: 1714211048777L
    knapPI_1_100_1000_1     seed value: 1714207477702L
*/

public class Main {
    public static void main(String[] args) {
        //long seed = System.currentTimeMillis();
        String filename = "knapPI_1_100_1000_1"; //change file names here
        long seed = 1714207477702L;              //change seed values here
        Random random = new Random(seed);

        // Define the problem instance
        double[] weights = null;
        double[] values = null;
        int capacity = 0;

        try {
            Scanner scanner = new Scanner(new File(filename));
            int numItems = scanner.nextInt();
            capacity = scanner.nextInt();
            weights = new double[numItems];
            values = new double[numItems];

            for (int i = 0; i < numItems; i++) {
                values[i] = Double.parseDouble(scanner.next());
                weights[i] = Double.parseDouble(scanner.next());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Define the GA parameters
        int populationSize = 100;
        double crossoverRate = 0.9;
        double mutationRate = 0.01;

        // Initialize the GA
        GeneticAlgorithm ga = new GeneticAlgorithm(weights, values, capacity, random, populationSize, crossoverRate,
                mutationRate);

        // Measure the runtime
        long startTime = System.currentTimeMillis();

        // Run the GA
        int[] solution = ga.run();

        ILS ils = new ILS(random);
        // Apply ILS to improve the solution
        int[] improvedSolution = ils.applyILS(solution, weights, values, capacity);

        // Compute the runtime
        long runtime = System.currentTimeMillis() - startTime;

        // Print the results
        System.out.println("Algorithm: GA + ILS");
        System.out.println("Seed Value: " + seed);
        System.out.println("Best Solution: " + ga.getFitness(improvedSolution));
        System.out.println("Runtime (seconds): " + runtime / 1000.0);
    }
}


