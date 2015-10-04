package main;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.AbstractButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.JMenuBar;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JFrame;

/**
 * Interactive photo browser, or Photothèque. 
 * The final version will allow the user to view, 
 * annotate, and organize a collection of photos.
 */
public class Phototheque extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
    /*
     * Menu components.
     */
    private JMenuBar menuBar;
    private JMenu menu;
    private JRadioButtonMenuItem rbMenuItem;
    private JMenuItem openMenuItem;
    private JMenuItem deleteMenuItem;
    private JMenuItem quitMenuItem;

	/*
	 * Other components
	 */
	private JLabel status;
    private JFileChooser fc;
    
    /**
     * Creates the menu bar menuBar, adds operations and returns it.
     * @return JMenuBar menuBar.
     */
    public JMenuBar createMenuBar() {
        menuBar = new JMenuBar();

        /* 
         * MENU 1: FILE  
         */
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
                "All operations dealing with photo files");
        menuBar.add(menu);

        //Operations in the File menu are added.
        ImageIcon openIcon = createImageIcon("/images/openIcon.png");
        openMenuItem = new JMenuItem("Open", openIcon);
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        openMenuItem.addActionListener(this);
        menu.add(openMenuItem);

        ImageIcon deleteIcon = createImageIcon("/images/deleteIcon.png");
        deleteMenuItem = new JMenuItem("Delete", deleteIcon);
        deleteMenuItem.setMnemonic(KeyEvent.VK_DELETE);
        deleteMenuItem.addActionListener(this);
        menu.add(deleteMenuItem);
        
        ImageIcon quitIcon = createImageIcon("/images/quitIcon.png"); 
        quitMenuItem = new JMenuItem("Quit", quitIcon);
        quitMenuItem.addActionListener(this);
        menu.add(quitMenuItem);

        /* 
         * MENU 2: VIEW  
         */
        menu = new JMenu("View");
        menu.setMnemonic(KeyEvent.VK_N);
        menu.getAccessibleContext().setAccessibleDescription(
                "All options dealing with the view");
        menuBar.add(menu);
        
        //Operations in the View menu are added.
        ButtonGroup group = new ButtonGroup();
   
        rbMenuItem = new JRadioButtonMenuItem("Photo viewer");
        //Is set to default selected.
        rbMenuItem.setSelected(true); 
        rbMenuItem.setMnemonic(KeyEvent.VK_P);
        group.add(rbMenuItem);
        rbMenuItem.addActionListener(this);
        menu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Browser");
        rbMenuItem.setMnemonic(KeyEvent.VK_B);
        group.add(rbMenuItem);
        rbMenuItem.addActionListener(this);
        menu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Split mode");
        rbMenuItem.setMnemonic(KeyEvent.VK_S);
        group.add(rbMenuItem);
        rbMenuItem.addActionListener(this);
        menu.add(rbMenuItem);

        return menuBar;
    }

    /**
     * Creates container contentPane, adds the programs contents and returns it.
     * @return Container contentPane.
     */
    public Container createContentPane() {
        contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);

        //Instantiate the status bar.
        status = new JLabel("ready");
        
        //Instantiate the tool bar.
        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolBar.setFloatable(false);
        addButtons(toolBar);

        //Add all program items to the content pane.
        contentPane.add(status, BorderLayout.SOUTH);
        contentPane.add(toolBar, BorderLayout.PAGE_START);

        return contentPane;
    }
    
    /**
     * Adds categories to tool bar toolBar.
     * @param toolBar the tool bar to whom categories are added.
     */
    public void addButtons(JToolBar toolBar) {
    	JToggleButton tb1 = new JToggleButton("Family");
    	tb1.addActionListener(this);
    	
    	JToggleButton tb2 = new JToggleButton("Friends");
    	tb2.addActionListener(this);
    	
    	JToggleButton tb3 = new JToggleButton("Fools");
    	tb3.addActionListener(this);

    	toolBar.add(tb1);
    	toolBar.add(tb2);
    	toolBar.add(tb3);
    }
    
    /**
     * From MenuDemo.java, Oracle and/or its affiliates.
     * Returns an ImageIcon, or null if the path was invalid. 
     * @param path String that represents relative path to the icon.
     * @return ImageIcon, or null if the path was invalid.
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Phototheque.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    /**
     * Creates the GUI and shows it.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Phototheque");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        Phototheque phototeque = new Phototheque();
        frame.setJMenuBar(phototeque.createMenuBar());
        frame.setContentPane(phototeque.createContentPane());
        
        //Display the window.
        ImageIcon programIcon = createImageIcon("/images/programIcon.png");
        frame.setIconImage(programIcon.getImage());
        frame.setSize(900, 600);
        frame.setExtendedState(MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(400, 400));
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
	
    /**
     * Defines what happens once an event is detected.
     */
	@Override
	public void actionPerformed(ActionEvent e) {
        //Handle open button action.
        if (e.getSource() == openMenuItem) {
        	//Create the file chooser
            fc = new JFileChooser();
            
            int returnVal = fc.showOpenDialog(Phototheque.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {           	
                File file = fc.getSelectedFile();
                status.setText("File '" + file.getName() + "' was opened");
            } else {
            	status.setText("Open command was cancelled");
            }
        } else if (e.getSource() == deleteMenuItem){
        	status.setText("File was deleted");
        } else if (e.getSource() == quitMenuItem){
        	System.exit(getDefaultCloseOperation());
        } else {
			AbstractButton abstractButton = (AbstractButton) e.getSource();
            
            boolean isSelected = abstractButton.getModel().isSelected();
            String selected = "selected";
            if (!isSelected) { 
            	selected = "deselected"; 
            }
            status.setText("'" + abstractButton.getText() + "' was " + selected);
        }
	}
}
