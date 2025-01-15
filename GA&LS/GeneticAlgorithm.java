import java.util.Random;

public class GeneticAlgorithm {
    private double[] weights;
    private double[] values;
    private int capacity;
    private Random random;
    private int populationSize;
    private double crossoverRate;
    private double mutationRate;

    public GeneticAlgorithm(double[] weights, double[] values, int capacity, Random random, int populationSize,
        double crossoverRate, double mutationRate) {
        this.weights = weights;
        this.values = values;
        this.capacity = capacity;
        this.random = random;
        this.populationSize = populationSize;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
    }

    public int[] run() {
        // Initialize population
        int[][] population = initializePopulation();
        // Loop until termination condition
        for (int generation = 0; generation < 100; generation++) { //100 is the numner of generations
            // Select parents
            int[][] parents = select(population);
            int[][] offspring = crossover(parents);
            mutate(offspring);
            // Select individuals for the next generation
            population = selectNextGeneration(population, offspring);
        }
        // Return the best individual from the last generation
        return getBestIndividual(population);
    }

    // Tournament selection
    private int[][] select(int[][] population) {
        int[][] selected = new int[population.length][weights.length];
        for (int i = 0; i < population.length; i++) {
            int[] individual1 = population[random.nextInt(population.length)];
            int[] individual2 = population[random.nextInt(population.length)];
            selected[i] = getFitness(individual1) > getFitness(individual2) ? individual1 : individual2;
        }
        return selected;
    }

    // Initialize population
    private int[][] initializePopulation() {
        int[][] population = new int[populationSize][weights.length]; // population parameter gets tuned here
        for (int i = 0; i < population.length; i++) {
            for (int j = 0; j < weights.length; j++) {
                population[i][j] = random.nextBoolean() ? 1 : 0;
            }
            // Remove items randomly until the solution is within capacity
            while (!isUnderCapacity(population[i])) {
                int randomIndex = random.nextInt(weights.length);
                population[i][randomIndex] = 0;
            }
        }
        return population;
    }

    // Check if a solution is feasible (within capacity)
    private boolean isUnderCapacity(int[] solution) {
        double totalWeight = 0;
        for (int i = 0; i < solution.length; i++) {
            if (solution[i] == 1) {
                totalWeight += weights[i];
                if (totalWeight > capacity) {
                    return false;
                }
            }
        }
        return true;
    }

    // Crossover
    private int[][] crossover(int[][] parents) {
        int[][] offspring = new int[parents.length][weights.length];
        for (int i = 0; i < parents.length; i += 2) {
            if (random.nextDouble() < crossoverRate) {
                int crossoverPoint = random.nextInt(weights.length);
                System.arraycopy(parents[i], 0, offspring[i], 0, crossoverPoint);
                System.arraycopy(parents[i + 1], crossoverPoint, offspring[i], crossoverPoint,
                        weights.length - crossoverPoint);
                System.arraycopy(parents[i + 1], 0, offspring[i + 1], 0, crossoverPoint);
                System.arraycopy(parents[i], crossoverPoint, offspring[i + 1], crossoverPoint, weights.length - crossoverPoint);
            } else {
                // If no crossover, offspring is a copy of parents
                offspring[i] = parents[i].clone();
                offspring[i + 1] = parents[i + 1].clone();
            }
        }
        return offspring;
    }

    // Mutation - bit-flip mutation
    private void mutate(int[][] population) {
        for (int[] individual : population) {
            if (random.nextDouble() < mutationRate) {
                int mutationPoint = random.nextInt(weights.length);
                individual[mutationPoint] = 1 - individual[mutationPoint];
            }
        }
    }

    // Select individuals for the next generation
    private int[][] selectNextGeneration(int[][] population, int[][] offspring) {
        int[][] nextGeneration = new int[population.length][weights.length];
        System.arraycopy(population, 0, nextGeneration, 0, population.length / 2);
        System.arraycopy(offspring, 0, nextGeneration, population.length / 2, population.length / 2);
        return nextGeneration;
    }

    // Get the best individual from the population
    private int[] getBestIndividual(int[][] population) {
        int[] bestIndividual = null;
        double bestFitness = Integer.MIN_VALUE;
        for (int[] individual : population) {
            double fitness = getFitness(individual);
            if (fitness > bestFitness) {
                bestIndividual = individual;
                bestFitness = fitness;
            }
        }
        return bestIndividual;
    }

    // Calculate the fitness of an individual
    public double getFitness(int[] individual) {
        double totalWeight = 0;
        double totalValue = 0;
        for (int i = 0; i < individual.length; i++) {
            if (individual[i] == 1) {
                totalWeight += weights[i];
                totalValue += values[i];
            }
        }
        return totalWeight <= capacity ? totalValue : 0;
    }
}