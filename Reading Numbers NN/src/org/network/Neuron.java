package org.network;

import org.activationFunctions.ActivationFunction;
import org.activationFunctions.Sigmoid;

public class Neuron {

//	public static final double WEIGHT_DEFAULT_MIN = -5;
//	public static final double WEIGHT_DEFAULT_MAX = 5;
//
//	public static final double BIAS_DEFAULT_MIN = -5;
//	public static final double BIAS_DEFAULT_MAX = 5;

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
			randomizeWeights();

			// bias
			bias = normalverteilung(0,1);
		}
	}

	private void randomizeWeights() {
		for (int w = 0; w < weights.length; w++) {
			weights[w] = normalverteilung(0, 1);
			
		}
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
	
	//Berechnet normalverteilte Zufallszahlen nach der Polar-Methode
	public static double normalverteilung(double erwartungswert, double standartabweichung) {
		
		double u, v, q, p;
		do {
			//Erzeuge zwei linearverteilte Zufallszahlen u und v
			u = 2 * Math.random() - 1;
			v = 2 * Math.random() - 1;
			//Berechne q = u� + v�
			q = u * u + v * v;
			//Wenn q nicht element aus ]0;1[, wiederhole
		} while(q <= 0 || q >= 1);
		
		//Berechne den Faktor p
		p = Math.sqrt((-2 * Math.log(q))/ q);
		
		//Erzeuge eine unabh�ngige standartnormalverteilte Zufallszahl
		double x = u * p;
		
		//Lineare Transformation zu beliebig normalverteilten Zufallszahlen
		return Math.sqrt(standartabweichung) * x + erwartungswert;
	}
}
