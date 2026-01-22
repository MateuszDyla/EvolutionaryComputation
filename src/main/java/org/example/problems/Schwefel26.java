package org.example.problems;

import org.example.Solution;

public class Schwefel26 extends Problem{
    public Schwefel26(int n, String name, int maxFES) {
        super(n, name, maxFES);
        this.eps = 1e-4;
        this.minimumFitnessValue = -837.96576;
        for (int i = 0; i < n; i++) {
            LowerBounds[i] = -500;
            UpperBounds[i] = 500;
        }
    }

    @Override
    public Solution evaluate(double[] x) {
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum += x[i] * Math.sin(Math.sqrt(Math.abs(x[i])));
        }
        fitnessValue = -sum;
        currentEvals++;
        return new Solution(x, fitnessValue);
    }
}
