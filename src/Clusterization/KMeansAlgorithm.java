package Clusterization;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;

import Utils.Neuron;
import Utils.Vector;

public class KMeansAlgorithm
{
    //private static final int NUM_CLUSTERS = 0;    // Total clusters.
   // private static int TOTAL_DATA;      // Total data points.
    
    private static ArrayList<Vector> points;
    
    private static ArrayList<Vector> dataSet = new ArrayList<Vector>();
    private static ArrayList<Neuron> centroids = new ArrayList<Neuron>();
    
	private static void readDataFromURL(String URL) throws IOException {

		points = new ArrayList<Vector>();

		URL url = new URL(URL);

		URLConnection yc = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				yc.getInputStream(), "UTF-8"));
		String inputLine;

		while ((inputLine = in.readLine()) != null) {
			String[] tab = inputLine.split(",");
			double[] tmp = new double[2];
			tmp[0] = Double.parseDouble(tab[0]);
			tmp[1] = Double.parseDouble(tab[1]);
			points.add(new Vector(tmp));
		}

		//TOTAL_DATA = points.size();
		
		in.close();

	}
    
    private static void initialize()
    {
    	try {
			readDataFromURL("http://hydra.ics.p.lodz.pl/data/sc201314/193933");
			
			System.out.print("INITIALIZATION START: \n");
			
			for(int i = 0; i<10; i++){
				centroids.add(new Neuron(2, (double)1.0));
				System.out.println(centroids.get(i).getWeightsVector()[0]+":"+ centroids.get(i).getWeightsVector()[1] + "\n");
			}
			
			System.out.print("INITIALIZATION END \n");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
        return;
    }
    
    private static void kMeanCluster()
    {
        final double bigNumber = Math.pow(10, 10);    // some big number that's sure to be larger than our data range.
        double minimum = bigNumber;                   // The minimum value to beat. 
        double distance = 0.0;                        // The current minimum value.
        int sampleNumber = 0;
        int cluster = 0;
        boolean isStillMoving = true;
        Vector newData = null;
        
        
        
        // Add in new data, one at a time, recalculating centroids with each new one. 
        while(dataSet.size() < points.size())
        {
            newData = new Vector(points.get(sampleNumber).getInputVectorData());
            dataSet.add(newData);
            minimum = bigNumber;
            for(int i = 0; i < centroids.size(); i++)
            {
                distance = centroids.get(i).evaluate(newData);
                if(distance < minimum){
                    minimum = distance;
                    cluster = i;
                }
            }
            newData.setCluster(cluster);
            
            // calculate new centroids.
            for(int i = 0; i < centroids.size(); i++)
            {
                double totalX = 0;
                double totalY = 0;
                double totalInCluster = 0;
                for(int j = 0; j < dataSet.size(); j++)
                {
                    if(dataSet.get(j).getCluster() == i){
                        totalX += dataSet.get(j).getInputVectorData()[0];
                        totalY += dataSet.get(j).getInputVectorData()[1];
                        totalInCluster++;
                    }
                }
                
                
                
                if(totalInCluster > 0){
                	double[] tmp = new double[2];
                    tmp[0] = ((double)totalX / (double)totalInCluster);
                    tmp[1] = ((double)totalY / (double)totalInCluster);
                    centroids.get(i).setWeights(tmp);
                    //System.out.println(centroids.get(i).getWeightsVector()[0]+":"+ centroids.get(i).getWeightsVector()[1] + "\n");
                }
                
                
            }
            sampleNumber++;
        }
        
        // Now, keep shifting centroids until equilibrium occurs.
        while(isStillMoving)
        {
            // calculate new centroids.
            for(int i = 0; i < centroids.size(); i++)
            {
                double totalX = 0;
                double totalY = 0;
                double totalInCluster = 0;
                for(int j = 0; j < dataSet.size(); j++)
                {
                    if(dataSet.get(j).getCluster() == i){
                        totalX += dataSet.get(j).getInputVectorData()[0];
                        totalY += dataSet.get(j).getInputVectorData()[1];
                        totalInCluster++;
                    }
                }
                if(totalInCluster > 0){
                	double[] tmp = new double[2];
                    tmp[0] = ((double)totalX / (double)totalInCluster);
                    tmp[1] = ((double)totalY / (double)totalInCluster);
                    centroids.get(i).setWeights(tmp);
                }
            }
            
            // Assign all data to the new centroids
            isStillMoving = false;
            
            for(int i = 0; i < dataSet.size(); i++)
            {
                Vector tempData = dataSet.get(i);
                minimum = bigNumber;
                for(int j = 0; j < centroids.size(); j++)
                {
                    distance = centroids.get(j).evaluate(tempData);
                    if(distance < minimum){
                        minimum = distance;
                        cluster = j;
                    }
                }
                tempData.setCluster(cluster);
                if(tempData.getCluster() != cluster){
                    tempData.setCluster(cluster);
                    isStillMoving = true;
                }
            }
        }
        return;
    }
    
  
    
    public static void main(String[] args)
    {
        initialize();
        kMeanCluster();
        
		JavaPlot p = new JavaPlot();
        
		ArrayList<ArrayList<Vector>> outputDatasetsList = new ArrayList<ArrayList<Vector>>();
		for (int i = 0; i < centroids.size(); i++) {

			ArrayList<Vector> a = new ArrayList<Vector>();
			a.add(new Vector(centroids.get(i).getWeightsVector()));
			outputDatasetsList.add(a);
			System.out.println(centroids.get(i).getWeightsVector()[0]+":"+ centroids.get(i).getWeightsVector()[1] + "\n");

		}

		for (Vector point : dataSet) {

			outputDatasetsList.get(point.getCluster()).add(point);

		}

		Random rand = new Random();

		for (ArrayList<Vector> data : outputDatasetsList) {
			System.out.println(data.size() +  "\n");
			double[][] d = new double[data.size()][2];
			
			for(int i = 0; i<data.size(); i++){
				d[i][0] = data.get(i).getInputVectorData()[0];
				d[i][1] = data.get(i).getInputVectorData()[1];
			}
			
			
			PlotStyle myStyle = new PlotStyle();
			myStyle.setStyle(Style.POINTS);
			myStyle.setLineType(NamedPlotColor.values()[rand.nextInt(NamedPlotColor.values().length)]);

			DataSetPlot myPlot = new DataSetPlot(d);
			myPlot.setPlotStyle(myStyle);

			p.addPlot(myPlot);
		}

		//double[][] data1 = new double[centroids.size()][2];
		
		p.set("key", "off");
		p.plot();
        
        
        return;
    }
}