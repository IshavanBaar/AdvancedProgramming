package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

/*
 * TODO I have noticed a bug where multiple texts are not well drawn. I have tried many other methods to draw 
 * on multiple lines but turned out to be really, really hard. It is possible with some hacks like deleting 
 * certain characters, but in my opinion it should be possible without this funny stuf and with just the one 
 * structure that stores text and its position (like the Map I used). That is why I left my code like this.
 */

/**
 * Component that stores and shows a photo, 
 * including annotations like strokes and text.
 */
public class PhotoComponent extends JComponent implements MouseListener, MouseMotionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	
	/*
	 * Image and its state information.
	 */
	private boolean imageFlipped;
	private Image image;
	private ImageIcon imageIcon;
	
	/*
	 * Structures to save the users drawn strokes (points) and text.
	 */
	private ArrayList<Point> drawnPoints = new ArrayList<Point>();
	private Map<Point, String> drawnText = new LinkedHashMap<Point, String>();
	
	/*
	 * Location point and characters of currently typed text.
	 */
	private Point currentTextPoint = null;
	private String currentText = "";
	
	/*
	 * Other variables to help with text on new lines.
	 */
	private String textValue = "";
	private int lines = 0; 
	
    public PhotoComponent(String imagePath) { 	
    	try {
    		// Load image.
    		image = ImageIO.read(new File(imagePath)); //Absolute path
    		imageIcon = new ImageIcon(imagePath);
    		
    		//Set sizes and listeners of this component.
        	this.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
        	this.setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
        	
        	// Adds listeners to this component.
        	this.addMouseListener(this);
        	this.addMouseMotionListener(this); 
        	this.addKeyListener(this);        	
    	} catch (IOException e) {
    		System.err.println("The image could not be read");
    	} catch (IllegalArgumentException e2) {
    		System.err.println("The image could not be read");
    	}
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Draws image if it is not flipped, otherwise white canvas and components.
        if (!imageFlipped) {
        	drawImage(g); 
        } else {
        	drawCanvas(g2);
        	drawStrokes(g2);
        	drawText(g2);	
        }
    }
    
    /**
     * Draws the photo image in the component.
     * @param g Graphics of this component.
     */
    private void drawImage(Graphics g) {	
    	g.setColor(Color.gray);          
        g.drawImage(image, 0, 0, null); 
    }
    
    /**
     * Draws the canvas where the user can add strokes and text.
     * @param g2 Graphics of this component.
     */
    private void drawCanvas(Graphics2D g2) {
    	g2.setColor(Color.white);
		g2.fillRect(0, 0, imageIcon.getIconWidth(), imageIcon.getIconHeight());
    }
    
    /**
     * Draws the users strokes.
     * @param g2 Graphics of this component.
     */
    private void drawStrokes(Graphics2D g2) {
		g2.setPaint(Color.black);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		for (int i = 0; i < drawnPoints.size() - 2; i++){
	        if (drawnPoints.get(i) != null && drawnPoints.get(i+1) != null) {
	        	Point p1 = drawnPoints.get(i);
    	        Point p2 = drawnPoints.get(i + 1);
    	        
    	        g2.drawLine(p1.x, p1.y, p2.x, p2.y);
	        }
	    }
    }
    
    /**
     * Draws the users text.
     * @param g2 Graphics of this component.
     */
    private void drawText(Graphics2D g2) {
    	g2.setPaint(Color.black);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setFont(new Font("Pristina", Font.PLAIN, 16));
		
		for (Map.Entry<Point, String> text : drawnText.entrySet()) {	
			if (text.getKey() != null) {
				// Get position of the text.
				double textX = text.getKey().getX();
    			double textY = text.getKey().getY();
    			
    			// Add the value of the newly added text.
    			textValue = textValue + text.getValue().substring(text.getValue().length() - 1);
    			
    			// Calculate distance from first character of typed text to end of this component.
    			double photoComponentEnd = this.getX() + this.getWidth();
    			double distanceToBorder = photoComponentEnd - (this.getX() + textX);
    			
    			// If next character ("c") will cross the photo border, put \n there.
    			if (g2.getFontMetrics().stringWidth(text.getValue() + "c")
    					- (distanceToBorder * lines) > distanceToBorder) {
    				lines++;
    				textValue = textValue + "\n";
    			}

    			drawString(g2, textValue, (int) textX, (int) textY);
			}
		}
    }
    
    /**
     * Draws a string. Could draw on multiple lines based on \n characters.
     * @param g2 Graphics that are used to draw the string.
     * @param text String that is drawn.
     * @param x X position of drawn string.
     * @param y Y position of drawn string.
     */
    private void drawString(Graphics2D g2, String text, int x, int y) {
    	String[] textSplit = text.split("\n");	
    	
    	for (String line : textSplit) {
    		// Adds a new line for every part of the text separated by \n.
    		g2.drawString(line, x, y += g2.getFontMetrics().getHeight());
        }
    }
    
    /**
     * Resets variables that keep information on the users typed text to make it possible to 
     * type another text.
     * TODO This does not work because it erases everything before as well. I have noticed this and
     * tried many other methods to draw on multiple lines but it is really, really hard. It is possible with
     * some hacks like deleting characters before, but in my opinion it should be possible with just the one
     * LinkedMap structure with text and Positions that I used. That is why I left my code like this.
     */
    private void resetTextVariables() {
		// Reset currently typed text variables.
		currentText = "";
		currentTextPoint = null;
		lines = 0;
		
		// Reset textValue (resets all text, makes problem worse)
		//textValue = "";
    }
    
	@Override
	public void mousePressed(MouseEvent e) {
		// Request a new focus and reset the text variables.
		this.requestFocusInWindow();
		resetTextVariables();
		
		// When component is double clicked, it is flipped.
		if (e.getClickCount() == 2) {
        	imageFlipped = !imageFlipped;       	
        	repaint();
        } else {
        	if (imageFlipped) {
        		// Update location of the first character of the current text.
        		currentTextPoint = e.getPoint();
        	}       	
        }
	}

	@Override
	public void mouseReleased(MouseEvent e) {
        if (imageFlipped) {
        	// Add a null value to prevent connecting all points.
        	drawnPoints.add(null);
        }
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (imageFlipped) {
			//Add all drawn points to the array.
			drawnPoints.add(e.getPoint());
		    repaint();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode =  e.getKeyCode();
		char keyChar = e.getKeyChar();
		
		if (keyCode == KeyEvent.VK_ENTER) {
			// Reset typed text variables to enable typing a new text.
			resetTextVariables();
		} else if (keyCode == KeyEvent.VK_SHIFT ||
				keyCode == KeyEvent.CTRL_DOWN_MASK ||
				keyCode == KeyEvent.ALT_GRAPH_DOWN_MASK ||
				keyCode == KeyEvent.ALT_DOWN_MASK ||
				keyCode == KeyEvent.VK_BACK_SPACE ||
				keyCode == KeyEvent.VK_DELETE ||
				keyCode == KeyEvent.VK_CAPS_LOCK) { 
			// Do nothing
		} else if (keyCode != KeyEvent.CHAR_UNDEFINED) {
			// Update the current text and put it in the map.		
			currentText = currentText + keyChar;
			
			drawnText.put(currentTextPoint, currentText);
			
			drawnText.toString();
			repaint();
		}		
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
}
