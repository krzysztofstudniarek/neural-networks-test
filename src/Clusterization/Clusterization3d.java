package Clusterization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
			tmp[0] = Double.parseDouble(tab[5]);
			tmp[1] = Double.parseDouble(tab[6]);
			tmp[2] = Double.parseDouble(tab[7]);
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
				ind++;
			}

			PlotStyle myStyle = new PlotStyle();
			myStyle.setStyle(Style.POINTS);
			myStyle.setLineType(NamedPlotColor.BLUE);

			DataSetPlot myPlot = new DataSetPlot(data);
			myPlot.setPlotStyle(myStyle);

			p.addPlot(myPlot);

			for (int i = 0; i < 600; i++) {
				neurons.add(new Neuron(3, 20));
			}

			Random r = new Random();

			int minId, numOfLearnLoops = 1000000;
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

			ArrayList<ArrayList<Vector>> outputDatasetsList = new ArrayList<ArrayList<Vector>>();
			for (int i = 0; i < neurons.size(); i++) {

				ArrayList<Vector> a = new ArrayList<Vector>();
				a.add(new Vector(neurons.get(i).getWeightsVector()));
				outputDatasetsList.add(a);

			}

			for (Vector point : points) {

				double min = Double.MAX_VALUE, tmp = min;
				minId = 0;

				for (int i = 0; i < neurons.size(); i++) {
					tmp = neurons.get(i).evaluate(point);
					if (tmp < min) {
						min = tmp;
						minId = i;
					}

				}

				outputDatasetsList.get(minId).add(point);

			}

			Random rand = new Random();

			for (ArrayList<Vector> dat : outputDatasetsList) {
				double[][] d = new double[dat.size()][3];
				
				for(int i = 0; i<dat.size(); i++){
					d[i][0] = dat.get(i).getInputVectorData()[0];
					d[i][1] = dat.get(i).getInputVectorData()[1];
					d[i][2] = dat.get(i).getInputVectorData()[2];
				}
				
				
				myStyle = new PlotStyle();
				myStyle.setStyle(Style.POINTS);
				myStyle.setLineType(NamedPlotColor.values()[rand.nextInt(NamedPlotColor.values().length)]);

				myPlot = new DataSetPlot(d);
				myPlot.setPlotStyle(myStyle);

				p.addPlot(myPlot);
			}

			
			p.set("key", "off");
			p.plot();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clusterizeNG() {

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
				ind++;
			}

			PlotStyle myStyle = new PlotStyle();
			myStyle.setStyle(Style.POINTS);
			myStyle.setLineType(NamedPlotColor.BLUE);

			DataSetPlot myPlot = new DataSetPlot(data);
			myPlot.setPlotStyle(myStyle);

			p.addPlot(myPlot);

			for (int i = 0; i < 10; i++) {
				neurons.add(new Neuron(3, 20));
			}

			Random r = new Random();

			int minId, numOfLearnLoops = 10000;
			double lambda = 1;

			for (int j = 0; j < numOfLearnLoops; j++) {
				final int k = r.nextInt(points.size());
				double min = Double.MAX_VALUE, tmp = min;
				minId = 0;

				Collections.sort(neurons, new Comparator<Neuron>() {
					public int compare(Neuron a, Neuron b) {
						if (b.evaluate(points.get(k)) < a
								.evaluate(points.get(k)))
							return 1;
						else
							return -1;
					}
				});

				for (int i = 0; i < neurons.size(); i++) {

					neurons.get(i)
							.learn(points.get(k), Math.exp(-i / lambda));
				}
				lambda -= 1 / numOfLearnLoops;
			}

			ArrayList<ArrayList<Vector>> outputDatasetsList = new ArrayList<ArrayList<Vector>>();
			for (int i = 0; i < neurons.size(); i++) {

				ArrayList<Vector> a = new ArrayList<Vector>();
				a.add(new Vector(neurons.get(i).getWeightsVector()));
				outputDatasetsList.add(a);

			}

			for (Vector point : points) {

				double min = Double.MAX_VALUE, tmp = min;
				minId = 0;

				for (int i = 0; i < neurons.size(); i++) {
					tmp = neurons.get(i).evaluate(point);
					if (tmp < min) {
						min = tmp;
						minId = i;
					}

				}

				outputDatasetsList.get(minId).add(point);

			}

			Random rand = new Random();

			for (ArrayList<Vector> dat : outputDatasetsList) {
				double[][] d = new double[dat.size()][3];
				
				for(int i = 0; i<dat.size(); i++){
					d[i][0] = dat.get(i).getInputVectorData()[0];
					d[i][1] = dat.get(i).getInputVectorData()[1];
					d[i][2] = dat.get(i).getInputVectorData()[2];
				}
				
				
				myStyle = new PlotStyle();
				myStyle.setStyle(Style.POINTS);
				myStyle.setLineType(NamedPlotColor.values()[rand.nextInt(NamedPlotColor.values().length)]);

				myPlot = new DataSetPlot(d);
				myPlot.setPlotStyle(myStyle);

				p.addPlot(myPlot);
			}

			
			p.set("key", "off");
			p.plot();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void clusterizeKMeans() {
		// TODO Auto-generated method stub
		
	}

}
