package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import resources.JFontChooser;

/**
 * Interactive photo browser, or Photothèque. 
 * The final version will allow the user to view, 
 * annotate, and organize a collection of photos.
 */
public class Phototheque extends JFrame implements ActionListener, ChangeListener {
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * Main panels in menu to hold content
	 */
	private JPanel contentPane;
	private PhotoComponent photoComponent;
	private JLabel status;
	
	// Radio button group that holds operations in view menu.
    private ButtonGroup viewRadioButtons = new ButtonGroup();
    
	public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
	/*
	 * Pop up window that is necessary for user customization.
	 */
	private JColorChooser colorChooser;
	
    /**
     * Creates the GUI and shows it.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Phototheque");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        Phototheque phototheque = new Phototheque();
        frame.setJMenuBar(phototheque.createMenuBar());
        frame.setContentPane(phototheque.createContentPane());
        
        //Display the window.
        ImageIcon programIcon = createImageIcon("/images/programIcon.png");
        frame.setIconImage(programIcon.getImage());
        frame.setSize(900, 600);
        frame.setExtendedState(MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(400, 400));
        frame.setVisible(true);
    }
	
    /**
     * Creates the menu bar menuBar, adds operations and returns it.
     * @return JMenuBar menuBar.
     */
    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Add the file and view menu to the bar.
        JMenu fileMenu = addMenuToBar("File", "All operations dealing with photo files", menuBar);
        JMenu viewMenu = addMenuToBar("View", "All operations dealing with the view", menuBar);
        JMenu drawMenu = addMenuToBar("Draw", "All operations dealing with drawing on photo", menuBar);

        // Add the open, delete and quit operation items to the file menu.
        addItemToMenu("Open", "/images/openIcon.png", fileMenu);
        addItemToMenu("Delete", "/images/deleteIcon.png", fileMenu);
        addItemToMenu("Quit", "/images/quitIcon.png", fileMenu);
        
        // Add the radio menu items to the view menu.
        addRadioItemToMenu("Photo viewer", true, viewMenu);
        addRadioItemToMenu("Browser", false, viewMenu);
        addRadioItemToMenu("Split mode", false, viewMenu);
        
        // Add customization operations to draw menu.
        addItemToMenu("Set color", "/images/openIcon.png", drawMenu);
        addItemToMenu("Set font", "/images/openIcon.png", drawMenu);
        
