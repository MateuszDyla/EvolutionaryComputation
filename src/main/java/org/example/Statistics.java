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
        bestSolution = sol[0];
        min = sol[0].fitnessValue;

        for (int i = 1; i < sol.length; i++) {
            if (sol[i].fitnessValue < min) {
                min = sol[i].fitnessValue;
                bestSolution = sol[i];
            }
        }
        return bestSolution;
    }

    private void calculateStd(Solution[] sol) {
        double sumSq = 0.0;

        for (int i = 0; i < sol.length; i++) {
            double diff = sol[i].fitnessValue - mean;
            sumSq += diff * diff;
        }

        double variance = sumSq / sol.length;
        this.std = Math.sqrt(variance);
    }

    public double calculateXStdInDimension(Solution[] solutions, int d) {
        double mean = calculateMeanXInDimension(solutions, d);
        double std = 0;
        for (Solution solution: solutions) {
            std += Math.pow(solution.x[d] - mean, 2);
        }
        return std;
    }

    Solution calculateAndShowStatistics(String problemName, Solution[] sol) {
        calculateMean(sol);
        Solution best = calculateMin(sol);
        calculateStd(sol);
        System.out.printf(problemName + ": min: " + (float)(min) +", avg: " + (float)(mean) + ", std: " + (float)(std) + "\n");
        System.out.printf("Best solution: " + (float)(min) + " for x: ");
        for (int i = 0; i < bestSolution.x.length; i++) {
            System.out.printf((float)(bestSolution.x[i]) + ", ");
        }
        System.out.println("\n");
        resetStatistics();
        return best;
    }

    void resetStatistics() {
        mean = 0;
        min = 0;
        std = 0;
    }

}
