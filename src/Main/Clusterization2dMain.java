package Main;

import Clusterization.Clusterization2d;
import Clusterization.Clusterization3d;
import Compression.KohonenCompressionNetwork;
import Compression.NeuralGasCompressionNetwork;

public class Clusterization2dMain {

	public static void main(final String[] args) {
		
/*		NeuralGasCompressionNetwork NG = new NeuralGasCompressionNetwork("boat.png", "res/boatNeural3.png", 4, 4, 16384, 100000);
		
		NG.compress();
		
		KohonenCompressionNetwork CN = new KohonenCompressionNetwork("boat.png", "res/boatKohonen3.png", 4, 4, 16384, 100000);
		
		CN.compress();
*/
		
		Clusterization3d clust3d = new Clusterization3d();
		clust3d.clusterize();
		
		Clusterization2d clust2d = new Clusterization2d();
		clust2d.clusterize();
	}
	
}
