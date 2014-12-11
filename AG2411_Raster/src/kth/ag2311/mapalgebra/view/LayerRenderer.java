package kth.ag2311.mapalgebra.view;

import java.awt.Component;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import kth.ag2311.mapalgebra.model.Layer;

public class LayerRenderer extends JLabel implements ListCellRenderer<Layer>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImageIcon iconEyeView;
	private ImageIcon iconEyeClose;
	public int iconWidth;
	public LayerRenderer() {
		URL urlEyeView = ClassLoader.getSystemResource("images/eye_view.png");
        iconEyeView = new ImageIcon(urlEyeView);
	    URL urlEyeClose = ClassLoader.getSystemResource("images/eye_close.png");
        iconEyeClose = new ImageIcon(urlEyeClose);
        iconWidth = iconEyeView.getIconWidth();
		setOpaque(true); 
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Layer> list,
			Layer layer, int index, boolean isSelected, boolean hasFocus) {
		
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        
		if (layer.isViewOnMap) {
	        setIcon(iconEyeView);
		} else {
	        setIcon(iconEyeClose);
		}
        setText(layer.name); 
		
		return this;
	}

}
