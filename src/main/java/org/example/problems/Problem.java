package org.example.problems;
import org.example.Solution;

import java.util.*;

public abstract class Problem {

    public int n; //number of dimensions
    public double[] LowerBounds;
    public double[] UpperBounds;
    double fitnessValue = 0;
    public int maxFES;
    public int currentEvals = 0;
    public double minimumFitnessValue = 0;
    public double eps;
    String name;

    public Problem(int n, String name, int maxFES) {
        this.n = n;
        this.LowerBounds = new double[n];
        this.UpperBounds = new double[n];
        this.name = name;
        this.maxFES = maxFES;
    }

    public double[] generateRandomInput() {
        fitnessValue = 0;
        double[] x = new double[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            x[i] = rand.nextDouble(UpperBounds[i] - LowerBounds[i]) + LowerBounds[i];
        }
        return x;
    }

    public abstract Solution evaluate(double[] x);


    public int getMaxFES() {
        return maxFES;
    }

    public String getName() {
        return name;
    }

    public void reset() {
        currentEvals = 0;
    }

}