package model;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Model representing all forms of annotation 
 * (strokes and text) for a PhotoComponent.
 */
public class AnnotationModel {
	
		// Saves users drawn strokes.
		private ArrayList<Point> drawnPoints;
		
		// Positions of text blocks.
		private ArrayList<Point> textPoints;
		
		// Stack of all typed text blocks and "+" character for separation of points.
		private String textValue;
		
		public AnnotationModel() {
			drawnPoints = new ArrayList<Point>();
			textPoints = new ArrayList<Point>();
			textValue = "";
		}

		public ArrayList<Point> getDrawnPoints() {
			return drawnPoints;
		}

		public void setDrawnPoints(ArrayList<Point> drawnPoints) {
			this.drawnPoints = drawnPoints;
		}

		public ArrayList<Point> getTextPoints() {
			return textPoints;
		}

		public void setTextPoints(ArrayList<Point> textPoints) {
			this.textPoints = textPoints;
		}

		public String getTextValue() {
			return textValue;
		}

		public void setTextValue(String textValue) {
			this.textValue = textValue;
		}		
		
}
