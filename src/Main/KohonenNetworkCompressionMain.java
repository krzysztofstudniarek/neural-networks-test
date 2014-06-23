package Main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Utils.Vector;

import Compression.KohonenCompressionNetwork;

public class KohonenNetworkCompressionMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		KohonenCompressionNetwork CN = new KohonenCompressionNetwork(
				"lena.png", "res/lenaKohonenPSNR.png", 4, 4, 1000000);

		ArrayList<Vector> logs = new ArrayList<Vector>();

		for (int i = 1; i < 100; i++) {
			for (int j = 1; j <= 10; j++) {
				logs.add(CN.compress(false, "out1.csv", 0.1 * j, 10*i));
			}
		}

		try {
			File f = new File("out1.csv");
			if (!f.exists()) {
				f.createNewFile();
			}

			BufferedWriter w = new BufferedWriter(new FileWriter(f));

			int ind = 0;

			for (Vector v : logs) {
				w.append(v.getInputVectorData()[0] + " "
						+ v.getInputVectorData()[1] + " "
						+ v.getInputVectorData()[2] + "\n");

				if (ind == 9) {
					w.append("\n");
					ind = 0;
				} else {
					ind++;
				}

			}

			w.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
