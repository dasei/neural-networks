package org.main;

import java.util.Arrays;

import org.network.Network;

public class Main {

	public static void main(String[] args) {
		test1();
//		Network net = new Network(new int[] {2, 25, 12, 1});
//		
//		long time = System.currentTimeMillis();
//		for(int i = 0; i < 10; i++) {
//			net.train(new double[] {1,  3}, new double[] {0.52});
//		}
//		System.out.println(System.currentTimeMillis() - time);
	}

	public static void test2() {
		Network net = Network.loadNetworkFromFile("H:/addiererBinär.txt");

		int d1, d2;
		char[] str;
		double[] inputs = new double[10];
		double[] outputs = new double[6];

		d1 = (int) (Math.random() * 32);
		d2 = (int) (Math.random() * 32);

		d1 = 0;
		d2 = 0;

		str = Integer.toBinaryString(d1).toCharArray();
		for (int j = 0; j < str.length; j++) {
			inputs[j + (5 - str.length)] = Integer.parseInt("" + str[j]);
		}
		str = Integer.toBinaryString(d2).toCharArray();
		for (int j = 0; j < str.length; j++) {
			inputs[j + 5 + (5 - str.length)] = Integer.parseInt("" + str[j]);
		}
		str = Integer.toBinaryString(d1 + d2).toCharArray();
		for (int j = 0; j < str.length; j++) {
			outputs[j] = Integer.parseInt("" + str[j]);
		}

		System.out.println("adding " + d1 + " and " + d2 + ", erg: " + Arrays.toString(net.feedForward(inputs)));

	}

	public static void test1() {
		Network net = new Network(new int[] { 10, 7, 7, 6 });

		// System.out.println("erg: " + net.feedForward(new double[] { })[0]);

		// System.out.println();
		//
		// long time = System.currentTimeMillis();
		int d1, d2;
		char[] str;
		double[] inputs = new double[10];
		double[] outputs = new double[6];
		for (int i = 0; i < 1000000000; i++) {
			d1 = (int) (Math.random() * 32);
			d2 = (int) (Math.random() * 32);

			str = Integer.toBinaryString(d1).toCharArray();
			for (int j = 0; j < str.length; j++) {
				inputs[j + (5 - str.length)] = Integer.parseInt("" + str[j]);
			}
			str = Integer.toBinaryString(d2).toCharArray();
			for (int j = 0; j < str.length; j++) {
				inputs[j + 5 + (5 - str.length)] = Integer.parseInt("" + str[j]);
			}
			str = Integer.toBinaryString(d1 + d2).toCharArray();
			for (int j = 0; j < str.length; j++) {
				outputs[j] = Integer.parseInt("" + str[j]);
			}

			net.train(inputs, outputs);
		}
		// System.out.println((System.currentTimeMillis() - time));
		
//		Network.exportToFile(net, "C:/addiererBinär.txt");

	}

}