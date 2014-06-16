package Clusterization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

import Utils.Neuron;
import Utils.Vector;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;

public class Clusterization3d implements ClusterizationInterface {

	private ArrayList<Vector> points;

	public ArrayList<Vector> getPoints() {
		return points;
	}

	public void readDataFromURL(String URL) throws IOException {

		points = new ArrayList<Vector>();

		URL url = new URL(URL);

		URLConnection yc = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				yc.getInputStream(), "UTF-8"));
		String inputLine;

		while ((inputLine = in.readLine()) != null) {
			String[] tab = inputLine.split(",");
			double[] tmp = new double[3];
			tmp[0] = Double.parseDouble(tab[0]);
			tmp[1] = Double.parseDouble(tab[1]);
			tmp[2] = Double.parseDouble(tab[2]);
			points.add(new Vector(tmp));

		}

		in.close();

	}

	public void clusterize() {

		try {
			readDataFromURL("http://hydra.ics.p.lodz.pl/data/sc201314/193933");

			ArrayList<Neuron> neurons = new ArrayList<Neuron>();

			JavaPlot p = new JavaPlot();
			p.newGraph3D();

			double[][] data = new double[points.size()][3];

			int ind = 0;

			for (Vector point : points) {
				data[ind][0] = point.getInputVectorData()[0];
				data[ind][1] = point.getInputVectorData()[1];
				data[ind][2] = point.getInputVectorData()[2];
				System.out.print(point.getInputVectorData()[0] + " : "
						+ point.getInputVectorData()[1] + " : "
						+ point.getInputVectorData()[2] + "\n");
				ind++;
			}

			PlotStyle myStyle = new PlotStyle();
			myStyle.setStyle(Style.POINTS);
			myStyle.setLineType(NamedPlotColor.BLUE);

			DataSetPlot myPlot = new DataSetPlot(data);
			myPlot.setPlotStyle(myStyle);

			p.addPlot(myPlot);

			for (int i = 0; i < 100; i++) {
				neurons.add(new Neuron(3, 20));
			}

			Random r = new Random();

			int minId, numOfLearnLoops = 100000;
			double lambda = 1;

			for (int j = 0; j < numOfLearnLoops; j++) {
				int k = r.nextInt(points.size());
				double min = Double.MAX_VALUE, tmp = min;
				minId = 0;

				for (int i = 0; i < neurons.size(); i++) {
					tmp = neurons.get(i).evaluate(points.get(k));
					if (tmp < min) {
						min = tmp;
						minId = i;
					}
				}

				for (int i = 0; i < neurons.size(); i++) {
					neurons.get(i).learn(points.get(k),
							Math.exp(-(minId - i) * (minId - i)
									/ (2 * lambda * lambda)));
				}
				lambda -= 1 / numOfLearnLoops;
			}

			double[][] data1 = new double[neurons.size()][3];

			for (int i = 0; i < neurons.size(); i++) {
				data1[i][0] = neurons.get(i).getWeightsVector()[0];
				data1[i][1] = neurons.get(i).getWeightsVector()[1];
				data1[i][2] = neurons.get(i).getWeightsVector()[2];
			}

			PlotStyle myStyle1 = new PlotStyle();
			myStyle1.setStyle(Style.POINTS);
			myStyle1.setLineType(NamedPlotColor.RED);

			DataSetPlot myPlot1 = new DataSetPlot(data1);
			myPlot1.setPlotStyle(myStyle1);

			p.addPlot(myPlot1);

			p.plot();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}