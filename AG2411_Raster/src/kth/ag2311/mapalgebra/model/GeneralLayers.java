package kth.ag2311.mapalgebra.model;

import kth.ag2311.mapalgebra.view.LayerMap;
import kth.ag2311.mapalgebra.view.LayerPanel;

public class GeneralLayers {
	public static Layer shortestPath;
	public static Layer picnicMap;
	public static Layer aspectMap;
	public static LayerMap generalMap;
	public static Layer maskLayer;
	public static LayerPanel generalLayers;
	
	public void updatePicnicMap() {
		LayerListModel layerlist = generalLayers.getLayerListModel();
		if (layerlist == null) {
			picnicMap = null;
			return;
		}
		
		if (layerlist.isEmpty()) {
			picnicMap = null;
			return;
		}
		
		int numOfLayer = layerlist.getSize();
		for (int i=0; i<numOfLayer; i++) {
			
		}
	}
}
