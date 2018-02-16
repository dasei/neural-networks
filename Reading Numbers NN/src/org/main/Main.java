package org.main;

import org.network.MathFunctions;
import org.network.Network;

public class Main {

	public static void main(String[] args) {

		Network net = new Network(new int[] { 2, 1600, 1600, 1600, 1 });
		double d1 = Math.random() * 5;
		double d2 = Math.random() * 10;
		long time = System.currentTimeMillis();
		net.train(new double[] { d1, d2 }, new double[] { MathFunctions.sigmoid(d1 + d2) });
		System.out.println(System.currentTimeMillis() - time);

	}

}