package kth.ag2311.mapalgebra.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JToolBar;

import kth.ag2311.mapalgebra.model.Layer;
import kth.ag2311.mapalgebra.model.LayerListModel;

public class LayerList extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private LayerListModel layerListModel;
	private JList<Layer> listLayer;

	/**
	 * Create the dialog.
	 */
	public LayerList(final JFrame parent) {
		super(parent);
		setTitle("Layer List");
		setMinimumSize(new Dimension(300, 300));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModalityType(JDialog.ModalityType.MODELESS);
        
		getContentPane().setLayout(new BorderLayout());

		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		{
			JToolBar toolBar = new JToolBar();
			toolBar.setFloatable(false);
			
		    URL urlLoad = ClassLoader.getSystemResource("images/folder.png");
	        ImageIcon iconLoad = new ImageIcon(urlLoad);
		    URL urlSave = ClassLoader.getSystemResource("images/save_floppy_disk.png");
	        ImageIcon iconSave = new ImageIcon(urlSave);
	        
		    URL urlAdd = ClassLoader.getSystemResource("images/plus.png");
	        ImageIcon iconAdd = new ImageIcon(urlAdd);
		    URL urlRemove = ClassLoader.getSystemResource("images/delete.png");
	        ImageIcon iconRemove = new ImageIcon(urlRemove);

	        URL urlUp = ClassLoader.getSystemResource("images/arrow_up.png");
	        ImageIcon iconUp = new ImageIcon(urlUp);
		    URL urlDown = ClassLoader.getSystemResource("images/arrow_down.png");
	        ImageIcon iconDown = new ImageIcon(urlDown);

	        contentPanel.add(toolBar,BorderLayout.NORTH);
			{
				JButton btnLoad = new JButton();
				btnLoad.setIcon(iconLoad);
				btnLoad.setToolTipText("Load Layer List");
				toolBar.add(btnLoad);
			}
			{
				JButton btnSave = new JButton();
				btnSave.setIcon(iconSave);
				btnSave.setToolTipText("Save Layer List");
				toolBar.add(btnSave);
			}

			toolBar.add(new JSeparator(SwingConstants.VERTICAL));
			
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
		            	removeLayerFromList();
		            }
		        });
			}
			
			toolBar.add(new JSeparator(SwingConstants.VERTICAL));
			
			{
				JButton btnUp = new JButton();
				btnUp.setIcon(iconUp);
				btnUp.setToolTipText("Move a layer UP");
				toolBar.add(btnUp);
			}
			{
				JButton btnDown = new JButton();
				btnDown.setIcon(iconDown);
				btnDown.setToolTipText("Move a layer DOWN");
				toolBar.add(btnDown);
			}
		}
		{
			JPanel listOfLayersPane = new JPanel();
			BoxLayout boxLayout = new BoxLayout(listOfLayersPane, BoxLayout.PAGE_AXIS);
			listOfLayersPane.setLayout(boxLayout);
			listOfLayersPane.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
			getContentPane().add(listOfLayersPane, BorderLayout.CENTER);
			{
				listLayer = new JList<Layer>();
				listLayer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				listLayer.setLayoutOrientation(JList.VERTICAL);
				listLayer.setVisibleRowCount(-1);
				listLayer.setCellRenderer(new LayerRenderer());
				int iconWidth = ((LayerRenderer) listLayer.getCellRenderer()).iconWidth;
				listLayer.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						int index = listLayer.locationToIndex(e.getPoint());
						Point p = e.getPoint();
						if (p.x<iconWidth) {
							Layer item = listLayer.getModel().getElementAt(index);
							item.isViewOnMap = !item.isViewOnMap;
							Rectangle rect = listLayer.getCellBounds(index, index);
							listLayer.repaint(rect);
						}
					}
				});

				JScrollPane listScroller = new JScrollPane(listLayer);
				
				listOfLayersPane.add(listScroller);

				layerListModel = new LayerListModel();
				listLayer.setModel(layerListModel);
			}
			{
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			//getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	private void updateToolbarButtons() {
		
	}
	
	private void importLayerFromFile() {
		JFileChooser fileopen = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("raster ASCII files", "txt");
        fileopen.addChoosableFileFilter(filter);
        fileopen.setCurrentDirectory(new File("c:/DataJava"));

        int ret = fileopen.showDialog(this, "Open raster file");

        if (ret == JFileChooser.APPROVE_OPTION) {
        	File selectedFile = fileopen.getSelectedFile();
        	
    		String name = selectedFile.getName();
    		int pos = name.lastIndexOf(".");
    		name = (pos > 0) ? name.substring(0, pos) : name;
    		String path = selectedFile.getAbsolutePath();
    		
        	Layer layer = new Layer(name, path);
        	if (layer.isImportSucessful) {
        		layerListModel.addElement(layer);
        	} else {
        		String msg = "ERROR! Cannot import layer from " + path;
        		JOptionPane.showMessageDialog(this, msg, "Error Message", JOptionPane.ERROR_MESSAGE);
        	}
        }
        
        updateToolbarButtons();
	}
	
	private void removeLayerFromList() {
		// get selected index
		int selectedIndex = listLayer.getSelectedIndex();
		if (selectedIndex<0) return;
		layerListModel.remove(selectedIndex);
		
		// need to render again ?
	}

}
