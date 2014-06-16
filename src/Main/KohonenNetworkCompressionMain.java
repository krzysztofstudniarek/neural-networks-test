package Main;

import Compression.KohonenCompressionNetwork;

public class KohonenNetworkCompressionMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		KohonenCompressionNetwork CN = new KohonenCompressionNetwork("boat.png", "res/boatKohonen3.png", 4, 4, 16384, 100000);
		
		CN.compress();

	}

}