        return menuBar;
    }
    
    /**
     * Adds new menu to menu bar menuBar and returns it.
     * @param name Name of menu.
     * @param description Description of menu.
     * @param menuBar MenuBar where the menu is added.
     * @return JMenu menu.
     */
	private JMenu addMenuToBar(String name, String description, JMenuBar menuBar) {
    	JMenu menu = new JMenu(name);
        menu.getAccessibleContext().setAccessibleDescription(description);
        menuBar.add(menu);	
        return menu;
	}
    
    /**
     * Adds operation menu item with its icon path iconPath to JMenu menu.
     * @param item Operation item to be added to menu.
     * @param iconPath Path to icon of this operation.
     * @param menu Menu where the item is added.
     */
	private void addItemToMenu(String item, String iconPath, JMenu menu) {
    	ImageIcon icon = createImageIcon(iconPath);
        
    	JMenuItem menuItem = new JMenuItem(item, icon);       
        menuItem.addActionListener(this);
        menuItem.setName(item);
        
        menu.add(menuItem);	
	}

    /**
     * Adds operation radio button item, optionally selects it and adds it to button group and JMenu menu.
     * @param item Operation radio button item to be added to menu.
     * @param selected Boolean defining if this radio button item should be selected.
     * @param menu Menu where the item is added.
     */
    private void addRadioItemToMenu(String item, Boolean selected, JMenu menu) {
    	JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem(item);
    	if (selected) {
    		rbMenuItem.setSelected(true); 
    	}
        rbMenuItem.addActionListener(this);
        
        viewRadioButtons.add(rbMenuItem);
        menu.add(rbMenuItem);
	}
    
	/**
     * Creates container contentPane, adds the programs contents and returns it.
     * @return Container contentPane.
     */
    public Container createContentPane() {
        contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);

        // Instantiate the status bar.
        status = new JLabel("ready");
        
        // Instantiate the tool bar.
        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolBar.setFloatable(false);
        addButtons(toolBar);

        // Add all program items to the content pane.
        contentPane.add(status, BorderLayout.SOUTH);
        contentPane.add(toolBar, BorderLayout.NORTH);
        
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
     * Opens image with name imageName and path imagePath.
     * @param imagePath String path to the image.
     * @param imageName String name of the image.
     */
    public void openPhoto(String imagePath, String imageName) {  	
    	//Remove existing photo if there.
    	removePhoto();
    	
    	JScrollPane scrollPane = new JScrollPane();
        
    	//Instantiate photo component in a new JPanel with centered GridBagLayout.
        photoComponent = new PhotoComponent(imagePath);
        photoComponent.setFocusable(true); 
        photoComponent.requestFocus();
        
        JPanel centeredPhotoPanel = new JPanel(new GridBagLayout());
        centeredPhotoPanel.add(photoComponent);
        scrollPane.setViewportView(centeredPhotoPanel);
        
        //Set scrollPanes properties
        scrollPane.getViewport().getView().setBackground(Color.gray);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.setName("photo");
        
        //Add to contentPane.
        contentPane.add(scrollPane, BorderLayout.CENTER);
        status.setText("File '" + imageName + "' was opened");
    }
    
    /**
     * Removes the photo component in the contentPane if it exists.
     */
    private void removePhoto() {
    	for (Component c : contentPane.getComponents()) {
    		if (c.getName() != null && c.getName().equals("photo")) {
    			contentPane.remove(c); 	
            	status.setText("File was removed");
    			break;
    		}
    	}	
    	status.setText("There is no file to be removed!");
	}
    
    /**
     * Defines what happens once an event is detected.
     */
	@Override
	public void actionPerformed(ActionEvent e) {
        if (e.getSource().toString().contains("[Open")) {
        	openFileChooser();        	
        } else if (e.getSource().toString().contains("[Delete")) {
        	removePhoto();
        } else if (e.getSource().toString().contains("[Quit")) {
        	System.exit(getDefaultCloseOperation());
        } else if(e.getSource().toString().contains("[Set color")) {
        	openColorChooser();
        } else if(e.getSource().toString().contains("[Set font")) {
        	openFontChooser();
        }
        else {
			// Show what is currently selected in the status bar.
        	AbstractButton abstractButton = (AbstractButton) e.getSource();
            
            boolean isSelected = abstractButton.getModel().isSelected();
            String selected = "selected";
            if (!isSelected) { 
            	selected = "deselected"; 
            }
            status.setText("'" + abstractButton.getText() + "' was " + selected);
        }
	}

	/**
	 * Opens a font chooser to change the font of the text.
	 */
	private void openFontChooser() {
        //Create and set up the color picker window. 
		status.setText("Font is being chosen...");
        JFontChooser fontChooser = new JFontChooser();
        int result = fontChooser.showDialog(Phototheque.this);
        if (result == JFontChooser.OK_OPTION)
        {
             Font font = fontChooser.getSelectedFont(); 
             
             // Pass font to photoComponent.
             if (photoComponent != null) {
             	photoComponent.setDrawFont(font);
             	status.setText("Font was set to " + font.getFontName());
             }
        }
	}

	/**
	 * Opens a file chooser that accepts the choice of 1 photo.
	 */
	private void openFileChooser() {
        // Instantiates file chooser and creates a filter.
        status.setText("File is being chosen...");
		JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
        		"Images (.jpg, .jpeg, .png, .gif, .bmp)", 
        		"jpg", "jpeg", "png", "gif", "bmp"));
        fileChooser.setAcceptAllFileFilterUsed(false);
    	
        // Handle file selection input.
    	int result = fileChooser.showOpenDialog(Phototheque.this);
        if (result == JFileChooser.APPROVE_OPTION) {           	           	
        	File file = fileChooser.getSelectedFile();
            openPhoto(file.getPath(), file.getName());
        } else {
        	status.setText("Open file was cancelled");
        }
	}

	/**
	 * Opens a color chooser to change the color of the text and strokes.
	 */
	private void openColorChooser() {
		//Set up color chooser for setting text color and add to panel
		status.setText("Color is being chosen...");
    	JPanel panel = new JPanel(new BorderLayout());
    	colorChooser = new JColorChooser(Color.black);
    	colorChooser.getSelectionModel().addChangeListener(this);	
    	panel.add(colorChooser);
    	
        //Create and set up the color picker window.
        JFrame frame = new JFrame("Color Chooser");
        frame.setSize(300, 300);
        frame.setMinimumSize(new Dimension(200, 200));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(panel);
        frame.setVisible(true);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Color color = colorChooser.getColor();
        if (photoComponent != null) {
        	photoComponent.setDrawColor(color);
        	status.setText("Color was set to R:" + color.getRed() + " G:" + color.getGreen() + " B:" + color.getBlue());
        }
	}
}
