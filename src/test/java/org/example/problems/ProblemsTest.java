package org.example.problems;
import java.lang.Object;
import org.example.Solution;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProblemsTest {

    int n = 40;
    int maxFES = 10000;
    //i - current set number of dimensions
    @Test
    void isSphereOptimumCalculatedCorrectly() {
        for (int i = 1; i < 100; i++) {
            Sphere sphere = new Sphere(i, "Sphere", maxFES);
            double[] x = new double[i];

            for (int j = 0; j < n; j++) {
                Solution solution = sphere.evaluate(x);
                assertEquals(0, solution.getFitnessValue());
            }
        }
    }

    @Test
    void isAckleyOptimumCalculatedCorrectly() {
        for (int i = 1; i < n; i++) {
            Ackley ackley = new Ackley(i, "Ackley", maxFES);
            double[] x = new double[i];

            for (int j = 0; j < n; j++) {
                Solution solution = ackley.evaluate(x);
                //error margin because of the operations in floating-point?
                assertEquals(0, solution.getFitnessValue(), 1e-10);
            }
        }
    }

    @Test
    void isSchwefelOptimumCalculatedCorrectly() {
        for (int i = 1; i < n; i++) {
            Schwefel26 schwefel26 = new Schwefel26(i, "Schwefel", maxFES);
            double[] x = new double[i];

            for (int j = 0; j < i; j++) {
                x[j] = 420.968746;
            }

            Solution solution = schwefel26.evaluate(x);
            assertEquals(-418.98288*i, solution.getFitnessValue(), 0.00001*i);
        }
    }

    @Test
    void isRosenbrockOptimumCalculatedCorrectly() {
        for (int i = 1; i < n; i++) {
            Rosenbrock rosenbrock = new Rosenbrock(i, "Rosenbrock", maxFES);
            double[] x = new double[i];

            for (int j = 0; j < i; j++) {
                x[j] = 1;
            }

            Solution solution = rosenbrock.evaluate(x);
            assertEquals(0, solution.getFitnessValue());
        }
    }

    @Test
    void isBukinOptimumCalculatedCorrectly() {
        Bukin bukin = new Bukin(2, "Bukin", maxFES);
        double[] x = {-10.0, 1};

        Solution solution = bukin.evaluate(x);
        assertEquals(0, solution.getFitnessValue());
    }

    @Test

    void isCarromTableOptimumCalculatedCorrectly() {
        CarromTable carromTable = new CarromTable(2, "CarromTAble", maxFES);
        double[] x = {9.64615726634881, 9.64615726634881};


        Solution solution = carromTable.evaluate(x);
        assertEquals(-24.15681551650653, solution.getFitnessValue(), 1e-7);
    }

    @Test
    void isEasomOptimumCalculatedCorrectly() {
        Easom easom = new Easom(2, "Easom", maxFES);
        double[] x = {Math.PI, Math.PI};

        Solution solution = easom.evaluate(x);
        assertEquals(-1, solution.getFitnessValue());
    }

    @Test
    void isTridOptimumCalculatedCorrectly() {
        for (int i = 1; i < n; i++) {
            Trid trid = new Trid(i, "Trid", maxFES);
            double[] x = new double[i];

            for (int j = 1; j <= i; j++) {
                x[j-1] = j*(i+1-j);
            }

            Solution solution = trid.evaluate(x);
            assertEquals(-i*((i+4) * (i-1)) / 6.0, solution.getFitnessValue());
        }
    }
}