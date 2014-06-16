package Compression;

public abstract class CompressionNetworkInterface {

	protected String imagePath;
	protected String outputPath;
	protected int frameWidth, frameHight, neuronsVectorSize, learningStepSize;
	
	public CompressionNetworkInterface(String imagePath, String outputPath, int frameWidth,
			int frameHight, int neuronsVectorSize, int learningStepSize){
		
		this.imagePath = imagePath;
		this.outputPath = outputPath;
		this.frameWidth = frameWidth;
		this.frameHight = frameHight;
		this.neuronsVectorSize = neuronsVectorSize;
		this.learningStepSize = learningStepSize;
		
	}
	
	
	public abstract void compress();

}
