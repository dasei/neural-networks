package org.network;

import org.activationFunctions.ActivationFunction;
import org.activationFunctions.Sigmoid;

public class Neuron {

	public static final double WEIGHT_DEFAULT_MIN = -5;
	public static final double WEIGHT_DEFAULT_MAX = 5;

	public static final double BIAS_DEFAULT_MIN = -5;
	public static final double BIAS_DEFAULT_MAX = 5;

	private double bias;
	private double[] weights;
	private int layerHidden;

	private ActivationFunction activationFunction;

	public Neuron(int layerHidden, Network network) {
		this(layerHidden, network, true, new Sigmoid());
	}

	public Neuron(int layerHidden, Network network, boolean randomizeValues, ActivationFunction activationFunction) {
		this.layerHidden = layerHidden;
		this.activationFunction = activationFunction;
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

		return activationFunction.activate(generateSum(inputs));
		// switch(activationMode) {
		// case Network.ACTIVATION_SIGMOID: return
		// MathFunctions.sigmoid(generateSum(inputs));
		// case Network.ACTIVATION_RELU: return MathFunctions.relu(generateSum(inputs));
		// case Network.ACTIVATION_LEAKY_RELU: return
		// MathFunctions.leakyRelu(generateSum(inputs));
		// default: return MathFunctions.sigmoid(generateSum(inputs));
		// }

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

	public void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}

	////////
	//// Getters

	public double[] getWeights() {
		return this.weights;
	}

	public double getBias() {
		return this.bias;
	}

	public ActivationFunction getActivationFunction() {
		return this.activationFunction;
	}
}
