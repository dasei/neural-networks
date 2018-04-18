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
//		double[] errTable = new double[(int) Math.min(iterations, dataAvailable)];

		double[] calculatedOutputs = new double[10];
		
		int dataPointer = -1;
		for(int l = 0; l < iterations; l++) {
			
			dataPointer++;
			
			if(dataPointer >= dataAvailable) {
				dataPointer = 0;
//				 errTable = new double[dataAvailable];
			}
			
			//TODO Hier eventuell was ändern wenn nötig
			calculatedOutputs = net.trainNormalized(imageData[dataPointer], labelData[dataPointer]);
//			errTable[dataPointer] = errorSum;
			
			if(l % 100 == 0 || l == iterations - 1) {
				calcError(labelData[dataPointer], calculatedOutputs);
				gui.updateTrainingProgress(l, iterations + 1, errorSum, "read number" , getIndexOfMaxNumber(labelData[dataPointer]) + " as", String.valueOf(outputtedNumber));
			}
			
			
			
			while(paused) {
				if(this.abort)
					break;
				
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
		
//		gui.updateTrainingProgress(0, 0, 0, "Searching for highest error","","");
//		//Get the worst percent of error value indices
//		int[] worstPercent = new int[(int) (Math.min(dataAvailable, iterations) * 0.01)];
//		
//		//Fill array with invalid values
//		for(int i = 0; i < worstPercent.length; i++) {
//			worstPercent[i] = -1;
//		}
//		
//		//Fill array with valid values
//		for(int i = 0; i < Math.min(dataAvailable, iterations); i++) {
//			double currError = errTable[i];
//			for(int z = 0; z < worstPercent.length; z++) {
//				if(worstPercent[z] == -1 || currError > errTable[worstPercent[z]]) {
//					int oldEntry = worstPercent[z];
//					worstPercent[z] = i;
//					for(int j = 0; j < worstPercent.length; j++) {
//						if(worstPercent[j] == -1) {
//							worstPercent[j] = oldEntry;
//							break;
//						}
//					}
//					break;
//				}
//			}
//		}
//		
//		//Train the worst data a hundred times
//		for(int i = 0; i < worstPercent.length; i++) {
//			int currentData = worstPercent[i];
//			double formerError = errTable[currentData];
//			double currentError = formerError;
//			for(int j = 0; j < 100; j++) {
//				calculatedOutputs = net.train(imageData[currentData], labelData[currentData]);
//				calcError(labelData[currentData], calculatedOutputs);
//				currentError = errorSum;
//				gui.updateTrainingProgress(i, worstPercent.length, currentError, "Reducing error", "of Data", String.valueOf(currentData));
//			}
//		}
		
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
