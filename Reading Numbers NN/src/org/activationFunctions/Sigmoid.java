package org.activationFunctions;

public class Sigmoid implements ActivationFunction {

	public double activate(double t) {
		return 1.0 / (1.0 + Math.exp(-t));
	}

	public double derivative(double x) {
		return activate(x) * (1 - activate(x));
	}

}
