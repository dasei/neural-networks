package org.trainingAlgorithms;

import org.dataLoaders.IDXLoader;
import org.gui.GUI;
import org.network.Network;

public class TrainingAlgorithm_ReadingNumbers implements TrainingAlgorithm {

	public static final String CATEGORY_IMAGES = "images";
	public static final String CATEGORY_LABELS = "labels";
	
	private double errorSum;
	private int outputtedNumber;
	
	
	@Override
	public void start(GUI gui, Network net, long iterations) {
		
		
//		IDXLoader labelLoader = new IDXLoader("H://train-labels.idx1-ubyte");
//		IDXLoader imageLoader = new IDXLoader("H://train-images.idx3-ubyte");
		
		//TODO das hier besser machen lel
		
		double[][] imageData = IDXLoader.getData(CATEGORY_IMAGES);
		double[][] labelData = IDXLoader.getData(CATEGORY_LABELS);
		int dataAvailable = Math.min(IDXLoader.getDataAvailable(CATEGORY_IMAGES), IDXLoader.getDataAvailable(CATEGORY_LABELS)); 
		
		
//		for(int img = 0; img < imageData.length; img++)
//			for(int px = 0; px < imageData[0].length; px++)
//				System.out.println(imageData[img][px]);
		
		
		
		
		double[] calculatedOutputs = new double[10];
		
//		System.out.println("starting training");
		int dataPointer = 0;
		for(int l = 0; l < iterations; l++) {
			
			
			
			calculatedOutputs = net.train(imageData[dataPointer], labelData[dataPointer]);
			
			
			if(l % 100 == 0) {
				calcError(labelData[dataPointer], calculatedOutputs);
				gui.updateTrainingProgress(l, iterations, errorSum, "read number" , getIndexOfMaxNumber(labelData[dataPointer]) + " as", String.valueOf(outputtedNumber));
			}
			
			dataPointer++;
			
			if(dataPointer >= dataAvailable) {
				dataPointer = 0;
			}
		}
		
//		IDXImageInterpreter.showImage(imageData[dataPointer-1], 28, false);		
		
		calcError(labelData[(int)(dataPointer-1)], calculatedOutputs);		
		gui.updateTrainingProgress(iterations, iterations, errorSum, "read number" , getIndexOfMaxNumber(labelData[dataPointer-1]) + " as", String.valueOf(outputtedNumber));
		
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
	
//	public class ImageDataSet extends DataSet{
//		
//		private double[][] data;
//		private int imageHeight;
//		private int imageWidth;
//		
//		public ImageDataSet(double[][] data, int imageWidth, int imageHeight) {
//			this.data = data;
//			this.imageWidth = imageWidth;
//			this.imageHeight = imageHeight;
//		}
//		
//		public double[][] getData() {
//			return data;
//		}
//		public int getImageHeight() {
//			return imageHeight;
//		}
//		public int getImageWidth() {
//			return imageWidth;
//		}
//	}
//	
//	abstract public class DataSet{		
//	}
}
