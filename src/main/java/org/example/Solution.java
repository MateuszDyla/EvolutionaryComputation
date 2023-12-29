package org.example;

public class Solution {

    double[] x;
    double fitnessValue;

    public Solution() {
        this.fitnessValue = 0;
    }

    public Solution(double[] x, double fitnessValue) {
        this.x = x;
        this.fitnessValue = fitnessValue;
    }

    public double getFitnessValue() {
        return fitnessValue;
    }

    public void changeInputOnIndex(int index, double val) {
        x[index] = val;
    }
}
