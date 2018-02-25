package org.gui;

import org.network.Network;
import org.trainingAlgorithms.TrainingAlgorithm;

public class GUITrainer {

	private GUI gui;

	private boolean currentlyTraining = false;
	private long trainingCyclesLeft;

	private boolean workingWithAlgorithm;
	private TrainingAlgorithm trainingAlgorithm;

	public GUITrainer(GUI gui) {
		// (trainerThread = new TrainerThread()).start();
		this.gui = gui;
		// new TrainerThread().start();
	}

	/**
	 * train network via algorithm
	 */
	public void startTraining(Network net, long iterations, TrainingAlgorithm trainingAlgorithm) {
		if (!this.currentlyTraining) {
			this.currentlyTraining = true;

			this.workingWithAlgorithm = true;
			this.trainingAlgorithm = trainingAlgorithm;
			this.trainingCyclesLeft = iterations;

			new TrainerThread(net).start();
		} else {
			System.out.println("training didn't start because the network is already being trained");
		}
	}

	// private volatile Network currentNetwork;
	private double[][] trainingDataInputs;
	private double[][] trainingDataOutputsExpected;

	/**
	 * traing network via inputs and outputs (manually)
	 */
	public void startTraining(Network net, int iterations, double[][] inputValues, double[][] expectedOutputs) {
		// System.out.println(net);
		// TODO what if another network is passed into this function; should the
		// current training be stopped?
		if (!this.currentlyTraining) {
			this.currentlyTraining = true;
			this.workingWithAlgorithm = false;

			this.trainingCyclesLeft = iterations;
			// this.currentNetwork = net;
			this.trainingDataInputs = inputValues.clone();
			this.trainingDataOutputsExpected = expectedOutputs.clone();

			new TrainerThread(net).start();

		} else {
			System.out.println("training didn't start because the network is already being trained");
		}
	}

	private class TrainerThread extends Thread {

		private Network net;

		public TrainerThread(Network net) {
			super();
			this.net = net;
		}

		public void run() {
			System.out.println("GUI training Thread running!");
			this.setName("GUI Network Trainer Thread");

			if (workingWithAlgorithm) {
				// TODO alles xD

				trainingAlgorithm.start(gui, net, trainingCyclesLeft);

			} else {

				long iterationsDone = 0;
				int dataset;
				// while loop for training manually
				while (trainingCyclesLeft > 0) {

					// System.out.println(currentNetwork);

					// System.out.println("-----------------------");
					// train
					dataset = (int) (iterationsDone % trainingDataInputs.length);
					// System.out.println("dataset: " + dataset);
					// System.out.println("training data: " +
					// trainingDataInputs[dataset][0] + " + "
					// + trainingDataInputs[dataset][1] + " = " +
					// trainingDataOutputsExpected[dataset][0]);
					net.train(trainingDataInputs[dataset], trainingDataOutputsExpected[dataset]);

					// System.out.println("ERROR: " +
					// currentNetwork.getErrorForAddition());
					// System.out.println("3 + 4 = " +
					// currentNetwork.feedForward(new double[] { 3, 4
					// })[0]);

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
					// currentNetwork.feedForward(new double[] { 3, 4
					// })[0]);
					//
					// trainingCyclesLeft--;
					// iterationsDone++;

				}

			}

			currentlyTraining = false;
		}
	}
}
