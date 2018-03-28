package org.trainingAlgorithms;

import org.gui.GUI;
import org.network.Network;

public class TrainingAlgorithm_Addition implements TrainingAlgorithm {

	private boolean paused = false;
	private boolean abort = false;
	
	public void run(GUI gui, Network net, long iterations) {
		long iterationCn = 0;

		double[] inputs = new double[2];
		double[] outputs = new double[1];
		double testOutput;

		int textUpdateInterval = 1000;

		while (iterationCn < iterations) {

			inputs[0] = (int) (Math.random() * 16 - 8);
			inputs[1] = (int) (Math.random() * 16 - 8);
			outputs[0] = inputs[0] + inputs[1];

			if (iterationCn % textUpdateInterval == 0) {
				testOutput = net.feedForward(new double[] { 3, 4 })[0];
				gui.updateTrainingProgress(iterationCn, iterations,
						Math.pow(outputs[0] - net.train(inputs, outputs)[0], 2), "3 + 4 = ", testOutput + "",
						"= " + (int) Math.round(testOutput));
			} else {
				net.train(inputs, outputs);
			}

			iterationCn++;
			
			while(paused) {
				try{
					Thread.sleep(10);
				}catch(Exception e) {}
			}
			
			if(this.abort) {
				break;
			}
		}

		testOutput = net.feedForward(new double[] { 3, 4 })[0];
		gui.updateTrainingProgress(iterationCn, iterations, Math.pow(outputs[0] - net.train(inputs, outputs)[0], 2),
				"3 + 4 = ", testOutput + "", "= " + (int) Math.round(testOutput));

//		System.out.println("finished!");
	}
	
	public void pause() {
		
	}
	
	public void resume() {
		
	}
	
	public void abort() {
		
	}
}
