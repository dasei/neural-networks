package org.network;

public class MathFunctions {
	
	public static double sigmoid(double t) {
		return 1.0/(1.0 + Math.exp(-t));
	}
	
	public static double derivativeSigmoid(double t) {
		return sigmoid(t)*(1-sigmoid(t));
	}
	
	public static double relu(double t) {
		return Math.max(0, t);
	}
	
	public static double derivativeRelu(double t) {
		if(t > 0) return 1;
		else return 0;
	}
	
	public static double leakyRelu(double t) {
		return Math.max(0.1*t, t);
	}
	
	public static double derivativeLeakyRelu(double t) {
		if(t > 0) {
			return 1;
		}
		
		return 0.1;
	}

}
