package org.network;

public class Network {

	private int inputNeuronsAmount;

	private Neuron[][] neurons;
	private double[][] neuronValues;
	private double[][] neuronDerivativeValues;

	/**
	 * 
	 * @param layout
	 *            array containing sizes of each layer (including input & output
	 *            layer) <br>
	 *            e.g. {9, 5, 5, 3}
	 */
	
	public Network(int[] layout) {
		inputNeuronsAmount = layout[0];

		// create neuron matrix
		neurons = new Neuron[layout.length - 1][];
		// create neuron layers
		for (int layer = 1; layer < layout.length; layer++) {
			neurons[layer - 1] = new Neuron[layout[layer]];

			// create neurons
			for (int n = 0; n < neurons[layer - 1].length; n++)
				neurons[layer - 1][n] = new Neuron(layer - 1, this);
		}

		// create neuronValues matrix for caching output values of the neurons
		neuronValues = new double[neurons.length][];
		for (int layer = 0; layer < neurons.length; layer++) {
			neuronValues[layer] = new double[neurons[layer].length];
		}
		
		//create neuronDerivativeValues for memoizing dC/dOut
		neuronDerivativeValues = new double[neurons.length][];
		for(int layer = 0; layer < neurons.length; layer++) {
			neuronDerivativeValues[layer] = new double[neurons[layer].length];
		}

		System.out.println("");
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
	
	//Generates network-output and stores viable information for backpropagation
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

	//////
	////////
	//////

	private void backpropagate(double[] expectedOutputs, double[] inputValues) {
		
		//loope backwards through all neurons and memoize their derivatives
		for(int layer = neurons.length - 1; layer >= 0; layer--) {
			for(int n = 0; n < neurons[layer].length; n++) {
				
				//if neuron is outputNeuron
				if(layer == neurons.length - 1) {
					//store the derivative of the cost-function over this neurons output
					neuronDerivativeValues[layer][n] = 
							2*(neuronValues[layer][n] - expectedOutputs[n]);
					
				}
				else {
					//Take the sum over the next layer of their derivatives over your output
					double sum = 0;
					for(int nextLayerNeuron = 0; nextLayerNeuron < neurons[layer + 1].length;
							nextLayerNeuron ++) {
						//get the weigth that connects this neuron with nextLayerNeuron
						double connectingWeight = 
								neurons[layer + 1][nextLayerNeuron].getWeights()[n];
						
						//get the derivative of sigmoid over nextLayerNeurons sum z
						double dSig = MathFunctions.derivativeSigmoid(
								neurons[layer + 1][nextLayerNeuron].generateSum(
										neuronValues[layer]));
						//add to the sum
						sum += connectingWeight * dSig * 
								neuronDerivativeValues[layer + 1][nextLayerNeuron];
					}
					neuronDerivativeValues[layer][n] = sum;
				}
				
				//update its weights and its bias
				double dSig;
				//Get the derivative of sigmoid over the sum z of this neuron
				if(layer != 0) {
					dSig = MathFunctions.derivativeSigmoid(
							neurons[layer][n].generateSum(neuronValues[layer - 1]));
				}
				else {
					dSig = MathFunctions.derivativeSigmoid(
							neurons[layer][n].generateSum(inputValues));
				}
				
				//generate gradient for gradient descent
				double[] gradient = new double[neurons[layer][n].getWeights().length];
				for(int weight = 0; weight < gradient.length; weight++) {
					//dC/dw = value of previous neuron * sigmoid' of its own sum * dC/dValue
					//gradient = - (dC/dw)
					double previousValue;
					
					if(layer != 0) {
						previousValue = neuronValues[layer - 1][weight];
					}
					else {
						previousValue = inputValues[weight];
					}
					
					gradient[weight] = -1*previousValue*dSig*neuronDerivativeValues[layer][n];
					
				}
				
				//Generate new Weights out of gradient
				double[] newWeights = neurons[layer][n].getWeights();
				for(int w = 0; w < newWeights.length; w++) {
					newWeights[w] = newWeights[w] + gradient[w];
				}
				neurons[layer][n].setWeights(newWeights);
				
				//generate new bias for neuron (-1 from derivative and gradient should eliminate)
				double biasCorrection = dSig * neuronDerivativeValues[layer][n];
				neurons[layer][n].setBias(neurons[layer][n].getBias() + biasCorrection);
			}
		}
	}
	
	public void train(double[] inputValues, double[] expectedOutputs) {
		feedForward(inputValues);
		
//		double errorSum = 0;
//		for(int i = 0; i < expectedOutputs.length; i++) {
//			errorSum += Math.pow(neuronValues[neuronValues.length - 1][i] - expectedOutputs[i], 2);
//		}
//		System.out.println(errorSum);
		
		backpropagate(expectedOutputs, inputValues);
		
//		feedForward(inputValues);
		
//		errorSum = 0;
//		for(int i = 0; i < expectedOutputs.length; i++) {
//			errorSum += Math.pow(neuronValues[neuronValues.length - 1][i] - expectedOutputs[i], 2);
//		}
		
//		System.out.println(errorSum);
	}
	
}
