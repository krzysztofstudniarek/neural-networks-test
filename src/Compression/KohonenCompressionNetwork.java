package Compression;

import java.util.ArrayList;
import java.util.Random;

import ImageHandling.Image;
import ImageHandling.ImageHandler;
import Utils.Neuron;
import Utils.Vector;

public class KohonenCompressionNetwork extends CompressionNetworkInterface {

	public KohonenCompressionNetwork(String imagePath, String outputPath,
			int frameWidth, int frameHight, int learningStepSize) {
		super(imagePath, outputPath, frameWidth, frameHight, learningStepSize);
	}

	public Vector compress(boolean saveToFile, String logFile, double lam,
			int neuronsVectorSize) {

		this.neuronsVectorSize = neuronsVectorSize;

		Image image = new Image(imagePath, frameWidth, frameHight);

		ArrayList<Vector> networkInput = image.getImageData();

		ArrayList<Neuron> neurons = new ArrayList<Neuron>();

		int neuronsArraySize = (image.getImageHeight() * image.getImageWidth())
				/ (frameWidth * frameHight);
		
		for (int i = 0; i < neuronsVectorSize; i++) {
			neurons.add(new Neuron(frameWidth, frameHight));
		}

		Random r = new Random();

		int minId, numOfLearnLoops = learningStepSize;

		double lambda = lam;

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
			lambda -= lam / numOfLearnLoops;
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

		double PSNR = 0, MSE = 0, sum = 0;

		for (int row = 0; row < image.getImageData().size(); row++) {
			for (int col = 0; col < 16; col++) {

				sum += (float) (image.getImageData().get(row)
						.getInputVectorData()[col] - (networkOutput.get(row)
						.getInputVectorData()[col] * networkOutput.get(row)
						.getInputVectorData()[col]));

			}
		}

		MSE = (double) ((double) sum/ (image.getImageHeight() * image
				.getImageWidth()));

		System.out.print("MSE = " + MSE + "\n");

		PSNR = 10 * Math.log10(255 * 255 / MSE);

		System.out.print("x : " + this.neuronsVectorSize + " y : " + lam
				+ " z : " + PSNR + "\n");

		// for (int i = 0; i < networkInput.size(); i++)
		if (saveToFile)
			ImageHandler.saveToFile(outputPath, networkOutput, frameWidth,
					frameHight, image.getImageWidth(), image.getImageHeight());

		double[] logInfo = new double[3];
		logInfo[0] = this.neuronsVectorSize;
		logInfo[1] = lam;
		logInfo[2] = PSNR;
		
		return new Vector(logInfo);
		
	}
}
