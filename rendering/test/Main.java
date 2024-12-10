package rendering.test;

import rendering.renderer.*;
import rendering.libraries.*;
import rendering.libraries.Point;

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
		
		
		Point p1 = new Point(0, 0, 0);
		Point p2 = new Point(100, 0, 0);
		Point p3 = new Point(0, 100, 0);
		
		Inequality a = new Inequality(p1, p2, ">");
		// if (a.slope >= 0) a.inequality = ">";
		// else a.inequality = "<";
		
		Inequality b = new Inequality(p2, p3, "<");
		// if (b.slope >= 0) b.inequality = ">";
		// else b.inequality = "<";
		
		Inequality c = new Inequality(p3, p1, "<");
		// if (c.slope >= 0) c.inequality = ">";
		// else c.inequality = "<";
		
		Point check = new Point(5, 5, 0);
		
		System.out.println(a.contains(check) && b.contains(check) && c.contains(check));
		System.out.println(c.slope);
		
	}
}


// Used to calculate opacity and obfuscation in cells
class Inequality {
	Point a;
	Point b;
	public String inequality; // Can either be "<" or ">"
	public double slope;
	
	// Precondition: inequality must be either "<" or ">"
	public Inequality(Point a, Point b, String inequality) {
		if (a.x <= b.x) {
			this.a = a;
			this.b = b;
		} else {
			this.a = b;
			this.b = a;
		}
		this.slope = (b.y - a.y) / (b.x - a.x);
		this.inequality = inequality;
	}
	
	public boolean contains(Point p) {
		if (inequality.equals(">")) {
			return p.y > getY(p.x);
		} else if (inequality.equals("<")) {
			return p.y < getY(p.x);
		} else {
			return false;
		}
		
	}
	
	public double getY(double x) {
		return slope * (x - a.x) + a.y;
	}
}