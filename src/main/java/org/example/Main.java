package org.example;

import org.example.problems.*;

public class Main {

    public static void main(String[] args) throws Exception {
        int n = 2; // number of dimensions (for problems supporting more than 2)
        int rep = 20; // number of repetitions
        int maxFES = 10000;

        Ackley ackley = new Ackley(n, "Ackley", maxFES);
        Bukin bukin = new Bukin(n, "Bukin", maxFES);
        CarromTable carromTable = new CarromTable(n, "CarromTable", maxFES);
        Easom easom = new Easom(n, "Easom", maxFES);
        Rosenbrock rosenbrock = new Rosenbrock(n, "Rosenbrock", maxFES);
        Schwefel26 schwefel26 = new Schwefel26(n, "Schwefel26", maxFES);
        Sphere sphere = new Sphere(n, "Sphere", maxFES);
        Trid trid = new Trid(n, "Trid", maxFES);

        Problem problems[] = {ackley, bukin, carromTable, easom, rosenbrock, schwefel26, sphere, trid};
//        Problem problems[] = {carromTable};

        Algorithm RSA = new RandomSearchAlgorithm();

        Algorithm DEA = new DEAlgorithm(0.5, 0.6, 20);
        Algorithm BRO = new BROptimization(20, 3);

//        System.out.println("----------------------------RSA-------------------------\n");
//        runAlg(rep, RSA, problems);

        System.out.println("----------------------------DEA-------------------------\n");
        runAlg(rep, DEA, problems);

        System.out.println("----------------------------BRO-------------------------\n");
        runAlg(rep, BRO, problems);

    }

    private static void runAlg(int rep, Algorithm alg, Problem[] problems) {
        Statistics statistics = new Statistics();
        for (Problem problem: problems) {
            Solution[] solutions = new Solution[rep];
            for (int i = 0; i < rep; i++) {
                problem.reset();
                solutions[i] = alg.compute(problem);
            }
            statistics.calculateAndShowStatistics(problem.getName(), solutions);
            statistics.resetStatistics();
        }
    }
}