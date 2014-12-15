package kth.ag2311.mapalgebra.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

public class DistancePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JFormattedTextField textDistance; 
	/**
	 * Create the panel.
	 */
	public DistancePanel(int dis) {
		setLayout(new BorderLayout());
		JButton btnDecrease = new JButton(" < ");
		JButton btnIncrease= new JButton(" > ");
		textDistance = new JFormattedTextField(dis);
		textDistance.setEditable(false);
		add(btnDecrease, BorderLayout.WEST);
		add(textDistance,BorderLayout.CENTER);
		add(btnIncrease, BorderLayout.EAST);
	}

}
