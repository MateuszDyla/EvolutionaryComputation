package org.example.problems;

import org.example.Solution;

public class Ackley extends Problem{

    double a = 20;
    double b = 0.2;
    double c = Math.PI*2;
    public Ackley(int n, String name, int maxFES) {
        super(n, name, maxFES);
        for (int i = 0; i < n; i++) {
            LowerBounds[i] = -32.768;
            UpperBounds[i] = 32.768;

        }
    }

    @Override
    public Solution evaluate(double[] x) {
        double sum1 = 0;
        double sum2 = 0;
        for (int i = 0; i < n; i++) {
            sum1 += x[i] * x[i];
            sum2 += Math.cos(c * x[i]);
        }

        fitnessValue = -a * Math.exp(-b * Math.sqrt(1.0/n * sum1)) - Math.exp(1.0/n * sum2) + a + Math.exp(1);
        currentEvals++;
        return new Solution(x, fitnessValue);
    }
}
