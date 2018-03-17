package org.main;

import java.io.File;

import org.activationFunctions.Linear;
import org.activationFunctions.ReLU;
import org.gui.GUI;
import org.network.Network;

public class Main {

	public static void main(String[] args) {
		// test1();
		// test2();

		new GUI();
		
//		idxTest();
	}
	
	public static void idxTest() {
		
//		IDXLoader loaderLabel = new IDXLoader("H://train-labels.idx1-ubyte");
//		IDXLoader loaderImage = new IDXLoader("H://train-images.idx3-ubyte");
//		
//		System.out.println("hello?");
//		
////		for(byte b : loaderImage.getData(1)) {
////			System.out.println(b);			
////		}
//		
//		loaderLabel.getData(6);
//		loaderImage.getData(6);
//		
//		
//		int[] dataLabel = loaderLabel.getData(6);
//		int[] dataImg = loaderImage.getData(6);
//		
//		for(int b : dataLabel) {
//			System.out.println(b);			
//		}
//
//		IDXImageInterpreter.showImage(dataImg, 28);
//		
////		for(byte b : loader.getData(5)) {			
////			System.out.println(b);			
////		}
		
	}

	public static void guiTest() {
		new GUI();
	}

	public static void test3() {
		try {
			Network net = Network.loadNetworkFromFile(new File("addierer.txt"));
			net.setLearningRate(0.0000001);
			net.setActivationFunction(new ReLU());
			double i1 = 7565465.466846;
			double i2 = 4864.78455;
			System.out.println(net.feedForward(new double[] { i1, i2 })[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void test2() {
		try {
			Network net = Network.loadNetworkFromFile(new File("addierer.txt"));
			net.setLearningRate(0.000001);
			net.setActivationFunction(new Linear());

			int d1, d2;
			int iterations = 100;
			double[][] inputs = new double[iterations][2];
			double[][] outputs = new double[iterations][1];

			for (int i = 0; i < iterations; i++) {
				d1 = (int) (Math.random() * 8);
				d2 = (int) (Math.random() * 8);
				inputs[i] = new double[] { d1, d2 };
				outputs[i] = new double[] { d1 + d2 };
			}

			net.trainingSession(inputs, outputs);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public static void test1() {
		Network net = new Network(new int[] { 2, 5, 5, 1 }, new Linear(), 0.0000001);

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

		Network.exportToFile(net, new File("addierer.txt"));

	}

}