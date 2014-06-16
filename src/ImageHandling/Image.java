package ImageHandling;

import java.io.IOException;
import java.util.ArrayList;

import Utils.Vector;

public class Image {

	ImageHandler image;
	String filePath;
	int frameWidht, frameHeight;
	
	public Image(String filePath, int frameWidth, int frameHeight){
		this.filePath = filePath;
		this.frameHeight = frameHeight;
		this.frameWidht = frameWidth;
		try {
			image = new ImageHandler(filePath, frameWidth, frameHeight);
		} catch (IOException e) {
			System.out.print(e.getMessage());
		}
	}
	
	public Vector getImageData(int frameRow, int frameCol){
		return image.getGrayScaleImageArray().get(frameRow+frameCol);
	}
	
	public int getImageWidth(){
		return image.getImageWidth();
	}
	
	public int getImageHeight(){
		return image.getImageHeight();
	}
	
	public ArrayList<Vector> getImageData(){
		return image.getGrayScaleImageArray();
	}
	
	public void draw(){
		double[] imageVector =  image.getGrayScaleImageArray().get(0).getInputVectorData();
		
		System.out.print("-----"+filePath+"-----\n");
		
		for(int i = 0; i< image.getImageHeight(); i++){
			for(int j = 0; j<image.getImageWidth(); j++){
				System.out.print(imageVector[i*5+j]);
			}
			System.out.print("\n");
		}
		
		System.out.print("\n\n");
		
	}

}
