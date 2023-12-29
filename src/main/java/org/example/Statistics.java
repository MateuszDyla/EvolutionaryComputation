package org.example;

public class Statistics {

    double mean;
    double min;
    double std;
    Solution bestSolution;

    private void calculateMean(Solution[] sol) {
        double sum = 0;
        for (Solution solution : sol) {
            sum += solution.fitnessValue;
        }
        mean = sum/sol.length;
    }

    private double calculateMeanXInDimension(Solution[] solutions, int d) {
        double mean = 0;
        for (Solution solution: solutions) {
            mean+=solution.x[d];
        }
        mean = mean/solutions.length;
        return mean;
    }

    public Solution calculateMin(Solution[] sol) {
        min = sol[0].fitnessValue;
        for (int i = 1; i < sol.length; i++) {
            if (sol[i].fitnessValue < min)
                min = sol[i].fitnessValue;
            this.bestSolution = sol[i];
        }
        return bestSolution;
    }

    private void calculateStd(Solution[] sol) {
        for (int i = 1; i < sol.length; i++) {
            std += Math.pow(sol[i].fitnessValue - mean, 2);
        }
        this.std = 1.0/sol.length * std;
    }

    public double calculateXStdInDimension(Solution[] solutions, int d) {
        double mean = calculateMeanXInDimension(solutions, d);
        double std = 0;
        for (Solution solution: solutions) {
            std += Math.pow(solution.x[d] - mean, 2);
        }
        return std;
    }

    void calculateAndShowStatistics(String problemName, Solution[] sol) {
        calculateMean(sol);
        calculateMin(sol);
        calculateStd(sol);
        System.out.printf(problemName + ": min: " + (float)(min) +", avg: " + (float)(mean) + ", std: " + (float)(std) + "\n");
        System.out.printf("Best solution: " + (float)(min) + " for x: ");
        for (int i = 0; i < bestSolution.x.length; i++) {
            System.out.printf((float)(bestSolution.x[i]) + ", ");
        }
        System.out.println("\n");
        resetStatistics();
    }

    void resetStatistics() {
        mean = 0;
        min = 0;
        std = 0;
    }

}
