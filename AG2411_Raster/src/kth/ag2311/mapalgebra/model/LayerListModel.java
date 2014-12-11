package kth.ag2311.mapalgebra.model;

import javax.swing.DefaultListModel;

public class LayerListModel extends DefaultListModel<Layer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public boolean up(int selectedIndex) {
		if (selectedIndex == 0) return false;
		Layer selectedLayer = get(selectedIndex);
		remove(selectedIndex);
		add(selectedIndex-1, selectedLayer);
		return true;
	}

	public boolean down(int selectedIndex) {
		int lastIndex = getSize() - 1;
		if (selectedIndex == lastIndex) return false;
		Layer selectedLayer = get(selectedIndex);
		remove(selectedIndex);
		add(selectedIndex+1, selectedLayer);
		return true;
	}
	
	public void save() {
		// TODO
		
	}
	
	public void load() {
		// TODO
		
	}

}
