package org.example;

import org.example.problems.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {


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
    private static final Path PATH = Paths.get("results.csv");


    public static void main(String[] args) throws Exception {
        int n = 2;
        int rep = 500; // number of repetitions
        int maxFES = 10000;
        Problem[] problems = getProblems(n, maxFES);

        Algorithm RSA = new RandomSearchAlgorithm();
        GeneticAlgorithm classicGA = new ClassicGA(365, 0.7549, 0.03242, 0.0685, 2, false);
        GeneticAlgorithm elitistGA = new ElitistGA(314, 0.7582, 0.0625, 0.0056, 6, false);
        GeneticAlgorithm steadyStateGA = new SteadyStateGA(345, 0.6526, 0.0470, 0.0118, 3, false);

//        System.out.println("----------------------------RSA-------------------------\n");
//        runAlg(rep, RSA, problems);

        System.out.println("----------------------------Classic GA-------------------------\n");
        runAlg(rep, classicGA, problems);
        System.out.println("----------------------------Elitist GA-------------------------\n");
        runAlg(rep, elitistGA, problems);
        System.out.println("----------------------------Steady State GA-------------------------\n");
        runAlg(rep, steadyStateGA, problems);


    }

    private static Problem[] getProblems(int n, int MaxFES) {
        int maxFES = 10000;

        Ackley ackley = new Ackley(n, "Ackley", maxFES);
        Rosenbrock rosenbrock = new Rosenbrock(n, "Rosenbrock", maxFES);
        Schwefel26 schwefel26 = new Schwefel26(n, "Schwefel26", maxFES);
        Sphere sphere = new Sphere(n, "Sphere", maxFES);

        Problem problems[] = {ackley, rosenbrock, schwefel26, sphere};
        return problems;
    }

    private static void runAlg(int rep, GeneticAlgorithm alg, Problem[] problems) {
        Statistics statistics = new Statistics();
        for (Problem problem: problems) {
            Solution warmup = null;
            for (int i = 0; i < 100; i++) {
                problem.reset();
                warmup = alg.compute(problem); // żeby JIT nie wywalił pracy
            }


            long t0 = System.nanoTime();
            Solution[] solutions = new Solution[rep];
            for (int i = 0; i < rep; i++) {
                problem.reset();
                solutions[i] = alg.compute(problem);
            }
            long t1 = System.nanoTime();
            double ms = (t1 - t0) / 1000000.0;

            statistics.calculateAndSaveStatistics(PATH, problem, alg, solutions, ms);
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

