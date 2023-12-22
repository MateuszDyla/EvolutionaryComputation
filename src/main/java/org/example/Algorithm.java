package org.example;

import org.example.problems.Problem;

public interface Algorithm {
    public String name = "";
    public Solution compute(Problem problem);

}