package kth.ag2311.mapalgebra.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
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
	private LayerListModel layerListModel;
	private int drawX, drawY;
	
	public void setLayerListModel(LayerListModel layersModel) {
		this.layerListModel = layersModel;
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
	}
	
	public void scaleImageMap() {
		
	}
	
	public LayerMap (JFrame parent) {
		imageMap = null;
		drawX = 0;
		drawY = 0;
		init(parent);
	}
	
	private void init(JFrame parent) {
		//statusbar =  parent.getStatusBar();
	}
	
    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        if (imageMap != null) {
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
