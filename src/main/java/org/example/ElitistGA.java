package org.example;

import org.example.problems.Problem;

import java.util.List;

public class ElitistGA extends ClassicGA {

    public ElitistGA(int populationSize, double crossoverRate, double mutationRate, double mutationStep, int tournamentSize, boolean stopOnOptimum) {
        super(populationSize, crossoverRate, mutationRate, mutationStep, tournamentSize, stopOnOptimum);
        this.name = "ElitistGA";
    }

    @Override
    protected List<Solution> replacePopulation(List<Solution> oldPopulation,
                                               List<Solution> offspring,
                                               Solution globalBest,
                                               Problem problem) {

        Solution bestOffspring = getBest(offspring);

        if (globalBest.fitnessValue < bestOffspring.fitnessValue) {
            int worstIndex = getWorstIndex(offspring);
            offspring.set(worstIndex, cloneSolution(globalBest));
        }

        return offspring;
    }
}
