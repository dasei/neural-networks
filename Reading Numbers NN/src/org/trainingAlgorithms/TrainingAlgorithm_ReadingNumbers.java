package org.trainingAlgorithms;

import org.gui.GUI;
import org.network.Network;

public class TrainingAlgorithm_ReadingNumbers implements TrainingAlgorithm {

	@Override
	public void start(GUI gui, Network net, long iterations) {
		
		double[] inputs;
		double[] outputs;
		
		for(long l = 0; l < iterations; l++) {
			inputs = new double[30]; //TODO get inputs from file
			outputs = new double[10]; //TODO get expected outputs
			
			net.train(inputs, outputs);
			
			//TODO error berechnen und string erstellen
			int currentError = 0;
			String notePart1 = "", notePart2 = "", notePart3 = "";
			
			gui.updateTrainingProgress(l, iterations, currentError, notePart1, notePart2, notePart3);
		}
	}

}
