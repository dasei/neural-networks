package org.network;

public class MathFunctions {
	
	public static double sigmoid(double t) {
		return 1.0/(1.0 + Math.exp(-t));
	}
	
	public static double derivativeSigmoid(double t) {
		return sigmoid(t)*(1-sigmoid(t));
	}

}
