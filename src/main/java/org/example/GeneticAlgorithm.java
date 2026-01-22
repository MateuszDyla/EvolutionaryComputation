package org.example;

import org.example.problems.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class GeneticAlgorithm implements Algorithm {

    protected int populationSize;
    protected double crossoverRate;
    protected double mutationRate;
    protected int tournamentSize;
    protected boolean stopOnOptimum = false;

    protected Random rand = new Random();

    protected GeneticAlgorithm(int populationSize,
                               double crossoverRate,
                               double mutationRate,
                               int tournamentSize, boolean stopOnOptimum) {
        this.populationSize = populationSize;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.tournamentSize = tournamentSize;
        this.stopOnOptimum = stopOnOptimum;
    }

    @Override
    public Solution compute(Problem problem) {
        problem.reset();
        // 1. Inicjalizacja
        List<Solution> population = initializePopulation(problem);
        Solution best = cloneSolution(getBest(population));

        // 2. Główna pętla GA (template method)
        while ( !terminationCondition(problem)) {
            // krok generowania potomków – zależny od wariantu GA
            List<Solution> offspring = createOffspring(population, problem);

            // strategia zastępowania populacji – zależna od wariantu GA
            population = replacePopulation(population, offspring, best, problem);

            // aktualizacja najlepszego rozwiązania globalnego
            best = updateBest(population, best);

            if ( stopOnOptimum && Math.abs(best.fitnessValue - problem.minimumFitnessValue) < problem.eps ) {
                break;
            }
        }

        return best;
    }

    /* ======================== METODY ABSTRAKCYJNE (hooki) ======================== */

    /**
     * Tworzenie nowych osobników na podstawie bieżącej populacji.
     * Classic GA: zwraca populację tej samej wielkości co populationSize.
     * SSGA: np. tylko 1–2 osobniki na iterację.
     */
    protected abstract List<Solution> createOffspring(List<Solution> population, Problem problem);

    /**
     * Strategia zastępowania populacji:
     * - Classic GA: potomstwo całkowicie zastępuje starą populację
     * - Elitist GA: jak wyżej, ale z gwarantowaniem elity
     * - SSGA: częściowa wymiana (steady-state)
     */
    protected abstract List<Solution> replacePopulation(List<Solution> oldPopulation,
                                                        List<Solution> offspring,
                                                        Solution globalBest,
                                                        Problem problem);

    /* ======================== WSPÓLNE METODY POMOCNICZE ======================== */

    protected boolean terminationCondition(Problem problem) {
        if (problem.currentEvals >= problem.getMaxFES()) {
            return true;
        }
        return false;
    }

    protected List<Solution> initializePopulation(Problem problem) {
        List<Solution> population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize && !terminationCondition(problem); i++) {
            double[] x = problem.generateRandomInput();
            Solution s = evaluateIndividual(x, problem);
            population.add(s);
        }
        return population;
    }

    protected Solution evaluateIndividual(double[] x, Problem problem) {
        return problem.evaluate(x);
    }

    protected Solution getBest(List<Solution> population) {
        Solution best = population.get(0);
        for (int i = 1; i < population.size(); i++) {
            if (population.get(i).fitnessValue < best.fitnessValue) { // MINIMIZACJA
                best = population.get(i);
            }
        }
        return best;
    }

    protected int getWorstIndex(List<Solution> population) {
        int worstIndex = 0;
        for (int i = 1; i < population.size(); i++) {
            if (population.get(i).fitnessValue > population.get(worstIndex).fitnessValue) { // MINIMIZACJA
                worstIndex = i;
            }
        }
        return worstIndex;
    }

    protected Solution updateBest(List<Solution> population, Solution currentBest) {
        Solution popBest = getBest(population);
        if (popBest.fitnessValue < currentBest.fitnessValue) {
            return cloneSolution(popBest);
        }
        return currentBest;
    }


    protected Solution cloneSolution(Solution s) {
        double[] xCopy = new double[s.x.length];
        System.arraycopy(s.x, 0, xCopy, 0, s.x.length);
        return new Solution(xCopy, s.fitnessValue);
    }

    protected Solution tournamentSelection(List<Solution> population) {
        Solution best = null;
        for (int i = 0; i < tournamentSize; i++) {
            Solution cand = population.get(rand.nextInt(population.size()));
            if (best == null || cand.fitnessValue < best.fitnessValue) {
                best = cand;
            }
        }
        return best;
    }

    /**
     * Proste krzyżowanie dla wektorów R^n – liniowa kombinacja.
     * Zwraca 2 dzieci.
     */protected Solution[] crossover(Solution p1, Solution p2, Problem problem) {
        int n = p1.x.length;
        double[] c1 = new double[n];
        double[] c2 = new double[n];

        for (int i = 0; i < n; i++) {
            double alpha = rand.nextDouble();
            c1[i] = alpha * p1.x[i] + (1 - alpha) * p2.x[i];
            c2[i] = alpha * p2.x[i] + (1 - alpha) * p1.x[i];

            c1[i] = repairToBounds(c1[i], problem.LowerBounds[i], problem.UpperBounds[i]);
            c2[i] = repairToBounds(c2[i], problem.LowerBounds[i], problem.UpperBounds[i]);
        }

        // dzieci z samym x, fitnessValue będzie nadpisany przy evaluateIndividual
        return new Solution[] {
                new Solution(c1, 0),
                new Solution(c2, 0)
        };
    }


    /**
     * Prosta mutacja: dodaj losowe zaburzenie w 10% zakresu.
     * Re-ewaluacja powinna być wykonana przez kod, który woła mutate(),
     * bo tutaj zmieniamy tylko genotyp (x).
     */
    protected void mutate(Solution s, Problem problem) {
        for (int i = 0; i < s.x.length; i++) {
            if (rand.nextDouble() < mutationRate) {
                double range = problem.UpperBounds[i] - problem.LowerBounds[i];
                double delta = (rand.nextDouble() * 2 - 1) * 0.01 * range; // ±10% zakresu
                double val = s.x[i] + delta;
                s.x[i] = repairToBounds(val, problem.LowerBounds[i], problem.UpperBounds[i]);
            }
        }
    }

    protected double repairToBounds(double val, double lb, double ub) {
        if (val < lb) return lb;
        if (val > ub) return ub;
        return val;
    }

    public void setStopOnOptimum(boolean stopOnOptimum) {
        this.stopOnOptimum = stopOnOptimum;
    }
}
