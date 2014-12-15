package kth.ag2311.mapalgebra.view;

import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import kth.ag2311.mapalgebra.model.LayerProperty;

public class LayerType extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	private JLabel lType;
	public void setType(int typeID) {
		switch (typeID) {
		case LayerProperty.TYPE_DEVELOPMENT:
			lType.setText("DEVELOPMENT");
			break;
		case LayerProperty.TYPE_ROAD:
			lType.setText("ROAD");
			break;
		case LayerProperty.TYPE_VEGETATION:
			lType.setText("VEGETATION");
			break;
		case LayerProperty.TYPE_HYDROLOGY:
			lType.setText("HYDROLOGY");
			break;
		case LayerProperty.TYPE_ELEVATION:
			lType.setText("ELEVATION");
			break;
		case LayerProperty.TYPE_UNDEFINED:
			lType.setText("UNDEFINED");
			break;
		}
	}
	
	public LayerType() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setBorder(new EmptyBorder(0, 5, 0, 0));
		JLabel textType = new JLabel("Type: ");
		add(textType);
		
        Font labelFont = UIManager.getFont("Label.font");
        labelFont = labelFont.deriveFont(Font.BOLD);
        labelFont = labelFont.deriveFont(14f);
        lType = new JLabel();
        lType.setFont(labelFont);
        add(lType);
        setType(LayerProperty.TYPE_UNDEFINED);
	}

}
