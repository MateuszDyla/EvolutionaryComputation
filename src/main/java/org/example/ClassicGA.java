package org.example;

import org.example.problems.Problem;

import java.util.ArrayList;
import java.util.List;

public class ClassicGA extends GeneticAlgorithm {

    public ClassicGA(int populationSize, double crossoverRate, double mutationRate, int tournamentSize, boolean stopOnOptimum) {
        super(populationSize, crossoverRate, mutationRate, tournamentSize, stopOnOptimum);
    }

    @Override
    protected List<Solution> createOffspring(List<Solution> population, Problem problem) {
        List<Solution> offspring = new ArrayList<>(populationSize);

        while (offspring.size() < populationSize && !terminationCondition(problem)) {
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
            if (offspring.size() + 1 < populationSize && !terminationCondition(problem)) {
                children[1] = evaluateIndividual(children[1].x, problem);
                offspring.add(children[1]);
            }
            offspring.add(children[0]);
        }

        return offspring;
    }

    @Override
    protected List<Solution> replacePopulation(List<Solution> oldPopulation,
                                               List<Solution> offspring,
                                               Solution globalBest,
                                               Problem problem) {
        return offspring;
    }
}
