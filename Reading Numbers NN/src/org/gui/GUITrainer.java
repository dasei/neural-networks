package org.gui;

import org.network.Network;
import org.trainingAlgorithms.TrainingAlgorithm;

public class GUITrainer {

	private GUI gui;

	private boolean busy = false; 	// training or calculating fitness
	private boolean paused;			// training paused 
	private long trainingCyclesLeft;
	private long testCyclesLeft;

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
		if (!this.busy) {
			this.busy = true;

			this.workingWithAlgorithm = true;
			this.trainingAlgorithm = trainingAlgorithm;
			this.trainingCyclesLeft = iterations;

			new TrainerThread(net).start();
		} else {
			System.out.println("training didn't start because the network is already being trained");
		}
	}

	private double[][] trainingDataInputs;
	private double[][] trainingDataOutputsExpected;
	
	private double[][] testDataInputs;
	private double[][] testDataOutputsExpected;

	/**
	 * traing network via inputs and outputs (manually)
	 */
	public void startTraining(Network net, int iterations, double[][] inputValues, double[][] expectedOutputs) {
		// System.out.println(net);
		if (!this.busy) {
			this.busy = true;
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
//			System.out.println("GUI training Thread running!");
			this.setName("GUI Network Trainer Thread");
			paused = false;
			
			if (workingWithAlgorithm) {

				trainingAlgorithm.run(gui, net, trainingCyclesLeft);

			} else {

				long iterationsDone = 0;
				int dataset;
				// while loop for training manually
				while (trainingCyclesLeft > 0) {

					dataset = (int) (iterationsDone % trainingDataInputs.length);
					
					net.train(trainingDataInputs[dataset], trainingDataOutputsExpected[dataset]);

					trainingCyclesLeft--;
					iterationsDone++;

				}
			}
			busy = false;
		}
	}
	
	public boolean isCurrentlyTraining() {
		return this.busy;
	}
	
	public void pauseOrResume() {
		if(this.isCurrentlyTraining()) {
			if(this.isPaused()) {
				paused = false;
				trainingAlgorithm.resume();
			}else {				
				paused = true;
				trainingAlgorithm.pause();			
			}
		}
	}
	
	public boolean isPaused() {
		return this.paused;
	}
	
	public void abortAlgorithm() {
		System.out.println("abort!");
		this.trainingAlgorithm.abort();
		this.busy = false;
	}
	
	public void startFitness(Network net, TrainingAlgorithm trainingAlgorithm) {		
		this.trainingAlgorithm = trainingAlgorithm;
		startFitness(net);
	}
	
	public void startFitness(Network net) {	
		this.busy = true;
		this.workingWithAlgorithm = true;
		
		new FitnessThread(net).start();		
	}
	
	private class FitnessThread extends Thread{
		private Network net;
		public FitnessThread(Network net) {
			super();
			this.net = net;
		}
		
		public void run() {
			this.setName("Neural Network FitnessTest");
			
			if(workingWithAlgorithm) {
				trainingAlgorithm.fitnessTest(gui, net);
			}else {
				long iterationsDone = 0;
				int dataset;
				int fitnessSum = 0;
				// while loop for training manually
				while (testCyclesLeft > 0) {

					dataset = (int) (iterationsDone % testDataInputs.length);

					double[] netOut = net.feedForward(trainingDataInputs[dataset]);
					boolean wasRight = true;
					for(int i = 0; i < testDataOutputsExpected.length; i++) {
						if(!(netOut[i] == testDataOutputsExpected[dataset][i])) {
							wasRight = false;
						}
					}
					
					if(wasRight) {
						fitnessSum++;
					}
					
					testCyclesLeft--;
					iterationsDone++;
				}
				
				//TODO in GUI einbauen
				System.out.println("Fitness: " + (fitnessSum + 0.0)/iterationsDone);
			}
			
			busy = false;
		}
	}
}
