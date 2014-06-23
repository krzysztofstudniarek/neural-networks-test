package Main;

import Compression.NeuralGasCompressionNetwork;

public class NeuralGasCompressionMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NeuralGasCompressionNetwork NG = new NeuralGasCompressionNetwork(
				"lena.png", "res/lenaNeural3.png", 16, 16, 100000);
		
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				NG.compress(false, "out.csv", 0.1*j, (int) Math.pow(2, i));
			}
		}
	}

}
