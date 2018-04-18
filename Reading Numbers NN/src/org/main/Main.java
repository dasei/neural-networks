package org.main;

import java.io.File;

import org.activationFunctions.Linear;
import org.activationFunctions.ReLU;
import org.dataLoaders.IDXLoader;
import org.gui.GUI;
import org.network.Network;
import org.trainingAlgorithms.TrainingAlgorithm_ReadingNumbers;

public class Main {

	public static void main(String[] args) {
		

		// Daniels krasser Rechner: 1.5 Mille in 2 Minuten
		// Marcos Pupsi: 336 Tausend in 2 Minuten
		
		new GUI();

		IDXLoader.startLoadingData("train-images.idx3-ubyte", TrainingAlgorithm_ReadingNumbers.CATEGORY_IMAGES, 60000, 28 * 28);
		IDXLoader.startLoadingData("train-labels.idx1-ubyte", TrainingAlgorithm_ReadingNumbers.CATEGORY_LABELS, 60000, 10);
		
		IDXLoader.startLoadingData("t10k-images.idx3-ubyte", TrainingAlgorithm_ReadingNumbers.CATEGORY_IMAGES_TEST, 10000, 28 * 28);
		IDXLoader.startLoadingData("t10k-labels.idx1-ubyte", TrainingAlgorithm_ReadingNumbers.CATEGORY_LABELS_TEST, 10000, 10);
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
			int iterations = 50000;
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