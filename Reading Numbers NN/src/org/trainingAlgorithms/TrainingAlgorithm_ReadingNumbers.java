package org.trainingAlgorithms;

import org.dataLoaders.IDXLoader;
import org.gui.GUI;
import org.network.Network;

public class TrainingAlgorithm_ReadingNumbers implements TrainingAlgorithm {

	@Override
	public void start(GUI gui, Network net, long iterations) {
		
		
		IDXLoader labelLoader = new IDXLoader("C://train-labels.idx1-ubyte");
		IDXLoader imageLoader = new IDXLoader("C://train-images.idx3-ubyte");
		
		//TODO das hier besser machen lel
		int[][] imageData;
		int[][] labelData;
		if(iterations > Integer.MAX_VALUE) {
			imageData = imageLoader.getData(Integer.MAX_VALUE);
			labelData = labelLoader.getData(Integer.MAX_VALUE);
			iterations = Integer.MAX_VALUE;
		}else {
			imageData = imageLoader.getData((int) iterations);
			labelData = labelLoader.getData((int) iterations);
		}

		double[] inputs = new double[imageData[0].length];
		double[] outputs = new double[10];
		
		double[] outputsByNetwork = new double[outputs.length];
		
		System.out.println("start training");
		
		for(int l = 0; l < iterations; l++) {
//			inputs = new double[30]; //TODO get inputs from file
//			outputs = new double[10]; //TODO get expected outputs
			for(int i = 0; i < imageData[l].length; i++)
				inputs[i] = imageData[l][i];				
			
			for(int i = 0; i < outputs.length; i++)
				outputs[i] = 0;
			
			outputs[labelData[l][0]] = 1;
			
			System.out.println("iteration Z");
			
			outputsByNetwork = net.train(inputs, outputs);
			
			//TODO error berechnen und string erstellen
			
			double sumError = 0;
			double maxVal = Double.MIN_VALUE;
			int maxIndex = -1;
			for(int o = 0; o < outputs.length; o++) {				
				sumError += Math.pow(outputs[o] - outputsByNetwork[o], 2);
				if(outputsByNetwork[o] > maxVal) {
					maxVal = outputsByNetwork[o];
					maxIndex = o;
				}
			}
			
			gui.updateTrainingProgress(l, iterations, sumError, "Read number", labelData[l][0] + " as", maxIndex + "");
			
			System.out.println("iterate");
		}
	}

}
