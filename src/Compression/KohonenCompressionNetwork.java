package Compression;

import java.util.ArrayList;
import java.util.Random;

import ImageHandling.Image;
import ImageHandling.ImageHandler;
import Utils.Neuron;
import Utils.Vector;

public class KohonenCompressionNetwork extends CompressionNetworkInterface {

	public KohonenCompressionNetwork(String imagePath, String outputPath,
			int frameWidth, int frameHight, int neuronsVectorSize,
			int learningStepSize) {
		super(imagePath, outputPath, frameWidth, frameHight, neuronsVectorSize,
				learningStepSize);
	}

	public void compress() {

		Image image = new Image(imagePath, frameWidth, frameHight);

		ArrayList<Vector> networkInput = image.getImageData();

		ArrayList<Neuron> neurons = new ArrayList<Neuron>();

		int neuronsArraySize = (image.getImageHeight() * image.getImageWidth())
				/ (frameWidth * frameHight);

		System.out.print(neuronsArraySize + "\n\n");
		System.out.print(networkInput.size() + "\n\n");

		for (int i = 0; i < neuronsVectorSize; i++) {
			neurons.add(new Neuron(frameWidth, frameHight));
		}

		Random r = new Random();

		int minId, numOfLearnLoops = learningStepSize;
		double lambda = 1;

		for (int j = 0; j < numOfLearnLoops; j++) {
			int k = r.nextInt(networkInput.size());
			double min = Double.MAX_VALUE, tmp = min;
			minId = 0;

			for (int i = 0; i < neurons.size(); i++) {
				tmp = neurons.get(i).evaluate(networkInput.get(k));
				if (tmp < min) {
					min = tmp;
					minId = i;
				}
			}

			for (int i = 0; i < neurons.size(); i++) {
				neurons.get(i).learn(
						networkInput.get(k),
						Math.exp(-(minId - i) * (minId - i)
								/ (2 * lambda * lambda)));
			}
			lambda -= 1 / numOfLearnLoops;
		}

		ArrayList<Vector> networkOutput = new ArrayList<Vector>();

		for (Vector ni : networkInput) {

			double min = Double.MAX_VALUE, tmp = min;
			minId = 0;

			for (int i = 0; i < neurons.size(); i++) {
				tmp = neurons.get(i).evaluate(ni);
				if (tmp < min) {
					min = tmp;
					minId = i;
				}

			}

			networkOutput
					.add(new Vector(neurons.get(minId).getWeightsVector()));

		}

		// for (int i = 0; i < networkInput.size(); i++)
		ImageHandler.saveToFile(outputPath, networkOutput, frameWidth,
				frameHight, image.getImageWidth(), image.getImageHeight());

	}

}
