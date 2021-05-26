package ch.epfl.tchu.thomas;

public class InputNeuron extends Neuron {

    public InputNeuron(double weight) {
        super(0, weight, null);
    }

    @Override
    public String toString() {
        return String.format("Input value : %s Weight : %s", value(), weight());
    }
}
