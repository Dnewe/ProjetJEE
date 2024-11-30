package com.jeeproject.util;

import com.jeeproject.model.Result;

import java.util.List;

public class MathUtil {


    public static double calculateAverage(List<Result> results) {
        double totalWeightedGrades = 0.0;
        double totalWeights = 0.0;
        for (Result result : results) {
            totalWeightedGrades += result.getGrade() / result.getMaxScore() * result.getWeight();
            totalWeights += result.getWeight();
        }
        return Math.round((totalWeights == 0.0 ? 0.0 : totalWeightedGrades / totalWeights)*20*100)/100.;
    }
}
