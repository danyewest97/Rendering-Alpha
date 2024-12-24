package rendering.test;

import rendering.renderer.*;
import rendering.renderer.Renderer;
import rendering.libraries.*;
import rendering.libraries.Point;
import rendering.libraries.Tri;

import java.util.Timer;
import java.util.*;

import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		// JFrame window = new JFrame("Test Window");
		
		/* window.setSize(500, 500);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		
		
		
		
		// JPanel pane = new JPanel() {
			// @Override
			// public void paintComponent(Graphics g) {
					
			// }
		// } */
		
		Point x = new Point(0, 0, 0);
		Point y = new Point(10, 0, 0);
		Point z = new Point(0, 5, 0);
		
		Tri test = new Tri(x, y, z);
		
		// Renderer r = new Renderer(500, 500);
		
		// ArrayList<int[]> methodCheck = r.findPoints(test);
		// for (int i = 0; i < methodCheck.size(); i++) {
			// int[] coords = methodCheck.get(i);
			// System.out.print("[" + coords[0] + "]" + "[" + coords[1] + "], ");
		// }
		
	}
}











/* // Classes needed for checking sub-pixel points for obfuscation and opacity

// Used to calculate opacity and obfuscation in cells
class Inequality {
	Point a;
	Point b;
	public String inequality; // Can either be "<" or ">"
	public double xslope;
	public double yslope;
	
	// Precondition: inequality must be either "<" or ">"
	public Inequality(Point a, Point b, String inequality) {
		if (a.x <= b.x) {
			this.a = a;
			this.b = b;
		} else {
			this.a = b;
			this.b = a;
		}
		this.xslope = (b.y - a.y) / (b.x - a.x);
		this.yslope = (b.x - a.x) / (b.y - a.y);
		this.inequality = inequality;
	}
	
	public boolean contains(Point p) {
		if (inequality.equals(">")) {
			if (b.y < a.y) {
				return (p.y >= getY(p.x)) || (p.x >= getX(p.y));
			} else {
				return (p.y >= getY(p.x)) || (p.x <= getX(p.y));
			}
		} else if (inequality.equals("<")) {
			if (b.y < a.y) {
				return (p.y <= getY(p.x)) || (p.x <= getX(p.y));
			} else {
				return (p.y <= getY(p.x)) || (p.x >= getX(p.y));
			}
		} else {
			return false;
		}
		
	}
	
	public double getY(double x) {
		return xslope * (x - a.x) + a.y;
	}
	
	public double getX(double y) {
		return yslope * (y - a.y) + a.x;
	}
}

// Triangle class by me

class Tri {
	public Point a;
	public Point b;
	public Point c;
	public Inequality ab;
	public Inequality bc;
	public Inequality ca;
	
	public Tri(Point a, Point b, Point c) {
		this.a = a;
		this.b = b;
		this.c = c;
		
		
		this.ab = new Inequality(a, b, ">");
		if (!ab.contains(c)) ab.inequality = "<";
		
		this.bc = new Inequality(b, c, ">");
		if (!bc.contains(a)) bc.inequality = "<";
		
		this.ca = new Inequality(c, a, ">");
		if (!ca.contains(b)) ca.inequality = "<";
	}
	
	
	public boolean contains(Point p) {
		return (ab.contains(p) && bc.contains(p) && ca.contains(p));
	}
}
 */