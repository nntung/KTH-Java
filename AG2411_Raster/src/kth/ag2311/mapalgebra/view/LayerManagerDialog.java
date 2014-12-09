package kth.ag2311.mapalgebra.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JToolBar;

public class LayerManagerDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private JList list;

	/**
	 * Create the dialog.
	 */
	public LayerManagerDialog(final JFrame parent) {
		super(parent);
		setTitle("Layer Manager");
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
				btnLoad.setToolTipText("Load Layer Manager");
				toolBar.add(btnLoad);
			}
			{
				JButton btnSave = new JButton();
				btnSave.setIcon(iconSave);
				btnSave.setToolTipText("Save Layer Manager");
				toolBar.add(btnSave);
			}

			toolBar.add(new JSeparator(SwingConstants.VERTICAL));
			
			{
				JButton btnAdd = new JButton();
				btnAdd.setIcon(iconAdd);
				btnAdd.setToolTipText("Import a Layer from file");
				toolBar.add(btnAdd);
			}
			{
				JButton btnRemove = new JButton();
				btnRemove.setIcon(iconRemove);
				btnRemove.setToolTipText("Remove a Layer from Layer Manager");
				toolBar.add(btnRemove);
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
				list = new JList();
				list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				list.setLayoutOrientation(JList.VERTICAL);
				list.setVisibleRowCount(-1);
				
				JScrollPane listScroller = new JScrollPane(list);
				listScroller.setPreferredSize(new Dimension(250, 80));
				
				listOfLayersPane.add(list);
				listOfLayersPane.add(listScroller);
			}
			{
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
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

}
