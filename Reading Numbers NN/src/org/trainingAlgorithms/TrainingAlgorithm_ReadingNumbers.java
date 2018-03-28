package org.trainingAlgorithms;

import org.dataLoaders.IDXLoader;
import org.gui.GUI;
import org.network.Network;

public class TrainingAlgorithm_ReadingNumbers implements TrainingAlgorithm {

	public static final String CATEGORY_IMAGES = "images";
	public static final String CATEGORY_LABELS = "labels";
	
	public static final String CATEGORY_IMAGES_TEST = "image-test";
	public static final String CATEGORY_LABELS_TEST = "labels-test";
	
	private double errorSum;
	private int outputtedNumber;
	private boolean paused = false;
	private boolean abort = false;
	
	@Override
	public void run(GUI gui, Network net, long iterations) {
		
		double[][] imageData = IDXLoader.getData(CATEGORY_IMAGES);
		double[][] labelData = IDXLoader.getData(CATEGORY_LABELS);
		int dataAvailable = Math.min(IDXLoader.getDataAvailable(CATEGORY_IMAGES), IDXLoader.getDataAvailable(CATEGORY_LABELS)); 

		double[] calculatedOutputs = new double[10];
		
		int dataPointer = -1;
		for(int l = 0; l < iterations; l++) {
			
			dataPointer++;
			
			if(dataPointer >= dataAvailable) {
				dataPointer = 0;
			}
			
			calculatedOutputs = net.train(imageData[dataPointer], labelData[dataPointer]);
			
			if(l % 100 == 0 || l == iterations - 1) {
				calcError(labelData[dataPointer], calculatedOutputs);
				gui.updateTrainingProgress(l, iterations, errorSum, "read number" , getIndexOfMaxNumber(labelData[dataPointer]) + " as", String.valueOf(outputtedNumber));
			}
			
			
			
			while(paused) {
				try{
					Thread.sleep(10);
				}catch(Exception e) {}
			}
			
			if(this.abort) {
				this.abort = false;
				this.paused = false;
				break;
			}
			
		}
		
	}
		
	private void calcError(double[] desiredOutputs, double[] calculatedOutputs) {
		//calculate error
		errorSum = 0;	
		
		//get highest output
		double maxOutput = Double.MIN_VALUE;
		
		for(int o = 0; o < desiredOutputs.length; o++) {
			errorSum += Math.pow(desiredOutputs[o]-calculatedOutputs[o], 2);
			if(calculatedOutputs[o] > maxOutput) {
				maxOutput = calculatedOutputs[o];
				outputtedNumber = o;
			}
		}
	}
	
	private int getIndexOfMaxNumber(double[] outputs) {
		double maxOutput = Double.MIN_VALUE;
		int index = -1;
		for(int o = 0; o < outputs.length; o++) {			
			if(outputs[o] > maxOutput) {
				maxOutput = outputs[o];
				index = o;
			}
		}
//		System.out.println("INDEX: " + index);
		return index;
	}
	
	public void pause() {
		this.paused = true;
	}
	
	public void resume() {
		this.paused = false;
	}
	
	public void abort() {
		this.abort = true;
	}
	
	@Override
	public void fitnessTest(GUI gui, Network net) {
		
		double[][] imageData = IDXLoader.getData(CATEGORY_IMAGES_TEST);
		double[][] labelData = IDXLoader.getData(CATEGORY_LABELS_TEST);
		int dataAvailable = Math.min(IDXLoader.getDataAvailable(CATEGORY_IMAGES_TEST), IDXLoader.getDataAvailable(CATEGORY_LABELS_TEST));
		
		double[] calculatedOutputs = new double[10];
		
		int dataPointer = -1;
		int fitnessSum = 0;
		for(int l = 0; l < dataAvailable; l++) {
			
			dataPointer++;
			
			if(dataPointer >= dataAvailable) {
				dataPointer = 0;
			}
			
			calculatedOutputs = net.feedForward(imageData[dataPointer]);
			
			this.outputtedNumber = getIndexOfMaxNumber(calculatedOutputs);
			
			if(getIndexOfMaxNumber(labelData[dataPointer]) == outputtedNumber) {
				fitnessSum ++;
			}
			
			gui.updateFitnessProcess(l + 1, dataAvailable, (fitnessSum)/(l+1.0), "read number" , getIndexOfMaxNumber(labelData[dataPointer]) + " as", String.valueOf(outputtedNumber));
			
		}
		
		
	}
}
