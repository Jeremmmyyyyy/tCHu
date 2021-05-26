package ch.epfl.tchu.thomas;

import java.util.List;

public class Neuron {

    private double bias;
    private double weight;
    private final List<Neuron> children;
    private double value;

    public Neuron(double bias, double weight, List<Neuron> children) {
        this.bias = bias;
        this.weight = weight;
        this.children = children;
    }

    public double bias() {
        return bias;
    }

    public double weight() {
        return weight;
    }

    public double value(){
        return value;
    }

    public List<Neuron> children() {
        return children;
    }

    public void setValue(double newValue) {
        value = newValue;
    }

    public void update() {
        double newValue = 0;
        for (Neuron child : children) {
            newValue += child.weight() * child.value();
        }
        value = NeuralMath.reluFunction(newValue + bias);
    }

    @Override
    public String toString() {
        return String.format("Value : %s Weight : %s Bias : %s", value, weight, bias);
    }


}
