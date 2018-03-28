package org.trainingAlgorithms;

import org.gui.GUI;
import org.network.Network;

public interface TrainingAlgorithm {

	public void run(GUI gui, Network net, long iterations);
	public void pause();
	public void resume();
	public void abort();
}
