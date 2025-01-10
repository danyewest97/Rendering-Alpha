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
	
	public ArrayList<Point> leftToRight = new ArrayList<Point>(); // Array that contains the points in the triangle going from left-most to right-most, not sur if I want to keep
	// because it could cause issues when updating a triangle, however I think it'll work for now
	public ArrayList<Point> leftToRightXY = new ArrayList<Point>(); // Array that contains the points in the triangle going from left-most to right-most in 2D
	
	public Tri(Point a, Point b, Point c, Renderer r) {
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
		double[] color = colorEquation(x, y);
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
	
	// I need to go through and comment all of this later
	// No f_cking clue if this works right or not lmao
	// Precondition: Point should be contained within the 2D coordinates of the triangle
	public double getZ(double x, double y) {
		Point left = leftToRight.get(0);
		Point middle = leftToRight.get(1);
		Point right = leftToRight.get(2);
		
		Point leftxy = leftToRightXY.get(0);
		Point middlexy = leftToRightXY.get(1);
		Point rightxy = leftToRightXY.get(2);
		
		// There's probably a more efficient way to do this
		// if (x <= middle.x) {
			// double dist = (x - left.x);
			// double ydist = (y - left.y);
			// if (dist == 0 && ydist == 0) return left.z;
			// else if (dist == 0) return left.z + ((y - left.y) / (middle.y - left.y)) * (middle.z - left.z);
			// else if (ydist == 0) return left.z + ((x - left.x) / (middle.x - left.x)) * (middle.z - left.z);
			// double percentA = dist / (right.x - left.x);
			// double percentB = dist / (middle.x - left.x);
			
			// double y1 = left.y + (right.y - left.y) * percentA;
			// double y2 = left.y + (middle.y - left.y) * percentB;
			// double z1 = left.z + (right.z - left.z) * percentA;
			// double z2 = left.z + (middle.z - left.z) * percentB;
			
			// double finalPercent = (y - y1) / (y2 - y1);
			// double z = z1 + finalPercent * (z2 - z1);
			// return z;
		// } else {
			// // Not sure if this is correct
			// double distA = (x - left.x);
			// double distB = (x - middle.x);
			// double ydist = (right.y - y);
			// if (distB == 0 && ydist == 0) return middle.z;
			// else if (distB == 0) return middle.z + ((y - middle.y) / (right.y - middle.y)) * (right.z - middle.z);
			// else if (ydist == 0) return middle.z + ((x - middle.x) / (right.x - middle.x)) * (right.z - middle.z);
			// double percentA = distA / (right.x - left.x);
			// double percentB = distB / (right.x - middle.x);
			
			// double y1 = left.y + (right.y - left.y) * percentA;
			// double y2 = middle.y + (right.y - middle.y) * percentB;
			// double z1 = left.z + (right.z - left.z) * percentA;
			// double z2 = middle.z + (right.z - middle.z) * percentB;
			
			// double finalPercent = (y - y1) / (y2 - y1);
			// double z = z1 + finalPercent * (z2 - z1);
			// return z;
		// }
		
		if (x <= middle.x) {
			double xpercent = (x - left.x) / (right.x - left.x);
			double ypercent = (y - left.y) / (middle.y - left.y);
			
			if (xpercent == 0 && ypercent == 0) return left.z;
			else if (xpercent == 0) return left.z + ypercent * (middle.z - left.z);
			else if (ypercent == 0) return left.z + xpercent * (right.z - left.z);
			
			double y1 = left.y + xpercent * (right.y - left.y);
			double y2 = left.y + xpercent * (middle.y - left.y);
			
			double z1 = left.z + xpercent * (right.z - left.z);
			double z2 = left.z + xpercent * (middle.z - left.z);
			
			double finalPercent = (y - y1) / (y2 - y1);
			double z = z1 + finalPercent * (z2 - z1);
			return z;
		} else {
			// Works for all the edge cases I tested/need to work, but I'm still not fully sure if this is the right solution
			double percentA = (x - left.x) / (right.x - left.x);
			double percentB = (x - middle.x) / (right.x - middle.x);
			
			double xpercent = (right.x - x) / (right.x - middle.x);
			double ypercent = (y - middle.y) / (right.y - middle.y);
			
			if (xpercent == 0 && ypercent == 0) return middle.z;
			else if (xpercent == 0) return middle.z + ypercent * (right.z - middle.z);
			else if (ypercent == 0) return middle.z + xpercent * (right.z - middle.z);
			
			double y1 = left.y + percentA * (right.y - left.y);
			double y2 = middle.y + percentB * (right.y - middle.y);
			
			double z1 = left.z + percentA * (right.z - left.z);
			double z2 = middle.z + percentB * (right.z - middle.z);
			
			double finalPercent = (y - y1) / (y2 - y1);
			double z = z1 + finalPercent * (z2 - z1);
			return z;
		}
	}
	
	
	public double dist(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
	
	
	@Override
	public Tri clone() {
		return new Tri(a, b, c, r) {
			@Override
			public double[] colorEquation(double x, double y) {
				// return this.colorEquation(x, y); // Doesn't work in Java
				// Make a wrapper class (i.e. a ColorEquation class) to hold the colorEquation method so it can be passed down between different clones of a Tri and copied to other Tris
			}
		};
	}
	
	public Tri clone(Renderer r) {
		return new Tri(a, b, c, r) {
			@Override
			public double[] colorEquation(double x, double y) {
				// return this.colorEquation(x, y); // Doesn't work in Java
			}
		};
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