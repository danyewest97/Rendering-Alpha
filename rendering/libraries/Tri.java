package rendering.libraries;

import rendering.libraries.*;
import rendering.renderer.*;

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
		
		Point axy = r.xy(a);
		Point bxy = r.xy(b);
		Point cxy = r.xy(c);
		
		this.ab = new Inequality(axy, bxy, ">");
		if (!ab.contains(cxy)) ab.inequality = "<";
		
		this.bc = new Inequality(bxy, cxy, ">");
		if (!bc.contains(axy) bc.inequality = "<";
		
		this.ca = new Inequality(cxy, axy, ">");
		if (!ca.contains(bxy)) ca.inequality = "<";
		
		
		leftToRight.add(a);
		leftToRight.add(b);
		leftToRight.add(c);
		Collections.sort(leftToRight);
		
		leftToRightXY.add(axy);
		leftToRightXY.add(bxy);
		leftToRightXY.add(cxy);
		Collections.sort(leftToRightXY);
	}
	
	// Meant to be overridden at object creation
	// Allows for the creation of triangles with an opacity and color that is dependent on x and y, however any doubles may be passed and x and y don't even have to be used
	// So, this allows for a function of any variable to be used to decide the opacity and color of this triangles
	// Opacity and RGB values should only be between 0.0 and 1.0
	// Must return a double array of length 4 {r, g, b, a}, may change this to just a Color object in the future but I want it this way to make sure the RGBA values are between 0.0 and 1.0
	public double[] colorEquation(double x, double y) {
		
	}
	
	// Makes sure that the opacity is between 0.0 and 1.0 before using it
	public Color getColor(double x, double y) {
		double[] color = colorEquation(x, y);
		double r;
		double g;
		double b;
		double a;
		
		// If the color array is not properly defined, this will set unset color values to 0.0 as a default value
		try r = color[0];
		catch (Exception e) r = 0;
		try g = color[1];
		catch (Exception e) g = 0;
		try b = color[2];
		catch (Exception e) b = 0;
		try a = color[3];
		catch (Exception e) a = 0;
		
		// Checking each value to make sure they aren't greater than 1.0 or less than 0.0
		if (r < 0) r = 0;
		if (r > 1) r = 1;
		if (g < 0) g = 0;
		if (g > 1) g = 1;
		if (b < 0) b = 0;
		if (b > 1) b = 1;
		if (a < 0) a = 0;
		if (a > 1) a = 1;
		
		Color result = new Color(r, g, b, a);
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
		
		if (x <= middle.x) {
			double dist = (x - leftxy.x);
			
			double yslopeA = (left.y - middle.y) / (left.x - middle.x);
			double zslopeA = (left.z - middle.z) / (left.x - middle.x);
			double yslopeB = (left.y - right.y) / (left.x - right.x);
			double zslopeB = (left.z - right.z) / (left.x - right.x);
			
			double x1 = left.x + dist;
			double y1 = left.y + yslopeA * dist;
			double z1 = left.z + zslopeA * dist;
			
			double x2 = left.x + dist;
			double y2 = left.y + yslopeB * dist;
			double z2 = left.z + zslopeB * dist;
			
			double finalPercent = dist(x, y, leftxy.x + x, leftxy.y + y) / dist(x1, y1, x2, y2);
			double z = z1 + finalPercent * (z2 - z1); // Not sure if this should be (z2 - z1) or (z1 - z2), need to experiment, but I think (z2 - z1) is right
			return z;
		} else {
			double distA = (x - leftxy.x);
			double distB = (x - middlexy.x);
			
			double yslopeA = (left.y - right.y) / (left.x - right.x);
			double zslopeA = (left.z - right.z) / (left.x - right.x);
			double yslopeB = (middle.y - right.y) / (middle.x - right.x);
			double zslopeB = (middle.z - right.z) / (middle.x - right.x);
			
			double x1 = left.x + distA;
			double y1 = left.y + yslopeA * distA;
			double z1 = left.z + zslopeA * distA;
			
			double x2 = middle.x + distB;
			double y2 = middle.y + yslopeB * distB;
			double z2 = middle.z + zslopeB * distB;
			
			
			Point temp = r.xy(new Point(x1, y1, z1));
			double finalPercent = dist(x, y, temp.x, temp.y) / dist(x1, y1, x2, y2);
			double z = z1 + finalPercent * (z2 - z1); // Not sure if this should be (z2 - z1) or (z1 - z2), need to experiment, but I think (z2 - z1) is right
		}
	}
	
	
	public double dist(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
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