package component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.PhotoModel;
import UI.PhotoUI;

/**
 * Component 
 * Listens for changes in the model.
 * Handles user input.
 */
public class PhotoComponent extends JComponent implements ChangeListener {

	private static final long serialVersionUID = 1L;
	
	// Model used for saving internal state information.
	private static PhotoModel model;

    public PhotoComponent(String imagePath) { 	
    	try {
    		setModel(new PhotoModel());
    		
    		// Set image in the model.
    		setImage(ImageIO.read(new File(imagePath))); 
    		setImageIcon(new ImageIcon(imagePath));
    		
    		setUI(new PhotoUI());
    		invalidate();    		
    	} catch (IOException e) {
    		System.err.println("The image could not be read");
    	} catch (IllegalArgumentException e2) {
    		System.err.println("The image could not be read");
    	}
    }
    
    public String getUIClassID() { 
    	return PhotoUI.UI_CLASS_ID; 
    }
    
    /**
     * Sets model of this component to m and adds component as change listener.
     * @param m PhotoModel new model of this component.
     */
    public void setModel(PhotoModel m) {
    	if (model != null) {
    			model.removeChangeListener(this);
    	}
    	model = m;
    	
    	// Add the component as listener for the change of model properties.
    	model.addChangeListener(this);
    }
    
    public PhotoModel getModel() {
    	return model;
    }
    
    public void setUI(PhotoUI ui) { 
    	super.setUI(ui); 
    }
    
    /**
     * Listener for change events in the model.
     * Repaint whenever change event is sent.
     */
	@Override
	public void stateChanged(ChangeEvent e) {
		// Request for focus whenever repainting is necessary.
		this.setFocusable(true);
		this.requestFocusInWindow();
		repaint();	
	}
	
    /*
     * ---------------------------------------------------------------------
     * GET/SET STATE VALUES IN MODEL
     * --------------------------------------------------------------------- 
    */
	
	public static int getIconWidth() {
		return model.getImageIcon().getIconWidth();
	}
	
	public static int getIconHeight() {
		return model.getImageIcon().getIconHeight();
	}
	
	
	public static Image getImage() {
		return model.getImage();
	}

	public static void setImage(Image image) {
		model.setImage(image);
	}

	public static ImageIcon getImageIcon() {
		return model.getImageIcon();
	}

	public static void setImageIcon(ImageIcon imageIcon) {
		model.setImageIcon(imageIcon);
	}

	public static boolean isImageFlipped() {
		return model.isImageFlipped();
	}

	public static void setImageFlipped(boolean imageFlipped) {
		model.setImageFlipped(imageFlipped);
	}

	
	public static ArrayList<Point> getDrawnPoints() {
		return model.getDrawnPoints();
	}

	public static void addDrawnPoint(Point point) {
		model.addDrawnPoint(point);
	}
	
	public static void setDrawnPoints(ArrayList<Point> drawnPoints) {
		model.setDrawnPoints(drawnPoints);
	}

	public static ArrayList<Point> getTextPoints() {
		return model.getTextPoints();
	}
	
	public static void addTextPoint(Point point) {
		model.addTextPoint(point);
	}
	
	public static void setTextPoints(ArrayList<Point> textPoints) {
		model.setTextPoints(textPoints);
	}

	public static String getTextValue() {
		return model.getTextValue();
	}

	public static void setTextValue(String textValue) {
		model.setTextValue(textValue);
	}	

	public static Color getDrawColor() {
		return model.getDrawColor();
	}

	public static void setDrawColor(Color drawColor) {
		model.setDrawColor(drawColor);;
	}

	public static Font getTextFont() {
		return model.getTextFont();
	}

	public static void setTextFont(Font textFont) {
		model.setTextFont(textFont);
	}	
}