package org.activationFunctions;

public class LeakyReLU implements ActivationFunction {

	public double activate(double t) {
		return Math.max(0.1 * t, t);
	}

	public double derivative(double x) {
		if (x > 0) {
			return 1;
		}

		return 0.1;
	}

}
