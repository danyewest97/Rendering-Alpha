package rendering.renderer;

import rendering.renderer.Cell;
import rendering.libraries.*;
import rendering.libraries.Tri;
import rendering.libraries.Point;

import java.util.*;
import java.util.Timer;
import java.awt.Color;


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
	
	public Color background = new Color(255, 255, 255, 255); // Will add to constructor later, for now white is default, also bg color should be opaque (0.0 opacity)
	
	
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
		Tri t = new Tri(a, b, c, this);
		this.triangles.add(t);
		ArrayList<int[]> coordsList = findPoints(t);
		dispersePoints(coordsList, t);
		return true;
	}
	
	public boolean tri(Tri temp) {
		// Tri t = new Tri(temp.a, temp.b, temp.c, this);
		temp.changeRenderer(this);
		this.triangles.add(temp);
		ArrayList<int[]> coordsList = findPoints(temp);
		dispersePoints(coordsList, temp);
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
	
	
	public boolean dispersePoints(ArrayList<int[]> coordsList, Tri t) {
		for (int i = 0; i < coordsList.size(); i++) {
			int[] coords = coordsList.get(i);
			
			int x = coords[0];
			int y = coords[1];
			
			
			if (cells[x][y] == null) cells[x][y] = new Cell(x, y);
			cells[x][y].addTriangle(t);
		}
		return true;
	}
	
	
	
	
	
	public boolean clear() {
		this.points = new Point[(int) width][(int) height];
		this.cells = new Cell[(int) width][(int) height];
		triangles = new ArrayList<Tri>();
		
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
