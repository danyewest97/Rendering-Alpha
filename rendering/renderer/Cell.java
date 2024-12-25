package rendering.renderer;

import rendering.libraries.*;
import rendering.libraries.Tri;
import rendering.libraries.Point;

import java.util.*;
import java.util.Timer;
import java.awt.Color;

// 1x1 box representing a pixel that stores info about pixels and is used to calculate things like opacity
public class Cell {
	public ArrayList<Tri> tris = new ArrayList<Tri>();
	public Color background = new Color(255, 255, 255, 0); // Will add to constructor later to be inherited from parent Renderer object, for now just using white
	public int x;
	public int y;
	
	// I will add this later as a means to improve the accuracy of the opacity and obfuscation at sub-pixel levels at the cost of performance
	// In the future (hopefully) my program will check subPoints points inside each cell for obfuscation, but for now I will just check the center of each cell
	// This is effectively the same as having subPoints = 1
	// int subPoints;
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean addTriangle(Tri t) {
		tris.add(t);
		
		
		return true;
	}
	
	
	// Calculates the final color of the cell to be displayed as a single pixel, takes into account the opacity and positions of all the triangles that go through this cell and blends them all together
	public Color calculateColor() {
		ArrayList<Color> colors = new ArrayList<Color>();
		
		// Coordinates of sub-pixel points to check, where (0, 0) is the top left of the cell and (1, 1) is the bottom right
		double[] xpoints = {0.5}; // Will change this later to include more points for checking several sub-pixel points, for now I'm just checking the center of each cell/pixel
		double[] ypoints = {0.5}; // Same for this
		
		
		
		// TODO: Apply obfuscation here by checking the z-values of points    EDIT: Done!
		for (int i = 0; i < xpoints.length /* <-- Will be changed to subPoints in the future */; i++) {
			double totalOpacity = 1;
			ArrayList<Point> points = new ArrayList<Point>();
			ArrayList<Color> subColors = new ArrayList<Color>();
			
			for (int j = 0; j < tris.size(); j++) {
				Tri t = tris.get(j);
				Point check = new Point((double) this.x + xpoints[i], (double) this.y + ypoints[i], 0);
				if (t.contains(check)) {
					double z = t.getZ(check.x, check.y);
					Color c = t.getColor(check.x, check.y);
					points.add(new Point(check.x, check.y, z, c));
				}
			}
			
			// May cause problems when two triangles have the same z-value but different opacities!! Needs to be fixed later
			Collections.sort(points, new sortByZ());
			for (int k = 0; k < points.size(); k++) {
				subColors.add(points.get(k).color);
			}
			// Adding the background, should have 0.0 opacity
			subColors.add(background);
			
			while (subColors.size() > 1) {
				Color colorA = subColors.get(0);
				Color colorB = subColors.get(1);
				double opacityA = (double) colorA.getAlpha();
				totalOpacity -= totalOpacity * ((255 - opacityA) / 255);
				
				// System.out.println(opacityB);
				
				Color newColor = mixColors(colorA, colorB, totalOpacity);
				subColors.set(0, newColor);
				subColors.remove(1);
			}
			
			colors.add(subColors.get(0));
		}
		
		
		// May cause problems when two triangles have the same z-value but different opacities!! Needs to be fixed later
		double totalOpacity = 1;
		while (colors.size() > 1) {
			Color colorA = colors.get(0);
			Color colorB = colors.get(1);
			double opacityA = (double) colorA.getAlpha();
			totalOpacity -= totalOpacity * ((255 - opacityA) / 255);
			
			
			Color newColor = mixColors(colorA, colorB, totalOpacity);
			colors.set(0, newColor);
			colors.remove(1);
		}
		
		return colors.get(0);
	}
	
	// Mixes two colors, with ratio being the ratio of a:b (Color a to Color b)
	public static Color mixColors(Color a, Color b, double mult) {
		
		double redA = a.getRed();
		double greenA = a.getGreen();
		double blueA = a.getBlue();
		double alphaA = a.getAlpha();
		
		double redB = b.getRed() * mult;
		double greenB = b.getGreen() * mult;
		double blueB = b.getBlue() * mult;
		double alphaB = b.getAlpha() * mult;
		
		// Adding 0.5 before casting to int to round to the nearest whole number
		// Taking the minimum of the value or 255 to prevent having too large a color value
		int finalRed = Math.min((int) (redA + redB + 0.5), 255);
		int finalGreen = Math.min((int) (greenA + greenB + 0.5), 255);
		int finalBlue = Math.min((int) (blueA + blueB + 0.5), 255);
		int finalAlpha = Math.min((int) (alphaA + alphaB + 0.5), 255);
		
		
		Color result = new Color(finalRed, finalGreen, finalBlue, finalAlpha);
		
		return result;
	}
	
	
	class sortByZ implements Comparator<Point> {
		public int compare(Point a, Point b){
			if (a.z < b.z) return -1;
			if (a.z > b.z) return 1;
			if (a.z == b.z) return 0;
			return 0;
		}
	}
}

