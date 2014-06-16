package Main;

import Compression.NeuralGasCompressionNetwork;

public class NeuralGasCompressionMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NeuralGasCompressionNetwork NG = new NeuralGasCompressionNetwork(
				"boat.png", "res/boatNeural3.png", 4, 4, 16384, 100000);

		NG.compress();

	}

}
