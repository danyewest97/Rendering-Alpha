package rendering.renderer;

import rendering.renderer.Cell;
import rendering.libraries.*;
import rendering.libraries.Tri;
import rendering.libraries.Point;

import java.util.*;
import java.util.Timer;
import java.awt.Color;


// Note: ctrl + shift + q to comment/uncomment full blocks

// 1/11/2025: Removing camera rotation and translation from the Renderer class
// 1/12/2025: Adding it back!
// 1/12/2025: Fuck that this is too complicated to put in Renderer, I wanna just have a different class to handle the rotational stuff, but it would be SO cool to have it built in
// 1/12/2025: Fuck yeah I figured it out!
// 1/13/2025: Nope too hard maybe later idk, I widowwy had it all figured out with zSensitivity = 0 but as soon as I add zSensitivity it breaks, gonna try to fix for now but
// might be easier and simpler and more elegant to put cam rotation in a different class

public class Renderer {
	public double camX = 250;
	public double camY = 250;
	public double camZ = 0;
	
	// Variables for rotating the camera
	public Point centerOfRotation = new Point(camX, camY, 50);
	public double rotX = 0;
	public double rotY = 0;
	public double rotZ = 0;
	
	
	public double zSensitivity = 0.0003;
	
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
	
	
	// Unused for now
	// public boolean tri(Point a, Point b, Point c) {
		// Tri t = new Tri(a, b, c, this, null);
		// this.triangles.add(t);
		// ArrayList<int[]> coordsList = findPoints(t);
		// dispersePoints(coordsList, t);
		// return true;
	// }
	
	
	public boolean tri(Tri temp) {
		Tri t = temp.clone();
		
		
		// Adding rotation
		t.a.rotateX(centerOfRotation, rotX);
		t.a.rotateY(centerOfRotation, rotY);
		t.a.rotateZ(centerOfRotation, rotZ);
		
		t.b.rotateX(centerOfRotation, rotX);
		t.b.rotateY(centerOfRotation, rotY);
		t.b.rotateZ(centerOfRotation, rotZ);
		
		t.c.rotateX(centerOfRotation, rotX);
		t.c.rotateY(centerOfRotation, rotY);
		t.c.rotateZ(centerOfRotation, rotZ);
		
		
		t.recalculate();
		
		t.changeRenderer(this);
		this.triangles.add(t);
		ArrayList<int[]> coordsList = findPoints(t);
		dispersePoints(coordsList, t);
		return true;
	}
	
	
	// Finds all the points on the screen that the given triangle passes through, first step in the obfuscation, opacity, and finally drawing calculations
	public ArrayList<int[]> findPoints(Tri t) {
		ArrayList<int[]> result = new ArrayList<int[]>();
		
		ArrayList<Point> xpoints = new ArrayList<Point>();
		ArrayList<Point> ypoints = new ArrayList<Point>();
		
		
		// Adding rotation
		// t.a.rotateX(centerOfRotation, rotX);
		// t.a.rotateY(centerOfRotation, rotY);
		// t.a.rotateZ(centerOfRotation, rotZ);
		
		// t.b.rotateX(centerOfRotation, rotX);
		// t.b.rotateY(centerOfRotation, rotY);
		// t.b.rotateZ(centerOfRotation, rotZ);
		
		// t.c.rotateX(centerOfRotation, rotX);
		// t.c.rotateY(centerOfRotation, rotY);
		// t.c.rotateZ(centerOfRotation, rotZ);
		
		
		
		xpoints.add(t.a);
		xpoints.add(t.b);
		xpoints.add(t.c);
		
		
		ypoints.add(t.a);
		ypoints.add(t.b);
		ypoints.add(t.c);
		
		
		Collections.sort(xpoints, new sortByX());
		Collections.sort(ypoints, new sortByY());
		
		
		
		// Using intValue() here because the xpoints and ypoints ArrayLists are arrays of Double objects instead of just doubles, meaning they can't be casted to an int the normal way
		int left = (int) xy(xpoints.get(0)).x;
		int right = (int) (xy(xpoints.get(2)).x + 1);
		int top = (int) xy(ypoints.get(0)).y;
		int bottom = (int) (xy(ypoints.get(2)).y + 1);
		
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
			
			
			if (x < width && x >= 0) {
				if (y < height && y >= 0) {
					if (cells[x][y] == null) {
						cells[x][y] = new Cell(x, y, this);
					}
				cells[x][y].addTriangle(t);
				}
			}
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
	
	
	class sortByX implements Comparator<Point> {
		public int compare(Point a, Point b){
			Point axy = xy(a);
			Point bxy = xy(b);
			if (axy.x < bxy.x) return -1;
			if (axy.x > bxy.x) return 1;
			if (axy.x == bxy.x) return 0;
			return 0;
		}
	}
	
	class sortByY implements Comparator<Point> {
		public int compare(Point a, Point b){
			Point axy = xy(a);
			Point bxy = xy(b);
			if (axy.y < bxy.y) return -1;
			if (axy.y > bxy.y) return 1;
			if (axy.y == bxy.y) return 0;
			return 0;
		}
	}
	
	// class DoubleTri {
		// Tri t;
		// Tri tr;
		// public DoubleTri(Tri t, Tri tr)
	// }
}



