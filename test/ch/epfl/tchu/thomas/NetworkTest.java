package ch.epfl.tchu.thomas;

import org.junit.jupiter.api.Test;

public class NetworkTest {

    @Test
    void feedForwardWorks() {
        Network network = new Network(4, 6);

        System.out.println("InputNeurons : ");
        network.inputNeurons().forEach(System.out::println);
        System.out.println("HiddenNeurons : ");
        network.hiddenNeurons().forEach(System.out::println);
        System.out.println("OutputNeurons : ");
        network.outputNeurons().forEach(System.out::println);

        network.feedForward();

        System.out.println("InputNeurons : ");
        network.inputNeurons().forEach(System.out::println);
        System.out.println("HiddenNeurons : ");
        network.hiddenNeurons().forEach(System.out::println);
        System.out.println("OutputNeurons : ");
        network.outputNeurons().forEach(System.out::println);

    }

    @Test
    void costCalculusWorks() {
        Network network = new Network(4, 6);

    }
}
