package rendering.renderer;

import rendering.libraries.*;
import rendering.libraries.Tri;
import rendering.libraries.Point;

import java.util.*;
import java.util.Timer;
import java.awt.Color;

// 1x1 box representing a pixel that stores info about pixels and is used to calculate things like opacity
public class Cell {
	public ArrayList<DoubleTri> tris = new ArrayList<DoubleTri>();
	public Color background = new Color(255, 255, 255, 255); // Will add to constructor later to be inherited from parent Renderer object, for now just using white
	public int x;
	public int y;
	public Renderer r;
	
	// I will add this later as a means to improve the accuracy of the opacity and obfuscation at sub-pixel levels at the cost of performance
	// In the future (hopefully) my program will check subPoints points inside each cell for obfuscation, but for now I will just check the center of each cell
	// This is effectively the same as having subPoints = 1
	// int subPoints;
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
		this.r = null;
	}
	
	public Cell(int x, int y, Renderer r) {
		this.x = x;
		this.y = y;
		this.r = r;
	}
	
	public boolean addTriangle(DoubleTri dt) {
		tris.add(dt);
		
		
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
			// double totalOpacity = 1;
			ArrayList<Point> points = new ArrayList<Point>();
			ArrayList<Color> subColors = new ArrayList<Color>();
			
			for (int j = 0; j < tris.size(); j++) {
				Tri t = tris.get(j).t;
				Tri tr = tris.get(j).tr; // Unrotated t
				
				// Undoing rotation in renderer to get color
				// tr.a.rotateX(r.centerOfRotation, -r.rotX);
				// tr.a.rotateY(r.centerOfRotation, -r.rotY);
				// tr.a.rotateZ(r.centerOfRotation, -r.rotZ);
				
				
				// tr.b.rotateX(r.centerOfRotation, -r.rotX);
				// tr.b.rotateY(r.centerOfRotation, -r.rotY);
				// tr.b.rotateZ(r.centerOfRotation, -r.rotZ);
				
				
				// tr.c.rotateX(r.centerOfRotation, -r.rotX);
				// tr.c.rotateY(r.centerOfRotation, -r.rotY);
				// tr.c.rotateZ(r.centerOfRotation, -r.rotZ);
				
				
				// if ((int) tr.a.x != 0) System.out.println((int) tr.a.x);
				
				// tr.recalculate(); // Should fix any errors in getZ() caused by rotation
				// t.recalculate();
				
				
				
				Point check = new Point((double) this.x + xpoints[i], (double) this.y + ypoints[i], 0);
				if (t.contains(check)) {
					double z = t.getZ(check.x, check.y);
					Point unrotated = new Point(check.x, check.y, z);
					
					if (r != null) {
						
						// EDIT: Deprecated for now, unable to confirm whether or not just using unrotated = r.toXYZ(unrotated, z + r.camZ), as seen below this comment chunk, yields
						// proper results, as it appears to stop working correctly when the triangle is close to parallel to the camera view, but I'm unsure if this is just an illusion
						// or if it is actually unintended behavior
						// Doing this to reverse the effects of zSensitivity in the Renderer (x and y slowly shift away from what they should be when rotating when zSensitivity != 0)
						// Point ar = r.xy(t.a);
						// Point br = r.xy(t.b);
						// Point cr = r.xy(t.c);
						
						// ar.z = t.a.z;
						// br.z = t.b.z;
						// cr.z = t.c.z;
						
						
						// Tri temp = new Tri(ar, br, cr, null, t.ce);
						
						// double realZ = temp.getZ(check.x, check.y);
						
						
						
						// Doing this to reverse the effects of zSensitivity in the Renderer (x and y slowly shift away from what they should be when rotating when zSensitivity != 0)
						unrotated = r.toXYZ(unrotated, z + r.camZ);
						unrotated.x += r.camX - r.width/2;
						unrotated.y += r.camY - r.height/2;
						
						
						unrotated.rotateX(r.centerOfRotation, -r.rotX);
						unrotated.rotateY(r.centerOfRotation, -r.rotY);
						unrotated.rotateZ(r.centerOfRotation, -r.rotZ);
						
					}
					
					Point pxy = unrotated;
					
					Color c = tr.getColor(pxy.x, pxy.y);
					points.add(new Point(pxy.x, pxy.y, z, c));
				}
			}
			
			
			ArrayList<Double> zValues = new ArrayList<Double>();
			
			// May cause problems when two triangles have the same z-value but different opacities!! Needs to be fixed later. UPDATE: Should be fixed!!
			Collections.sort(points, new sortByZ());
			for (int k = 0; k < points.size(); k++) {
				Point p = points.get(k);
				subColors.add(p.color);
				zValues.add(p.z);
			}
			
			
			for (int k = 0; k < subColors.size(); k++) {
				ArrayList<Color> sameZValues = new ArrayList<Color>();
				double startZ = zValues.get(k); 
				sameZValues.add(subColors.get(k));
				
				while (k < subColors.size() - 1 && zValues.get(k + 1) == startZ) {
					sameZValues.add(subColors.get(k + 1));
					subColors.remove(k + 1);
					zValues.remove(k + 1);
				}
				
				Color blendedColor = mixColors(sameZValues);
				subColors.set(k, blendedColor);
			}
			
			// Adding the background, should have 0.0 opacity
			subColors.add(background);
			
			while (subColors.size() > 1) {
				Color colorA = subColors.get(0);
				Color colorB = subColors.get(1);
				
				
				Color newColor = addColors(colorA, colorB);
				subColors.set(0, newColor);
				subColors.remove(1);
			}
			
			if (subColors.size() > 0) {
				colors.add(subColors.get(0));
			} else {
				return background;
			}
		}
		
		
		// May cause problems when two triangles have the same z-value but different opacities!! Needs to be fixed later. UPDATE: Should be fixed!!
		while (colors.size() > 1) {
			Color colorA = colors.get(0);
			Color colorB = colors.get(1);
			
			
			Color newColor = addColors(colorA, colorB);
			colors.set(0, newColor);
			colors.remove(1);
		}
		
		return colors.get(0);
	}
	
	
	// TODO: Make several colors mix properly! UPDATE: Finished
	// Adds two colors by putting one on top of the other
	public static Color addColors(Color a, Color b) {
		double alphaA = a.getAlpha() / (double) 255;
		double alphaB = b.getAlpha() / (double) 255;
		
		double percentA = alphaA;
		double percentB = (1 - alphaA) * alphaB;
		
		
		
		double redA = a.getRed();
		double greenA = a.getGreen();
		double blueA = a.getBlue();
		
		double redB = b.getRed();
		double greenB = b.getGreen();
		double blueB = b.getBlue();
		
		
		// int finalRed = Math.min((int) (((redA * alphaA) + (redB * (1 - alphaA) * ratio)) + 0.5), 255);
		// int finalGreen = Math.min((int) (((greenA * alphaA) + (greenB * (1 - alphaA) * ratio)) + 0.5), 255);
		// int finalBlue = Math.min((int) (((blueA * alphaA) + (blueB * (1 - alphaA) * ratio)) + 0.5), 255);
		// int finalAlpha = Math.min((int) (((255 * alphaA) + (255 * alphaB) * ratio) + 0.5), 255);
		
		
		// Adding 0.5 before casting to int to round to the nearest whole number
		// Taking the minimum of the value or 255 to prevent having too large a color value
		int finalRed = Math.min((int) ((redA + redB * percentB) + 0.5), 255);
		int finalGreen = Math.min((int) ((greenA + greenB * percentB) + 0.5), 255);
		int finalBlue = Math.min((int) ((blueA + blueB * percentB) + 0.5), 255);
		int finalAlpha = Math.min((int) ((255 * (percentA + percentB)) + 0.5), 255);
		
		
		
		Color result = new Color(finalRed, finalGreen, finalBlue, finalAlpha);
		
		return result;
	}
	
	
	
	// Mixes 2 colors together instead of adding one on top of another, not currently in use
	public static Color mixColors(Color a, Color b) {
		double alphaA = a.getAlpha() / (double) 255;
		double alphaB = b.getAlpha() / (double) 255;
		
		
		
		double redA = a.getRed();
		double greenA = a.getGreen();
		double blueA = a.getBlue();
		
		double redB = b.getRed();
		double greenB = b.getGreen();
		double blueB = b.getBlue();
		
		
		int finalRed = Math.min((int) ((redA * alphaA + redB * alphaB) + 0.5), 255);
		int finalGreen = Math.min((int) ((greenA  * alphaA + greenB * alphaB) + 0.5), 255);
		int finalBlue = Math.min((int) ((blueA * alphaA  + blueB * alphaB) + 0.5), 255);
		int finalAlpha = Math.min((int) ((255 * ((alphaA + alphaB) / 2)) + 0.5), 255);
		
		
		Color result = new Color(finalRed, finalGreen, finalBlue, finalAlpha);
		
		return result;
	}
	
	
	// Mixes many colors together instead of adding one on top of another
	public static Color mixColors(ArrayList<Color> colors) {
		int numColors = colors.size();
		double finalAlpha = 0;
		double finalRed = 0;
		double finalGreen = 0;
		double finalBlue = 0;
		double totalAlpha = 0;
		
		for (int i = 0; i < numColors; i++) {
			totalAlpha += colors.get(i).getAlpha() / (double) 255;
		}
		
		finalAlpha = totalAlpha/*  / (double) numColors */;
		
		for (int i = 0; i < numColors; i++) {
			Color c = colors.get(i);
			double alpha = c.getAlpha() / (double) 255;
			double red = c.getRed() * (alpha / totalAlpha);
			double green = c.getGreen() * (alpha / totalAlpha);
			double blue = c.getBlue() * (alpha / totalAlpha);
			
			finalRed += red;
			finalGreen += green;
			finalBlue += blue;
		}
		
		
		
		
		Color result = new Color(Math.min((int) (finalRed + 0.5), 255), Math.min((int) (finalGreen + 0.5), 255), Math.min((int) (finalBlue + 0.5), 255), Math.min((int) ((finalAlpha * 255) + 0.5), 255));
		
		return result;
	}
	
	
	public static double lerp(double a, double b, double percent) {
		return a + (b - a) * percent;
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

