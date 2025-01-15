import java.util.Random;

public class ILS {
    private Random random;

    public ILS(Random random) {
        this.random = random;
    }

    public int[] applyILS(int[] solution, double[] weights, double[] values, int capacity) {
        int[] bestSolution = solution.clone();
        int[] currentSolution = solution.clone();

        for (int i = 0; i < 100; i++) { // Number of ILS iterations
            currentSolution = perturb(currentSolution, random);
            currentSolution = applyLocalSearch(currentSolution, weights, values, capacity);

            if (getFitness(currentSolution, weights, values, capacity) > getFitness(bestSolution, weights, values, capacity)) {
                bestSolution = currentSolution.clone();
            }
        }
        return bestSolution;
    }

    private int[] perturb(int[] solution, Random random) {
        int[] perturbedSolution = solution.clone();
        int index1 = random.nextInt(solution.length);
        int index2 = random.nextInt(solution.length);
        int temp = perturbedSolution[index1];
        perturbedSolution[index1] = perturbedSolution[index2];
        perturbedSolution[index2] = temp;
        return perturbedSolution;
    }

    private int[] applyLocalSearch(int[] solution, double[] weights, double[] values, int capacity) {
        int[] newSolution = solution.clone();
        boolean improved;
        do {
            improved = false;
            for (int i = 0; i < newSolution.length; i++) {
                for (int j = i + 1; j < newSolution.length; j++) {
                    int[] swappedSolution = swapItems(newSolution, i, j);
                    if (isValid(swappedSolution, weights, capacity) && getFitness(swappedSolution, weights, values, capacity) > getFitness(newSolution, weights, values, capacity)) {
                        newSolution = swappedSolution;
                        improved = true;
                    }
                }
            }
        } while (improved);
        return newSolution;
    }

    private int[] swapItems(int[] solution, int i, int j) {
        int[] newSolution = solution.clone();
        int temp = newSolution[i];
        newSolution[i] = newSolution[j];
        newSolution[j] = temp;
        return newSolution;
    }

    private boolean isValid(int[] solution, double[] weights, double capacity) {
        double totalWeight = 0;
        for (int i = 0; i < solution.length; i++) {
            if (solution[i] == 1) {
                totalWeight += weights[i];
            }
        }
        return totalWeight <= capacity;
    }

    private double getFitness(int[] solution, double[] weights, double[] values, double capacity) {
        double totalWeight = 0;
        double totalValue = 0;
        for (int i = 0; i < solution.length; i++) {
            if (solution[i] == 1) {
                totalWeight += weights[i];
                totalValue += values[i];
            }
        }
        return totalWeight <= capacity ? totalValue : 0;
    }
}
