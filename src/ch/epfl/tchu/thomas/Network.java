package ch.epfl.tchu.thomas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Network {

    private final int inputCount;
    private final int hiddenCount;
    private final int outputCount;

    private final List<Neuron> inputNeurons;
    private final List<Neuron> hiddenNeurons;
    private final List<Neuron> outputNeurons;

    private static final List<String> OUTPUTS = List.of("Tree", "Village", "City", "House", "Wood", "Field");


    public Network(int inputCount, int outputCount) {
        assert OUTPUTS.size() == outputCount;

        this.inputCount = inputCount;
        this.hiddenCount = (inputCount + outputCount) / 2;
        this.outputCount = outputCount;

        Random rng = new Random();

        inputNeurons = new ArrayList<>();
        for (int i = 0; i < inputCount; i++) {
            inputNeurons.add(new InputNeuron(rng.nextDouble()));
        }

        hiddenNeurons = new ArrayList<>();
        for (int i = 0; i < hiddenCount; i++) {
            hiddenNeurons.add(new Neuron(rng.nextDouble(), rng.nextDouble(), inputNeurons));
        }

        outputNeurons = new ArrayList<>();
        for (int i = 0; i < outputCount; i++) {
            outputNeurons.add(new OutputNeuron(rng.nextDouble(), hiddenNeurons, OUTPUTS.get(i)));
        }

        setInputs(List.of(0., 1., 0., 1.));

    }

    public List<Double> outputs() {
        List<Double> outputs = new ArrayList<>();
        outputNeurons.forEach(n -> outputs.add(n.value()));

        return outputs;
    }

    private void setInputs(List<Double> inputs) {
        assert inputs.size() == inputCount;
        for (int i = 0; i < inputCount; i++) {
            inputNeurons.get(i).setValue(inputs.get(i));
        }
    }

    public void train(TrainingSet trainingSet) {

        List<Double> costs = new ArrayList<>();

        trainingSet.dataMap().forEach((inputs, expected) -> {
            setInputs(inputs);
            feedForward();
            System.out.printf("Inputs : %s Outputs : %s Expected : %s\n", inputs, outputs(), expected);

            costs.add(NeuralMath.costFunction(outputs(), expected));
        });

        System.out.println(costs);

    }


    public void feedForward() {
        hiddenNeurons.forEach(Neuron::update);
        outputNeurons.forEach(Neuron::update);
    }

    public List<Neuron> inputNeurons() {
        return inputNeurons;
    }

    public List<Neuron> hiddenNeurons() {
        return hiddenNeurons;
    }

    public List<Neuron> outputNeurons() {
        return outputNeurons;
    }
}
