package ch.epfl.tchu.thomas;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class NeuronTest {

    @Test
    public void updateWork() {
        List<Neuron> children = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            children.add(new InputNeuron(2));
            children.get(i).setValue(5);
        }

        Neuron neuron = new Neuron(10, 2, children);
        neuron.update();

        System.out.println(neuron);
    }

}