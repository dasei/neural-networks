package org.network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;

import org.activationFunctions.ActivationFunction;
import org.activationFunctions.Sigmoid;

public class Network {

	private int inputNeuronsAmount;

	private Neuron[][] neurons;
	private double[][] neuronValues;
	private double[][] neuronDerivativeValues;
	private double[][] neuronSumValues;
	// private int activationMode;
	private double learningRate;

	private ActivationFunction activationFunction;

	// public static final int ACTIVATION_SIGMOID = 0;
	// public static final int ACTIVATION_RELU = 1;
	// public static final int ACTIVATION_LEAKY_RELU = 2;
	public static final double DEFAULT_LEARNING_RATE = 0.1;

	//////
	//////// generate Network randomly
	//////

	/**
	 * 
	 * @param layout
	 *            array containing sizes of each layer (including input & output
	 *            layer) <br>
	 *            e.g. {9, 5, 5, 3}
	 */

	public Network(int[] layout) {
		this(layout, true, new Sigmoid(), DEFAULT_LEARNING_RATE);
	}

	public Network(int[] layout, ActivationFunction activationFunction) {
		this(layout, true, activationFunction, DEFAULT_LEARNING_RATE);
	}

	public Network(int[] layout, boolean randomizeValues) {
		this(layout, randomizeValues, new Sigmoid(), DEFAULT_LEARNING_RATE);
	}

	public Network(int[] layout, ActivationFunction activationFunction, double learningRate) {
		this(layout, true, activationFunction, learningRate);
	}

	//////
	//////// generate Network randomly OR without value initialization
	//////

	private Network(int[] layout, boolean randomizeValues, ActivationFunction activationFunction, double learningRate) {
		inputNeuronsAmount = layout[0];
		this.activationFunction = activationFunction;
		this.learningRate = learningRate;

		// create neuron matrix
		neurons = new Neuron[layout.length - 1][];
		// create neuron layers
		for (int layer = 1; layer < layout.length; layer++) {
			neurons[layer - 1] = new Neuron[layout[layer]];

			// create neurons
			for (int n = 0; n < neurons[layer - 1].length; n++)
				neurons[layer - 1][n] = new Neuron(layer - 1, this, randomizeValues, activationFunction);
		}

		// create neuronValues matrix for caching output values of the neurons
		neuronValues = new double[neurons.length][];
		for (int layer = 0; layer < neurons.length; layer++) {
			neuronValues[layer] = new double[neurons[layer].length];
		}

		// create neuronDerivativeValues for memoizing dC/dOut
		neuronDerivativeValues = new double[neurons.length][];
		for (int layer = 0; layer < neurons.length; layer++) {
			neuronDerivativeValues[layer] = new double[neurons[layer].length];
		}

		// create neuronSumValues for memoizing the z-sum values
		neuronSumValues = new double[neurons.length][];
		for (int layer = 0; layer < neurons.length; layer++) {
			neuronSumValues[layer] = new double[neurons[layer].length];
		}
	}

	////////////
	////////// generate Network from file
	////////

