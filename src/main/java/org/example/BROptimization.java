package org.example;

import org.example.problems.Problem;

import java.util.Random;

public class BROptimization implements Algorithm{

    int NP;
    int threshold;
    double[] lb;
    double[] ub;
    Problem problem;

    public BROptimization(int NP, int threshold) throws Exception {
        if (NP < 4) {
            throw new Exception("NP should be >= 4");
        }
        this.NP = NP;
        this.threshold = threshold;
    }

    @Override
    public Solution compute(Problem problem) {
        Random rand = new Random();
        Statistics statistics = new Statistics();

        int[] xDamage = new int[NP];
        Solution[] population = new Solution[NP];

        this.problem = problem;
        lb = problem.LowerBounds;
        ub = problem.UpperBounds;

        int maxCicle = problem.maxFES - NP;         //max number of generations
        int shrink = (int) Math.ceil(Math.log10(maxCicle));
        int delta = Math.round((float) (maxCicle/shrink));
        int iter = 0;
        for (int i = 0; i < NP; i++) {
            //initialize soldiers
            population[i] = problem.evaluate(problem.generateRandomInput());
        }
        Solution bestSol;

        while (problem.currentEvals <= problem.maxFES) {
            iter+=1;
            int bestIndex = findBestSolution(population);
            bestSol = population[bestIndex];
            for (int i=1; i < NP; i++) {
                //compare ith soldier with nearest one
                int j = findClosestTo(i, population);
                int dam = j;
                int vic = i;
                if (population[i].fitnessValue < population[j].fitnessValue) {
                    dam = i;
                    vic = j;
                }
                Solution damSol = population[dam];
                Solution vicSol = population[vic];

                //change the position on soldier based on...
                if (xDamage[dam] < threshold) {
                    for (int d = 0; d < problem.n; d++) {
                        double newPos = rand.nextDouble()*(Math.max(damSol.x[d], bestSol.x[d]) - Math.min(damSol.x[d], bestSol.x[d])) + Math.max(damSol.x[d], bestSol.x[d]);
                        if (newPos > ub[d]) newPos = ub[d];
                        if (newPos < lb[d]) newPos = lb[d];

                        population[dam].x[d] = newPos;
                    }
                    xDamage[dam] = xDamage[dam] + 1;
                }
                else {//player dies and respawn
                    for (int d = 0; d < problem.n; d++) {
                        double newPos = rand.nextDouble() * (ub[d] - lb[d]) + lb[d];
                        if (newPos > ub[d]) newPos = ub[d];
                        if (newPos < lb[d]) newPos = lb[d];

                        population[dam].x[d] = newPos;
                    }
                }
                population[dam] = problem.evaluate(population[dam].x);
                xDamage[dam] = 0;
                xDamage[vic] = 0;
            }
            if (iter >= delta) {
                updateBounds(population, bestSol);
                delta = delta + Math.round((float) (delta/2));
            }

        }
        int bestIndex = findBestSolution(population);
        bestSol = population[bestIndex];
        return bestSol;
    }

    private int findClosestTo(int index, Solution[] population) {
        double closestDistance = 0;
        int closestIndex = 0;
        Solution sol = population[index];

        for (int i = 0; i < NP; i++) {
            if (i == index) continue;

            Solution sol2 = population[i];
            double dist = 0;
            for (int j = 0; j < sol.x.length; j++) {
                dist+= Math.pow(sol2.x[j] - sol.x[j], 2);
            }
            dist = Math.sqrt(dist);

            if (i == 0) {
                closestDistance = dist;
            }
            else {
                if (dist < closestDistance) {
                    closestDistance = dist;
                    closestIndex = i;
                }
            }
        }
        return closestIndex;
    }

    private void updateBounds(Solution[] pop, Solution bestSol) {
        Statistics statistics = new Statistics();
        for (int d = 0; d < lb.length; d++) {
            double std = statistics.calculateXStdInDimension(pop, d);
            lb[d] = bestSol.x[d] - std;
            ub[d] = bestSol.x[d] + std;
            if (lb[d] < problem.LowerBounds[d])
                lb[d] = problem.LowerBounds[d];

            if (ub[d] > problem.UpperBounds[d])
                ub[d] = problem.UpperBounds[d];
        }
    }

    private int findBestSolution(Solution[] population) {
        int min = 0;
        for (int i = 0; i < population.length; i++) {
            if (population[i].fitnessValue < population[min].fitnessValue)
                min = i;
        }

        return min;
    }
}
