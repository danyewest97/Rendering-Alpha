package rendering.renderer;

import rendering.libraries.*;
import rendering.libraries.Tri;
import rendering.libraries.Point;

import java.util.*;
import java.util.Timer;


// Note: ctrl + shift + q to comment/uncomment full blocks

public class Renderer {
	public double camX = 250;
	public double camY = 250;
	public double camZ = 0;
	public double zSensitivity = 0.003; // May not be necessary
	
	public double width;
	public double height;
	
	public Point[][] points; // 2D array of points that will be returned upon updating
	public Cell[][] cells; // 2D array of cells that is used for calculating correct opacity and obfuscation
	public ArrayList<Tri> triangles = new ArrayList<Tri>(); //
	
	
	// Constructors
	public Renderer(double width, double height) {
		this.width = width;
		this.height = height;
		
		this.cells = new Cell[(int) width][(int) height];
		this.points = new Point[(int) width][(int) height];
	}
	
	
	public Renderer(double width, double height, ArrayList<Tri> triangles) {
		this.width = width;
		this.height = height;
		
		this.points = new Point[(int) width][(int) height];
		this.cells = new Cell[(int) width][(int) height];
		this.triangles = triangles;
	}
	
	// Must be overridden on object creation -- may switch to a different method but this should work for now, same as a draw() or run() or paint() function with something like a JS Canvas
	public void draw() {
		// Drawing code here
	}
	
	public boolean tri(Point a, Point b, Point c) {
		Tri t = new Tri(a, b, c);
		this.triangles.add(t);
		coordsList = findPoints(t);
		dispersePoints(coordsList, t);
		return true;
	}
	
	
	// Finds all the points on the screen that the given triangle passes through, first step in the obfuscation, opacity, and finally drawing calculations
	public ArrayList<int[]> findPoints(Tri t) {
		ArrayList<int[]> result = new ArrayList<int[]>();
		
		ArrayList<Double> xpoints = new ArrayList<Double>();
		ArrayList<Double> ypoints = new ArrayList<Double>();
		
		double x1 = t.a.x;
		double x2 = t.b.x;
		double x3 = t.c.x;
		
		double y1 = t.a.y;
		double y2 = t.b.y;
		double y3 = t.c.y;
		
		xpoints.add(x1);
		ypoints.add(y1);
		
		
		// Manually sorting xpoints and ypoints arrays because I'm lazy AF (this is literally harder than using Collections.sort())
		
		if (x2 <= x1) xpoints.add(0, x2);
		else xpoints.add(x2);
		
		if (y2 <= y1) ypoints.add(0, y2);
		else xpoints.add(y2);
		
		
		if (x3 <= xpoints.get(0)) xpoints.add(0, x3);
		else if (x3 <= xpoints.get(1)) xpoints.add(1, x3);
		else xpoints.add(x3);
		
		if (y3 <= ypoints.get(0)) ypoints.add(0, x3);
		else if (y3 <= ypoints.get(1)) ypoints.add(1, x3);
		else ypoints.add(y3);
		
		// Using intValue() here because the xpoints and ypoints ArrayLists are arrays of Double objects instead of just doubles, meaning they can't be casted to an int the normal way
		int left = (int) xpoints.get(0).intValue();
		int right = (int) (xpoints.get(2).intValue() + 1);
		int top = (int) ypoints.get(0).intValue();
		int bottom = (int) (ypoints.get(2).intValue() + 1);
		
		// Double for loop, could slow down the code with large triangles but that's a risk we have to take (there may be/probably is a more efficient solution)
		
		for (int i = left; i <= right; i++) {
			for (int j = top; j <= bottom; j++) {
				Point check = new Point(i, j, 0);
				if (t.contains(check)) result.add(new int[] {i, j});
			}
		}
		
		return result;
	}
	
	
	public boolean dispersePoints(ArrayList<int[]> coordsList, Triangle t) {
		for (int i = 0; i < coordsList.size(); i++) {
			int[] coords = coordsList.get(i);
			
			int x = coords[0];
			int y = coords[1];
			
			
			
		}
		return true;
	}
	
	
	
	
	
	public boolean clear() {
		this.points = new Point[(int) width][(int) height];
		this.cells = new Cell[(int) width][(int) height];
		
		return true;
	}
	
	
	// For projecting points onto the 2D plane of the camera (or reversing the projection)
	
	public Point xy(Point point) {
		double rx = camX;
		double ry = camY;
		
		double x = point.x;
		double y = point.y;
		double z = point.z;
		
		double p = Math.exp(-(z * zSensitivity));
		if (z == 0) p = 1;
		
		rx += (x - camX) * p;
		ry += (y - camY) * p;
		
		
		Point result = new Point(rx, ry, 0, point.color);
		
		return result;
	}
	
	public Point toXYZ(Point point, double z) {
		double x = point.x;
		double y = point.y;
		
		double p = Math.exp(-(z * zSensitivity));
		
		double rx = x;
		double ry = y;
		
		
		rx += p * camX;
		ry += p * camY;

		
		rx -= (camX);
		ry -= (camY);
		
		rx /= p;
		ry /= p;
		
		
		Point result = new Point(rx, ry, z, point.color);
		
		return result;
	}
}




// 1x1 box representing a pixel that stores info about pixels and is used to calculate things like opacity
class Cell {
	ArrayList<Tri> tris = new ArrayList<Tri>();
	int x;
	int y;
	
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
		
		
		// TODO: Apply obfuscation here by checking the z-values of points
		for (int i = 0; i < xpoints.length /* <-- Will be changed to subPoints in the future */; i++) {
			for (int j = 0; j < tris.size(); j++) {
				Tri t = tris.get(j);
				Point check = new Point((double) this.x + xpoints[i], (double) this.y + ypoints[i]);
				if (t.contains(check)) colors.add(t.getColor(check.x, check.y));
			}
		}
		
		while (colors.size() > 1) {
			// Color colorA = 
			// colors.set(0, mixColors
		}
	}
	
	// Mixes two colors, with ratio being the ratio of a:b (Color a to Color b)
	public static mixColors(Color a, Color b, double ratio) {
		if (ratio > 1) ratio = 1;
		
		double redA = a.getRed() * ratio;
		double blueA = a.getBlue() * ratio;
		double greenA = a.getGreen() * ratio;
		double alphaA = a.getAlpha() * ratio;
		
		double bRatio = 1 - ratio;
		double redB = b.getRed() * bRatio;
		double blueB = b.getBlue() * bRatio;
		double greenB = b.getGreen() * bRatio;
		double alphaB = b.getAlpha() * bRatio;
		
		// Adding 0.5 before casting to int to round to the nearest whole number
		int finalRed = (int) (redA + redB + 0.5);
		int finalBlue = (int) (blueA + blueB + 0.5);
		int finalGreen = (int) (greenA + greenB + 0.5);
		int finalAlpha = (int) (alphaA + alphaB + 0.5);
		
		Color result = new Color(finalRed, finalBlue, finalGreen, finalAlpha);
		
		return result;
	}
}

