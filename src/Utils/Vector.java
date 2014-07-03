package Utils;

public class Vector {

	private double[] vector;
	private int cluster;

	public int getCluster() {
		return cluster;
	}

	public void setCluster(int cluster) {
		this.cluster = cluster;
	}

	public Vector(double[] vector) {
		this.vector = vector;
	}

	public double[] getInputVectorData() {
		return this.vector;
	}
}

