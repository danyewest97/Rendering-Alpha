package rendering.renderer;

import rendering.libraries.*;
import rendering.libraries.Point;

import java.util.*;
import java.util.Timer;

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
		
		this.cells = new Cell[width][height];
		this.points = new Point[width][height];
	}
	
	
	public Renderer(double width, double height, ArrayList<Tri> triangles) {
		this.width = width;
		this.height = height;
		
		this.points = new Point[width][height];
		this.cells = new Cell[width][height];
		this.triangles = triangles;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public boolean clear() {
		this.points = new Point[width][height];
		this.cells = new Cell[width][height];
		
		return true;
	}
	
	
	// For projecting points onto the 2D plane of the camera (or reversing the projection)
	
	public static Point xy(Point point) {
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
	
	public static Point toXYZ(Point point, double z) {
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
	ArrayList<Line> lines
	
}


// Used to calculate opacity and obfuscation in cells
class Line