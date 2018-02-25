package org.trainingAlgorithms;

import org.gui.GUI;
import org.network.Network;

public class TrainingAlgorithm_Addition implements TrainingAlgorithm {

	public void start(GUI gui, Network net, long iterations) {
		long iterationCn = 0;

		double[] inputs = new double[2];
		double[] outputs = new double[1];

		int textUpdateInterval = 1000;

		while (iterationCn < iterations) {

			inputs[0] = (int) (Math.random() * 16 - 8);
			inputs[1] = (int) (Math.random() * 16 - 8);
			outputs[0] = inputs[0] + inputs[1];

			if (iterationCn % textUpdateInterval == 0) {
				gui.updateTrainingProgress(iterationCn, iterations,
						Math.pow(outputs[0] - net.train(inputs, outputs)[0], 2),
						"3 + 4 = " + net.feedForward(new double[] { 3, 4 })[0]);
			} else {
				net.train(inputs, outputs);
			}

			iterationCn++;
		}

		gui.updateTrainingProgress(iterationCn, iterations, Math.pow(outputs[0] - net.train(inputs, outputs)[0], 2),
				"3 + 4 = " + net.feedForward(new double[] { 3, 4 })[0]);

		System.out.println("finished!");
	}
}
