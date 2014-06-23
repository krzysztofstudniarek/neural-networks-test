package Compression;

import Utils.Vector;



public abstract class CompressionNetworkInterface {

	protected String imagePath;
	protected String outputPath;
	protected int frameWidth, frameHight, neuronsVectorSize, learningStepSize;
	
	public CompressionNetworkInterface(String imagePath, String outputPath, int frameWidth,
			int frameHight, int learningStepSize){
		
		this.imagePath = imagePath;
		this.outputPath = outputPath;
		this.frameWidth = frameWidth;
		this.frameHight = frameHight;
		this.learningStepSize = learningStepSize;
		
	}
	
	
	public abstract Vector compress(boolean saveToFile, String logFile, double lambda, int neuronsVectorSize);

}
