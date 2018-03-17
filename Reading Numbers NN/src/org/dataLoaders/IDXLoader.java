package org.dataLoaders;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;

public class IDXLoader {
	
	private File file;
	private FileInputStream fileIn;
	
	private long pointer;
	private byte[] data;
	
	public IDXLoader(String path) {
		file = new File(path);
		pointer = 0;		
	}
	
	public int[][] getData(int amount) {
		
		int[][] data = null;
		
		//read file and save data
		try {
			fileIn = new FileInputStream(file);
			
			//check if its an image or label file
			byte[] buf = new byte[4];
			fileIn.read(buf);			
			int magicNumber = ByteBuffer.wrap(buf).getInt();
			
			switch(magicNumber) {
			case 2049:
				//label file
				fileIn.skip(4 + pointer);
				
				data = new int[amount][1];
				break;
				
			case 2051:
				//image file				
				fileIn.skip(12 + (pointer*28*28));
				
				data = new int[amount][28 * 28];
				break;
				
			default:		
				//invalid magic number
				System.err.println("invalid 'magic number' in idx file: '" + file.getAbsolutePath() + "'");
				return null;
			}
			
			pointer += amount;
			
			// read and put data into data array
			int b;
			for(int i = 0; i < data.length; i++) {
				for(int j = 0; j < data[0].length; j++) {
					b = fileIn.read();
					if(b >= 0)
						data[i][j] = b;
					else {
						//System.out.println("end of file has been reached, check for false training data");
						pointer = 0;
						fileIn.close();
						fileIn = new FileInputStream(file);
					}
				}
			}
			
			fileIn.close();
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return data;
	}
}
