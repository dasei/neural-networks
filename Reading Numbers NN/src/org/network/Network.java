package org.network;

public class Network {

	private int inputNeuronsAmount;

	private Neuron[][] neurons;
	private double[][] neuronValues;

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

	//@formatter:off
	public void backpropagate(double[] expectedOutputs) {
		
		//loope durch alle Neuronen
			
			//speichere (((  d cost / d outputDesNeurons  )))
		
			//loope durch seine weights
				
				
		
	}
	//@formatter:on
}
