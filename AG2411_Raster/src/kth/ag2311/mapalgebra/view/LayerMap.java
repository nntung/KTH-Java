package kth.ag2311.mapalgebra.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

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
	private BufferedImage image;
	private Layer layer;
	
	public void setLayerListModel(Layer layer) {

	}
	
	public void updateImage() {
		
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
