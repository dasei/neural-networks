package org.network;

public class Neuron {

	public static final double WEIGHT_DEFAULT_MIN = -5;
	public static final double WEIGHT_DEFAULT_MAX = 5;

	public static final double BIAS_DEFAULT_MIN = -5;
	public static final double BIAS_DEFAULT_MAX = 5;

	private double bias;
	private double[] weights;
	private int layerHidden;
	
	private int activationMode;

	public Neuron(int layerHidden, Network network) {
		this(layerHidden, network, true, Network.ACTIVATION_SIGMOID);
	}

	public Neuron(int layerHidden, Network network, boolean randomizeValues, int activationMode) {
		this.layerHidden = layerHidden;
		this.activationMode = activationMode;
		// weights
		weights = new double[network.getAmountOfNeuronsInHiddenLayer(this.layerHidden - 1)];

		if (randomizeValues) {
			randomizeWeights(WEIGHT_DEFAULT_MIN, WEIGHT_DEFAULT_MAX);

			// bias
			bias = (Math.random() * (BIAS_DEFAULT_MAX - BIAS_DEFAULT_MIN)) + BIAS_DEFAULT_MIN;
		}
	}

	private void randomizeWeights(double min, double max) {
		for (int w = 0; w < weights.length; w++)
			weights[w] = (Math.random() * (max - min)) + min;
	}

	public double generateOutput(double[] inputs) {
		
		switch(activationMode) {
		case 0: return MathFunctions.sigmoid(generateSum(inputs));
		case 1: return MathFunctions.relu(generateSum(inputs));
		default: return MathFunctions.sigmoid(generateSum(inputs));
		}

	}

	public double generateSum(double[] inputs) {
		if (inputs.length != weights.length)
			return Double.NEGATIVE_INFINITY;

		double sum = 0;
		for (int i = 0; i < inputs.length; i++)
			sum += inputs[i] * weights[i];

		return sum - bias;
	}

	////////
	//// Setters

	public void setWeights(double[] weights) {
		if (this.weights.length == weights.length) {
			this.weights = weights.clone();
		}
	}

	public void setWeight(int weightIndex, double newValue) {
		this.weights[weightIndex] = newValue;
	}

	public void setBias(double newValue) {
		this.bias = newValue;
	}

	////////
	//// Getters

	public double[] getWeights() {
		return this.weights;
	}

	public double getBias() {
		return this.bias;
	}
}
