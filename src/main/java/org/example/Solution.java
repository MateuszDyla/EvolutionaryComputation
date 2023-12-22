package org.example;

public class Solution {

    double[] input;
    double fitnessValue;

    public Solution() {
        this.fitnessValue = 0;
    }

    public Solution(double[] input, double fitnessValue) {
        this.input = input;
        this.fitnessValue = fitnessValue;
    }

    public double getFitnessValue() {
        return fitnessValue;
    }
}
