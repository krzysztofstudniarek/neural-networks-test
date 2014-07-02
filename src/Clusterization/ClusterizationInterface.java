package Clusterization;

import java.io.IOException;

public interface ClusterizationInterface {

	public void readDataFromURL(String URL) throws IOException;
	public void clusterize();
	public void clusterizeNG();
	
}
