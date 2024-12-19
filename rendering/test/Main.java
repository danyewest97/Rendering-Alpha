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
		
		Inequality b = new Inequality(p2, p3, "<");
		
		Inequality c = new Inequality(p3, p1, ">");
		
		Point check = new Point(5, 5, 0);
		
		// for (int i = 0; i < 100; i++) {
			// Point p = new Point((Math.random() * 100), (Math.random() * 100), 0);
			// System.out.println(p);
			// System.out.println(b.contains(p));
		// }
		
		Tri test = new Tri(p1, p2, p3);
		// System.out.println(test.contains(check));
		
		// System.out.println(a.contains(check) && b.contains(check) && c.contains(check));
		// System.out.println(c.yslope);
		
		
		for (int i = 0; i < 100; i++) {
			Point p = new Point((Math.random() * 100), (Math.random() * 100), 0);
			// System.out.println(p);
			boolean inequalityContains = (a.contains(p) && b.contains(p) && c.contains(p));
			boolean areaContains = GFG.isInside(0, 0, 100, 0, 0, 100, p.x, p.y);
			// if (inequalityContains != areaContains) {
				// System.out.println(p);
				// System.out.println(inequalityContains);
				// System.out.println(areaContains);
			// }
			
			
			boolean triContains = (test.contains(p));
			if (triContains != inequalityContains) {
				System.out.println(p);
				System.out.println(inequalityContains);
				System.out.println(triContains);
			}
			
			// if (triContains != areaContains) {
				// System.out.println(p);
				// System.out.println(triContains);
				// System.out.println(areaContains);
			// }
		}
		
	}
}


// Two ways to test for whether a point is inside a given triangle or not
// My way (the first one) uses three inequalities in the triangle
// Internet way (the second one) checks if the sum of the areas of all three triangles, formed by the given point and any two of the three triangle points, is equal to the total triangle area
// I will be using my way for this project as it works even with doubles, while the other way is only consistent if you are only using integers, but I will need to use doubles

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


class GFG {
     
    /* A utility function to calculate area of triangle 
       formed by (x1, y1) (x2, y2) and (x3, y3) */
    static double area(double x1, double y1, double x2, double y2,
                                        double x3, double y3)
    {
       return Math.abs((x1*(y2-y3) + x2*(y3-y1)+
                                    x3*(y1-y2))/2.0);
    }
      
    /* A function to check whether point P(x, y) lies
       inside the triangle formed by A(x1, y1),
       B(x2, y2) and C(x3, y3) */
    static boolean isInside(double x1, double y1, double x2,
                double y2, double x3, double y3, double x, double y)
    {   
       /* Calculate area of triangle ABC */
        double A = area (x1, y1, x2, y2, x3, y3);
      
       /* Calculate area of triangle PBC */ 
        double A1 = area (x, y, x2, y2, x3, y3);
      
       /* Calculate area of triangle PAC */ 
        double A2 = area (x1, y1, x, y, x3, y3);
      
       /* Calculate area of triangle PAB */  
        double A3 = area (x1, y1, x2, y2, x, y);
        
       /* Check if sum of A1, A2 and A3 is same as A */
        return (A == A1 + A2 + A3);
    }
     
    /* Driver program to test above function */
    public static void main(String[] args) 
    {
        /* Let us check whether the point P(10, 15)
           lies inside the triangle formed by 
           A(0, 0), B(20, 0) and C(10, 30) */
       if (isInside(0, 0, 20, 0, 10, 30, 10, 15))
           System.out.println("Inside");
       else
           System.out.println("Not Inside");
             
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
		// ArrayList<Point> order = new ArrayList<Point>();
		// order.add(a);
		// if (b.x <= order.get(0).x) {
			// order.add(0, b);
		// } else {
			// order.add(b);
		// }
		// if (c.x <= order.get(0).x) {
			// order.add(0, c);
		// } else if (c.x <= order.get(1).x) {
			// order.add(1, c);
		// } else {
			// order.add(c);
		// }
		
		// this.a = order.get(0);
		// this.b = order.get(1);
		// this.c = order.get(2);
		this.a = a;
		this.b = b;
		this.c = c;
		
		// System.out.println(this.a);
		// System.out.println(this.b);
		// System.out.println(this.c);
		
		
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