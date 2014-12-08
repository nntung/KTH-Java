package kth.ag2311.mapalgebra.model;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Storing a raster
 * 
 * @author Nga Nguyen
 *
 */
public class Layer {

	// //////////////////////////////////////////
	// Fields/Attributes
	//

	/**
	 * Name of a layer
	 */
	public String name;

	/**
	 * Path of data file
	 */
	public String path;

	/**
	 * Number of rows of the layer
	 */
	public int nRows;

	/**
	 * Number of columns of the layer
	 */
	public int nCols;

	/**
	 * Two double values representing the coordinates of the lower left corner
	 * of the layer
	 */
	public double originX;
	public double originY;

	/**
	 * Resolution of the layer
	 */
	public double resolution;

	/**
	 * Array of values the constitutes the layer in two dimensional nRows x
	 * nCols
	 */
	public double[][] values;

	/**
	 * no value - ArcGIS uses -9999 by default
	 */
	public double nullValue;

	/**
	 * max value of matrix values
	 */
	public double maxValue;

	/**
	 * min value of matrix values
	 */
	public double minValue;

	// //////////////////////////////////////////
	// Methods
	//

	/**
	 * Construction method of Layer class and load data
	 * 
	 * @param layerName
	 *            Name of layer
	 * @param inputFile
	 *            Path of input file
	 */
	public Layer(String layerName, String inputFile) {
		// Name this layer with layerName
		this.name = layerName;
		this.path = inputFile;

		System.out.println("Loading... " + inputFile);

		// use buffering, reading one line at a time
		// Exception may be thrown while reading (and writing) a file.
		BufferedReader bufReader = null; // obj for reading file

		// try to read content of fileName (path)
		try {
			// put data of filename to obj Reader
			bufReader = new BufferedReader(new FileReader(inputFile));

			String line = null;
			String[] words; // separate by a space

			// read #01 line - nCols int
			line = bufReader.readLine();
			if (line != null) {
				// split line into words separated by space
				words = line.split(" ");
				// get last word in array of words
				String numValue = words[words.length - 1];
				// try to convert String to Int
				nCols = Integer.parseInt(numValue);
			}

			// read #02 line - nRows int
			line = bufReader.readLine();
			if (line != null) {
				words = line.split(" ");
				String numValue = words[words.length - 1];
				nRows = Integer.parseInt(numValue);
			}

			// read #03 line - xllcorner double
			line = bufReader.readLine();
			if (line != null) {
				words = line.split(" ");
				String numValue = words[words.length - 1];
				originX = Double.parseDouble(numValue);
			}

			// read #04 line - yllcorner double
			line = bufReader.readLine();
			if (line != null) {
				words = line.split(" ");
				String numValue = words[words.length - 1];
				originY = Double.parseDouble(numValue);
			}

			// read #05 line - cellsize double
			line = bufReader.readLine();
			if (line != null) {
				words = line.split(" ");
				String numValue = words[words.length - 1];
				resolution = Double.parseDouble(numValue);
			}

			// read #06 line - NODATA_value int
			line = bufReader.readLine();
			if (line != null) {
				words = line.split(" ");
				String numValue = words[words.length - 1];
				nullValue = Double.parseDouble(numValue);
			}

			// IMPORTANT! Don't forget to create a matrix 2D of values
			values = new double[nRows][nCols];

			// continue read data
			for (int i = 0; i < nRows; i++) { // loop nRows
				line = bufReader.readLine();
				if (line != null) {

					// split line into words separated by space
					words = line.split(" ");

					for (int j = 0; j < nCols; j++) { // loop nCols
						// get value of word jth in words
						double value = Double.parseDouble(words[j]);

						// assign value to 2D-values
						values[i][j] = value;
					}
				}
			}

		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				bufReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Loaded!");
	}

	/**
	 * Construction method of Layer class with input fields
	 * 
	 * @param layerName
	 *            name of Layer
	 * @param nrow
	 *            Number of rows
	 * @param ncol
	 *            Number of col
	 * @param oX
	 *            the Origin of X
	 * @param oY
	 *            the Origin of Y
	 * @param res
	 *            resolution of layer
	 * @param nullVal
	 *            NODATA value
	 */
	public Layer(String layerName, int nrow, int ncol, double oX, double oY,
			double res, double nullVal) {
		this.name = layerName;
		this.nRows = nrow;
		this.nCols = ncol;
		this.originX = oX;
		this.originY = oY;
		this.resolution = res;
		this.nullValue = nullVal;

		// IMPORTANT! Don't forget to create a matrix 2D of values
		values = new double[nRows][nCols];

	}

	/**
	 * Display all value of Layer class to console
	 * 
	 * @param allContentData
	 *            do you want to show all DATA, true=show, false=not show
	 * 
	 */
	public void print(boolean allContentData) {
		System.out.println("Layer:	" + name);
		System.out.println("Path:	" + path);
		System.out.println("---------------------------------------");

		System.out.println("ncols			" + nCols);
		System.out.println("nrows			" + nRows);
		System.out.println("xllcorner 		" + originX);
		System.out.println("yllcorner 		" + originY);
		System.out.println("cellsize 		" + resolution);
		System.out.println("NODATA_value		" + nullValue);

		if (allContentData) {
			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					System.out.print(values[i][j] + " ");
				}
				System.out.println();
			}
		}
	}

	/**
	 * Save value of Layer class into text file The file format can be imported
	 * to ArcGIS
	 * 
	 * @param outputFile
	 *            Path of output file
	 */
	public void save(String outputFile) {
		System.out.println("Saving...");

		// use buffering, writing one line at a time
		// Exception may be thrown while reading (and writing) a file.
		BufferedWriter bufWriter = null;

		// try to write content into fileName (path)
		try {
			// use buffering, writing one line at a time
			// Exception may be thrown while reading (and writing) a file.
			bufWriter = new BufferedWriter(new FileWriter(outputFile));

			String line = null;

			// write #01 line - nCols int
			line = "ncols			 " + nCols;
			bufWriter.write(line);
			bufWriter.newLine();

			// write #02 line - nRows int
			line = "nrows			 " + nRows;
			bufWriter.write(line);
			bufWriter.newLine();

			// write #03 line - xllcorner double
			line = "xllcorner 		 " + originX;
			bufWriter.write(line);
			bufWriter.newLine();

			// write #04 line - yllcorner double
			line = "yllcorner 		 " + originY;
			bufWriter.write(line);
			bufWriter.newLine();

			// write #04 line - cellsize int
			line = "cellsize 		 " + resolution;
			bufWriter.write(line);
			bufWriter.newLine();

			// write #06 line - NODATA_value int
			line = "NODATA_value		 " + nullValue;
			bufWriter.write(line);
			bufWriter.newLine();

			// continue write data
			for (int i = 0; i < nRows; i++) { // loop nRows
				// create a line from each row of values
				// line must be empty before adding values
				line = "";

				// adding values in a row to line
				for (int j = 0; j < nCols; j++) { // loop nCols
					line += values[i][j] + " ";
				}

				// save it to buffer
				bufWriter.write(line);
				// make a break, and continue to saving a next row
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

		System.out.println("Saved!");
		System.out.println("You can try to import to ArcGIS!");
	}

	/**
	 * Constant of max Gray Level
	 */
	private final static int maxGray = 255;

	/**
	 * Default RGB for NODATA cells
	 */
	private final static int[] nullGray = { 0, 0, 0 };

	/**
	 * 
	 * @return maximum value of image data
	 */
	public double getMax() {
		maxValue = Double.MIN_VALUE;
		for (int i = 0; i < nRows; i++) { // loop nRows
			for (int j = 0; j < nCols; j++) { // loop nCols
				double value = this.values[i][j];
				if (maxValue < value && value != nullValue)
					maxValue = value;
			}
		}
		return maxValue;
	}

	/**
	 * 
	 * @return minimum value of image data
	 */
	public double getMin() {
		minValue = Double.MAX_VALUE;
		for (int i = 0; i < nRows; i++) { // loop nRows
			for (int j = 0; j < nCols; j++) { // loop nCols
				double value = this.values[i][j];
				if (minValue > value && value != nullValue)
					minValue = value;
			}
		}
		return minValue;
	}

	/**
	 * show this Layer as an gray-scale image on the screen
	 */

	public void map() {
		BufferedImage image = new BufferedImage(nRows, nCols,
				BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();

		double grayscale = maxGray / (this.getMax() - this.getMin());

		// write data to raster
		int[] color = new int[3];
		for (int i = 0; i < nRows; i++) { // loop nRows
			for (int j = 0; j < nCols; j++) { // loop nCols
				// create color for this point
				if (values[i][j] == nullValue) {
					color[0] = nullGray[0]; // Red
					color[1] = nullGray[1]; // Green
					color[2] = nullGray[2]; // Blue
				} else {
					int value = (int) (values[i][j] * grayscale);
					color[0] = value; // Red
					color[1] = value; // Green
					color[2] = value; // Blue
				}
				raster.setPixel(i, j, color);
			}
		}

		// show this image on the screen
		JFrame jframe = new JFrame();
		JLabel jlabel = new JLabel();
		ImageIcon ii = new ImageIcon(image);

		jlabel.setIcon(ii);
		jframe.add(jlabel);
		jframe.setSize(nCols + 25, nRows + 50);
		jframe.setVisible(true);

	}

	/**
	 * show this Layer as an color image on the screen using 24bit RGBA
	 * 
	 * @param interestingValues
	 *            list of interesting values
	 */
	public void map(double[] interestingValues) {
		BufferedImage image = new BufferedImage(nRows, nCols,
				BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();

		double grayscale = maxGray / (this.getMax() - this.getMin());
		// create random color for each interesting values
		Random rand = new Random();
		int numOfInterest = interestingValues.length;
		int[][] colorPanel = new int[numOfInterest][3];
		for (int k = 0; k < numOfInterest; k++) {
			colorPanel[k][0] = rand.nextInt(maxGray + 1);
			colorPanel[k][1] = rand.nextInt(maxGray + 1);
			colorPanel[k][2] = rand.nextInt(maxGray + 1);
		}

		// write data to raster
		int[] color = new int[3];
		for (int i = 0; i < nRows; i++) { // loop nRows
			for (int j = 0; j < nCols; j++) { // loop nCols
				// create color for this point
				if (values[i][j] == nullValue) {
					color[0] = nullGray[0]; // Red
					color[1] = nullGray[1]; // Green
					color[2] = nullGray[2]; // Blue
				} else {
					// default color is grayscale
					double value = values[i][j] * grayscale;
					color[0] = (int) value; // Red
					color[1] = (int) value; // Green
					color[2] = (int) value; // Blue

					// get color for interesting value
					for (int k = 0; k < numOfInterest; k++) {
						if (interestingValues[k] == values[i][j]) {
							color[0] = colorPanel[k][0]; // Red
							color[1] = colorPanel[k][1]; // Green
							color[2] = colorPanel[k][2]; // Blue
							break;
						}
					}
				}
				raster.setPixel(i, j, color);
			}
		}

		// show this image on the screen
		JFrame jframe = new JFrame();
		JLabel jlabel = new JLabel();
		ImageIcon ii = new ImageIcon(image);

		jlabel.setIcon(ii);
		jframe.add(jlabel);
		jframe.setSize(nCols + 25, nRows + 50);
		jframe.setVisible(true);

	}

	/**
	 * Create a layer with values equal sum of this.values + inLayer.values
	 * 
	 * @param inLayer
	 *            obj of inLayer
	 * @param outLayerName
	 *            name of outLayer
	 * @return obj of ourLayer
	 */
	public Layer localSum(Layer inLayer, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, originX,
				originY, resolution, nullValue);

		for (int i = 0; i < nRows; i++) { // loop nRows
			for (int j = 0; j < nCols; j++) { // loop nCols
				if ((this.values[i][j] == this.nullValue)
						|| (inLayer.values[i][j] == inLayer.nullValue)) {

					outLayer.values[i][j] = outLayer.nullValue;

				} else {

					outLayer.values[i][j] = this.values[i][j]
							+ inLayer.values[i][j];
				}
			}
		}

		return outLayer;
	}

	/**
	 * Internal Radius using in focalVariety method
	 */
	private int Radius;

	/**
	 * Internal deltaRow using in getNeighborhood method
	 */
	private int[][] dRow;

	/**
	 * Internal deltaCol using in getNeighborhood method
	 */
	private int[][] dCol;

	/**
	 * Create deltaRow and deltaCol Note: call it once for saving processing
	 */
	private void createDelta() {
		int size = Radius * 2 + 1;

		int delta = -Radius;
		this.dRow = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				this.dRow[i][j] = delta;
			}
			delta++;
		}

		this.dCol = new int[size][size];
		for (int i = 0; i < size; i++) {
			delta = -Radius;
			for (int j = 0; j < size; j++) {
				this.dCol[i][j] = delta;
				delta++;
			}
		}
	}

	/**
	 * Internal Mask: square or circle Using getNeighborhood method
	 */
	private int[][] mask;

	/**
	 * Create a square or a circle mask
	 * 
	 * @param square
	 *            TRUE=square, FALSE=circle
	 */
	private void createMask(boolean square) {
		int size = Radius * 2 + 1;
		this.mask = new int[size][size];
		if (square) {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					this.mask[i][j] = 1;
				}
			}
		} else { // circle

			// all are zeros
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					this.mask[i][j] = 0;
				}
			}

			// get circle boundary
			int xC = Radius;
			int yC = Radius;
			int d = (5 - Radius * 4) / 4;
			int x = 0;
			int y = Radius;
			do {
				this.mask[yC + y][xC + x] = 1;
				this.mask[yC - y][xC + x] = 1;
				this.mask[yC + y][xC - x] = 1;
				this.mask[yC - y][xC - x] = 1;
				this.mask[yC + x][xC + y] = 1;
				this.mask[yC - x][xC + y] = 1;
				this.mask[yC + x][xC - y] = 1;
				this.mask[yC - x][xC - y] = 1;

				if (d < 0) {
					d += 2 * x + 1;
				} else {
					d += 2 * (x - y) + 1;
					y--;
				}
				x++;
			} while (x <= y);

			// fill in circle
			for (int i = 0; i < size; i++) {
				// find left
				int l = 0;
				while (this.mask[i][l] == 0 && l < size)
					l++;
				// find right
				int r = size - 1;
				while (this.mask[i][r] == 0 && r > 0)
					r--;

				// fill 1 from l to r
				for (int j = l; j < r; j++)
					this.mask[i][j] = 1;
			}

		}

	}

	/**
	 * Get all neighbor values of cell (row,col)
	 * 
	 * @param rIdx
	 *            Row index
	 * @param cIdx
	 *            Column index
	 * @return List of neighbor values
	 */
	private ArrayList<Integer> getNeighborhood(int rIdx, int cIdx) {
		ArrayList<Integer> neighbors = new ArrayList<Integer>();
		int size = Radius * 2 + 1;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (this.mask[i][j] == 1) {
					// get real row and col after apply mask and delta
					int row = rIdx + dRow[i][j];
					int col = cIdx + dCol[i][j];
					if (row >= 0 && row < nRows && col >= 0 && col < nCols)
						neighbors.add((int) this.values[row][col]);
				}
			}
		}

		return neighbors;
	}

	/**
	 * Focal Statistics method: get Variety/Difference value
	 * 
	 * @param radius
	 * @param square
	 * @param outLayerName
	 *            Name of output Layer
	 * @return Layer object storing the result of this operation
	 */
	public Layer focalVariety(int radius, boolean square, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, originX,
				originY, resolution, nullValue);

		// IMPORTANT! Need to create Delta and Mask
		Radius = radius;
		createDelta();
		createMask(square);

		for (int i = 0; i < nRows; i++) { // loop nRows
			for (int j = 0; j < nCols; j++) { // loop nCols

				// get list of neighbors
				ArrayList<Integer> neighbors = getNeighborhood(i, j);

				// get number of neighbors, if it is empty then continue
				int numOfNeighbors = 0;
				if (neighbors.isEmpty()) {
					continue;
				} else {
					numOfNeighbors = neighbors.size();
				}

				// get Variety of neighbors
				Collections.sort(neighbors);
				int variety = 1;
				for (int k = 0; k < numOfNeighbors - 1; k++) {
					int v1 = neighbors.get(k);
					int v2 = neighbors.get(k + 1);
					if (v1 != v2 && v1 != nullValue && v2 != nullValue)
						variety++;
					// else ignore !!!
				}

				// assign to cell in outLayer
				outLayer.values[i][j] = variety;
			}
		}

		return outLayer;
	}

	/**
	 * Zonal Statistics method: get Minimum value
	 * 
	 * @param zoneLayer Layer zone
	 * @param outLayerName
	 *            Name of output Layer
	 * @return Layer object storing the result of this operation
	 */
	public Layer zonalMinimum(Layer zoneLayer, String outLayerName) {
		Layer outLayer = new Layer(outLayerName, nRows, nCols, originX,
				originY, resolution, nullValue);

		// Create hash map of zoneIndex and min value of this zone
		// Double = zoneIndex (key); Double = minimum value of this zone (value)
		HashMap<Double, Double> zm = new HashMap<Double, Double>();

		for (int i = 0; i < nRows; i++) { // loop nRows
			for (int j = 0; j < nCols; j++) { // loop nCols
				double vZone = zoneLayer.values[i][j];
				double vLayer = this.values[i][j];

				// ignore NODATA
				if (vZone == nullValue)
					continue;

				// try to retrieve from hash map
				Double zoneIdx = new Double(vZone);
				Double minValue = zm.get(zoneIdx);

				// if not then create new
				if (minValue == null) {
					zm.put(zoneIdx, vLayer);
				} else {
					// compare and update
					if (minValue > vLayer) {
						zm.put(zoneIdx, vLayer);
					}
				}
			}
		}

		// create outLayer
		for (int i = 0; i < nRows; i++) { // loop nRows
			for (int j = 0; j < nCols; j++) { // loop nCols
				double vZone = zoneLayer.values[i][j];
				Double zoneIdx = new Double(vZone);
				Double minValue = zm.get(zoneIdx);

				if (vZone == nullValue) {
					outLayer.values[i][j] = outLayer.nullValue;
				} else {
					outLayer.values[i][j] = minValue.doubleValue();
				}
			}
		}

		return outLayer;
	}
	
	
	/**
	 * SHORTEST PATH 
	 */
	
	private final static int[] dx = {-1,0,1,1,1,0,-1,-1};
	private final static int[] dy = {-1,-1,-1,0,1,1,1,0};
	
	// 1 2 3
	// 8 0 4
	// 7 6 5
	
	private void setPoint(Point p, double v) {
		this.values[p.y][p.x] = v;
	}
	
	private double getPoint(Point p) {
		return this.values[p.y][p.x];
	}
	
	private boolean isValidPoint(Point p) {
		if (p.x<0 || p.y<0 || p.x>nCols || p.y>nRows)
			return false;
		else 
			return true;
	}
	
	public Layer getShortestPath(int fromX, int fromY, int toX, int toY) {
		Point startingPoint = new Point(fromY, fromX); // STRANGE!
		Point endingPoint = new Point(toY, toX); // STRANGE!
		
		if (!isValidPoint(startingPoint) || !isValidPoint(endingPoint)) 
			return null;
		
		Layer outLayer = new Layer("visitedLayer", nRows, nCols, originX,
				originY, resolution, 0);
		
		for (int i=0; i<8; i++) {
			// calculate coordinates
			outLayer.values[startingPoint.y + dy[i]][startingPoint.x + dx[i]] = 2;
			outLayer.values[endingPoint.y + dy[i]][endingPoint.x + dx[i]] = 2;
		}
		
		Layer visited = new Layer("visitedLayer", nRows, nCols, originX,
				originY, resolution, 0);

		Layer weight = new Layer("visitedLayer", nRows, nCols, originX,
				originY, resolution, 0);
		
		int maxValue = this.nRows + this.nCols;

		ArrayList<Point> candidates = new ArrayList<Point>();
		ArrayList<Point> candidatesRoad = new ArrayList<Point>();
		ArrayList<Point> candidatesEnd = new ArrayList<Point>();
		
		candidates.add(startingPoint);
		Point currentPoint = candidates.get(0);
		weight.setPoint(currentPoint, 0);
		visited.setPoint(currentPoint, 1000);
				
		int xx,yy;
		int idx = 0;
		double newWeight, setWeight;
		Point tmp = null;
		
		boolean foundRoadFromStarting = false;
		Point startOnRoad = null;
		
		// find the possible shortest way to road
		while (!currentPoint.isEqual(endingPoint) && !candidates.isEmpty()) {
			
			// update from min Point
			for (int i=0; i<8; i++) {
				// calculate coordinates
				xx = currentPoint.x + dx[i];
				yy = currentPoint.y + dy[i];
				// check if this coordinates is inside matrix
				if (xx>0 && yy>0 && xx<nCols && yy<nRows) {
					tmp = new Point(xx,yy);
					// check if this Point is visited as a min Point
					if (this.getPoint(tmp)!=this.nullValue) {
						startOnRoad = new Point(tmp);
						foundRoadFromStarting = true;
						break;
					} else {
						if (visited.getPoint(tmp) < 100) {
							setWeight = (this.getPoint(tmp) == this.nullValue) ? maxValue : 1;
							newWeight = weight.getPoint(currentPoint) + setWeight;
							if (weight.getPoint(tmp) == 0) {
								// check if this Point visit at the first time
								visited.setPoint(tmp, i+1);
								weight.setPoint(tmp, newWeight);
								candidates.add(tmp);
							} else if (newWeight < weight.getPoint(tmp)) {
								// check if newWeight is shorter, then update
								visited.setPoint(tmp, i+1);
								weight.setPoint(tmp, newWeight);
							}
						}
					}
				}
			}
			
			if (foundRoadFromStarting) break;
			
			// remove this min Point
			candidates.remove(idx);
			
			// find another min Point
			double minValue = Double.MAX_VALUE;
			int canSize = candidates.size();
			if (canSize>0) {
				for (int i=0; i<canSize; i++) {
					tmp = candidates.get(i);
					if (minValue > weight.getPoint(tmp)) {
						idx = i;
						minValue = weight.getPoint(tmp);
					}
				}
				
				currentPoint = candidates.get(idx);
				double dir = visited.getPoint(currentPoint);
				visited.setPoint(currentPoint, dir * 100);
			} else {
				break;
			}
		}
		
		candidatesRoad.add(startOnRoad);
		int idxRoad = 0;
		
		// find the possible shortest way on road ONLY
		while (!currentPoint.isEqual(endingPoint) && !candidatesRoad.isEmpty()) {
			
			// update from min Point
			for (int i=0; i<8; i++) {
				// calculate coordinates
				xx = currentPoint.x + dx[i];
				yy = currentPoint.y + dy[i];
				// check if this coordinates is inside matrix
				if (xx>0 && yy>0 && xx<nCols && yy<nRows) {
					tmp = new Point(xx,yy);
					// check if this Point is visited as a min Point
					if (visited.getPoint(tmp) < 100
							&& this.getPoint(tmp)!=this.nullValue) {
						setWeight = (this.getPoint(tmp) == this.nullValue) ? maxValue : 1;
						newWeight = weight.getPoint(currentPoint) + setWeight;
						if (weight.getPoint(tmp) == 0) {
							// check if this Point visit at the first time
							visited.setPoint(tmp, i+1);
							weight.setPoint(tmp, newWeight);
							candidatesRoad.add(tmp);
						} else if (newWeight < weight.getPoint(tmp)) {
							// check if newWeight is shorter, then update
							visited.setPoint(tmp, i+1);
							weight.setPoint(tmp, newWeight);
						}
					}
				}
			}
			
			// remove this min Point
			candidatesRoad.remove(idxRoad);
			
			// find another min Point
			double minValue = Double.MAX_VALUE;
			int canSize = candidatesRoad.size();
			if (canSize>0) {
				for (int i=0; i<canSize; i++) {
					tmp = candidatesRoad.get(i);
					if (minValue > weight.getPoint(tmp)) {
						idxRoad = i;
						minValue = weight.getPoint(tmp);
					}
				}
				
				currentPoint = candidatesRoad.get(idxRoad);
				double dir = visited.getPoint(currentPoint);
				visited.setPoint(currentPoint, dir * 100);
			} else {
				break;
			}
		}
		
		candidatesEnd.add(endingPoint);
		int idxEnd = 0;
		currentPoint = candidatesEnd.get(0);
		weight.setPoint(currentPoint, 0);
		visited.setPoint(currentPoint, 1000);
		
		boolean foundWayFromEnding = false;
		Point endingOnRoad = null;
		Point endingFromRoad = null;
		
		// find the possible shortest way from ending point to visited road (ONLY ONCE)
		while (!candidatesEnd.isEmpty()) {
			
			// update from min Point
			for (int i=0; i<8; i++) {
				// calculate coordinates
				xx = currentPoint.x + dx[i];
				yy = currentPoint.y + dy[i];
				// check if this coordinates is inside matrix
				if (xx>0 && yy>0 && xx<nCols && yy<nRows) {
					tmp = new Point(xx,yy);
					// check if this Point is visited as a min Point
					if (visited.getPoint(tmp) < 100) {
						setWeight = (this.getPoint(tmp) == this.nullValue) ? maxValue : 1;
						newWeight = weight.getPoint(currentPoint) + setWeight;
						if (weight.getPoint(tmp) == 0) {
							// check if this Point visit at the first time
							visited.setPoint(tmp, i+1);
							weight.setPoint(tmp, newWeight);
							candidatesEnd.add(tmp);
						} else if (newWeight < weight.getPoint(tmp)) {
							// check if newWeight is shorter, then update
							visited.setPoint(tmp, i+1);
							weight.setPoint(tmp, newWeight);
						}
					} else {
						if (this.getPoint(tmp)!=this.nullValue) {
							// FOUND!!!
							endingOnRoad = new Point(xx, yy);
							endingFromRoad = new Point(currentPoint);
							foundWayFromEnding = true;
							break;
						}
					}
				}
			}
			
			if (foundWayFromEnding) break;
			
			// remove this min Point
			candidatesEnd.remove(idxEnd);
			
			// find another min Point
			double minValue = Double.MAX_VALUE;
			int canSize = candidatesEnd.size();
			if (canSize>0) {
				for (int i=0; i<canSize; i++) {
					tmp = candidatesEnd.get(i);
					if (minValue > weight.getPoint(tmp)) {
						idxEnd = i;
						minValue = weight.getPoint(tmp);
					}
				}
				
				currentPoint = candidatesEnd.get(idxEnd);
				double dir = visited.getPoint(currentPoint);
				visited.setPoint(currentPoint, dir * 100);
			} else {
				break;
			}
		}
		
		// weight.map();

		// trace back the shortest way
		if (foundWayFromEnding) {
			
			// trace back from endingOnRoad to starting Point
			while (visited.getPoint(endingOnRoad) != 1000) {
				outLayer.setPoint(endingOnRoad, 1);
				int direction = (int) visited.getPoint(endingOnRoad); 
				switch (direction) {
				case 100: 
					endingOnRoad.x ++;
					endingOnRoad.y ++;
					break;
				case 200:
					//endingOnRoad.x ++;
					endingOnRoad.y ++;
					break;
				case 300:
					endingOnRoad.x --;
					endingOnRoad.y ++;
					break;
				case 400:
					endingOnRoad.x --;
					//endingOnRoad.y ++;
					break;
				case 500:
					endingOnRoad.x --;
					endingOnRoad.y --;
					break;
				case 600:
					//endingOnRoad.x ++;
					endingOnRoad.y --;
					break;
				case 700:
					endingOnRoad.x ++;
					endingOnRoad.y --;
					break;
				case 800:
					endingOnRoad.x ++;
					//endingOnRoad.y ++;
					break;
				}
			}
			
			// trace back from endingFromRoad to ending Point

			while (visited.getPoint(endingFromRoad) != 1000) {
				outLayer.setPoint(endingFromRoad, 1);
				int direction = (int) visited.getPoint(endingFromRoad); 
				switch (direction) {
				case 100: 
					endingFromRoad.x ++;
					endingFromRoad.y ++;
					break;
				case 200:
					//endingFromRoad.x ++;
					endingFromRoad.y ++;
					break;
				case 300:
					endingFromRoad.x --;
					endingFromRoad.y ++;
					break;
				case 400:
					endingFromRoad.x --;
					//endingFromRoad.y ++;
					break;
				case 500:
					endingFromRoad.x --;
					endingFromRoad.y --;
					break;
				case 600:
					//endingFromRoad.x ++;
					endingFromRoad.y --;
					break;
				case 700:
					endingFromRoad.x ++;
					endingFromRoad.y --;
					break;
				case 800:
					endingFromRoad.x ++;
					//endingFromRoad.y ++;
					break;
				}
			}
		}
				
		return outLayer;
	}

	public void merge(Layer layer) {
		for (int i = 0; i < nRows; i++) { // loop nRows
			for (int j = 0; j < nCols; j++) { // loop nCols
				if (i<layer.nRows && j<layer.nCols) {
					if (layer.values[i][j] != layer.nullValue) {
						this.values[i][j] = layer.values[i][j];
					}
				}
			}
		}
	}

}
