package org.example;

import org.example.problems.Problem;

import java.util.ArrayList;
import java.util.List;

public class SteadyStateGA extends GeneticAlgorithm {

    public SteadyStateGA(int populationSize,
                         double crossoverRate,
                         double mutationRate,
                         double mutationStep,
                         int tournamentSize,
                         boolean stopOnOptimum) {
        super(populationSize, crossoverRate, mutationRate, mutationStep, tournamentSize, stopOnOptimum);
        this.name="SteadyStateGA";
    }

    @Override
    protected List<Solution> createOffspring(List<Solution> population, Problem problem) {
        List<Solution> offspring = new ArrayList<>(2);

        Solution p1 = tournamentSelection(population);
        Solution p2 = tournamentSelection(population);

        Solution[] children;
        if (rand.nextDouble() < crossoverRate) {
            children = crossover(p1, p2, problem);
        } else {
            children = new Solution[]{cloneSolution(p1), cloneSolution(p2)};
        }

        mutate(children[0], problem);
        mutate(children[1], problem);

        children[0] = evaluateIndividual(children[0].x, problem);
        offspring.add(children[0]);

        boolean child0IsOpt = stopOnOptimum
                && Math.abs(children[0].fitnessValue - problem.minimumFitnessValue) < problem.eps;

        if (!child0IsOpt && !terminationCondition(problem)) {
            children[1] = evaluateIndividual(children[1].x, problem);
            offspring.add(children[1]);
        }

        return offspring;
    }

    @Override
    protected List<Solution> replacePopulation(List<Solution> population,
                                               List<Solution> offspring,
                                               Solution globalBest,
                                               Problem problem) {

        int worstIndex = getWorstIndex(population);
        double worstFitness = population.get(worstIndex).fitnessValue;

        for (Solution child : offspring) {
            if (child.fitnessValue < worstFitness) {
                population.set(worstIndex, child);

                worstIndex = getWorstIndex(population);
                worstFitness = population.get(worstIndex).fitnessValue;
            }
        }

        return population;
    }
}
