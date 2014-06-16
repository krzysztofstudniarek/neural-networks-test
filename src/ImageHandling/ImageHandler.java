package ImageHandling;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Utils.Vector;

public class ImageHandler {

	static int width;
	static int height;
	ArrayList<Vector> imageVectors;

	public ImageHandler(String filePath, int frameWidth, int frameHeight)
			throws IOException {

		BufferedImage hugeImage = ImageIO.read(new File(filePath));
		readImage(hugeImage, frameWidth, frameHeight);

	}

	public ImageHandler() {

	}

	private void readImage(BufferedImage image, int frameWidth, int frameHeight) {
		width = image.getWidth();
		height = image.getHeight();

		int numOfFramesRow = width / frameWidth;
		int numOfFramesCol = height / frameHeight;

		int i = 0;
		Color c;
		imageVectors = new ArrayList<Vector>();
		double[] imageVector = new double[frameWidth * frameHeight];
		int frameRowInd, frameColInd;
		frameRowInd = frameColInd = 0;
		for (frameColInd = 0; frameColInd < numOfFramesCol; frameColInd++) {
			for (frameRowInd = 0; frameRowInd < numOfFramesRow; frameRowInd++) {
				for (int col = frameColInd * frameHeight; col < (frameColInd + 1)
						* frameHeight; col++) {
					for (int row = frameRowInd * frameWidth; row < (frameRowInd + 1)
							* frameWidth; row++) {

						c = new Color(image.getRGB(row, col));
						imageVector[i++] = (double) (c.getBlue() + c.getGreen() + c
								.getRed()) / 675;
					}
				}
				i = 0;
				imageVectors.add(new Vector(imageVector));
				imageVector = new double[frameWidth * frameHeight];
			}
		}
	}

	public static void saveToFile(String filePath, double[] weights,
			int imageWidth, int imageHeight) {

		BufferedImage image = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = (WritableRaster) image.getData();

		int[] rgb = new int[weights.length * 3];
		Color c;

		for (int i = 0; i < weights.length; i++) {
			c = new Color((int) (weights[i] * 225), (int) (weights[i] * 225),
					(int) (weights[i] * 225));

			rgb[i * 3] = c.getRed();
			rgb[i * 3 + 1] = c.getGreen();
			rgb[i * 3 + 2] = c.getBlue();

		}

		raster.setPixels(0, 0, imageWidth, imageHeight, rgb);
		image.setData(raster);
		try {
			File f = new File(filePath);
			if (!f.exists()) {

				f.createNewFile();

			}

			ImageIO.write(image, "png", f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void saveToFile(String filePath, ArrayList<Vector> weights,
			int frameWidth, int frameHight, int imageWidth, int imageHight) {

		BufferedImage image = new BufferedImage(imageWidth, imageHight,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		WritableRaster raster = (WritableRaster) image.getData();

		int x = 0, y = 0;

		for (Vector w : weights) {

			int[] rgb = new int[w.getInputVectorData().length * 3];
			Color c;

			for (int i = 0; i < w.getInputVectorData().length; i++) {
				c = new Color((int) (w.getInputVectorData()[i] * 225),
						(int) (w.getInputVectorData()[i] * 225),
						(int) (w.getInputVectorData()[i] * 225));

				rgb[i * 3] = c.getRed();
				rgb[i * 3 + 1] = c.getGreen();
				rgb[i * 3 + 2] = c.getBlue();

			}

			raster.setPixels(x, y, frameWidth, frameHight, rgb);

			x += frameWidth;
			if (x >= image.getWidth()) {
				x = 0;
				y += frameHight;
			}

		}

		image.setData(raster);

		try {
			File f = new File(filePath);
			if (!f.exists()) {

				f.createNewFile();

			}

			ImageIO.write(image, "png", f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<Vector> getGrayScaleImageArray() {
		return this.imageVectors;
	}

	public int getImageWidth() {
		return this.width;
	}

	public int getImageHeight() {
		return this.height;
	}

}