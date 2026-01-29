package org.example;

import org.example.problems.*;
import java.util.*;

public class IraceRunner {

    public static void main(String[] args) {
        Map<String, String> a = parseArgs(args);

        String algName = a.getOrDefault("alg", "ClassicGA");
        String problemName = a.getOrDefault("problem", "Ackley");

        int n = Integer.parseInt(a.getOrDefault("n", "2"));
        int maxFES = Integer.parseInt(a.getOrDefault("fes", "10000"));
        long seed = Long.parseLong(a.getOrDefault("seed", "1"));

        int pop = Integer.parseInt(a.getOrDefault("pop", "100"));
        double cx = Double.parseDouble(a.getOrDefault("cx", "0.7"));
        double pm = Double.parseDouble(a.getOrDefault("pm", "0.01"));
        double step = Double.parseDouble(a.getOrDefault("step", "0.01"));
        int tour = Integer.parseInt(a.getOrDefault("tour", "4"));
        boolean stopOnOptimum = Boolean.parseBoolean(a.getOrDefault("stopOnOptimum", "false"));

        // ile replikacji na tej samej konfiguracji (dla niedeterministycznych real-testów)
        int reps = Integer.parseInt(a.getOrDefault("reps", "1"));

        try {
            double cost = runWithReplications(problemName, algName, n, maxFES, seed,
                    pop, cx, pm, step, tour, stopOnOptimum, reps);

            if (!Double.isFinite(cost)) {
                System.out.println("1e100");
                return;
            }
            System.out.printf(Locale.ROOT, "%.15g%n", cost);
        } catch (Exception e) {
            System.out.println("1e100");
        }
    }

    private static double runWithReplications(
            String problemName, String algName,
            int n, int maxFES, long seed,
            int pop, double cx, double pm, double step, int tour,
            boolean stopOnOptimum,
            int reps
    ) {
        double[] costs = new double[Math.max(1, reps)];

        for (int r = 0; r < costs.length; r++) {
            long runSeed = seed + 1000003L * r; // rozróżnij replikacje deterministycznie
            costs[r] = singleRunCost(problemName, algName, n, maxFES, runSeed,
                    pop, cx, pm, step, tour, stopOnOptimum);

            if (!Double.isFinite(costs[r])) {
                costs[r] = 1e100;
            }
        }

        // median (bardziej odporna niż mean na outliery)
        Arrays.sort(costs);
        int mid = costs.length / 2;
        if (costs.length % 2 == 1) return costs[mid];
        return 0.5 * (costs[mid - 1] + costs[mid]);
    }

    private static double singleRunCost(
            String problemName, String algName,
            int n, int maxFES, long seed,
            int pop, double cx, double pm, double step, int tour,
            boolean stopOnOptimum
    ) {
        Random rng = new Random(seed);

        Problem problem = buildProblem(problemName, n, maxFES);
        problem.setRandom(rng);

        GeneticAlgorithm alg = buildAlgorithm(algName, pop, cx, pm, step, tour, stopOnOptimum);
        alg.setRandom(rng);

        problem.reset();
        Solution best = alg.compute(problem);

        double fitness = best.getFitnessValue();
        if (!Double.isFinite(fitness)) return 1e100;

        double fStar = problem.getMinimumFitnessValue();

        // gap względem optimum (zakładamy minimizację)
        double gap = Math.max(0.0, fitness - fStar);

        // stabilniejsza normalizacja skali między problemami
        double scale = Math.max(1.0, Math.max(Math.abs(fStar), Math.abs(fitness)));

        return gap / scale;
    }

    private static Problem buildProblem(String name, int n, int maxFES) {
        return switch (name) {
            case "Ackley" -> new Ackley(n, "Ackley", maxFES);
            case "Rosenbrock" -> new Rosenbrock(n, "Rosenbrock", maxFES);
            case "Schwefel26" -> new Schwefel26(n, "Schwefel26", maxFES);
            case "Sphere" -> new Sphere(n, "Sphere", maxFES);
            default -> throw new IllegalArgumentException("Unknown problem: " + name);
        };
    }

    private static GeneticAlgorithm buildAlgorithm(
            String name,
            int pop, double cx, double pm, double step, int tour,
            boolean stopOnOptimum
    ) {
        return switch (name) {
            case "ClassicGA" -> new ClassicGA(pop, cx, pm, step, tour, stopOnOptimum);
            case "ElitistGA" -> new ElitistGA(pop, cx, pm, step, tour, stopOnOptimum);
            case "SteadyStateGA" -> new SteadyStateGA(pop, cx, pm, step, tour, stopOnOptimum);
            default -> throw new IllegalArgumentException("Unknown alg: " + name);
        };
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> m = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String token = args[i];
            if (!token.startsWith("--")) continue;

            String key = token.substring(2);
            String val = "true";

            if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                val = args[++i];
            }
            m.put(key, val);
        }
        return m;
    }
}
