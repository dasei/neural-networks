package org.network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Network {

	private int inputNeuronsAmount;

	private Neuron[][] neurons;
	private double[][] neuronValues;
	private double[][] neuronDerivativeValues;

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
		this(layout, true);
	}

	//////
	//////// generate Network randomly OR without value initialization
	//////

	private Network(int[] layout, boolean randomizeValues) {
		inputNeuronsAmount = layout[0];

		// create neuron matrix
		neurons = new Neuron[layout.length - 1][];
		// create neuron layers
		for (int layer = 1; layer < layout.length; layer++) {
			neurons[layer - 1] = new Neuron[layout[layer]];

			// create neurons
			for (int n = 0; n < neurons[layer - 1].length; n++)
				neurons[layer - 1][n] = new Neuron(layer - 1, this, randomizeValues);
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
			//// assign values

			// loop through layers
			for (int layer = 1; layer < networkLayoutInt.length; layer++) {

				// loop through neurons
				for (int n = 0; n < networkLayoutInt[layer]; n++) {

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
			//// print values of all weights && biases

			// loop though layers
			for (int hiddenLayer = 0; hiddenLayer < network.neurons.length; hiddenLayer++) {

				// loop through neurons
				for (int n = 0; n < network.neurons[hiddenLayer].length; n++) {

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

	// Generates network-output and stores viable information for
	// backpropagation
	public double[] feedForward(double[] inputValues) {

		for (int layer = 0; layer < neuronValues.length; layer++) {
			if (layer == 0) {
				for (int n = 0; n < neurons[layer].length; n++) {
					neuronValues[layer][n] = neurons[layer][n].generateOutput(inputValues);
				}
			} else {
				for (int n = 0; n < neurons[layer].length; n++) {
					neuronValues[layer][n] = neurons[layer][n].generateOutput(neuronValues[layer - 1]);
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

		// loope backwards through all neurons and memoize their derivatives
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

						// get the derivative of sigmoid over nextLayerNeurons
						// sum z
						double dSig = MathFunctions.derivativeSigmoid(
								neurons[layer + 1][nextLayerNeuron].generateSum(neuronValues[layer]));
						// add to the sum
						sum += connectingWeight * dSig * neuronDerivativeValues[layer + 1][nextLayerNeuron];
					}
					neuronDerivativeValues[layer][n] = sum;
				}

				// update its weights and its bias
				double dSig;
				// Get the derivative of sigmoid over the sum z of this neuron
				if (layer != 0) {
					dSig = MathFunctions.derivativeSigmoid(neurons[layer][n].generateSum(neuronValues[layer - 1]));
				} else {
					dSig = MathFunctions.derivativeSigmoid(neurons[layer][n].generateSum(inputValues));
				}

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

					gradient[weight] = -1 * previousValue * dSig * neuronDerivativeValues[layer][n];

				}

				// Generate new Weights out of gradient
				double[] newWeights = neurons[layer][n].getWeights();
				for (int w = 0; w < newWeights.length; w++) {
					newWeights[w] = newWeights[w] + gradient[w];
				}
				// neurons[layer][n].setWeights(newWeights);

				// generate new bias for neuron (-1 from derivative and gradient
				// should eliminate)
				double biasCorrection = dSig * neuronDerivativeValues[layer][n];
				neurons[layer][n].setBias(neurons[layer][n].getBias() + biasCorrection);
			}
		}

	}

	public void setBiasOfNeuron(int hiddenLayer, int neuron, double newValue) {
		neurons[hiddenLayer][neuron].setBias(newValue);
	}

	public void train(double[] inputValues, double[] expectedOutputs) {
		feedForward(inputValues);

		// double errorSum = 0;
		// for (int i = 0; i < expectedOutputs.length; i++) {
		// errorSum += Math.pow(neuronValues[neuronValues.length - 1][i] -
		// expectedOutputs[i], 2);
		// }
		// System.out.println(errorSum);

		backpropagate(expectedOutputs, inputValues);

		// feedForward(inputValues);
		//
		// errorSum = 0;
		// for(int i = 0; i < expectedOutputs.length; i++) {
		// errorSum += Math.pow(neuronValues[neuronValues.length - 1][i] -
		// expectedOutputs[i], 2);
		// }
		//
		// System.out.println(errorSum);
	}

}
