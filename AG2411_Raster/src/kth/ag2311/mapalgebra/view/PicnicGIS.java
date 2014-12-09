package kth.ag2311.mapalgebra.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import kth.ag2311.mapalgebra.model.Layer;
import kth.ag2311.mapalgebra.model.LayerManager;

public class PicnicGIS {

	private JFrame appFrame;
	private LayerManagerDialog layerManagerDlg;
	private LayerMap layerMap;
	private JLabel statusbar;
	
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
		appFrame.setBounds(100, 100, 640, 480);
		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
		layerManagerDlg = new LayerManagerDialog(appFrame);
		
		// set application icon
	    URL urlIcon = ClassLoader.getSystemResource("images/picnic.png");
	    ImageIcon icon = new ImageIcon (urlIcon);
	    
	    try {
	    	appFrame.getContentPane().setLayout(new BorderLayout());
	    	appFrame.setIconImage(icon.getImage());
	    	
	    	layerMap = new LayerMap(appFrame);
	    	appFrame.getContentPane().add(layerMap, BorderLayout.CENTER);
	    	
	    	JMenuBar menuBar = new JMenuBar();
	    	appFrame.setJMenuBar(menuBar);
	    	
	    	JMenu mnApp = new JMenu("App");
	    	menuBar.add(mnApp);
	    	
	    	JMenuItem mntmExit = new JMenuItem("Exit");
	    	mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
	    	mntmExit.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			System.exit(0);
	    		}
	    	});
	    	mnApp.add(mntmExit);
	    	
	    	JMenu mnLayer = new JMenu("Layer");
	    	menuBar.add(mnLayer);
	    	
	    	JMenu mnView = new JMenu("View");
	    	menuBar.add(mnView);
	    	
	    	JMenuItem mntmLoad = new JMenuItem("Load");
	    	mntmLoad.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			JFileChooser fileopen = new JFileChooser();
	                FileFilter filter = new FileNameExtensionFilter("raster ASCII files", "txt");
	                fileopen.addChoosableFileFilter(filter);

	                int ret = fileopen.showDialog(appFrame, "Open raster file");

	                if (ret == JFileChooser.APPROVE_OPTION) {
	                	File selectedFile = fileopen.getSelectedFile();
	                	// TODO: Loading file!
	                	loadLayer(selectedFile);
	                }
	    		}
	    	});
	    	mntmLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
	    	mnLayer.add(mntmLoad);
	    	
	    	menuBar.add(Box.createHorizontalGlue());
	    	
	    	JMenu mnHelp = new JMenu("Help");
	    	menuBar.add(mnHelp);
	    	
	    	statusbar = new JLabel("Statusbar");
	    	statusbar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	    	statusbar.setVisible(true);
	    	appFrame.getContentPane().add(statusbar, BorderLayout.SOUTH);
	    	
	        JCheckBoxMenuItem mntmcbStatusBar = new JCheckBoxMenuItem("show Status Bar");
	        mntmcbStatusBar.setState(true);

	        mntmcbStatusBar.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent event) {
	              if (statusbar.isVisible()) {
	                  statusbar.setVisible(false);
	              } else {
	                  statusbar.setVisible(true);
	              }
	            }
	        });
	    	mnView.add(mntmcbStatusBar);
	        
	        JCheckBoxMenuItem mntmcbLayerManager = new JCheckBoxMenuItem("show Layer Manager");
	        mntmcbLayerManager.setState(true);

	        mntmcbLayerManager.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent event) {
	              if (layerManagerDlg.isVisible()) {
	            	  layerManagerDlg.setVisible(false);
	              } else {
	            	  layerManagerDlg.setVisible(true);
	              }
	            }
	        });
	        mnView.add(mntmcbLayerManager);
	        
	        layerManagerDlg.addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosing(WindowEvent e) {
	            	mntmcbLayerManager.setState(false);
	            }
	        });
	        
	        layerManagerDlg.setVisible(true);
	        
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    }
		
	}
	
	private void loadLayer(File file) {
		String name = file.getName();
		int pos = name.lastIndexOf(".");
		name = (pos > 0) ? name.substring(0, pos) : name;
		String path = file.getAbsolutePath();

		// update GUI Loading ..
		statusbar.setText("Loading .. " + path);

		// perform loading
		Layer layer = new Layer(name, path);
		LayerManager.add(layer);
		
		// update GUI Loaded ..
		statusbar.setText("Loaded .. " + path);
	}

}
