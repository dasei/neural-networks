package org.activationFunctions;

public class Linear implements ActivationFunction {

	public double activate(double t) {
		return t;
	}

	public double derivative(double x) {
		return 1;
	}

}
