package kth.ag2311.mapalgebra.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import kth.ag2311.mapalgebra.model.Element;

public class ElementRenderer extends JLabel implements ListCellRenderer<Element>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static int iconWidth = 24;
	public ElementRenderer() {
		setOpaque(true); 
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Element> list,
			Element element, int index, boolean isSelected, boolean hasFocus) {
		
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        
        // set icon
        BufferedImage bufIcon = new BufferedImage(24, 24, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufIcon.createGraphics();
        g2d.setColor(new Color(element.red, element.green, element.blue));
        g2d.fillRect(0, 0, Element.iconwidth, Element.iconwidth);
        ImageIcon imageIcon = new ImageIcon(bufIcon);
        setIcon(imageIcon);
        
        // set description
		String des = Double.toString(element.value) + " = " + element.description;
        setText(des); 

        // set interest
		if (element.interest) {
            setBackground(list.getSelectionBackground());
            setForeground(new Color(250,250,250));
		} else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
		}

		return this;
	}

}
