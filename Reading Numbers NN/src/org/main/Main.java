package org.main;

import org.activationFunctions.Linear;
import org.network.Network;

public class Main {

	public static void main(String[] args) {
		test1();
		test2();
		// Network net = new Network(new int[] {2, 2, 1},
		// Network.ACTIVATION_LEAKY_RELU);
		//
		// long time = System.currentTimeMillis();
		// for(int i = 0; i < 1000; i++) {
		// net.train(new double[] {1, 3}, new double[] {1});
		// }
		// System.out.println(System.currentTimeMillis() - time);
	}

	public static void test2() {
		Network net = Network.loadNetworkFromFile("addierer.txt");
		net.setLearningRate(0.0000001);
		net.setActivationFunction(new Linear());

		int d1, d2;
		int iterations = 100;
		double[][] inputs = new double[iterations][2];
		double[][] outputs = new double[iterations][1];

		for (int i = 0; i < iterations; i++) {
			d1 = (int) (Math.random() * 32 - 16);
			d2 = (int) (Math.random() * 32 - 16);
			inputs[i] = new double[] { d1, d2 };
			outputs[i] = new double[] { d1 + d2 };
		}

		net.trainingSession(inputs, outputs);

		System.out.println(net.feedForward(new double[] { 2390, 23432 })[0]);

	}

	public static void test1() {
		Network net = new Network(new int[] { 2, 5, 5, 1 }, new Linear(), 0.0000001);

		long time = System.currentTimeMillis();
		int d1, d2;
		double[] inputs = new double[2];
		double[] outputs = new double[1];
		for (int i = 0; i < 5000000; i++) {
			d1 = (int) (Math.random() * 16 - 8);
			d2 = (int) (Math.random() * 16 - 8);
			inputs = new double[] { d1, d2 };
			outputs = new double[] { d1 + d2 };
			net.train(inputs, outputs);
		}

		System.out.println((System.currentTimeMillis() - time));

		Network.exportToFile(net, "addierer.txt");

	}

}