package ch.epfl.tchu.thomas;

import java.util.List;

import static java.lang.Math.max;

public final class NeuralMath {

    public static double reluFunction(double x) {
        return max(0, x);
    }

    public static double costFunction(List<Double> actual, List<Double> expected) {
        assert actual.size() == expected.size();

        double cost = 0.;
        for (int i = 0; i < actual.size(); i++) {
//            System.out.printf("Cost = %s | newCost = (actual(%s) - expected(%s))**2 = %s\n", cost, actual.get(i), expected.get(i), (actual.get(i) - expected.get(i)) * (actual.get(i) - expected.get(i)));
            cost += (actual.get(i) - expected.get(i)) * (actual.get(i) - expected.get(i));
        }

        return cost;
    }

    public static double reluDerivative(double x) {
        return x <= 0 ? 0 : 1;
    }

}
