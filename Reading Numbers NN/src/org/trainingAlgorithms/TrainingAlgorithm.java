package org.trainingAlgorithms;

import org.gui.GUI;
import org.network.Network;

public interface TrainingAlgorithm {

	public void start(GUI gui, Network net, long iterations);
}
