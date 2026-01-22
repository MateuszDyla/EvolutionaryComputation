package org.example;

import org.example.problems.*;

public class Main {

    Solution rosenbrockMin = new Solution(new double[]{1.0, 1.0}, 0.0);
    Solution schefelmin = new Solution(new double[]{420.9687, 420.9687}, -837.9658);
    Solution sphereMin = new Solution(new double[]{0.0, 0.0}, 0.0);
    Solution ackleyMin = new Solution(new double[]{0.0, 0.0}, 0.0);



//    public static void main(String[] args) throws Exception {
//        int maxFES = 10000;
//        int n = 2; // number of dimensions (for problems supporting more than 2)
//
//        Algorithm ClassicGA = new ClassicGA(100, 0.7, 0.01, 3);
//        Ackley ackley = new Ackley(n, "Ackley", maxFES);
//        Rosenbrock rosenbrock = new Rosenbrock(n, "Rosenbrock", maxFES);
//        Schwefel26 schwefel26 = new Schwefel26(n, "Schwefel26", maxFES);
//        Sphere sphere = new Sphere(n, "Sphere", maxFES);
//
//    }

//    public static void runProgram(int dimensions, int maxFES) {
//        Algorithm ClassicGA = new ClassicGA(100, 0.7, 0.01, 3);
//        Algorithm ElitistGA = new ElitistGA(150, 0.7, 0.01, 4);
//        Algorithm SteadyStateGA = new SteadyStateGA(150, 0.7, 0.01, 4);
//
//        Ackley ackley = new Ackley(dimensions, "Ackley", maxFES);
//        Rosenbrock rosenbrock = new Rosenbrock(dimensions, "Rosenbrock", maxFES);
//        Schwefel26 schwefel26 = new Schwefel26(dimensions, "Schwefel26", maxFES);
//        Sphere sphere = new Sphere(dimensions, "Sphere", maxFES);
//
//
//
//    }

//    public static void main(String[] args) {
//        int dimensions = 2;
//        int maxFES = 10000;
//        runProgram(dimensions, maxFES);
//    }


    public static void main(String[] args) throws Exception {
        int n = 2;
        int rep = 500; // number of repetitions
        Problem[] problems = getProblems(n);
//        Problem problems[] = {carromTable};

        Algorithm RSA = new RandomSearchAlgorithm();
        GeneticAlgorithm classicGA = new ClassicGA(100, 0.7, 0.01, 4, false);
        GeneticAlgorithm elitistGA = new ElitistGA(150, 0.7, 0.01, 4, false);
        GeneticAlgorithm steadyStateGA = new SteadyStateGA(150, 0.7, 0.01, 4, false);

//        System.out.println("----------------------------RSA-------------------------\n");
//        runAlg(rep, RSA, problems);
//
//        System.out.println("----------------------------Classic GA-------------------------\n");
//        runAlg(rep, classicGA, problems);
//        System.out.println("----------------------------Elitist GA-------------------------\n");
//        runAlg(rep, elitistGA, problems);
//        System.out.println("----------------------------Steady State GA-------------------------\n");
//        runAlg(rep, steadyStateGA, problems);

        classicGA.setStopOnOptimum(true);
        elitistGA.setStopOnOptimum(true);
        steadyStateGA.setStopOnOptimum(true);

        System.out.println("-------------------FES until optimum found----------------------\n");
        System.out.printf("\nClassic GA results:\n");
        calculateUntilBestFoundForProblems(classicGA, problems, rep);
        System.out.printf("\nElitist GA results:\n");
        calculateUntilBestFoundForProblems(elitistGA, problems, rep);
        System.out.printf("\nSteady State GA results:\n");
        calculateUntilBestFoundForProblems(steadyStateGA, problems, rep);


    }

    private static Problem[] getProblems(int n) {
        int maxFES = 10000000;

        Ackley ackley = new Ackley(n, "Ackley", maxFES);
//        Bukin bukin = new Bukin(n, "Bukin", maxFES);
//        CarromTable carromTable = new CarromTable(n, "CarromTable", maxFES);
//        Easom easom = new Easom(n, "Easom", maxFES);
        Rosenbrock rosenbrock = new Rosenbrock(n, "Rosenbrock", maxFES);
        Schwefel26 schwefel26 = new Schwefel26(n, "Schwefel26", maxFES);
        Sphere sphere = new Sphere(n, "Sphere", maxFES);
//        Trid trid = new Trid(n, "Trid", maxFES);

        Problem problems[] = {ackley, rosenbrock, schwefel26, sphere};
        return problems;
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


    private static void calculateUntilBestFoundForProblems(Algorithm alg, Problem[] problems, int rep) {
        System.out.printf("\nResults for algorithm: %s\n", alg.getClass().getSimpleName());

        for (Problem problem : problems) {
            long totalFES = 0;
            double totalFitness = 0;

            for (int i = 0; i < rep; i++) {
                problem.reset(); // Ważne: reset licznika przed każdym uruchomieniem
                Solution solution = alg.compute(problem); //
                totalFES += problem.currentEvals; //
                totalFitness += solution.getFitnessValue(); //
            }

            double avgFES = (double) totalFES / rep;
            double avgFitness = totalFitness / rep;

            System.out.printf("%s: Avg FES until optimum: %.2f (Avg fitness: %.2E)\n",
                    problem.getName(), avgFES, avgFitness);
        }
    }


}

