package kth.ag2311.mapalgebra.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import kth.ag2311.mapalgebra.model.Layer;
import kth.ag2311.mapalgebra.model.LayerListModel;

public class LayerMap extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	private BufferedImage imageMap;
	private BufferedImage scaleMap;
	private LayerListModel layerListModel;
	private int drawX, drawY;
	private int scale;
	
	public void setLayerListModel(LayerListModel layersModel) {
		this.layerListModel = layersModel;
	}
	
	public void setDrawingPoint(int x, int y) {
		int numberOfLayers = layerListModel.getSize();
		if (numberOfLayers == 0) return;
		drawX = x;
		drawY = y;
	}
	
	public int getDrawX() {
		return drawX;
	}

	public int getDrawY() {
		return drawY;
	}
	
	public void zoomIn() {
		int numberOfLayers = layerListModel.getSize();
		if (numberOfLayers == 0) return;
		if (scale < 4) {
			scale ++;
			scaleImageMap();
		}
	}
	
	public void zoomOut() {
		int numberOfLayers = layerListModel.getSize();
		if (numberOfLayers == 0) return;
		if (scale > 1) {
			scale --;
			scaleImageMap();
		}
	} 
	
	public void renderImageMap() {
		int numberOfLayers = layerListModel.getSize();
		if (numberOfLayers == 0) return;
		
		Layer firstLayer = layerListModel.elementAt(0);
		int width = firstLayer.nCols;
		int heigth = firstLayer.nRows;
		imageMap = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gbi = imageMap.createGraphics();
		int idx = numberOfLayers - 1;
		while (idx>=0) {
			Layer layer = layerListModel.elementAt(idx);
			if (layer.isViewOnMap)
				gbi.drawImage(layer.imageMap, 0, 0, null);
			idx--;
		}
		
		if (scale>1)
			scaleImageMap();
	}
	
	public void scaleImageMap() {
        if (imageMap == null) return;
        if (scale == 1) return;
		int width = imageMap.getWidth() * scale;
		int height = imageMap.getHeight() * scale;
		scaleMap = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = scaleMap.createGraphics();
	    try {
	        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
	        //g.clearRect(0, 0, width, height);
	        g.drawImage(imageMap, 0, 0, width, height, null);
	    } finally {
	        g.dispose();
	    }
	}
	
	public LayerMap (JFrame parent) {
		imageMap = null;
		drawX = 0;
		drawY = 0;
		scale = 1;
		init(parent);
	}
	
	private void init(JFrame parent) {
		//statusbar =  parent.getStatusBar();
	}
	
    private void doDrawing(Graphics g) {
    	if (layerListModel == null)  return;
        Graphics2D g2d = (Graphics2D) g;
    	
    	int numberOfLayers = layerListModel.getSize();
        if (numberOfLayers>0 && imageMap != null) {
        	if (scale>1)
        		g2d.drawImage(scaleMap, drawX, drawY, null);        	
        	else 	
        		g2d.drawImage(imageMap, drawX, drawY, null);        	

        } else {
        	g2d.drawString("Add layer into Layer List", 50, 50);
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

}
