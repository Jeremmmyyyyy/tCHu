package ch.epfl.tchu.thomas;

import java.util.List;

public class OutputNeuron<E> extends Neuron {

    private final E output;

    public OutputNeuron(double bias, List<Neuron> children, E output) {
        super(bias, 0, children);
        this.output = output;
    }

    public E output() {
        return output;
    }

    @Override
    public String toString() {
        return String.format("Value : %s Output : %s", value(), output);
    }
}
