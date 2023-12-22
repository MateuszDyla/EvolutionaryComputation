package org.example.problems;

import org.example.Solution;

public class Trid extends Problem {
    public Trid(int n, String name, int maxFES) {
        super(n, name, maxFES);

        for (int i = 0; i < n; i++) {
            LowerBounds[i] = -n*n;
            UpperBounds[i] = n*n;
        }
    }

    @Override
    public Solution evaluate(double[] x) {
        double sum1 = 0;
        double sum2 = 0;
        for (int i = 0; i < n; i++) {
            sum1 += Math.pow(x[i] - 1, 2);
            if (i >=1)
                sum2 += x[i] * x[i-1];
         }
        fitnessValue = sum1 - sum2;
        currentEvals++;
        return new Solution(x, fitnessValue);
    }

}
