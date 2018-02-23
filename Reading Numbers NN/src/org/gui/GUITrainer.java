package org.gui;

import org.network.Network;

public class GUITrainer {

	// private TrainerThread trainerThread;
	private boolean currentlyTraining = false;
	private int trainingCyclesLeft = 0;

	private volatile Network currentNetwork;
	private double[][] trainingDataInputs;
	private double[][] trainingDataOutputsExpected;

	public GUITrainer() {
		// (trainerThread = new TrainerThread()).start();
		new TrainerThread().start();
	}

	public void startTraining(Network net, int iterations, double[][] inputValues, double[][] expectedOutputs) {
		// System.out.println(net);
		// TODO what if another network is passed into this function; should the
		// current training be stopped?
		if (!this.currentlyTraining) {

			this.trainingCyclesLeft = iterations;
			this.currentNetwork = net;
			this.trainingDataInputs = inputValues.clone();
			this.trainingDataOutputsExpected = expectedOutputs.clone();

		} else {
			System.out.println("training didn't start because the network is already being trained");
		}
	}

	private class TrainerThread extends Thread {
		public void run() {
			this.setName("GUI Network Trainer Thread");

			int iterationsDone = 0;
			int dataset;
			while (true) {

				// System.out.println(currentNetwork);

				if (trainingCyclesLeft > 0) {
					System.out.println("-----------------------");
					// train
					dataset = iterationsDone % trainingDataInputs.length;
					System.out.println("dataset: " + dataset);
					System.out.println("training data: " + trainingDataInputs[dataset][0] + " + "
							+ trainingDataInputs[dataset][1] + " = " + trainingDataOutputsExpected[dataset][0]);
					currentNetwork.train(trainingDataInputs[dataset], trainingDataOutputsExpected[dataset]);

					System.out.println("ERROR: " + currentNetwork.getErrorForAddition());
					System.out.println("3 + 4 = " + currentNetwork.feedForward(new double[] { 3, 4 })[0]);

					trainingCyclesLeft--;
					iterationsDone++;

					// } else if (trainingCyclesLeft == 1) {
					// dataset = (int) (Math.random() *
					// trainingDataInputs.length);
					//
					// System.out.println("dataset: " + dataset);
					// currentNetwork.train(trainingDataInputs[dataset],
					// trainingDataOutputsExpected[dataset]);
					//
					// System.out.println("ERROR: " +
					// currentNetwork.getErrorForAddition());
					// System.out.println("3 + 4 = " +
					// currentNetwork.feedForward(new double[] { 3, 4 })[0]);
					//
					// trainingCyclesLeft--;
					// iterationsDone++;
				} else {
					iterationsDone = 0;

					try {
						Thread.sleep(10);
					} catch (Exception e) {
					}
				}

			}
		}
	}
}
