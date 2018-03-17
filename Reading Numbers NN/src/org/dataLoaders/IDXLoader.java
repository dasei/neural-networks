package org.dataLoaders;
import java.io.*;

public class IDXLoader {
	
	private File file;
	private int pointer;
	
	public IDXLoader(String path) {
		file = new File(path);
		pointer = 0;
	}

}
