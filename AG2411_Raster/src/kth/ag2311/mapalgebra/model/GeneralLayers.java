package kth.ag2311.mapalgebra.model;

import kth.ag2311.mapalgebra.view.LayerMap;
import kth.ag2311.mapalgebra.view.LayerPanel;

public class GeneralLayers {
	public static Layer shortestPath;
	public static Layer picnicMap;
	public static LayerMap generalMap;
	public static Layer maskLayer;
	public static LayerPanel generalLayers;
	
	public static void updatePicnicMap() {
		LayerListModel layerlist = generalLayers.getLayerListModel();
		if (layerlist == null) {
			picnicMap = null;
			return;
		}
		
		if (layerlist.isEmpty()) {
			picnicMap = null;
			return;
		}
		
		Layer layer = layerlist.get(0);
		picnicMap = new Layer("picnicMask", layer.nRows, layer.nCols, layer.originX,
				layer.originY, layer.resolution, 0);
		picnicMap.getValues(layer.layerMask);
		
		int numOfLayer = layerlist.getSize();
		for (int i=1; i<numOfLayer; i++) {
			layer = layerlist.get(i);
			if (layer.isAddToPicnicMap) {
				picnicMap.classify(layer.layerMask);
			}
		}
		
		picnicMap.property = new LayerProperty();
		picnicMap.property.type = LayerProperty.TYPE_PICNIC;
		picnicMap.renderMap();
	}
}
