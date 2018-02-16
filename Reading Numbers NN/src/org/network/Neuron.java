package org.network;

public class Neuron {

	public static final double WEIGHT_DEFAULT_MIN = -1;
	public static final double WEIGHT_DEFAULT_MAX = 1;

	public static final double BIAS_DEFAULT_MIN = -1;
	public static final double BIAS_DEFAULT_MAX = 1;

	private double bias;
	private double[] weights;

	private int layerHidden;

	public Neuron(int layerHidden, Network network) {
		this.layerHidden = layerHidden;

		// weights
		weights = new double[network.getAmountOfNeuronsInHiddenLayer(this.layerHidden - 1)];
		randomizeWeights(WEIGHT_DEFAULT_MIN, WEIGHT_DEFAULT_MAX);

		// bias
		bias = (Math.random() * (BIAS_DEFAULT_MAX - BIAS_DEFAULT_MIN)) + BIAS_DEFAULT_MIN;
	}

	private void randomizeWeights(double min, double max) {
		for (int w = 0; w < weights.length; w++)
			weights[w] = (Math.random() * (max - min)) + min;
	}

	public double generateOutput(double[] inputs) {

		if (inputs.length != weights.length)
			return Double.NEGATIVE_INFINITY;

		double sum = 0;
		for (int i = 0; i < inputs.length; i++)
			sum += inputs[i] * weights[i];

		return sigmoid(sum - bias);
		// return sum - bias;
	}

	//////

	private double sigmoid(double t) {
		return 1.0 / (1.0 + Math.exp(-t));
	}
}
