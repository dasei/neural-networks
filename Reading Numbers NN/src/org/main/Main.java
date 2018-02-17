package org.main;

import org.activationFunctions.*;
import org.network.Network;

public class Main {

	public static void main(String[] args) {
		test1();
		test2();
//		test3();
	}
	
	public static void test3() {
		Network net = Network.loadNetworkFromFile("addierer.txt");
		net.setLearningRate(0.0000001);
		net.setActivationFunction(new ReLU());
		double i1 = 7565465.466846;
		double i2 = 4864.78455;
		System.out.println(net.feedForward(new double[] {i1, i2})[0]);
	}

	public static void test2() {
		Network net = Network.loadNetworkFromFile("addierer.txt");
		net.setLearningRate(0.000001);
		net.setActivationFunction(new Linear());

		int d1, d2;
		int iterations = 100;
		double[][] inputs = new double[iterations][2];
		double[][] outputs = new double[iterations][1];

		for (int i = 0; i < iterations; i++) {
			d1 = (int) (Math.random() * 16);
			d2 = (int) (Math.random() * 16);
			inputs[i] = new double[] { d1, d2 };
			outputs[i] = new double[] { d1 + d2 };
		}

		net.trainingSession(inputs, outputs);

	}

	public static void test1() {
		Network net = new Network(new int[] { 2, 5, 5, 1 }, new Linear(), 0.000001);

		long time = System.currentTimeMillis();
		int d1, d2;
		double[] inputs = new double[2];
		double[] outputs = new double[1];
		for (int i = 0; i < 5000000; i++) {
			d1 = (int) (Math.random() * 8);
			d2 = (int) (Math.random() * 8);
			inputs = new double[] { d1, d2 };
			outputs = new double[] { d1 + d2 };
			net.train(inputs, outputs);
		}

		System.out.println((System.currentTimeMillis() - time));

		Network.exportToFile(net, "addierer.txt");

	}

}