package org.example;

import org.example.problems.Problem;

import java.util.Random;

public class DEAlgorithm implements Algorithm{

    private double CR;
    Statistics statistics = new Statistics();
    private double F;
    private int NP;
//    public String name = "DEA";
    public DEAlgorithm(double CR, double F, int NP) throws Exception {
        this.CR = CR;
        if (F > 2.0 || F <= 0) {
            throw new Exception("F should be in range [0,2.0]");
        }
        if (CR > 1.0 || CR < 0) {
            throw new Exception("CR should be in range [0,1.0]");
        }
        this.F = F;
        if (NP < 4) {
            throw new Exception("NP should be >= 4");
        }
        this.NP = NP;
    }

    @Override
    public Solution compute(Problem problem) {
        Solution[] population = new Solution[NP];
        //random agent indexes
        int a, b, c;
        //random dimension R
        int R;
        Random rand = new Random();

        for (int i = 0; i < NP; i++) {
            //initialize agents
            population[i] = problem.evaluate(problem.generateRandomInput());
        }
        while (problem.currentEvals <= problem.maxFES) {
            for (int i = 0; i < NP; i++) {//0
                do a = rand.nextInt(NP);//1
                while (a == i);

                do b = rand.nextInt(NP);
                while (b == i && b == a);

                do c = rand.nextInt(NP);
                while (c == i && c == a && c == b);

                R = rand.nextInt(problem.n);
                double[] y = new double[problem.n];
                //each dimension
                for (int j = 0; j < problem.n; j++) {
                    if (rand.nextDouble() < CR || R == j) {
                        y[j] = population[a].x[j] + F * (population[b].x[j] - population[c].x[j]);
                        if (y[j] > problem.UpperBounds[j])
                            y[j] = problem.UpperBounds[j];
                        else if (y[j] < problem.LowerBounds[j])
                            y[j] = problem.LowerBounds[j];
                    }
                    else {
                        y[j] = population[i].x[j];
                    }
                    Solution newSol = problem.evaluate(y);
                    if (population[i].fitnessValue > newSol.fitnessValue)
                        population[i] = newSol;
                }
            }
        }
        statistics.resetStatistics();
        statistics.calculateMin(population);

        return statistics.bestSolution;
    }
}
