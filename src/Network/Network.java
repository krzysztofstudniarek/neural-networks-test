package Network;

import java.util.ArrayList;
import java.util.Random;

import ImageHandling.Image;
import ImageHandling.ImageHandler;
import Utils.Neuron;
import Utils.Vector;

public class Network {

	private ArrayList<Neuron> neurons;

	public Network() {
		this.neurons = new ArrayList<Neuron>();
	}

	public void learn() {

		String[] alphabetImages = { "a.png", "b.png", "c.png", "d.png",
				"e.png", "f.png", "g.png", "h.png", "i.png", "j.png", "k.png",
				"l.png", "m.png", "n.png", "o.png", "p.png", "q.png", "r.png",
				"s.png", "t.png", "u.png", "w.png", "x.png", "y.png", "z.png" };

		// String[] alphabetImages = { "pic1.png", "pic2.png", "pic3.png" };

		ArrayList<Image> images = new ArrayList<Image>();

		for (String filePath : alphabetImages) {
			images.add(new Image("alphabet2/" + filePath, 30, 30));
		}

		ArrayList<Vector> inputVectors = new ArrayList<Vector>();

		for (Image img : images) {
			inputVectors.add(img
					.getImageData(0, 0));
		}

		neurons = new ArrayList<Neuron>();

		for (int i = 0; i < 50; i++) {
			neurons.add(new Neuron(images.get(0).getImageWidth(), images.get(0)
					.getImageHeight()));
		}

		int minId = 0;
		int tmpMin;

		Random r = new Random();

		for (int j = 0; j < 100000; j++) {
			int k = r.nextInt(inputVectors.size());
			double min = Double.MAX_VALUE, tmp = min;
			minId = 0;

			for (int i = 0; i < neurons.size(); i++) {
				tmp = neurons.get(i).evaluate(inputVectors.get(k));
				if (tmp < min) {
					min = tmp;
					minId = i;
				}
			}

			neurons.get((minId + 1) % neurons.size()).learn(
					inputVectors.get(k), 0.7);
			
			neurons.get(minId).learn(inputVectors.get(k), 0.7);
			
			if (minId > 0) {
				neurons.get((minId - 1)).learn(
						inputVectors.get(k), 0.7);
			} else {
				neurons.get(neurons.size()-1).learn(
						inputVectors.get(k), 0.7);
			}

		}

		
		for (int i = 0; i < neurons.size(); i++) {
			ImageHandler.saveToFile("result/neuron" + i + ".png", neurons.get(i).getWeightsVector(), 30, 30);
		}
	}

	public void evaluate(String imagePath) {

		Image image = new Image(imagePath, 30, 30);

		Vector networkInput = image
				.getImageData(0,0);

		int minId = 0;
		double min = Double.MAX_VALUE, tmp = min;
		minId = 0;

		for (int i = 0; i < neurons.size(); i++) {
			tmp = neurons.get(i).evaluate(networkInput);
			if (tmp < min) {
				min = tmp;
				minId = i;
			}

		}

		System.out.print(minId + " : " + min + "\n");

	}

}
