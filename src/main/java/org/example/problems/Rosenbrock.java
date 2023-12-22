package org.example.problems;

import org.example.Solution;

public class Rosenbrock extends Problem{
    public Rosenbrock(int n, String name, int maxFES) {
        super(n, name, maxFES);
        for (int i = 0; i < n; i++) {
            LowerBounds[i] = -5;
            UpperBounds[i] = 10;
        }
    }

    @Override
    public Solution evaluate(double[] x) {
        fitnessValue = 0;
        for (int i = 0; i < n-1; i++) {
            fitnessValue += 100*(Math.pow(x[i+1] - x[i]*x[i], 2) + Math.pow(x[i] - 1, 2));
        }
        currentEvals++;
        return new Solution(x, fitnessValue);
    }
}
