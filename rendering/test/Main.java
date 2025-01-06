package rendering.test;

import rendering.renderer.*;
import rendering.renderer.Renderer;
import rendering.libraries.*;
import rendering.libraries.Point;
import rendering.libraries.Tri;

import java.util.Timer;
import java.util.*;
import java.util.Timer;

import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

public class Main {
	public static BufferedImage img;
	public static int millis = 0;
	public static void main(String[] args) {
		JFrame window = new JFrame("Test Window");
		
		window.setSize(500, 500);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		
		
		
		
		Point x = new Point(0, 0, 0);
		Point y = new Point(100, 0, 0);
		Point z = new Point(0, 100, 0);
		
		Point x2 = new Point(0, 0, 10);
		Point y2 = new Point(100, 0, 10);
		Point z2 = new Point(0, 100, 10);
		
		
		Tri test = new Tri(x, y, z, null) {
			@Override
			public double[] colorEquation(double x, double y) {
				double opacity = 1 - Math.min(Math.sqrt(Math.pow(x / 450, 2) + Math.pow(y / 150, 2)), 1);
				double red = Math.min(Math.sqrt(Math.pow(x / 300, 2) + Math.pow(y / 50, 2)), 1);
				double green = Math.min(Math.sqrt(Math.pow(x / 400, 2) + Math.pow((100 - y) / 100, 2)), 1);
				double[] result = {red, green, 1.0, opacity};
				return result;
			}
		};
		
		Tri test2 = new Tri(x2, y2, z2, null) {
			@Override
			public double[] colorEquation(double x, double y) {
				double opacity = Math.abs(Math.sin((double) millis / 360));
				double[] result = {1.0, 0.0, 0.0, opacity};
				return result;
			}
		};
		
		
		
		Renderer r = new Renderer(500, 500) {
			@Override
			public void draw() {
				tri(test);
				tri(test2);
			}
		};
		r.draw();
		
		
		img = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
		
		
		for (int i = 0; i < r.cells.length; i++) {
			for (int j = 0; j < r.cells[i].length; j++) {
				img.setRGB(i, j, Color.white.getRGB());
			}
		}
		
		for (int i = 0; i < r.cells.length; i++) {
			for (int j = 0; j < r.cells[i].length; j++) {
				Cell c = r.cells[i][j];
				if (c != null) {
					Color color = c.calculateColor();
					int cr = color.getRed();
					int cg = color.getGreen();
					int cb = color.getBlue();
					int ca = color.getAlpha();
					// System.out.print("|[" + i + "]" + "[" + j + "] = " + cr + ", " + cg + ", " + cb + ", " + ca + "| ");
					
					
					img.setRGB(i, j, color.getRGB());
				}
			}
		}
		
		
		
		// ArrayList<int[]> methodCheck = r.findPoints(test);
		// for (int i = 0; i < methodCheck.size(); i++) {
			// int[] coords = methodCheck.get(i);
			// System.out.print("[" + coords[0] + "]" + "[" + coords[1] + "], ");
		// }
		
		// System.out.println(test.getZ(10, 0));
		
		
		JPanel pane = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponents(g);
				
				//
				test2.a.x += 0.7;
				test2.b.x += 0.7;
				test2.c.x += 0.7;
				
				
				
				img = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
				
				r.clear();
				r.draw();
				
				// System.out.println(r.triangles.get(0).a.x);
				
				
				for (int i = 0; i < r.cells.length; i++) {
					for (int j = 0; j < r.cells[i].length; j++) {
						img.setRGB(i, j, r.background.getRGB());
					}
				}
				
				for (int i = 0; i < r.cells.length; i++) {
					for (int j = 0; j < r.cells[i].length; j++) {
						Cell c = r.cells[i][j];
						if (c != null) {
							Color color = c.calculateColor();
							int cr = color.getRed();
							int cg = color.getGreen();
							int cb = color.getBlue();
							int ca = color.getAlpha();
							// System.out.print("|[" + i + "]" + "[" + j + "] = " + cr + ", " + cg + ", " + cb + ", " + ca + "| ");
							
							
							img.setRGB(i, j, color.getRGB());
						}
					}
				}
				//
				
				
				
				
				
				g.drawImage(img, 0, 0, this);
				g.dispose();
			}
		};
		
		pane.setVisible(true);
		pane.setOpaque(false);
		pane.setBounds(new Rectangle(1920, 1080));
		
		window.add(pane);
		pane.repaint();
		
		
		Timer frames = new Timer();
		
		frames.schedule(new TimerTask() {
			@Override
			public void run() {
				
				
				
				pane.repaint();
				window.repaint();
				
				// System.out.println(test2.getColor(10, 10).getAlpha() / (double) 255);
				
			}
		}, 0, 1);
		
		
		Timer movement = new Timer();
		
		movement.schedule(new TimerTask() {
			@Override
			public void run() {
				// test.a.x += 0.04;
				// test.b.x += 0.04;
				// test.c.x += 0.04;
				
				millis++;
			}
		}, 0, 1);
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