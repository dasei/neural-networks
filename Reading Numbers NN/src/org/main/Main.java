package org.main;

import org.network.Network;

public class Main {

	public static void main(String[] args) {
		System.out.println(new Network(new int[] { 1, 2, 1 }).feedForward(new double[] { 1.5 })[0]);

	}

}
