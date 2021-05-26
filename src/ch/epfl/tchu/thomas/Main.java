package ch.epfl.tchu.thomas;

public class Main {

    //TODO Make the feedFroward process with a matrix f(W*a + b) W square matrix, a, b column vectors (for one layer)

    public static void main(String[] args) {
        Network network = new Network(4, 4);
//
//        System.out.println(network.output());
//        network.feedForward();
//        System.out.println(network.output());

//        TrainingSet trainingSet = new TrainingSet("/home/yann/Documents/EPFL/BA2/POO/Thomas/trainingSet.txt", 16);

        network.train(new TrainingSet("/home/yann/Documents/EPFL/BA2/POO/Thomas/trainingSet.txt", 16));
//        trainingSet.dataMap().forEach((l, s) -> System.out.printf("Input : %s Output : %s\n", l, s));

    }
}
