package kth.ag2311.mapalgebra.view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import kth.ag2311.mapalgebra.model.Layer;

public class PicnicGIS {

	private JFrame appFrame;
	private LayerList layerList;
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
		appFrame.setMinimumSize(new Dimension(640, 480));
		appFrame.setBounds(100, 100, 800, 600);
		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		layerList = new LayerList(appFrame);
		
		// set application icon
	    URL urlIcon = ClassLoader.getSystemResource("images/picnic.png");
	    ImageIcon icon = new ImageIcon (urlIcon);
	    
	    try {
	    	appFrame.getContentPane().setLayout(new BorderLayout());
	    	appFrame.setIconImage(icon.getImage());
	    	
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
				JToggleButton btnMouseSelect = new JToggleButton();
				btnMouseSelect.setIcon(iconMouseSelect);
				btnMouseSelect.setToolTipText("Select on map");
				toolBar.add(btnMouseSelect);
			}
			{
				JToggleButton btnMouseMove = new JToggleButton();
				btnMouseMove.setIcon(iconMouseMove);
				btnMouseMove.setToolTipText("Move map");
				toolBar.add(btnMouseMove);
			}

			toolBar.add(new JSeparator(SwingConstants.VERTICAL));
			
			{
				JToggleButton btnZoomIn = new JToggleButton();
				btnZoomIn.setIcon(iconZomeIn);
				btnZoomIn.setToolTipText("Zoom In");
				toolBar.add(btnZoomIn);
			}
			{
				JToggleButton btnZoomOut = new JToggleButton();
				btnZoomOut.setIcon(iconZoomOut);
				btnZoomOut.setToolTipText("Zoom Out");
				toolBar.add(btnZoomOut);
			}
			
			toolBar.add(new JSeparator(SwingConstants.VERTICAL));
			
			{
				JButton btnCentralize = new JButton();
				btnCentralize.setIcon(iconCenteralize);
				btnCentralize.setToolTipText("Go to Central of a map");
				toolBar.add(btnCentralize);
			}
		
	    	
	    	layerMap = new LayerMap(appFrame);
	    	appFrame.getContentPane().add(layerMap, BorderLayout.CENTER);
	    	//layerMap.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    	/*Toolkit toolkit = layerMap.getToolkit();
	    	Image image = toolkit.getImage("images/pencil.gif");
	    	Cursor cc = toolkit.createCustomCursor(image, new Point(0,0), "img");
	    	layerMap.setCursor(cc);*/
	    	  
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
	    		    	
	    	JMenu mnView = new JMenu("View");
	    	menuBar.add(mnView);
	    	
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
	              if (layerList.isVisible()) {
	            	  layerList.setVisible(false);
	              } else {
	            	  layerList.setVisible(true);
	              }
	            }
	        });
	        mnView.add(mntmcbLayerManager);
	        
	        layerList.addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosing(WindowEvent e) {
	            	mntmcbLayerManager.setState(false);
	            }
	        });
	        
	        layerList.setVisible(true);
	        
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    }
		
	}
	
}
