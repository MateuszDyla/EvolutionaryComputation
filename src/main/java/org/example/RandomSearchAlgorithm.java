package org.example;

import org.example.problems.Problem;

public class RandomSearchAlgorithm implements Algorithm {

    Solution bestSolution = new Solution();
    public String name = "RandomSearch";

    public Solution compute(Problem problem) {

        for (int i = 0; i < problem.getMaxFES(); i++) {
            Solution solution = problem.evaluate(problem.generateRandomInput());
            if (i == 0) bestSolution = solution;
            else if (bestSolution.getFitnessValue() > solution.getFitnessValue())
                bestSolution = solution;
        }
        return bestSolution;
    }

}
