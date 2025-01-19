package rendering.libraries;

import rendering.libraries.*;
import rendering.libraries.Point;
import rendering.libraries.Inequality;
import rendering.renderer.*;
import java.util.*;
import java.awt.Color;

public class Tri {
	public Point a;
	public Point b;
	public Point c;
	public Inequality ab;
	public Inequality bc;
	public Inequality ca;
	
	public Renderer r;
	
	public ColorEquation ce = new ColorEquation() {
		public double[] color(double x, double y, Tri t) {
			double[] result = {1.0, 1.0, 1.0, 1.0};
			return result;
		}
	};
	
	public ArrayList<Point> leftToRight = new ArrayList<Point>(); // Array that contains the points in the triangle going from left-most to right-most, not sur if I want to keep
	// because it could cause issues when updating a triangle, however I think it'll work for now
	public ArrayList<Point> leftToRightXY = new ArrayList<Point>(); // Array that contains the points in the triangle going from left-most to right-most in 2D
	
	public Tri(Point a, Point b, Point c, Renderer r, ColorEquation ce) {
		this.a = a;
		this.b = b;
		this.c = c;
		
		Point axy;
		Point bxy;
		Point cxy;
		
		if (r != null) {
			axy = r.xy(a);
			bxy = r.xy(b);
			cxy = r.xy(c);
		} else {
			axy = a;
			bxy = b;
			cxy = c;
		}
		
		this.ab = new Inequality(axy, bxy, ">");
		if (!ab.contains(cxy)) ab.inequality = "<";
		
		this.bc = new Inequality(bxy, cxy, ">");
		if (!bc.contains(axy)) bc.inequality = "<";
		
		this.ca = new Inequality(cxy, axy, ">");
		if (!ca.contains(bxy)) ca.inequality = "<";
		
		
		leftToRight.add(a);
		leftToRight.add(b);
		leftToRight.add(c);
		Collections.sort(leftToRight, new sortByX());
		
		
		leftToRightXY.add(axy);
		leftToRightXY.add(bxy);
		leftToRightXY.add(cxy);
		Collections.sort(leftToRightXY, new sortByX());
		
		if (ce != null) this.ce = ce;
	}
	
	public void changeRenderer(Renderer r) {
		this.r = r;
		
		Point axy;
		Point bxy;
		Point cxy;
		
		
		if (r != null) {
			axy = r.xy(a);
			bxy = r.xy(b);
			cxy = r.xy(c);
		} else {
			axy = a;
			bxy = b;
			cxy = c;
		}
		
		this.ab = new Inequality(axy, bxy, ">");
		if (!ab.contains(cxy)) ab.inequality = "<";
		
		this.bc = new Inequality(bxy, cxy, ">");
		if (!bc.contains(axy)) bc.inequality = "<";
		
		this.ca = new Inequality(cxy, axy, ">");
		if (!ca.contains(bxy)) ca.inequality = "<";
		
		
		leftToRight = new ArrayList<Point>();
		leftToRight.add(a);
		leftToRight.add(b);
		leftToRight.add(c);
		Collections.sort(leftToRight, new sortByX());
		
		leftToRightXY = new ArrayList<Point>();
		leftToRightXY.add(axy);
		leftToRightXY.add(bxy);
		leftToRightXY.add(cxy);
		Collections.sort(leftToRightXY, new sortByX());
	}
	
	// Meant to be overridden at object creation
	// Allows for the creation of triangles with an opacity and color that is dependent on x and y, however any doubles may be passed and x and y don't even have to be used
	// So, this allows for a function of any variable to be used to decide the opacity and color of this triangles
	// Opacity and RGB values should only be between 0.0 and 1.0
	// Must return a double array of length 4 {r, g, b, a}, may change this to just a Color object in the future but I want it this way to make sure the RGBA values are between 0.0 and 1.0
	public double[] colorEquation(double x, double y) {
		// Default is black
		double[] result = {0, 0, 0, 0};
		return result;
	}
	
	// Makes sure that the opacity is between 0.0 and 1.0 before using it
	public Color getColor(double x, double y) {
		double[] color = ce.color(x, y, this);
		double r;
		double g;
		double b;
		double a;
		
		// If the color array is not properly defined, this will set unset color values to 0.0 as a default value
		try {r = color[0];}
		catch (Exception e) {r = 0;}
		try {g = color[1];}
		catch (Exception e) {g = 0;}
		try {b = color[2];}
		catch (Exception e) {b = 0;}
		try {a = color[3];}
		catch (Exception e) {a = 0;}
		
		// Checking each value to make sure they aren't greater than 1.0 or less than 0.0
		if (r < 0) r = 0;
		if (r > 1) r = 1;
		if (g < 0) g = 0;
		if (g > 1) g = 1;
		if (b < 0) b = 0;
		if (b > 1) b = 1;
		if (a < 0) a = 0;
		if (a > 1) a = 1;
		
		Color result = new Color((float) r, (float) g, (float) b, (float) a);
		return result;
	}
	
	
	
	public boolean contains(Point p) {
		return (ab.contains(p) && bc.contains(p) && ca.contains(p));
	}
	


	public double getZ(double x, double y) {
		// (Had to look this one up, uses vector math stuff that I don't fully understand)
		// Algorithm for finding the equation for a plane given three points from GeeksForGeeks
		double x1 = a.x;
		double y1 = a.y;
		double z1 = a.z;

		double x2 = b.x;
		double y2 = b.y;
		double z2 = b.z;

		double x3 = c.x;
		double y3 = c.y;
		double z3 = c.z;


		double a1 = x2 - x1;
		double b1 = y2 - y1;
		double c1 = z2 - z1;
		double a2 = x3 - x1;
		double b2 = y3 - y1;
		double c2 = z3 - z1;
		double a = b1 * c2 - b2 * c1;
		double b = a2 * c1 - a1 * c2;
		double c = a1 * b2 - b1 * a2;
		double d = (- a * x1 - b * y1 - c * z1);
		

		double z = -(((a * x) + (b * y) + (d)) / c);
		return z;
	}
	
	
	
	public void recalculate() {
		Point axy;
		Point bxy;
		Point cxy;
		
		
		if (r != null) {
			axy = r.xy(a);
			bxy = r.xy(b);
			cxy = r.xy(c);
		} else {
			axy = a;
			bxy = b;
			cxy = c;
		}
		
		this.ab = new Inequality(axy, bxy, ">");
		if (!ab.contains(cxy)) ab.inequality = "<";
		
		this.bc = new Inequality(bxy, cxy, ">");
		if (!bc.contains(axy)) bc.inequality = "<";
		
		this.ca = new Inequality(cxy, axy, ">");
		if (!ca.contains(bxy)) ca.inequality = "<";
		
		
		leftToRight = new ArrayList<Point>();
		leftToRight.add(a);
		leftToRight.add(b);
		leftToRight.add(c);
		Collections.sort(leftToRight, new sortByX());
		
		leftToRightXY = new ArrayList<Point>();
		leftToRightXY.add(axy);
		leftToRightXY.add(bxy);
		leftToRightXY.add(cxy);
		Collections.sort(leftToRightXY, new sortByX());
	}
	
	
	
	public double dist(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
	
	
	
	@Override
	public Tri clone() {
		return new Tri(a.clone(), b.clone(), c.clone(), r, ce);
	}
	
	
	class sortByX implements Comparator<Point> {
		public int compare(Point a, Point b){
			if (a.x < b.x) return -1;
			if (a.x > b.x) return 1;
			if (a.x == b.x) return 0;
			return 0;
		}
	}
}