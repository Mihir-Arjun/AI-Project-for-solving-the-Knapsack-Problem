import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/*These are the file names of each problem instance 
    f1_l-d_kp_10_269        seed value: 1714209323205L
    f2_l-d_kp_20_878        seed value: 1713877276773L
    f3_l-d_kp_4_20          seed value: 1713873038380L
    f4_l-d_kp_4_11          seed value: 1713873102948L
    f5_l-d_kp_15_375        seed value: 1714209745828L
    f6_l-d_kp_10_60         seed value: 1714209831671L
    f7_l-d_kp_7_50          seed value: 1714209915358L
    f8_l-d_kp_23_10000      seed value: 1714209990033L
    f9_l-d_kp_5_80          seed value: 1713875416342L
    f10_l-d_kp_20_879       seed value: 1714210166834L
    knapPI_1_100_1000_1     seed value: 1714207314650L
*/

public class Main {
    public static void main(String[] args) {
        // Initialize the seed for reproducibility
        // long seed = System.currentTimeMillis();
        String filename = "knapPI_1_100_1000_1"; //problem instances can be changed here
        long seed = 1714207314650L;              //seed values corresponding to a problem instance can be changed here
        Random random = new Random(seed);

        // Define the problem instance
        double[] weights = null;
        double[] values = null;
        int capacity = 0;

        try {
            Scanner scanner = new Scanner(new File(filename));
            int numItems = scanner.nextInt();
            // System.out.println("numItems "+numItems);
            capacity = scanner.nextInt();
            // System.out.println("Capacity "+capacity);
            weights = new double[numItems];
            values = new double[numItems];

            for (int i = 0; i < numItems; i++) {
                // String token = scanner.next();
                // System.out.println("Token: " + token);
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

        // Compute the runtime
        long runtime = System.currentTimeMillis() - startTime;

        // Print the results
        System.out.println("Algorithm: GA");
        System.out.println("Seed Value: " + seed);
        System.out.println("Best Solution: " + ga.getFitness(solution));
        System.out.println("Runtime (seconds): " + runtime / 1000.0);
    }
}