	public static Network loadNetworkFromFile(String inputFilePath) {

		Network newNetwork = null;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(inputFilePath)));

			////////
			//// read network layout
			String[] networkLayout = reader.readLine().split(":")[1].split(",");
			int[] networkLayoutInt = new int[networkLayout.length];
			for (int layer = 0; layer < networkLayout.length; layer++) {
				networkLayoutInt[layer] = Integer.parseInt(networkLayout[layer]);
			}

			////////
			//// init network with given layout
			newNetwork = new Network(networkLayoutInt, false);

			////////
			//// read default lerning rate
			newNetwork.learningRate = Double.parseDouble(reader.readLine().split(":")[1]);

			////////
			//// assign values

			// loop through layers
			for (int layer = 1; layer < networkLayoutInt.length; layer++) {

				// loop through neurons
				for (int n = 0; n < networkLayoutInt[layer]; n++) {

					// assign activation function
					newNetwork.setActivationFunctionForNeuron(layer - 1, n, (ActivationFunction) (Class
							.forName("org.activationFunctions." + reader.readLine()).getConstructor().newInstance()));

					// loop through && assign weights
					for (int w = 0; w < networkLayoutInt[layer - 1]; w++) {
						newNetwork.setWeightOfNeuron(layer - 1, n, w, Double.parseDouble(reader.readLine()));
					}

					// assign bias
					newNetwork.setBiasOfNeuron(layer - 1, n, Double.parseDouble(reader.readLine()));
				}
			}

			reader.close();

		} catch (Exception e) {
			System.out.println("failed to load NN from file: '" + inputFilePath + "'");
			e.printStackTrace();
		}

		return newNetwork;
	}

	////////
	//// export Network to file
	////////

	public static void exportToFile(Network network, String exportFilePath) {

		PrintWriter writer;

		try {
			writer = new PrintWriter(new FileWriter(new File(exportFilePath)));

			////////
			//// print layout of this Network
			String layoutStr = "layout:" + network.inputNeuronsAmount + ",";
			for (int hiddenLayer = 0; hiddenLayer < network.neurons.length; hiddenLayer++) {
				layoutStr += network.neurons[hiddenLayer].length
						+ (hiddenLayer == network.neurons.length - 1 ? "" : ",");
			}
			writer.println(layoutStr);

			////////
			//// print default lerniing rate
			writer.println("learning rate:" + network.learningRate);

			////////
			//// print values of all weights && biases

			// loop though layers
			for (int hiddenLayer = 0; hiddenLayer < network.neurons.length; hiddenLayer++) {

				// loop through neurons
				for (int n = 0; n < network.neurons[hiddenLayer].length; n++) {

					writer.println(network.neurons[hiddenLayer][n].getActivationFunction().getClass().getSimpleName());

					// loop through weights
					for (int weight = 0; weight < network.getAmountOfNeuronsInHiddenLayer(hiddenLayer - 1); weight++) {

						// print weight
						writer.println(network.neurons[hiddenLayer][n].getWeights()[weight]);
					}

					// print bias
					writer.println(network.neurons[hiddenLayer][n].getBias());
				}

			}

			writer.close();

		} catch (Exception e) {
			System.out.println("failed to export nn to file: '" + exportFilePath + "'");
			e.printStackTrace();
		}

	}

	public int getAmountOfNeuronsInHiddenLayer(int hiddenLayer) {
		int layer = hiddenLayer + 1;
		if (layer < 0 || hiddenLayer >= neurons.length)
			return -1;

		if (layer > 0)
			return neurons[hiddenLayer].length;
		else
			return inputNeuronsAmount;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	// Sets given activationMode to ALL Neurons
	public void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
		for (Neuron[] layer : neurons) {
			for (Neuron neuron : layer) {
				neuron.setActivationFunction(activationFunction);
			}
		}
	}

	public double getLearningRate() {
		return this.learningRate;
	}

	// Generates network-output and stores viable information for
	// backpropagation
	public double[] feedForward(double[] inputValues) {

		for (int layer = 0; layer < neuronValues.length; layer++) {
			if (layer == 0) {
				for (int n = 0; n < neurons[layer].length; n++) {
					neuronValues[layer][n] = neurons[layer][n].generateOutput(inputValues);
					neuronSumValues[layer][n] = neurons[layer][n].generateSum(inputValues);
				}
			} else {
				for (int n = 0; n < neurons[layer].length; n++) {
					neuronValues[layer][n] = neurons[layer][n].generateOutput(neuronValues[layer - 1]);
					neuronSumValues[layer][n] = neurons[layer][n].generateSum(neuronValues[layer - 1]);
				}
			}
		}

		return neuronValues[neuronValues.length - 1];
	}

	public void setWeightOfNeuron(int hiddenLayer, int neuron, int weightIndex, double newValue) {
		neurons[hiddenLayer][neuron].setWeight(weightIndex, newValue);
	}

	////////
	//// Backpropagation
	////////

	private void backpropagate(double[] expectedOutputs, double[] inputValues) {

		// loop backwards through all neurons and memoize their derivatives
		for (int layer = neurons.length - 1; layer >= 0; layer--) {
			for (int n = 0; n < neurons[layer].length; n++) {

				// if neuron is outputNeuron
				if (layer == neurons.length - 1) {
					// store the derivative of the cost-function over this
					// neurons output
					neuronDerivativeValues[layer][n] = 2 * (neuronValues[layer][n] - expectedOutputs[n]);

				} else {
					// Take the sum over the next layer of their derivatives
					// over your output
					double sum = 0;
					for (int nextLayerNeuron = 0; nextLayerNeuron < neurons[layer + 1].length; nextLayerNeuron++) {
						// get the weigth that connects this neuron with
						// nextLayerNeuron
						double connectingWeight = neurons[layer + 1][nextLayerNeuron].getWeights()[n];

						// get the derivative of the activation over nextLayerNeurons
						// sum z
						double dActivation;
						// Get the right derivative function according to activationMode

						dActivation = activationFunction.derivative(neuronSumValues[layer + 1][nextLayerNeuron]);

						// switch (activationMode) {
						// case ACTIVATION_SIGMOID: dActivation = MathFunctions.derivativeSigmoid(
						// neuronSumValues[layer + 1][nextLayerNeuron]); break;
						// case ACTIVATION_RELU: dActivation = MathFunctions.derivativeRelu(
						// neuronSumValues[layer + 1][nextLayerNeuron]); break;
						// case ACTIVATION_LEAKY_RELU: dActivation = MathFunctions.derivativeLeakyRelu(
						// neuronSumValues[layer + 1][nextLayerNeuron]); break;
						// default: dActivation = MathFunctions.derivativeSigmoid(
						// neuronSumValues[layer + 1][nextLayerNeuron]); break;
						// }

						// add to the sum
						sum += connectingWeight * dActivation * neuronDerivativeValues[layer + 1][nextLayerNeuron];
					}
					neuronDerivativeValues[layer][n] = sum;
				}

				// update its weights and its bias
				// Get the derivative of activation over the sum z of this neuron
				double dActivation;

				// Get the right derivative function according to activationMode
				dActivation = activationFunction.derivative(neuronSumValues[layer][n]);
				// switch (activationMode) {
				// case ACTIVATION_SIGMOID: dActivation = MathFunctions.derivativeSigmoid(
				// neuronSumValues[layer][n]); break;
				// case ACTIVATION_RELU: dActivation = MathFunctions.derivativeRelu(
				// neuronSumValues[layer][n]); break;
				// case ACTIVATION_LEAKY_RELU: dActivation = MathFunctions.derivativeLeakyRelu(
				// neuronSumValues[layer][n]); break;
				// default: dActivation = MathFunctions.derivativeSigmoid(
				// neuronSumValues[layer][n]); break;
				// }

				// if (layer != 0) {
				// dSig =
				// MathFunctions.derivativeSigmoid(neurons[layer][n].generateSum(neuronValues[layer
				// - 1]));
				// } else {
				// dSig =
				// MathFunctions.derivativeSigmoid(neurons[layer][n].generateSum(inputValues));
				// }

				// generate gradient for gradient descent
				double[] gradient = new double[neurons[layer][n].getWeights().length];
				for (int weight = 0; weight < gradient.length; weight++) {
					// dC/dw = value of previous neuron * sigmoid' of its own
					// sum * dC/dValue
					// gradient = - (dC/dw)
					double previousValue;

					if (layer != 0) {
						previousValue = neuronValues[layer - 1][weight];
					} else {
						previousValue = inputValues[weight];
					}

					gradient[weight] = -1 * previousValue * dActivation * neuronDerivativeValues[layer][n]
							* learningRate;

				}

				// Generate new Weights out of gradient
				double[] newWeights = neurons[layer][n].getWeights();
				for (int w = 0; w < newWeights.length; w++) {
					newWeights[w] = newWeights[w] + gradient[w];
				}
				// neurons[layer][n].setWeights(newWeights);

				// generate new bias for neuron (-1 from derivative and gradient
				// should eliminate)
				double biasCorrection = dActivation * neuronDerivativeValues[layer][n] * learningRate;
				neurons[layer][n].setBias(neurons[layer][n].getBias() + biasCorrection);
			}
		}

	}

	public void setBiasOfNeuron(int hiddenLayer, int neuron, double newValue) {
		neurons[hiddenLayer][neuron].setBias(newValue);
	}

	// returns output before training
	public double[] train(double[] inputValues, double[] expectedOutputs) {
		double[] output = feedForward(inputValues);
		backpropagate(expectedOutputs, inputValues);
		return output;
	}

	public void trainingSession(double[][] inputValues, double[][] expectedOutputs) {
		int iterations = inputValues.length;
		int fitnessSum = 0;

		for (int i = 0; i < iterations; i++) {
			double[] output = train(inputValues[i], expectedOutputs[i]);
			String outString = Arrays.toString(output);
			String expectedOutString = Arrays.toString(expectedOutputs[i]);

			System.out.println("Expected: " + expectedOutString + " Out: " + outString);

			boolean isRight = true;
			for (int outNeuron = 0; outNeuron < output.length; outNeuron++) {
				if (!(Math.round(output[outNeuron]) == expectedOutputs[i][outNeuron])) {
					isRight = false;
					System.out.println("Fehler in Neuron: " + outNeuron);
					break;
				}
			}

			if (isRight) {
				fitnessSum++;
			}
		}

		System.out.println("Fitness: " + ((double) fitnessSum / iterations) * 100 + "%");
	}

	public void setActivationFunctionForNeuron(int hiddenLayer, int n, ActivationFunction f) {
		neurons[hiddenLayer][n].setActivationFunction(f);
	}

}
