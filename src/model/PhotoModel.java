package model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Model for the PhotoComponent
 * Holds information on the components internal state. 
 */
public class PhotoModel {

	// Image of the photoComponent
	private Image image;
	
	// ImageIcon of the photoComponent
	private ImageIcon imageIcon;

	// True iff image is flipped to the white canvas side.
	private boolean imageFlipped;

	// The drawn strokes.
	private ArrayList<Point> drawnPoints;
	
	// Positions of text blocks.
	private ArrayList<Point> textPoints;
	
	// Stack of all typed text blocks and "+" character for separation of points.
	private String textValue;
	
	private Color drawColor;
	
	private Font textFont;
	
	// Listeners that will fire change events to the component.
	private List<ChangeListener> changeListeners;
	
	/**
	 * Constructor.
	 */
	public PhotoModel() {
		changeListeners = new ArrayList<ChangeListener>();
		imageFlipped = false;
		drawnPoints = new ArrayList<Point>();
		textPoints = new ArrayList<Point>();
		textValue = "";
	}
	
	/**
	 * Adds listener listener to listeners list.
	 * @param listener ChangeListener the listener you add.
	 */
	public void addChangeListener (ChangeListener listener) {
		changeListeners.add(listener);
	}

	/**
	 * Remove listener listener to listeners list.
	 * @param listener ChangeListener the listener you remove.
	 */
	public void removeChangeListener (ChangeListener listener) {
		changeListeners.remove(listener);
	}
	
	/**
	 * Fires a change event so that the photoComponent will know 
	 * that something has changed in its state.
	 */
	private void fireChange () {
		for (ChangeListener listener : changeListeners) {
			ChangeEvent e = new ChangeEvent(listener);
			listener.stateChanged(e);
		}
	}
	
    /*
     * ---------------------------------------------------------------------
     * GETTERS AND SETTERS FOR IN MODEL
     * --------------------------------------------------------------------- 
    */
	
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
		fireChange();
	}

	public ImageIcon getImageIcon() {
		return imageIcon;
	}

	public void setImageIcon(ImageIcon imageIcon) {
		this.imageIcon = imageIcon;
		fireChange();
	}

	public boolean isImageFlipped() {
		return imageFlipped;
	}

	public void setImageFlipped(boolean imageFlipped) {
		this.imageFlipped = imageFlipped;
		fireChange();
	}

	
	public ArrayList<Point> getDrawnPoints() {
		return drawnPoints;
	}
	
	public void addDrawnPoint(Point point) {
		drawnPoints.add(point);
		fireChange();
	}

	public void setDrawnPoints(ArrayList<Point> drawnPoints) {
		this.drawnPoints = drawnPoints;
		fireChange();
	}

	public ArrayList<Point> getTextPoints() {
		return textPoints;
	}
	
	public void addTextPoint(Point point) {
		textPoints.add(point);
		fireChange();
	}

	public void setTextPoints(ArrayList<Point> textPoints) {
		this.textPoints = textPoints;
		fireChange();
	}

	public String getTextValue() {
		return textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
		fireChange();
	}

	public Color getDrawColor() {
		return drawColor;
	}

	public void setDrawColor(Color drawColor) {
		this.drawColor = drawColor;
		fireChange();
	}

	public Font getTextFont() {
		return textFont;
	}

	public void setTextFont(Font textFont) {
		this.textFont = textFont;
		fireChange();
	}		
	
}
