package kth.ag2311.mapalgebra.model;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import javax.swing.BoundedRangeModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import kth.ag2311.mapalgebra.view.ElementRenderer;
import kth.ag2311.mapalgebra.view.LayerType;

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
	
	public boolean isImportSucessful;

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
			
			// get max and min value
			getMax();
			getMin();

			isImportSucessful = true;
			
			System.out.println("Loaded!");

		} catch (NumberFormatException ex) {
			isImportSucessful = false;
			ex.printStackTrace();
		} catch (IOException ex) {
			isImportSucessful = false;
			ex.printStackTrace();
		} finally {
			try {
				bufReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
		BufferedImage image = new BufferedImage(nCols, nRows,
				BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();

		double grayscale = maxGray ;
		if (maxValue > minValue)
			grayscale = maxGray / (maxValue - minValue);

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
				raster.setPixel(j, i, color);
			}
		}

		// show this image on the screen
		JFrame jframe = new JFrame();
		JLabel jlabel = new JLabel();
		ImageIcon ii = new ImageIcon(image);

		jlabel.setIcon(ii);
		jframe.add(jlabel);
		jframe.setTitle(this.name);
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
		BufferedImage image = new BufferedImage(nCols, nRows,
				BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();

		double grayscale = maxGray ;
		if (maxValue > minValue)
			grayscale = maxGray / (maxValue - minValue);
		
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
				raster.setPixel(j, i, color);
			}
		}

		// show this image on the screen
		JFrame jframe = new JFrame();
		JLabel jlabel = new JLabel();
		ImageIcon ii = new ImageIcon(image);

		jlabel.setIcon(ii);
		jframe.add(jlabel);
		jframe.setTitle(this.name);
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
	private void createFocal(boolean square) {
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
		createFocal(square);

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
	public final static double shortestValue = 100;
	public final static double shortestPoint = 200;
	
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
		if (p.x<0 || p.y<0 || p.x>=nCols || p.y>=nRows)
			return false;
		else 
			return true;
	}
	
	public Layer getShortestPath(int fromX, int fromY, int toX, int toY) {
		Point startingPoint = new Point(fromX, fromY);
		Point endingPoint = new Point(toX, toY);
		
		if (!isValidPoint(startingPoint) || !isValidPoint(endingPoint)) 
			return null;
		
		Layer outLayer = new Layer("visitedLayer", nRows, nCols, originX,
				originY, resolution, 0);
		
		for (int i=0; i<8; i++) {
			// calculate coordinates
			outLayer.values[startingPoint.y + dy[i]][startingPoint.x + dx[i]] = shortestPoint;
			outLayer.values[endingPoint.y + dy[i]][endingPoint.x + dx[i]] = shortestPoint;
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
		
		// TODO: Need to find all possible shortest way to road
		
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
				outLayer.setPoint(endingOnRoad, shortestValue);
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
				outLayer.setPoint(endingFromRoad, shortestValue);
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

	public void mergeShortest(Layer layer) {
		for (int i = 0; i < nRows; i++) { // loop nRows
			for (int j = 0; j < nCols; j++) { // loop nCols
				if (i<layer.nRows && j<layer.nCols) {
					if (layer.values[i][j] == shortestPoint || layer.values[i][j] == shortestValue ) {
						this.values[i][j] = layer.values[i][j];
					}
				}
			}
		}
	}
	
	public boolean isViewOnMap;
	
	@Override
	public String toString() {
		return name;
	}
	
	// IMAGE of Layer
	
	private final static int defaultAlpha = 150;
	public BufferedImage imageMap;
	public void renderMap() {
		imageMap = new BufferedImage(nCols, nRows, BufferedImage.TYPE_INT_ARGB);
		
		// fill all transparent
		Graphics2D g2d = imageMap.createGraphics();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0));
        g2d.fillRect(0, 0, nCols, nRows);

        // set each point        
		WritableRaster raster = imageMap.getRaster();
		
		int[] colors = new int[4];

		switch (property.type) {
		case LayerProperty.TYPE_ROAD:
			int[] colorBlack = {10,10,10,250};
			
			for (int i = 0; i < nRows; i++) { // loop nRows
				for (int j = 0; j < nCols; j++) { // loop nCols
					// create color for this point
					if (values[i][j] != nullValue) {
						raster.setPixel(j, i, colorBlack);
					}
				}
			}
			break;
		case LayerProperty.TYPE_VEGETATION:
		case LayerProperty.TYPE_DEVELOPMENT:
		case LayerProperty.TYPE_HYDROLOGY:
			for (int i = 0; i < nRows; i++) { // loop nRows
				for (int j = 0; j < nCols; j++) { // loop nCols
					// create color for this point
					double value = values[i][j];
					if (value != nullValue) {
						property.colorAlphas.get(value).getColor(colors);
						raster.setPixel(j, i, colors);
					}
				}
			}
			break;
		case LayerProperty.TYPE_ASPECT:
			for (int i = 0; i < nRows; i++) { // loop nRows
				for (int j = 0; j < nCols; j++) { // loop nCols
					// create color for this point
					double value = values[i][j];
					if (value != nullValue) {
						property.aspectColor[(int) value].getColor(colors);
						raster.setPixel(j, i, colors);
					}
				}
			}
			break;
		case LayerProperty.TYPE_SLOPE:
			for (int i = 0; i < nRows; i++) { // loop nRows
				for (int j = 0; j < nCols; j++) { // loop nCols
					// create color for this point
					double value = values[i][j];
					if (value != nullValue) {
						property.slopeColor[(int) value].getColor(colors);
						raster.setPixel(j, i, colors);
					}
				}
			}
			break;

		case LayerProperty.TYPE_ELEVATION:
			double grayscale = maxGray ;
			if (maxValue > minValue)
				grayscale = maxGray / (maxValue - minValue);
			
			// write data to raster
			for (int i = 0; i < nRows; i++) { // loop nRows
				for (int j = 0; j < nCols; j++) { // loop nCols
					// create color for this point
					if (values[i][j] != nullValue) {
						int value = (int) ((values[i][j] - minValue) * grayscale);
						colors[0] = value; // Alpha
						colors[1] = value; // Red
						colors[2] = value; // Green
						colors[3] = property.transparent; // 250
						raster.setPixel(j, i, colors);
					}
				}
			}
			break;
		}
		
		
	}
	
	// PROPERTY of Layer
	
	private final static String FILE_EXTENSION_PROPERTY = ".pro";
	public LayerProperty property;
	public void loadProperty() {
		int pos = path.lastIndexOf(".");
		String proPath = (pos > 0) ? path.substring(0, pos) : path;
		proPath = proPath + FILE_EXTENSION_PROPERTY;
		
		Properties prop = new Properties();
		InputStream input = null;
		property = new LayerProperty(); 
		try {
	 
			input = new FileInputStream(proPath);
	 
			// load a properties file
			prop.load(input);
			property.setProperty(prop);
			propertyChange = false;
	 
		} catch (IOException ex) {
			// TODO Show warning message that the layer does not have property file
			ex.printStackTrace();
		} finally {
			createPropertyPanel();
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void loadProperty(int type) {
		int pos = path.lastIndexOf(".");
		String proPath = (pos > 0) ? path.substring(0, pos) : path;
		proPath = proPath + FILE_EXTENSION_PROPERTY;
		
		Properties prop = new Properties();
		InputStream input = null;
		property = new LayerProperty(); 
		try {
	 
			input = new FileInputStream(proPath);
	 
			// load a properties file
			prop.load(input);
			property.setProperty(prop);
			property.type = type; // this is a different
			propertyChange = false;
	 
		} catch (IOException ex) {
			// TODO Show warning message that the layer does not have property file
			ex.printStackTrace();
		} finally {
			createPropertyPanel();
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void saveProperty() {
		int pos = path.lastIndexOf(".");
		String proPath = (pos > 0) ? path.substring(0, pos) : path;
		proPath = proPath + FILE_EXTENSION_PROPERTY;
		
		Properties prop = new Properties();
		OutputStream output = null;
		try {
	 
			output = new FileOutputStream(proPath);
	 
			// save a properties file
			property.createProperty(prop);
			prop.store(output, null);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// RENDER of mask of interest
	public Layer layerMask;
	public void createLayerMask() {
		switch (property.type) {
		case LayerProperty.TYPE_ROAD:
			layerMask = getShortestPath(property.startX, property.startY, 
					property.endX, property.endY);
			break;
		case LayerProperty.TYPE_VEGETATION:
			layerMask = getInterest();
			break;
		case LayerProperty.TYPE_DEVELOPMENT:
			layerMask = getInterestWithDistance(false, property.awayFrom);
			break;
		case LayerProperty.TYPE_HYDROLOGY:
			layerMask = getInterestWithDistance(true, property.closeBy);
			break;
		case LayerProperty.TYPE_ELEVATION:
			layerMask = null;
			break;
		case LayerProperty.TYPE_ASPECT:
			layerMask = getAspectInterest();
			break;
		case LayerProperty.TYPE_SLOPE:
			layerMask = getSlopeInterest();
			break;

		}
	}
	
	private Layer getInterestWithDistance(boolean isCloseBy, int dis) {
		Layer outLayer = new Layer("interestMask", nRows, nCols, originX,
				originY, resolution, 0);

		// IMPORTANT! Need to create Delta and Mask
		Radius = dis;
		createDelta();
		createFocal(false);

		for (int i = 0; i < nRows; i++) { // loop nRows
			for (int j = 0; j < nCols; j++) { // loop nCols

				if (values[i][j]>0) { // TODO TODO TODO
					// get list of neighbors
					ArrayList<Point> neighbors = getNeighborhoodPoint(i, j);

					// get number of neighbors, if it is empty then continue
					int numOfNeighbors = 0;
					if (neighbors.isEmpty()) {
						continue;
					} else {
						numOfNeighbors = neighbors.size();
					}
					
					for (int k = 0; k < numOfNeighbors; k++) {
						Point p = neighbors.get(k);
						outLayer.values[p.y][p.x] = 1;
					}
				}
			}
		}

		if (!isCloseBy) {
			for (int i = 0; i < nRows; i++) { // loop nRows
				for (int j = 0; j < nCols; j++) { // loop nCols
					if (values[i][j] > 0) {
						outLayer.values[i][j] = 0;
					} else {
						outLayer.values[i][j] = (outLayer.values[i][j] > 0) ? 0 : 1;
					}
				}
			}
		} else {
			for (int i = 0; i < nRows; i++) { // loop nRows
				for (int j = 0; j < nCols; j++) { // loop nCols
					if (values[i][j] > 0) {
						outLayer.values[i][j] = 0;
					}
				}
			}
		}
		
		return outLayer;
	}
	
	private ArrayList<Point> getNeighborhoodPoint(int rIdx, int cIdx) {
		ArrayList<Point> neighbors = new ArrayList<Point>();
		int size = Radius * 2 + 1;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (this.mask[i][j] == 1) {
					// get real row and col after apply mask and delta
					int row = rIdx + dRow[i][j];
					int col = cIdx + dCol[i][j];
					if (row >= 0 && row < nRows && col >= 0 && col < nCols)
						neighbors.add(new Point(col,row));
				}
			}
		}

		return neighbors;
	}
	
	private Layer getInterest() {
		Layer outLayer = new Layer("interestMask", nRows, nCols, originX,
				originY, resolution, 0);
		
		double value;
		for (int i = 0; i < nRows; i++) { // loop nRows
			for (int j = 0; j < nCols; j++) { // loop nCols
				value = values[i][j];
				if (property.interests.get(value)) {
					outLayer.values[i][j] = 1;
				}
			}
		}
		
		return outLayer;
	}
	
	private Layer getAspectInterest() {
		Layer outLayer = new Layer("interestMask", nRows, nCols, originX,
				originY, resolution, 0);
		
		double value;
		for (int i = 0; i < nRows; i++) { // loop nRows
			for (int j = 0; j < nCols; j++) { // loop nCols
				value = values[i][j];
				if (property.aspectInterest[(int)value]) {
					outLayer.values[i][j] = 1;
				}
			}
		}
		
		return outLayer;
	}
	
	private Layer getSlopeInterest() {
		Layer outLayer = new Layer("interestMask", nRows, nCols, originX,
				originY, resolution, 0);
		
		double value;
		for (int i = 0; i < nRows; i++) { // loop nRows
			for (int j = 0; j < nCols; j++) { // loop nCols
				value = values[i][j];
				if (property.slopeInterest[(int)value]) {
					outLayer.values[i][j] = 1;
				}
			}
		}
		
		return outLayer;
	}
	
	public BufferedImage imageMask;
	public void renderMaskOfInterest() {
		// create buffer
		imageMask = new BufferedImage(nCols, nRows, BufferedImage.TYPE_INT_ARGB);
		// fill all transparent
		Graphics2D g2d = imageMask.createGraphics();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0));
        g2d.fillRect(0, 0, nCols, nRows);
		// do not update if null
		if (layerMask == null) return;
		
		switch (property.type) {
		case LayerProperty.TYPE_ROAD:
			createRoadMask();
			break;
		case LayerProperty.TYPE_VEGETATION:
		case LayerProperty.TYPE_DEVELOPMENT:
		case LayerProperty.TYPE_HYDROLOGY:
		case LayerProperty.TYPE_ASPECT:
		case LayerProperty.TYPE_SLOPE:
			createInterestMask();
			break;
		}
	}
	
	private void createInterestMask() {
		WritableRaster raster = imageMask.getRaster();
		
		// write data to raster
		int[] color1 = {0,0,250,250};
		
		for (int i = 0; i < nRows; i++) { // loop nRows
			for (int j = 0; j < nCols; j++) { // loop nCols
				// create color for this point
				if (layerMask.values[i][j] > 0) {
					raster.setPixel(j, i, color1);
				} 
			}
		}
		
	}

	private void createRoadMask() {
		WritableRaster raster = imageMask.getRaster();
		
		// write data to raster
		int[] color1 = {250,0,0,200};
		
		for (int i = 0; i < nRows; i++) { // loop nRows
			for (int j = 0; j < nCols; j++) { // loop nCols
				// create color for this point
				if (layerMask.values[i][j] > 0) {
					raster.setPixel(j, i, color1);
				} 
			}
		}
	}
	
	public JPanel propertyPanel;
	private void createPropertyPanel() {
		switch (property.type) {
		case LayerProperty.TYPE_ROAD:
			createRoadPropertyPanel();
			break;
		case LayerProperty.TYPE_VEGETATION:
			createVegetationPropertyPanel();
			break;
		case LayerProperty.TYPE_DEVELOPMENT:
			createDevOrHydroPropertyPanel(true);
			break;
		case LayerProperty.TYPE_HYDROLOGY:
			createDevOrHydroPropertyPanel(false);
			break;
		case LayerProperty.TYPE_ELEVATION:
			createElevationPropertyPanel();
			break;
		case LayerProperty.TYPE_ASPECT:
			createAspectPropertyPanel();
			break;
		case LayerProperty.TYPE_SLOPE:
			createSlopePropertyPanel();
			break;
		default:
			propertyPanel = new JPanel();
		}
	}
	
	// General Property Panel
	private boolean propertyChange;
	private LayerType layerType;
	JCheckBox cbShowMask;
	
	// ROAD Property Panel
	JToggleButton btnStart;
	JToggleButton btnEnd;
	JCheckBox cbAddToShortestPath;
	
	private void createRoadPropertyPanel() {
		propertyPanel = new JPanel();
		propertyPanel.setLayout(new BorderLayout());
		
		layerType = new LayerType();
		propertyPanel.add(layerType, BorderLayout.NORTH);
		layerType.setType(LayerProperty.TYPE_ROAD);
		
		JPanel panel = new JPanel();
		propertyPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		btnStart = new JToggleButton("Start point");
		btnStart.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(btnStart);
		
		panel.add(Box.createRigidArea(new Dimension(0,5)));
		
		btnEnd = new JToggleButton("End point");
		btnEnd.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(btnEnd);
		
		btnStart.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if (btnStart.isSelected()) {
    				btnEnd.setSelected(false);
    			}
    		}
    	});
		
		btnEnd.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if (btnEnd.isSelected()) {
    				btnStart.setSelected(false);
    			}
    		}
    	});
		
		JPanel control = new JPanel();
		propertyPanel.add(control, BorderLayout.SOUTH);
		control.setLayout(new BoxLayout(control, BoxLayout.Y_AXIS));

		cbAddToShortestPath = new JCheckBox("Add to Shortest Path");
		control.add(cbAddToShortestPath);
		cbAddToShortestPath.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (cbAddToShortestPath.isSelected()) {
					setThisLayerToShortestPathLayer();
				} else {
					removeThisLayerFromShortestPathLayer();
				}
			}
		});
		cbAddToShortestPath.setSelected(true);
		
		cbShowMask = new JCheckBox("Show mask");
		control.add(cbShowMask);
		cbShowMask.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				setMask();
			}
		});
		cbShowMask.setSelected(true);

		JButton btnSaveChanges = new JButton("Save changes");
		control.add(btnSaveChanges);
		btnSaveChanges.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			saveProperty();
    		}
    	});
			
		updateRoadPropertyPanel();
	}
	
	private void setThisLayerToShortestPathLayer() {
		GeneralLayers.shortestPath = this;
		GeneralLayers.generalMap.renderImageMap();
		GeneralLayers.generalMap.repaint();
	}
	private void removeThisLayerFromShortestPathLayer() {
		GeneralLayers.shortestPath = null;
		GeneralLayers.generalMap.renderImageMap();
		GeneralLayers.generalMap.repaint();
	}
	
	public void setImageMask() {
		GeneralLayers.maskLayer = this;
		GeneralLayers.generalMap.renderImageMap();
		GeneralLayers.generalMap.repaint();
	}
	public void removeImageMask() {
		GeneralLayers.maskLayer = null;
		GeneralLayers.generalMap.renderImageMap();
		GeneralLayers.generalMap.repaint();
	}
	public void setMask() {
		if (cbShowMask == null) return;
		
		if (cbShowMask.isSelected()) {
			setImageMask();
		} else {
			removeImageMask();
		}
	}
	
	private void updateRoadPropertyPanel() {
		String sX = (property.startX >0) ? Integer.toString(property.startX) : "?";
		String sY = (property.startY >0) ? Integer.toString(property.startY) : "?";
		btnStart.setText("Start point (X="+ sX + ", Y=" + sY + ")");
		sX = (property.endX >0) ? Integer.toString(property.endX) : "?";
		sY = (property.endY >0) ? Integer.toString(property.endY) : "?";
		btnEnd.setText("End point (X="+ sX + ", Y=" + sY + ")");
	}
	
	private boolean updateRoadSelection(int xx, int yy) {
		if (btnStart.isSelected()) {
			propertyChange = true;
			property.startX = xx;
			property.startY = yy;
			updateRoadPropertyPanel();
			layerMask = getShortestPath(property.startX, property.startY, 
					property.endX, property.endY);
			renderMaskOfInterest();
			return true;
		}
		if (btnEnd.isSelected()) {
			propertyChange = true;
			property.endX = xx;
			property.endY = yy;
			updateRoadPropertyPanel();
			layerMask = getShortestPath(property.startX, property.startY, 
					property.endX, property.endY);
			renderMaskOfInterest();
			return true;
		}
		return false;
	}

	public boolean updateSelection(int xx, int yy) {
		boolean isUpdate = false;
		switch (property.type) {
		case LayerProperty.TYPE_ROAD:
			isUpdate = updateRoadSelection(xx,yy);
			break;
		case LayerProperty.TYPE_VEGETATION:
			//TODO No need!
			break;
		}
		return isUpdate;
	}
	
	private JList<Element> listElement;
	private ElementListModel elementListModel;
	private JToggleButton btnSetInterest;
	private JCheckBox cbAddToPicnicMap;
	private void createVegetationPropertyPanel() {
		propertyPanel = new JPanel();
		propertyPanel.setLayout(new BorderLayout());
		
		layerType = new LayerType();
		propertyPanel.add(layerType, BorderLayout.NORTH);
		layerType.setType(LayerProperty.TYPE_VEGETATION);
		
		JPanel panel = new JPanel();
		propertyPanel.add(panel, BorderLayout.CENTER);
		BoxLayout boxLayoutLayers = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
		panel.setLayout(boxLayoutLayers);
		
		listElement = new JList<Element>();
		listElement.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listElement.setLayoutOrientation(JList.VERTICAL);
		listElement.setVisibleRowCount(-1);
		listElement.setCellRenderer(new ElementRenderer());
		listElement.addMouseListener(new MouseAdapter() {
			
			public void mouseReleased(MouseEvent e) {				
				int index = listElement.locationToIndex(e.getPoint());
				Element elementItem = listElement.getModel().getElementAt(index);
				int cursorX = e.getPoint().x;
				if (cursorX < Element.iconwidth) {
					// show color chooser
					Rectangle rect = listElement.getCellBounds(index, index);
					listElement.repaint(rect);
					
					// TODO update layerMap
					//GeneralLayers.generalLayer.renderImageMap();
					//GeneralLayers.generalLayer.repaint();
				}
				
				if (btnSetInterest.isSelected()) {
					elementItem.interest = !elementItem.interest; 
					Rectangle rect = listElement.getCellBounds(index, index);
					listElement.repaint(rect);
					
					// update property
					property.interests.put(elementItem.value, elementItem.interest);
					layerMask = getInterest();
					renderMaskOfInterest();
					GeneralLayers.generalMap.renderImageMap();
					GeneralLayers.generalMap.repaint();
				}
				
			}
		});
		
		elementListModel = new ElementListModel();
		listElement.setModel(elementListModel);
		
		JScrollPane listScroller = new JScrollPane(listElement);
		panel.add(listScroller);
		
		JPanel control = new JPanel();
		propertyPanel.add(control, BorderLayout.SOUTH);
		control.setLayout(new BoxLayout(control, BoxLayout.Y_AXIS));

		btnSetInterest = new JToggleButton("Set Interests");
		control.add(btnSetInterest);
		btnSetInterest.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			// TODO ...
    		}
    	});
		
		cbAddToPicnicMap = new JCheckBox("Add to Picnic Map");
		control.add(cbAddToPicnicMap);
		cbAddToPicnicMap.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updatePicnicMap();
			}
		});
		cbAddToPicnicMap.setSelected(true);
		
		cbShowMask = new JCheckBox("Show mask");
		control.add(cbShowMask);
		cbShowMask.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (cbShowMask.isSelected()) {
					setImageMask();
				} else {
					removeImageMask();
				}
			}
		});
		cbShowMask.setSelected(false);

		JButton btnSaveChanges = new JButton("Save changes");
		control.add(btnSaveChanges);
		btnSaveChanges.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			saveProperty();
    		}
    	});
		
		createElementListModel();	
		//updateVegetationPropertyPanel(); why?? // TODO
	}
	
	private int distance;
	private void createDevOrHydroPropertyPanel(boolean isDev) {
		propertyPanel = new JPanel();
		propertyPanel.setLayout(new BorderLayout());
		
		layerType = new LayerType();
		propertyPanel.add(layerType, BorderLayout.NORTH);
		if (isDev)
			layerType.setType(LayerProperty.TYPE_DEVELOPMENT);
		else 
			layerType.setType(LayerProperty.TYPE_HYDROLOGY);
			
		JPanel panel = new JPanel();
		propertyPanel.add(panel, BorderLayout.CENTER);
		BoxLayout boxLayoutLayers = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
		panel.setLayout(boxLayoutLayers);
		
		listElement = new JList<Element>();
		listElement.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listElement.setLayoutOrientation(JList.VERTICAL);
		listElement.setVisibleRowCount(-1);
		listElement.setCellRenderer(new ElementRenderer());
		listElement.addMouseListener(new MouseAdapter() {
			
			public void mouseReleased(MouseEvent e) {				
				int index = listElement.locationToIndex(e.getPoint());
				//Element elementItem = listElement.getModel().getElementAt(index);
				int cursorX = e.getPoint().x;
				if (cursorX < Element.iconwidth) {
					// show color chooser
					Rectangle rect = listElement.getCellBounds(index, index);
					listElement.repaint(rect);
					
					// TODO update layerMap
					// renderMap()
					//GeneralLayers.generalLayer.renderImageMap();
					//GeneralLayers.generalLayer.repaint();
				}
				
			}
		});
		
		elementListModel = new ElementListModel();
		listElement.setModel(elementListModel);
		
		JScrollPane listScroller = new JScrollPane(listElement);
		panel.add(listScroller);
		
		JPanel control = new JPanel();
		propertyPanel.add(control, BorderLayout.SOUTH);
		control.setLayout(new BoxLayout(control, BoxLayout.Y_AXIS));

		String btnName = "";
		if (isDev) {
			btnName = "Set Away Distance";
			distance = property.awayFrom;
		} else {
			btnName = "Set Close Distance";
			distance = property.closeBy;
		}
		
		JPanel distancePanel = new JPanel();
		JButton btnDecrease = new JButton(" < ");
		JButton btnIncrease= new JButton(" > ");
		
		btnDecrease.setEnabled(false);
		btnIncrease.setEnabled(false);
		
		btnSetInterest = new JToggleButton(btnName);
		btnSetInterest.setToolTipText(btnName);
		control.add(btnSetInterest);
		btnSetInterest.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if (btnSetInterest.isSelected()) {
    				btnDecrease.setEnabled(true);
    				btnIncrease.setEnabled(true);
    			} else {
    				btnDecrease.setEnabled(false);
    				btnIncrease.setEnabled(false);
    			}
    		}
    	});
		btnSetInterest.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		control.add(distancePanel);
		distancePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		distancePanel.setLayout(new BoxLayout(distancePanel, BoxLayout.LINE_AXIS));
		JTextField textDistance = new JTextField(Integer.toString(distance));
		textDistance.setEditable(false);
		textDistance.setHorizontalAlignment(JTextField.CENTER);

		distancePanel.add(btnDecrease);
		distancePanel.add(textDistance);
		distancePanel.add(btnIncrease);
		
		btnDecrease.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if (distance > 0) {
    				distance --;
    				textDistance.setText(Integer.toString(distance));
    				if (isDev) {
						property.awayFrom = distance;
						layerMask = getInterestWithDistance(false, property.awayFrom);
					} else {
						property.closeBy = distance;
						layerMask = getInterestWithDistance(true, property.closeBy);
					}
						
					renderMaskOfInterest();
					GeneralLayers.generalMap.renderImageMap();
					GeneralLayers.generalMap.repaint();
    			}
    			
    		}
    	});
		
		btnIncrease.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if (distance < 25) {
    				distance ++;
        			textDistance.setText(Integer.toString(distance));

					if (isDev) {
						property.awayFrom = distance;
						layerMask = getInterestWithDistance(false, property.awayFrom);
					} else {
						property.closeBy = distance;
						layerMask = getInterestWithDistance(true, property.closeBy);
					}
					renderMaskOfInterest();
					GeneralLayers.generalMap.renderImageMap();
					GeneralLayers.generalMap.repaint();
    			}
    		}
    	});
		
		cbAddToPicnicMap = new JCheckBox("Add to Picnic Map");
		control.add(cbAddToPicnicMap);
		cbAddToPicnicMap.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updatePicnicMap();
			}
		});
		cbAddToPicnicMap.setSelected(true);
		
		cbShowMask = new JCheckBox("Show mask");
		control.add(cbShowMask);
		cbShowMask.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (cbShowMask.isSelected()) {
					setImageMask();
				} else {
					removeImageMask();
				}
			}
		});
		cbShowMask.setSelected(false);

		JButton btnSaveChanges = new JButton("Save changes");
		control.add(btnSaveChanges);
		btnSaveChanges.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			saveProperty();
    		}
    	});
		
		createElementListModel();	
	}

	private void createElementListModel() {
		for (Double value : property.descriptions.keySet()) {
			String des = property.descriptions.get(value);
			String color = property.colors.get(value);
			Boolean interest = property.interests.get(value);
			Element element = new Element(value, des, color, interest);
			elementListModel.addElement(element);
		}
	}
	
	private void createAspectPropertyPanel() {
		propertyPanel = new JPanel();
		propertyPanel.setLayout(new BorderLayout());
		
		layerType = new LayerType();
		propertyPanel.add(layerType, BorderLayout.NORTH);
		layerType.setType(LayerProperty.TYPE_ASPECT);
		
		JPanel panel = new JPanel();
		propertyPanel.add(panel, BorderLayout.CENTER);
		BoxLayout boxLayoutLayers = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
		panel.setLayout(boxLayoutLayers);
		
		listElement = new JList<Element>();
		listElement.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listElement.setLayoutOrientation(JList.VERTICAL);
		listElement.setVisibleRowCount(-1);
		listElement.setCellRenderer(new ElementRenderer());
		listElement.addMouseListener(new MouseAdapter() {
			
			public void mouseReleased(MouseEvent e) {				
				int index = listElement.locationToIndex(e.getPoint());
				Element elementItem = listElement.getModel().getElementAt(index);
				int cursorX = e.getPoint().x;
				if (cursorX < Element.iconwidth) {
					// show color chooser
					Rectangle rect = listElement.getCellBounds(index, index);
					listElement.repaint(rect);
					
					// TODO update layerMap
					// renderMap(); // render Map again
					//GeneralLayers.generalLayer.renderImageMap();
					//GeneralLayers.generalLayer.repaint();
				}
				
				if (btnSetInterest.isSelected()) {
					elementItem.interest = !elementItem.interest; 
					Rectangle rect = listElement.getCellBounds(index, index);
					listElement.repaint(rect);
					
					// update property
					property.aspectInterest[elementItem.value.intValue()] = elementItem.interest;
					layerMask = getAspectInterest();
					renderMaskOfInterest();
					GeneralLayers.generalMap.renderImageMap();
					GeneralLayers.generalMap.repaint();
				}
				
			}
		});
		
		elementListModel = new ElementListModel();
		listElement.setModel(elementListModel);
		
		JScrollPane listScroller = new JScrollPane(listElement);
		panel.add(listScroller);
		
		JPanel control = new JPanel();
		propertyPanel.add(control, BorderLayout.SOUTH);
		control.setLayout(new BoxLayout(control, BoxLayout.Y_AXIS));

		btnSetInterest = new JToggleButton("Set Interests");
		control.add(btnSetInterest);
		btnSetInterest.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			// TODO ...
    		}
    	});
		
		cbAddToPicnicMap = new JCheckBox("Add to Picnic Map");
		control.add(cbAddToPicnicMap);
		cbAddToPicnicMap.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updatePicnicMap();
			}
		});
		cbAddToPicnicMap.setSelected(true);
		
		cbShowMask = new JCheckBox("Show mask");
		control.add(cbShowMask);
		cbShowMask.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (cbShowMask.isSelected()) {
					setImageMask();
				} else {
					removeImageMask();
				}
			}
		});
		cbShowMask.setSelected(false);

		JButton btnSaveChanges = new JButton("Save changes");
		control.add(btnSaveChanges);
		btnSaveChanges.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			saveProperty();
    		}
    	});
		
		createAspectListModel();	
	}
	
	private void createAspectListModel() {
		int num = LayerProperty.numAspect - 1; // do not count the last (North)
		for (int i = 0; i < num; i++) {
			String des = property.aspectDescription[i];
			ColorAlpha color = property.aspectColor[i];
			Boolean interest = property.aspectInterest[i];
			Element element = new Element((double) i, des, color, interest);
			elementListModel.addElement(element);
		}
	}
	
	private void createSlopePropertyPanel() {
		propertyPanel = new JPanel();
		propertyPanel.setLayout(new BorderLayout());
		
		layerType = new LayerType();
		propertyPanel.add(layerType, BorderLayout.NORTH);
		layerType.setType(LayerProperty.TYPE_SLOPE);
		
		JPanel panel = new JPanel();
		propertyPanel.add(panel, BorderLayout.CENTER);
		BoxLayout boxLayoutLayers = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
		panel.setLayout(boxLayoutLayers);
		
		listElement = new JList<Element>();
		listElement.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listElement.setLayoutOrientation(JList.VERTICAL);
		listElement.setVisibleRowCount(-1);
		listElement.setCellRenderer(new ElementRenderer());
		listElement.addMouseListener(new MouseAdapter() {
			
			public void mouseReleased(MouseEvent e) {				
				int index = listElement.locationToIndex(e.getPoint());
				Element elementItem = listElement.getModel().getElementAt(index);
				int cursorX = e.getPoint().x;
				if (cursorX < Element.iconwidth) {
					// show color chooser
					Rectangle rect = listElement.getCellBounds(index, index);
					listElement.repaint(rect);
					
					// TODO update layerMap
					// renderMap(); // render Map again
					//GeneralLayers.generalLayer.renderImageMap();
					//GeneralLayers.generalLayer.repaint();
				}
				
				if (btnSetInterest.isSelected()) {
					elementItem.interest = !elementItem.interest; 
					Rectangle rect = listElement.getCellBounds(index, index);
					listElement.repaint(rect);
					
					// update property
					property.slopeInterest[elementItem.value.intValue()] = elementItem.interest;
					layerMask = getSlopeInterest();
					renderMaskOfInterest();
					GeneralLayers.generalMap.renderImageMap();
					GeneralLayers.generalMap.repaint();
				}
				
			}
		});
		
		elementListModel = new ElementListModel();
		listElement.setModel(elementListModel);
		
		JScrollPane listScroller = new JScrollPane(listElement);
		panel.add(listScroller);
		
		JPanel control = new JPanel();
		propertyPanel.add(control, BorderLayout.SOUTH);
		control.setLayout(new BoxLayout(control, BoxLayout.Y_AXIS));

		btnSetInterest = new JToggleButton("Set Interests");
		control.add(btnSetInterest);
		btnSetInterest.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			// TODO ...
    		}
    	});
		
		cbAddToPicnicMap = new JCheckBox("Add to Picnic Map");
		control.add(cbAddToPicnicMap);
		cbAddToPicnicMap.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updatePicnicMap();
			}
		});
		cbAddToPicnicMap.setSelected(true);
		
		cbShowMask = new JCheckBox("Show mask");
		control.add(cbShowMask);
		cbShowMask.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (cbShowMask.isSelected()) {
					setImageMask();
				} else {
					removeImageMask();
				}
			}
		});
		cbShowMask.setSelected(false);

		JButton btnSaveChanges = new JButton("Save changes");
		control.add(btnSaveChanges);
		btnSaveChanges.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			saveProperty();
    		}
    	});
		
		createSlopeListModel();	
	}

	private void createSlopeListModel() {
		int num = LayerProperty.numSlope; 
		for (int i = 0; i < num; i++) {
			String des = property.slopeDescription[i];
			ColorAlpha color = property.slopeColor[i];
			Boolean interest = property.slopeInterest[i];
			Element element = new Element((double) i, des, color, interest);
			elementListModel.addElement(element);
		}
	}

	private void createElevationPropertyPanel() {
		propertyPanel = new JPanel();
		propertyPanel.setLayout(new BorderLayout());
		
		layerType = new LayerType();
		propertyPanel.add(layerType, BorderLayout.NORTH);
		layerType.setType(LayerProperty.TYPE_ELEVATION);
		
		JPanel panel = new JPanel();
		propertyPanel.add(panel, BorderLayout.CENTER);
		BoxLayout boxLayoutLayers = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
		panel.setLayout(boxLayoutLayers);
		
		JLabel textTrans = new JLabel("Set transparent of layer");
		textTrans.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(textTrans);
		panel.add(Box.createRigidArea(new Dimension(0,5)));
		JSlider sliderTransparent = new JSlider(JSlider.HORIZONTAL, 50, 250, 150);
		panel.add(sliderTransparent);
		sliderTransparent.setAlignmentX(Component.LEFT_ALIGNMENT);
		sliderTransparent.setMinorTickSpacing(10);
		sliderTransparent.setMajorTickSpacing(50);
		sliderTransparent.setPaintTicks(true);
		sliderTransparent.setSnapToTicks(true);
		sliderTransparent.setPaintTrack(true);
		sliderTransparent.setPaintLabels(true);
		sliderTransparent.setValue(property.transparent);
		sliderTransparent.setToolTipText(Integer.toString(sliderTransparent.getValue()));
		
		BoundedRangeModel sliderModel = sliderTransparent.getModel();
		sliderModel.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent event) {
				// update property and render Map again
				property.transparent = sliderTransparent.getValue();
				renderMap(); // render Map again
				GeneralLayers.generalMap.renderImageMap();
				GeneralLayers.generalMap.repaint();

			}
			
		});
		
		JPanel control = new JPanel();
		propertyPanel.add(control, BorderLayout.SOUTH);
		control.setLayout(new BoxLayout(control, BoxLayout.Y_AXIS));

		JButton btnSaveChanges = new JButton("Save changes");
		control.add(btnSaveChanges);
		btnSaveChanges.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			saveProperty();
    		}
    	});
	}
	
	protected void updatePicnicMap() {
		// TODO Auto-generated method stub
		
	}
	
	public String getDescription(int xx, int yy) {
		String des = "";
		
		if (!isValidPoint(new Point(xx, yy))) return des;
		
		double value = values[yy][xx];
		switch (property.type) {
		case LayerProperty.TYPE_ROAD:
			if (value>0)
				des = "Road";
			else 
				des = "Blank";
			break;
		case LayerProperty.TYPE_VEGETATION:
		case LayerProperty.TYPE_DEVELOPMENT:
		case LayerProperty.TYPE_HYDROLOGY:
			des = property.descriptions.get(value);
			break;
		case LayerProperty.TYPE_ELEVATION:
			des = "High = " + Double.toString(value);
			break;
		case LayerProperty.TYPE_ASPECT:
			des = "aspect = " + property.aspectDescription[(int) value];
			break;
		case LayerProperty.TYPE_SLOPE:
			des = "slope = " + property.slopeDescription[(int) value];
			break;
		default:
			break;
		}
		
		return des;
	}
	
	public Layer getAspect() {
		Layer outLayer = new Layer("aspect", nRows, nCols, originX,
				originY, resolution, nullValue);
		double[][] sw = new double[3][3];
		Point ps = new Point(1,1);
		int xs, ys, xv, yv;
		double Dzx, Dzy;
		double a, b, c, d, e, f, g, h, i;
		double aspect;
		double cell;
		for (int row = 0; row < nRows; row++) { // loop nRows
			for (int col = 0; col < nCols; col++) { // loop nCols
				//create slide window values
				sw[ps.y][ps.x] = values[row][col];
				for (int k=0; k<8; k++) {
					xs = ps.x + dx[k];
					ys = ps.y + dy[k];
					xv = col + dx[k];
					yv = row + dy[k];
					if (isValidPoint(new Point(xv,yv))) {
						sw[ys][xs] = values[yv][xv];
					} else {
						sw[ys][xs] = values[row][col];
					}
				}
				// calculate aspect
				// a b c
				// d e f
				// g h i
				// [dz/dx] = ((c + 2f + i) - (a + 2d + g)) / 8
				// [dz/dy] = ((g + 2h + i) - (a + 2b + c)) / 8
				// aspect = 57.29578 * atan2 ([dz/dy], -[dz/dx])
				// if aspect < 0
				//    cell = 90.0 - aspect
				//  else if aspect > 90.0
				//    cell = 360.0 - aspect + 90.0
				//  else
				//    cell = 90.0 - aspect
				a = sw[0][0]; b = sw[0][1]; c = sw[0][2];
				d = sw[1][0]; e = sw[1][1]; f = sw[1][2];
				g = sw[1][0]; h = sw[1][1]; i = sw[1][2];
				Dzx = ((c + 2*f + i) - (a + 2*d + g)) / 8;
				Dzy = ((g + 2*h + i) - (a + 2*b + c)) / 8;
				if (Dzx==0 && Dzy==0) {
					cell = -1;
				} else {
					aspect = 57.29578 * Math.atan2 (Dzy, -Dzx);
					if (aspect < 0) {
						cell = 90.0 - aspect;
					} else if (aspect > 90.0) {
						cell = 360.0 - aspect + 90.0;
					} else {
						cell = 90.0 - aspect;
					}
				}
				
				aspect = e; // just for remove warning
				if (cell!=-1) {
					int k = 1;
					while (k<LayerProperty.numAspect) {
						if (cell>=property.aspectRange[k]) 
							k++;
						else
							break;
					}
					if (k==10) k=2;
					outLayer.values[row][col] = k - 1;
				}
			}
		}
		
		return outLayer;
	}

	public Layer getSlope() {
		Layer outLayer = new Layer("slope", nRows, nCols, originX,
				originY, resolution, nullValue);
		double[][] sw = new double[3][3];
		Point ps = new Point(1,1);
		int xs, ys, xv, yv;
		double Dzx, Dzy;
		double a, b, c, d, e, f, g, h, i;
		double slope;
		double rise_run;
		for (int row = 0; row < nRows; row++) { // loop nRows
			for (int col = 0; col < nCols; col++) { // loop nCols
				//create slide window values
				sw[ps.y][ps.x] = values[row][col];
				for (int k=0; k<8; k++) {
					xs = ps.x + dx[k];
					ys = ps.y + dy[k];
					xv = col + dx[k];
					yv = row + dy[k];
					if (isValidPoint(new Point(xv,yv))) {
						sw[ys][xs] = values[yv][xv];
					} else {
						sw[ys][xs] = values[row][col];
					}
				}
				// calculate slope
				// a b c
				// d e f
				// g h i
				// [dz/dx] = ((c + 2f + i) - (a + 2d + g) / (8 * x_cellsize)
				// x_cellsize = 5 be default
				// [dz/dy] = ((g + 2h + i) - (a + 2b + c)) / (8 * y_cellsize)
				// y_cellsize = 5 be default
				//  rise_run = sqrt([dz/dx]2 + [dz/dy]2]
				//  slope_degrees = ATAN (rise_run) * 57.29578
				a = sw[0][0]; b = sw[0][1]; c = sw[0][2];
				d = sw[1][0]; e = sw[1][1]; f = sw[1][2];
				g = sw[1][0]; h = sw[1][1]; i = sw[1][2];
				Dzx = ((c + 2*f + i) - (a + 2*d + g)) / (40);
				Dzy = ((g + 2*h + i) - (a + 2*b + c)) / (40);
				rise_run = Math.sqrt(Math.pow(Dzx,2) + Math.pow(Dzy,2));
				slope = 57.29578 * Math.atan(rise_run);
				
				rise_run = e;	// remove warning of e

				int k = 0;
				while (k<LayerProperty.numSlope) {
					if (slope>=property.slopeRange[k]) 
						k++;
					else
						break;
				}
				if (k==0) 
					k=1; 
				outLayer.values[row][col] = k - 1;
				
				//outLayer.values[row][col] = slope;	
			}
		}
		
		return outLayer;
	}


}
