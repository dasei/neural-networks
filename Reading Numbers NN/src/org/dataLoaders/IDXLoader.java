package org.dataLoaders;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class IDXLoader {
	
	private static ArrayList<double[][]> data = new ArrayList<double[][]>();
	private static ArrayList<String> dataCategory = new ArrayList<String>();
	private static ArrayList<Integer> dataLoadedProgress = new ArrayList<Integer>();
	
	public static void startLoadingData(String filePath, String dataCategory, int dataAmount, int valsPerData) {
		data.add(new double[dataAmount][valsPerData]);
		IDXLoader.dataCategory.add(dataCategory);
		dataLoadedProgress.add(0);
		
		startLoadingData(dataCategory, new File(filePath));
	}
	
	public static double[][] getData(String category){
		return data.get(dataCategory.indexOf(category));
	}
	
	public static int getDataAvailable(String category){
		return dataLoadedProgress.get(dataCategory.indexOf(category));
	}
	
	private static void startLoadingData(String category, File file) {
		
		(new Thread() {
			public void run() {
				long timeStart = System.currentTimeMillis();
				
				//read file and save data
				try {
					
					byte[] dataBuf = Files.readAllBytes(Paths.get(file.getPath()));
					
					//check if its an image or label file
					int magicNumber = ByteBuffer.wrap(dataBuf,0,4).getInt();					
					int dataCount = ByteBuffer.wrap(dataBuf,4,4).getInt();					
					
					double[][] data = IDXLoader.data.get(dataCategory.indexOf(category));
					int offset;
					switch(magicNumber) {
					case 2049:						
						//label file
						offset = 8;
						data = new double[dataCount][10];
						
						for(int label = 0; label < data.length; label++)
							data[label][dataBuf[label + offset]] = 1;
						
						break;
					case 2051:
						//image file
						offset = 16;
						
						int imgWidth = ByteBuffer.wrap(dataBuf,8,4).getInt();
						int imgHeight = ByteBuffer.wrap(dataBuf,12,4).getInt();
						int pixelCount = imgWidth * imgHeight;
						data = new double[dataCount][imgWidth * imgHeight];
						
						for(int b = 0; b < dataBuf.length - offset; b++) {
							
							data[b / pixelCount][b % pixelCount] = map((dataBuf[b+offset] & 0xFF), 0, 255, -1, 1);
	
						}
						
						
									
						break;						
					default:		
						//invalid magic number
						System.err.println("invalid 'magic number' in idx file: '" + file.getAbsolutePath() + "'");
						return;
					}
						
					dataLoadedProgress.set(dataCategory.indexOf(category), data.length);
					IDXLoader.data.set(dataCategory.indexOf(category), data);
					
					System.out.println("time used for loading file: '" + file.getName() + "' (ms): " + (System.currentTimeMillis() - timeStart));
					
//					System.out.println("ar test: " + (data == IDXLoader.data.get(dataCategory.indexOf(category))));
//					System.out.println("ar test: " + (data == IDXLoader.data.get(dataCategory.indexOf(category))));
					
//					for(int img = 0; img < data.length; img++)
//						for(int px = 0; px < data[0].length; px++)
//							System.out.println(data[img][px]);
					
					
					
				}catch(Exception e) {
					e.printStackTrace();
				}				
			}
		}).start();
	}
	
	
	//Lineare Transformation
	final static double EPSILON = 1e-12;
	
	public static double map(double valueCoord1,
	        double startCoord1, double endCoord1,
	        double startCoord2, double endCoord2) {

	    if (Math.abs(endCoord1 - startCoord1) < EPSILON) {
	        throw new ArithmeticException("/ 0");
	    }

	    double offset = startCoord2;
	    double ratio = (endCoord2 - startCoord2) / (endCoord1 - startCoord1);
	    return ratio * (valueCoord1 - startCoord1) + offset;
	}
}
