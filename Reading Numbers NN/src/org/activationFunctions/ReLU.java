package org.activationFunctions;

public class ReLU implements ActivationFunction {

	public double activate(double t) {
		return Math.max(0, t);
	}

	public double derivative(double x) {
		if (x > 0)
			return 1;
		else
			return 0;
	}

}
