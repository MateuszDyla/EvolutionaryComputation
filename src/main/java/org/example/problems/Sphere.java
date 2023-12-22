package org.example.problems;

import org.example.Solution;

public class Sphere extends Problem{
    public Sphere(int n, String name, int maxFES) {
        super(n, name, maxFES);
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                LowerBounds[i] = -100;
                UpperBounds[i] = 100;

            }
            else {
                LowerBounds[i] = -10;
                UpperBounds[i] = 10;
            }
        }
    }

    @Override
    public Solution evaluate(double[] x) {
        fitnessValue = 0;
        for (int i = 0; i < n; i++) {
            fitnessValue += x[i] * x[i];
        }
        currentEvals++;
        return new Solution(x, fitnessValue);
    }
}
