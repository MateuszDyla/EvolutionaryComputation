package org.example.problems;

import org.example.Solution;

public class Bukin extends Problem{
    public Bukin(int n, String name, int maxFES) {
        //In Bukin problem n is fixed to 2
        super(2, name, maxFES);
        LowerBounds[0] = -15;
        LowerBounds[1] = -3;
        UpperBounds[0] = -5;
        UpperBounds[1] = 3;
    }

    @Override
    public Solution evaluate(double[] x) {
        fitnessValue = 100 * Math.sqrt(Math.abs(x[1] - 0.01 * x[0]*x[0])) + 0.01*Math.abs(x[0]+10);
        currentEvals++;
        return new Solution(x, fitnessValue);
    }
}
