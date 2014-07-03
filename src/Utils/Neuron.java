package Utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Neuron {

	private double[] weights;
	public double[] getWeights() {
		return weights;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}

	int width, height;

	public Neuron(int imageWidth, int imageHeight) {

		weights = new double[imageWidth * imageHeight];
		this.width = imageWidth;
		this.height = imageHeight;
		initialize();

	}

	public Neuron(int dimension, double range) {

		weights = new double[dimension];
		this.width = 0;
		this.height = 0;

		Random r = new Random();
		for (int i = 0; i < dimension; i++) {
			this.weights[i] = r.nextDouble() * range - range / 2;
		}

	}

	public void initialize() {

		for (int i = 0; i < weights.length; i++) {
			weights[i] = Math.random();
		}

	}

	public double evaluate(Vector inputVector) {

		double dist = 0;

		for (int i = 0; i < inputVector.getInputVectorData().length; i++) {


			dist += (weights[i] - inputVector.getInputVectorData()[i])
					* (weights[i] - inputVector.getInputVectorData()[i]);

		}

		return Math.sqrt(dist);
	}

	public void learn(Vector inputVector, double ratio) {

		for (int i = 0; i < inputVector.getInputVectorData().length; i++) {
			weights[i] += ratio  
					* (inputVector.getInputVectorData()[i] - weights[i]);
		}

		// weights = Vector.normalizeVector(weights);
	}

	public double[] getWeightsVector() {
		return weights;
	}

}
