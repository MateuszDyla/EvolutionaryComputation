package org.example.problems;

import org.example.Solution;

public class Easom extends Problem{
    public Easom(int n, String name, int maxFES) {
        //n is fixed for this problem
        super(2, name, maxFES);
        for (int i = 0; i < 2; i++) {
            LowerBounds[i] = -100;
            UpperBounds[i] = 100;
        }
    }

    @Override
    public Solution evaluate(double[] x) {
        fitnessValue = -Math.cos(x[0]) * Math.cos(x[1]) * Math.exp(-Math.pow(x[0] - Math.PI,2) - Math.pow(x[1] - Math.PI,2));
        currentEvals++;
        return new Solution(x, fitnessValue);
    }
}
