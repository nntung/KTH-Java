package kth.ag2311.mapalgebra.view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.io.File;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import kth.ag2311.mapalgebra.model.GeneralLayers;
import kth.ag2311.mapalgebra.model.LayerListModel;
import kth.ag2311.mapalgebra.model.LayerManager;

public class PicnicGIS {

	private JFrame appFrame;
	private LayerPanel layerPane;
	private LayerMap layerMap;
	private JPanel statusPanel;
	private JPanel statusXYZoom;
	private JLabel statusText;
	private JLabel statusZoom;
	private JLabel statusX;
	private JLabel statusY;
	
	private boolean isMouseSelect;
	private boolean isReadyToMove;
	private int disX, disY;
	private LayerManager layerManager;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PicnicGIS window = new PicnicGIS();
					window.appFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PicnicGIS() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		appFrame = new JFrame();
		appFrame.setMinimumSize(new Dimension(640, 480));
		appFrame.setBounds(100, 100, 800, 800);
		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// set application icon
	    URL urlIcon = ClassLoader.getSystemResource("images/picnic.png");
	    ImageIcon icon = new ImageIcon (urlIcon);
	    
	    try {
	    	appFrame.getContentPane().setLayout(new BorderLayout());
	    	appFrame.setIconImage(icon.getImage());
	    	appFrame.setTitle("PicnicGIS");
	   
	    	JToolBar toolBar = new JToolBar();
			toolBar.setFloatable(false);
			toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
			
		    URL urlMouseSelect = ClassLoader.getSystemResource("images/cursor.png");
	        ImageIcon iconMouseSelect = new ImageIcon(urlMouseSelect);
		    URL urlMouseMove = ClassLoader.getSystemResource("images/hand.png");
	        ImageIcon iconMouseMove = new ImageIcon(urlMouseMove);
	        
		    URL urlZoomIn = ClassLoader.getSystemResource("images/zoom_in.png");
	        ImageIcon iconZomeIn = new ImageIcon(urlZoomIn);
		    URL urlZoomOut = ClassLoader.getSystemResource("images/zoom_out.png");
	        ImageIcon iconZoomOut = new ImageIcon(urlZoomOut);
	        
		    URL urlCenteralize = ClassLoader.getSystemResource("images/centerlize.png");
	        ImageIcon iconCenteralize = new ImageIcon(urlCenteralize);

	        appFrame.getContentPane().add(toolBar,BorderLayout.NORTH);
			{
				URL urlLoad = ClassLoader.getSystemResource("images/folder.png");
		        ImageIcon iconLoad = new ImageIcon(urlLoad);
			    URL urlSave = ClassLoader.getSystemResource("images/save_floppy_disk.png");
		        ImageIcon iconSave = new ImageIcon(urlSave);
				
		        {
					JButton btnLoad = new JButton();
					btnLoad.setIcon(iconLoad);
					btnLoad.setToolTipText("Load Layer List");
					toolBar.add(btnLoad);
					btnLoad.addActionListener(new ActionListener() {
			    		public void actionPerformed(ActionEvent e) {
			    			loadLayerManager();
			    		}
			    	});
				}
				{
					JButton btnSave = new JButton();
					btnSave.setIcon(iconSave);
					btnSave.setToolTipText("Save Layer List");
					toolBar.add(btnSave);
					btnSave.addActionListener(new ActionListener() {
			    		public void actionPerformed(ActionEvent e) {
			    			saveLayerManager();
			    		}
			    	});
				}
				
				toolBar.add(new JSeparator(SwingConstants.VERTICAL));
				
				JToggleButton btnMouseSelect = new JToggleButton();
				JToggleButton btnMouseMove = new JToggleButton();

				isMouseSelect = true;
				isReadyToMove = false;
				
				btnMouseSelect.setIcon(iconMouseSelect);
				btnMouseSelect.setToolTipText("Select on map");
				toolBar.add(btnMouseSelect);
				btnMouseSelect.setSelected(true);
				btnMouseSelect.addActionListener(new ActionListener() {
		    		public void actionPerformed(ActionEvent e) {
		    			btnMouseSelect.setSelected(true);
						btnMouseMove.setSelected(false);
						isMouseSelect = true;
						layerMap.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		    		}
		    	});
				
				btnMouseMove.setIcon(iconMouseMove);
				btnMouseMove.setToolTipText("Move map");
				toolBar.add(btnMouseMove);
				btnMouseMove.setSelected(false);
				btnMouseMove.addActionListener(new ActionListener() {
		    		public void actionPerformed(ActionEvent e) {
		    			btnMouseSelect.setSelected(false);
						btnMouseMove.setSelected(true);
						isMouseSelect = false;
						layerMap.setCursor(new Cursor(Cursor.HAND_CURSOR));
		    		}
		    	});

			}

			toolBar.add(new JSeparator(SwingConstants.VERTICAL));
			
			{
				JButton btnZoomIn = new JButton();
				btnZoomIn.setIcon(iconZomeIn);
				btnZoomIn.setToolTipText("Zoom In");
				toolBar.add(btnZoomIn);
				btnZoomIn.addActionListener(new ActionListener() {
		    		public void actionPerformed(ActionEvent e) {
		    			layerMap.zoomIn();
		    			layerMap.repaint();
		    			
		    			// update zoom status
		    			int zoom = layerMap.scale * 100;
		    			statusZoom.setText("Zoom: " + zoom + "%");
		    		}
		    	});
			}
			{
				JButton btnZoomOut = new JButton();
				btnZoomOut.setIcon(iconZoomOut);
				btnZoomOut.setToolTipText("Zoom Out");
				toolBar.add(btnZoomOut);
				btnZoomOut.addActionListener(new ActionListener() {
		    		public void actionPerformed(ActionEvent e) {
		    			layerMap.zoomOut();
		    			layerMap.repaint();

		    			// update zoom status
		    			int zoom = layerMap.scale * 100;
		    			statusZoom.setText("Zoom: " + zoom + "%");
		    		}
		    	});
			}
			
			toolBar.add(new JSeparator(SwingConstants.VERTICAL));
			
			URL urlPinicMap = ClassLoader.getSystemResource("images/picnicmap.png");
	        ImageIcon iconPicnicMap = new ImageIcon(urlPinicMap);
		    URL urlShortestPath = ClassLoader.getSystemResource("images/shortest.png");
	        ImageIcon iconShortestPath = new ImageIcon(urlShortestPath);
	        
	        {
				JToggleButton btnPicnicMap = new JToggleButton();
				btnPicnicMap.setIcon(iconPicnicMap);
				btnPicnicMap.setToolTipText("Show picnic map");
				toolBar.add(btnPicnicMap);
				btnPicnicMap.addActionListener(new ActionListener() {
		    		public void actionPerformed(ActionEvent e) {
		    			//TODO Show Picnic map
		    		}
		    	});
			}
			{
				JToggleButton btnShortestMap = new JToggleButton();
				btnShortestMap.setIcon(iconShortestPath);
				btnShortestMap.setToolTipText("Show shortest path");
				toolBar.add(btnShortestMap);
				btnShortestMap.addActionListener(new ActionListener() {
		    		public void actionPerformed(ActionEvent e) {
		    			if (btnShortestMap.isSelected()) {
		    				layerMap.showPath = true;
		    				layerMap.renderImageMap();
		    				layerMap.repaint();
		    			} else {
		    				layerMap.showPath = false;
		    				layerMap.renderImageMap();
		    				layerMap.repaint();
		    			}
		    		}
		    	});
			}
			
			{
				JButton btnCentralize = new JButton();
				btnCentralize.setIcon(iconCenteralize);
				btnCentralize.setToolTipText("Go to Central of a map");
				//toolBar.add(btnCentralize);
				btnCentralize.addActionListener(new ActionListener() {
		    		public void actionPerformed(ActionEvent e) {
		    			layerMap.setDrawingPoint(0, 0);
		    			layerMap.repaint();
		    		}
		    	});
			}
		
	    	
	    	layerMap = new LayerMap(appFrame);
	    	appFrame.getContentPane().add(layerMap, BorderLayout.CENTER);
	    	settingMouseEventToLayerMap();
	    	GeneralLayers.generalMap = layerMap;
	    	
	    	JMenuBar menuBar = new JMenuBar();
	    	appFrame.setJMenuBar(menuBar);
	    	
	    	JMenu mnApp = new JMenu("App");
	    	menuBar.add(mnApp);
	    	
	    	JMenuItem mntmLoadMap = new JMenuItem("Load Map");
	    	mntmLoadMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
	    	mntmLoadMap.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			loadLayerManager();
	    		}
	    	});
	    	mnApp.add(mntmLoadMap);
	    	
	    	JMenuItem mntmSaveMap = new JMenuItem("Save Map");
	    	mntmSaveMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
	    	mntmSaveMap.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			saveLayerManager();
	    		}
	    	});
	    	mnApp.add(mntmSaveMap);

	    	mnApp.add(new JSeparator());
	    	
	    	JMenuItem mntmExit = new JMenuItem("Exit");
	    	mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
	    	mntmExit.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			System.exit(0);
	    		}
	    	});
	    	mnApp.add(mntmExit);
	    	
	    	JMenu mnView = new JMenu("View");
	    	menuBar.add(mnView);
	    	
	    	menuBar.add(Box.createHorizontalGlue());
	    	
	    	JMenu mnHelp = new JMenu("Help");
	    	menuBar.add(mnHelp);
	    	
	    	JMenuItem mntmAbout = new JMenuItem("About");
	    	mntmAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.CTRL_MASK));
	    	mntmAbout.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			//TODO about dialog
	    		}
	    	});
	    	mnHelp.add(mntmAbout);
	    	
	    	statusPanel = new JPanel();
	    	appFrame.getContentPane().add(statusPanel, BorderLayout.SOUTH);
	    	statusPanel.setLayout(new BorderLayout());
	    	statusPanel.setVisible(true);
	    	
	    	statusXYZoom = new JPanel();
	    	statusXYZoom.setLayout(new BoxLayout(statusXYZoom, BoxLayout.LINE_AXIS));
	    	statusXYZoom.setVisible(true);
	    	statusPanel.add(statusXYZoom, BorderLayout.WEST);
	    	
	    	statusX = new JLabel("X = ");
	    	statusX.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	    	statusX.setVisible(true);
	    	statusXYZoom.add(statusX);
	    	statusXYZoom.add(Box.createHorizontalStrut(5));
	    	statusY = new JLabel("Y = ");
	    	statusY.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	    	statusY.setVisible(true);
	    	statusXYZoom.add(statusY);
	    	statusXYZoom.add(Box.createHorizontalStrut(5));
	    	statusZoom = new JLabel("Zoom: 100%");
	    	statusZoom.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	    	statusZoom.setVisible(true);
	    	statusXYZoom.add(statusZoom);
	    	statusXYZoom.add(Box.createHorizontalStrut(5));
	    	
	    	statusText = new JLabel("Ready");
	    	statusText.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	    	statusText.setVisible(true);
	    	statusPanel.add(statusText, BorderLayout.CENTER);
	    	
	        JCheckBoxMenuItem mntmcbStatusBar = new JCheckBoxMenuItem("show Status Bar");
	        mntmcbStatusBar.setState(true);

	        mntmcbStatusBar.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent event) {
	              if (statusPanel.isVisible()) {
	            	  statusPanel.setVisible(false);
	              } else {
	            	  statusPanel.setVisible(true);
	              }
	            }
	        });
	    	mnView.add(mntmcbStatusBar);
	        
	        JCheckBoxMenuItem mntmcbLayerManager = new JCheckBoxMenuItem("show Layer Manager");
	        mntmcbLayerManager.setState(true);

	        mntmcbLayerManager.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent event) {
	              if (layerPane.isVisible()) {
	            	  layerPane.setVisible(false);
	              } else {
	            	  layerPane.setVisible(true);
	              }
	            }
	        });
	        mnView.add(mntmcbLayerManager);
	        
	        layerPane = new LayerPanel(appFrame);
	        appFrame.getContentPane().add(layerPane, BorderLayout.EAST);
	        layerPane.setVisible(true);
	        layerPane.setLayerMap(layerMap);
	        layerMap.setLayerListModel(layerPane.getLayerListModel());
	        GeneralLayers.generalLayers = layerPane;
	        
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    }
		
	}
	
	private void settingMouseEventToLayerMap() {
		
		layerMap.addMouseListener(new MouseListener() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        if (isMouseSelect) {
		        	int pX = (e.getX() - layerMap.getDrawX()) / layerMap.scale;
		        	int pY = (e.getY() - layerMap.getDrawY()) / layerMap.scale;
					layerPane.updateSelection(pX, pY);
					statusX.setText("X = " + pX);
					statusY.setText("Y = " + pY);
					if (layerPane.selectedLayer!=null) {
						statusText.setText(layerPane.selectedLayer.getDescription(pX, pY));
					}
		        }
		    }

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
		        if (!isMouseSelect) {
					disX = e.getX() - layerMap.getDrawX();
			        disY = e.getY() - layerMap.getDrawY();

			        isReadyToMove = true;
		        	layerMap.setCursor(new Cursor(Cursor.MOVE_CURSOR));
		        }
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
		        if (!isMouseSelect) {
		        	isReadyToMove = false;
		        	layerMap.setCursor(new Cursor(Cursor.HAND_CURSOR));
		        }
			}
		});
		
		layerMap.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {

		        if (!isMouseSelect) {
					if (isReadyToMove) {
						int x = e.getX() - disX;
				        int y = e.getY() - disY;
			        	layerMap.setDrawingPoint(x, y);
			        	layerMap.repaint();
		        	}
		        }
				
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				
		        if (isMouseSelect) {
		        	if (layerPane.selectedLayer != null && layerPane.selectedLayer.isViewOnMap) {
			        	int pX = (e.getX() - layerMap.getDrawX()) / layerMap.scale;
			        	int pY = (e.getY() - layerMap.getDrawY()) / layerMap.scale;
						statusX.setText("X = " + pX);
						statusY.setText("Y = " + pY);
						statusText.setText(layerPane.selectedLayer.getDescription(pX, pY));
		        	}
		        }

			}
			
		});
	}
	
	private void loadLayerManager() {
		JFileChooser openFile = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("Layer manager file", "lym");
		openFile.addChoosableFileFilter(filter);
		openFile.setFileFilter(filter);
		int ret = openFile.showDialog(appFrame, "Load");
        if (ret == JFileChooser.APPROVE_OPTION) {
        	File selectedFile = openFile.getSelectedFile();
        	layerManager = new LayerManager(selectedFile.getAbsolutePath());
        	// TODO layerManager.load(layerlist);
        }

	}
	
	private void saveLayerManager() {
		if (layerManager==null) {
			saveLayerManagerAs();
		} else {
			LayerListModel layerlist = layerPane.getLayerListModel();
			if (layerlist == null) return;
			if (layerlist.isEmpty()) return;

			layerManager.save(layerlist);
		}
	}
	
	private void saveLayerManagerAs() {
		LayerListModel layerlist = layerPane.getLayerListModel();
		if (layerlist == null) return;
		if (layerlist.isEmpty()) return;
		
		JFileChooser saveFile = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("Layer manager file", "lym");
		saveFile.addChoosableFileFilter(filter);
		saveFile.setFileFilter(filter);

		int ret = saveFile.showDialog(appFrame, "Save");
        if (ret == JFileChooser.APPROVE_OPTION) {
        	File selectedFile = saveFile.getSelectedFile();
        	layerManager = new LayerManager(selectedFile.getAbsolutePath());
        	layerManager.save(layerlist);
        }
	}
}
