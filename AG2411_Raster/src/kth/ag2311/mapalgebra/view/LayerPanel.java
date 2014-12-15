package kth.ag2311.mapalgebra.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import kth.ag2311.mapalgebra.model.GeneralLayers;
import kth.ag2311.mapalgebra.model.Layer;
import kth.ag2311.mapalgebra.model.LayerListModel;

public class LayerPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dataPath;
	private JList<Layer> listLayer;
	
	public Layer selectedLayer;
	private LayerListModel layerListModel;
	public LayerListModel getLayerListModel() {
		return this.layerListModel;
	}
	
	private LayerMap layerMap;
	public void setLayerMap(LayerMap layermap) {
		layerMap = layermap;
	}
	
	private JPanel propertyPanel;
	
	/**
	 * Create the panel.
	 */
	public LayerPanel(JFrame p) {
		
		init();
	}
	
	private void init() {
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		JToolBar toolBar = new JToolBar();
		add(toolBar,BorderLayout.NORTH);
		toolBar.setFloatable(false);
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		URL urlAdd = ClassLoader.getSystemResource("images/plus.png");
        ImageIcon iconAdd = new ImageIcon(urlAdd);
	    URL urlRemove = ClassLoader.getSystemResource("images/delete.png");
        ImageIcon iconRemove = new ImageIcon(urlRemove);

        URL urlUp = ClassLoader.getSystemResource("images/arrow_up.png");
        ImageIcon iconUp = new ImageIcon(urlUp);
	    URL urlDown = ClassLoader.getSystemResource("images/arrow_down.png");
        ImageIcon iconDown = new ImageIcon(urlDown);
		
		{
			JButton btnAdd = new JButton();
			btnAdd.setIcon(iconAdd);
			btnAdd.setToolTipText("Import a Layer from file");
			toolBar.add(btnAdd);
			
			btnAdd.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent event) {
	            	importLayerFromFile();
	            }
	        });
		}
		{
			JButton btnRemove = new JButton();
			btnRemove.setIcon(iconRemove);
			btnRemove.setToolTipText("Remove a layer from Layer List");
			toolBar.add(btnRemove);
			
			btnRemove.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent event) {
	        		int selectedIndex = listLayer.getSelectedIndex();
	        		if (selectedIndex<0) return;
	        		layerListModel.remove(selectedIndex);
	        		// need to render map again
	            	layerMap.renderImageMap();
					layerMap.repaint();
					// assign null to selected
					selectedLayer = null;
					// update settings panel
					changePropertyPanel();
	            }
	        });
		}
		
		{
			JButton btnUp = new JButton();
			btnUp.setIcon(iconUp);
			btnUp.setToolTipText("Move a layer UP");
			toolBar.add(btnUp);

			btnUp.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent event) {
	        		int selectedIndex = listLayer.getSelectedIndex();
	        		if (selectedIndex<0) return;
	        		boolean isDone = layerListModel.up(selectedIndex);
	            	if (isDone) listLayer.setSelectedIndex(selectedIndex - 1);
	        		// need to render map again
	            	layerMap.renderImageMap();
					layerMap.repaint();
	            }
	        });
		}
		{
			JButton btnDown = new JButton();
			btnDown.setIcon(iconDown);
			btnDown.setToolTipText("Move a layer DOWN");
			toolBar.add(btnDown);

			btnDown.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent event) {
	        		int selectedIndex = listLayer.getSelectedIndex();
	        		if (selectedIndex<0) return;
	        		boolean isDone = layerListModel.down(selectedIndex);
	        		if (isDone) listLayer.setSelectedIndex(selectedIndex + 1);
	            	// need to render map again
	        		layerMap.renderImageMap();
					layerMap.repaint();
	            }
	        });
		}
		
		Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Font titleFont = UIManager.getFont("TitledBorder.font");
        titleFont = titleFont.deriveFont(Font.BOLD);
        titleFont = titleFont.deriveFont(14f);
        
        TitledBorder titleLayers = BorderFactory.createTitledBorder(lowerEtched, "List of layers");
        titleLayers.setTitleJustification(TitledBorder.LEFT);
        titleLayers.setTitleFont( titleFont );

		JPanel listOfLayersPane = new JPanel();
		BoxLayout boxLayoutLayers = new BoxLayout(listOfLayersPane, BoxLayout.PAGE_AXIS);
		listOfLayersPane.setLayout(boxLayoutLayers);
		listOfLayersPane.setBorder(titleLayers);
		listOfLayersPane.setPreferredSize(new Dimension(200,180));
		{
			listLayer = new JList<Layer>();
			listLayer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			listLayer.setLayoutOrientation(JList.VERTICAL);
			listLayer.setVisibleRowCount(-1);
			listLayer.setCellRenderer(new LayerRenderer());
			int iconWidth = ((LayerRenderer) listLayer.getCellRenderer()).iconWidth;
			listLayer.addMouseListener(new MouseAdapter() {
				
				public void mouseReleased(MouseEvent e) {
					int index = listLayer.locationToIndex(e.getPoint());
					Layer layerItem = listLayer.getModel().getElementAt(index);
					Point p = e.getPoint();
					if (p.x < iconWidth) {
						layerItem.isViewOnMap = !layerItem.isViewOnMap;
						Rectangle rect = listLayer.getCellBounds(index, index);
						listLayer.repaint(rect);
						
						// update layerMap
						layerMap.renderImageMap();
						layerMap.repaint();
					}
					
					if (selectedLayer != layerItem) {
						//update selected layer
						selectedLayer = layerItem;
						//update mask
						selectedLayer.setMask();
						//update property panel
						changePropertyPanel();
					}
				}
			});
			
			layerListModel = new LayerListModel();
			listLayer.setModel(layerListModel);
			
			JScrollPane listScroller = new JScrollPane(listLayer);
			listOfLayersPane.add(listScroller);

		}

        TitledBorder titleProperty = BorderFactory.createTitledBorder(lowerEtched, "Property of a layer");
        titleProperty.setTitleJustification(TitledBorder.LEFT);
        titleProperty.setTitleFont( titleFont );
        
        JPanel propertyOfLayerPane = new JPanel();
        propertyOfLayerPane.setLayout(new BorderLayout());
        propertyOfLayerPane.setBorder(titleProperty);
		
		propertyPanel = new JPanel();
		propertyPanel.setLayout(new CardLayout());
		propertyPanel.add(new JPanel(), "blank");
		propertyOfLayerPane.add(propertyPanel, BorderLayout.CENTER);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	    splitPane.setTopComponent(listOfLayersPane);
	    splitPane.setBottomComponent(propertyOfLayerPane);
	    add(splitPane, BorderLayout.CENTER);
	}

	private void importLayerFromFile() {
		JFileChooser fileopen = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("Raster ASCII files", "txt");
        fileopen.addChoosableFileFilter(filter);
        fileopen.setFileFilter(filter);
        if (dataPath!=null && !dataPath.isEmpty())
        	fileopen.setCurrentDirectory(new File(dataPath));

        int ret = fileopen.showDialog(this, "Import");

        if (ret == JFileChooser.APPROVE_OPTION) {
        	File selectedFile = fileopen.getSelectedFile();
        	dataPath = selectedFile.getParent();
        	importLayer(selectedFile);
        }
	}
	
	public void importLayer(File file) {
		String name = file.getName();
		int pos = name.lastIndexOf(".");
		name = (pos > 0) ? name.substring(0, pos) : name;
		String path = file.getAbsolutePath();
		
    	Layer layer = new Layer(name, path);
    	if (layer.isImportSucessful) {
    		layer.name = layer.name + " (" + layer.nCols + "x" + layer.nRows + ")";
    		layer.isViewOnMap = true;
    		layer.loadProperty();
    		layer.renderMap();
    		layer.createLayerMask();
    		layer.renderMaskOfInterest();
    		
    		//add to list model
    		layerListModel.addElement(layer);
    		//add to property panel cards
    		propertyPanel.add(layer.propertyPanel, layer.name);
    		
    		GeneralLayers.generalLayer.renderImageMap();
    		GeneralLayers.generalLayer.repaint();
    		
    	} else {
    		String msg = "ERROR! Cannot import layer from " + path;
    		JOptionPane.showMessageDialog(this, msg, "Error Message", JOptionPane.ERROR_MESSAGE);
    	}
		
	}

	private void changePropertyPanel() {
		if (selectedLayer == null) {
			
		} else {
			CardLayout cl = (CardLayout)(propertyPanel.getLayout());
		    cl.show(propertyPanel, selectedLayer.name);
		}
	}
	
	public void updateSelection(int xx, int yy) {
		if (selectedLayer == null) {
			
		} else {
			boolean isUpdate = selectedLayer.updateSelection(xx,yy);
			if (isUpdate) {
				layerMap.renderImageMap();
				layerMap.repaint();
			}
		}
	}


}
