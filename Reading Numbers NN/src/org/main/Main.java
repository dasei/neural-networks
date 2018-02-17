package org.main;


import org.network.Network;

public class Main {

	public static void main(String[] args) {
		test2();
//		Network net = new Network(new int[] {2, 2, 1}, Network.ACTIVATION_LEAKY_RELU);
//		
//		long time = System.currentTimeMillis();
//		for(int i = 0; i < 1000; i++) {
//			net.train(new double[] {1,  3}, new double[] {1});
//		}
//		System.out.println(System.currentTimeMillis() - time);
	}

	public static void test2() {
		Network net = new Network(new int[] { 2, 5, 5, 1 }, Network.ACTIVATION_LEAKY_RELU, 0.000001);

		int d1, d2;
		int iterations = 10000000;
		double[][] inputs = new double[iterations][2];
		double[][] outputs = new double[iterations][1];
		
		for (int i = 0; i < iterations; i++) {
			d1 = (int) (Math.random() * 8);
			d2 = (int) (Math.random() * 8);
			inputs[i] = new double[] {d1, d2};
			outputs[i] = new double[] {d1 + d2};
		}
		
		net.trainingSession(inputs, outputs);

	}

	public static void test1() {
		Network net = new Network(new int[] { 2, 5, 5, 1 }, Network.ACTIVATION_LEAKY_RELU, 0.000001);

		long time = System.currentTimeMillis();
		int d1, d2;
		double[] inputs = new double[2];
		double[] outputs = new double[1];
		for (int i = 0; i < 1000000; i++) {
			d1 = (int) (Math.random() * 8);
			d2 = (int) (Math.random() * 8);
			inputs = new double[] {d1, d2};
			outputs = new double[] {d1 + d2};
			net.train(inputs, outputs);
		}

		 System.out.println((System.currentTimeMillis() - time));
		
		Network.exportToFile(net, "addierer.txt");

	}

}