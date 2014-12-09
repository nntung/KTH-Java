package kth.ag2311.mapalgebra.view;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import kth.ag2311.mapalgebra.model.Layer;

public class LayerMap extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	private Layer layer;
	
	public void setLayer(Layer layer) {
		this.layer = layer;
	}
	
	public LayerMap (JFrame parent) {
		init(parent);
	}
	
	private void init(JFrame parent) {
		//statusbar =  parent.getStatusBar();
	}
	
    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        if (layer != null) {
        	for (int i = 0; i < layer.nRows; i++) { // loop nRows
    			for (int j = 0; j < layer.nCols; j++) { // loop nCols
    				// create color for this point
    				if (layer.values[i][j] == layer.nullValue) {
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

        } else {
        	g2d.drawString("Java 2D", 50, 50);
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

}
