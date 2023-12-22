package org.example.problems;

import org.example.Solution;

public class CarromTable extends Problem{
    public CarromTable(int n, String name, int maxFES) {
        // n is fixed in this problem
        super(2, name, maxFES);
        for (int i = 0; i < 2; i++) {
            LowerBounds[i] = -10;
            UpperBounds[i] = 10;
        }
    }

    @Override
    public Solution evaluate(double[] x) {
        double power = 2 * Math.abs(1 - (Math.sqrt(x[0]*x[0] + x[1] * x[1])/Math.PI));
        fitnessValue = -1.0/30 * Math.pow(Math.E, power) * Math.pow(Math.cos(x[0]), 2) * Math.pow(Math.cos(x[1]), 2);
        currentEvals++;
        return new Solution(x, fitnessValue);

    }
}
