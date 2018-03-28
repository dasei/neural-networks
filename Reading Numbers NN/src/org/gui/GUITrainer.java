package org.gui;

import org.network.Network;
import org.trainingAlgorithms.TrainingAlgorithm;
import org.trainingAlgorithms.TrainingAlgorithm_ReadingNumbers;

public class GUITrainer {

	private GUI gui;

	private TrainingState state;
	private long trainingCyclesLeft;
	private long testCyclesLeft;

	private boolean workingWithAlgorithm;
	private TrainingAlgorithm trainingAlgorithm;

	public GUITrainer(GUI gui) {
		// (trainerThread = new TrainerThread()).start();
		this.gui = gui;
		this.state = TrainingState.WAITING;
		setTrainingAlgorithm(new TrainingAlgorithm_ReadingNumbers());
		// new TrainerThread().start();
	}

	/**
	 * train network via algorithm
	 */
	public void startTraining(Network net, long iterations) {
		if(trainingAlgorithm == null) {
			return;
		}
		
		if (state == TrainingState.WAITING) {
			this.state = TrainingState.TRAINING;

			this.workingWithAlgorithm = true;
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
		if (state == TrainingState.WAITING) {
			this.state = TrainingState.TRAINING;
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
			state = TrainingState.WAITING;
		}
	}
	
	public void startFitness(Network net) {
		if(trainingAlgorithm == null) {
			return;
		}
		
		if(state == TrainingState.WAITING) {
			state = TrainingState.FITNESS;
			this.workingWithAlgorithm = true;
			
			new FitnessThread(net).start();
		} 
		else if(state == TrainingState.PAUSED) {
			state =TrainingState.FITNESS_ON_PAUSE;
			this.workingWithAlgorithm = true;
			
			new FitnessThread(net).start();
		}
		else {
			System.out.println("FitnessTest didn't start because Training is already running");
		}
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
			
			if(state == TrainingState.FITNESS) {
				state = TrainingState.WAITING;
			}
			else if(state == TrainingState.FITNESS_ON_PAUSE) {
				state = TrainingState.PAUSED;
			}

		}
	}
	
	public void pauseOrResume() {
		if(this.isCurrentlyTraining() && state != TrainingState.FITNESS && 
				state != TrainingState.FITNESS_ON_PAUSE) {
			if(state == TrainingState.PAUSED) {
				state = TrainingState.TRAINING;
				trainingAlgorithm.resume();
			}else {				
				state = TrainingState.PAUSED;
				trainingAlgorithm.pause();
			}
		}
	}

	public void abortAlgorithm() {
		this.trainingAlgorithm.abort();
		this.state = TrainingState.WAITING;
	}

	public void setTrainingAlgorithm(TrainingAlgorithm algo) {
		this.trainingAlgorithm = algo;
	}

	public boolean isPaused() {
		return (state == TrainingState.PAUSED);
	}

	public boolean isCurrentlyTraining() {
		return (state != TrainingState.WAITING);
	}
	
	public TrainingState getState() {
		return this.state;
	}
}
