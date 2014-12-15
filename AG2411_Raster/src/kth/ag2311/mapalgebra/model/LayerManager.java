package kth.ag2311.mapalgebra.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LayerManager {
	public String path;
	public LayerManager(String path) {
		this.path = path;
	}
	
	public void load(LayerListModel layerlist) {
		 // TODO
	}
	
	public void save(LayerListModel layerlist) {
		BufferedWriter bufWriter = null;

		// try to write content into fileName (path)
		try {
			// use buffering, writing one line at a time
			// Exception may be thrown while reading (and writing) a file.
			bufWriter = new BufferedWriter(new FileWriter(path));

			String line = null;
			
			int numberOfLayer = layerlist.size();
			line = Integer.toString(numberOfLayer);
			bufWriter.write(line);
			bufWriter.newLine();

			// write information of each layer
			for (int i = 0; i < numberOfLayer; i++) { 
				line = layerlist.get(i).path;
				bufWriter.write(line);
				bufWriter.newLine();
			}

		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				bufWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
