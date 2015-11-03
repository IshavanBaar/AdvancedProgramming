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

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import model.AnnotationModel;

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
	
	//Model used for saving data.
	private AnnotationModel model;
	
	// Number of lines in text blocks.
	private int lines = 0; 
	
	/*
	 * Variables corresponding to drawing
	 */
	private Color drawColor;
	private Font drawFont;

    public PhotoComponent(String imagePath) { 	
    	try {
    		//Create model for saving data.
    		model = new AnnotationModel();
    		
    		// Load image.
    		image = ImageIO.read(new File(imagePath)); //Absolute path
    		imageIcon = new ImageIcon(imagePath);
    		
    		// Set sizes and listeners of this component.
        	this.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
        	this.setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
        	
        	// Set default drawing variables.
        	this.setDrawColor(Color.black);
        	this.setDrawFont(new Font("Pristina", Font.PLAIN, 16));
        	
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
    
    public void setDrawColor(Color drawColor) {
    	this.drawColor = drawColor;
    }
    
    public void setDrawFont(Font drawFont) {
    	this.drawFont = drawFont;
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
        	
        	g2.setPaint(drawColor);
    		g2.setFont(drawFont);
    		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    		
        	drawStrokes(g2);
        	drawText(g2, model.getTextValue());	
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
    	ArrayList<Point> drawnPoints = model.getDrawnPoints();
		
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
    private void drawText(Graphics2D g2, String recText) {
    	//Empty string, nothing needs to be done.
		if (recText.length() == 0 || recText.equals("+")) {} 		
		//Remove "+" at the end of the string and recursively draw remaining text.
		else if (recText.contains("+") && recText.charAt(recText.length() - 1) == '+') {
			drawText(g2, recText.substring(0, recText.length() - 1));
		} 
		//Draw last written string and recursively draw remaining text.
		else if (recText.contains("+")) {			
			int plusCount = 0;
			int lastPlusPosition = -1;
			
			//Use position of plus to get the correct position of the text.
			for (int i = 0; i < recText.length(); i++) {
				if (recText.charAt(i) == '+') {
					plusCount++;
					lastPlusPosition = i;
				}
			}
			
			//Draw string.
			String text = recText.substring(lastPlusPosition + 1, recText.length());
			if (text.length() != 0) {
				prepareForDrawing(g2, text, plusCount);
			}
			
			drawText(g2, recText.substring(0, lastPlusPosition));
		}
    }
    
	private void prepareForDrawing(Graphics2D g2, String text, int plusCount) {
		
		Point pointToDraw = model.getTextPoints().get(plusCount - 1);
		
		if (pointToDraw != null) {
			// Get position of the text.
			double textX = pointToDraw.getX();
			double textY = pointToDraw.getY();
			
			// Add the value of the newly added text.
			
			// Calculate distance from first character of typed text to end of this component.
			double photoComponentEnd = this.getX() + this.getWidth();
			double distanceToBorder = photoComponentEnd - (this.getX() + textX);
			
			String textToDraw = "";
			lines = 0;
			
			for (int i = 0; i < text.length(); i++) {
				textToDraw = textToDraw + text.charAt(i);
				
				/* 
				 * If next character ("c") will cross the photo border, put \n there.
				 * Margin of 10px for wrapping.
				 */
				if (g2.getFontMetrics().stringWidth(text.substring(0, i) + text.charAt(i) + "c")
						- ((distanceToBorder-10) * lines) > (distanceToBorder-10)) {
					lines++;
					textToDraw = textToDraw + "\n";
				}
			}
			
			drawString(g2, textToDraw, (int) textX, (int) textY);
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
     */
    private void resetTextVariables() {
		lines = 0;
		
		// Put a "+" sign to show separation of words.
		model.setTextValue(model.getTextValue() + "+");
    }
    
    @Override
	public void mousePressed(MouseEvent e) {
		// Request a new focus and reset the text variables.
		this.requestFocusInWindow();	
		
		// When component is double clicked, it is flipped.
		if (e.getClickCount() == 2) {
        	imageFlipped = !imageFlipped;       	
        	repaint();
        } else {
        	if (imageFlipped) {
        		resetTextVariables();
        		
        		// Update location of the first character of the current text.
        		model.getTextPoints().add(e.getPoint());
        	}       	
        }
	}

	@Override
	public void mouseReleased(MouseEvent e) {
        if (imageFlipped) {
        	// Add a null value to prevent connecting all points.
        	model.getDrawnPoints().add(null);
        }
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (imageFlipped) {
			//Add all drawn points to the array.
			model.getDrawnPoints().add(e.getPoint());
		    repaint();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode =  e.getKeyCode();
		char keyChar = e.getKeyChar();
		
		if (keyCode == KeyEvent.VK_SHIFT ||
				keyCode == KeyEvent.CTRL_DOWN_MASK ||
				keyCode == KeyEvent.ALT_GRAPH_DOWN_MASK ||
				keyCode == KeyEvent.ALT_DOWN_MASK ||
				keyCode == KeyEvent.VK_BACK_SPACE ||
				keyCode == KeyEvent.VK_DELETE ||
				keyCode == KeyEvent.VK_CAPS_LOCK) { 
			// Do nothing
		} else if (keyCode != KeyEvent.CHAR_UNDEFINED) {
			// Update the current text and put on the string that represents a stack
			model.setTextValue(model.getTextValue() + keyChar);	
			
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
