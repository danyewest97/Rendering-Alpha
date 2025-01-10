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


import java.io.*;
import javax.imageio.*;

public class Main {
	public static BufferedImage img;
	public static Tri test;
	public static Tri test2;
	public static Renderer r;
	public static int millis = 0;
	
	
	public static Point x = new Point(0, 0, 0);
	public static Point y = new Point(100, 0, 0);
	public static Point z = new Point(0, 100, 0);
	
	public static Point x2 = new Point(0, 0, 10);
	public static Point y2 = new Point(100, 0, 10);
	public static Point z2 = new Point(0, 100, 10);
	
	
	public static BufferedImage testImage;
	
	
	public static void main(String[] args) {
		JFrame window = new JFrame("Test Window");
		
		window.setSize(500, 500);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		
		
		
		
		try {
			// testImage = ImageIO.read(new File("rendering/test/prestige-porcelain-kindred.png"));
			
			
			// File file = new File("rendering/test/prestige-porcelain-kindred.png");
			File file = new File("rendering/test/testImage.png");
			FileInputStream fis = new FileInputStream(file);
			testImage = ImageIO.read(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
		// Initializing the renderer
		r = new Renderer(500, 500) {
			@Override
			public void draw() {
				tri(test);
				tri(test2);
			}
		};
		
		
		
		
		
		
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
				
				
				
				
				draw();
				makeImage();
				clear();
				
				
				
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
				// x2.x += 0.04;
				// y2.x += 0.04;
				// z2.x += 0.04;
				
				// x2.z += 0.04;
				// y2.z += 0.04;
				z2.z += 0.04;
				
				millis++;
			}
		}, 0, 1);
	}
	
	
	
	
	
	
	
	// Where all the drawing instructions go
	public static void draw() {
		
		
		
		test = new Tri(x, y, z, null) {
			@Override
			public double[] colorEquation(double x, double y) {
				// double opacity = 1 - Math.min(Math.sqrt(Math.pow(x / 450, 2) + Math.pow(y / 150, 2)), 1);
				// double red = Math.min(Math.sqrt(Math.pow(x / 300, 2) + Math.pow(y / 50, 2)), 1);
				// double green = Math.min(Math.sqrt(Math.pow(x / 400, 2) + Math.pow((100 - y) / 100, 2)), 1);
				
				// x and y values relative to the top left of the triangle
				double relX = x - a.x;
				// double relY = y - a.y;
				double sin = Math.abs(Math.sin(relX / 10));
				double[] result = {0.0, 0.0, sin, sin / 2};
				
				// Point center = new Point((a.x + b.x + c.x) / 3, (a.y + b.y + c.y) / 3, (a.z + b.z + c.z) / 3);
				// double dist = 1 - Math.abs(1 - (Math.sqrt(Math.pow(center.x - x, 2) + Math.pow(center.y - y, 2)) / 10));
				// double[] result = {0.0, 0.0, dist, dist};
				
				return result;
			}
		};
		
		test2 = new Tri(x2, y2, z2, null) {
			@Override
			public double[] colorEquation(double x, double y) {
				// double opacity = Math.abs(Math.sin((double) millis / 360));
				// double[] result = {1.0, 0.0, 0.0, opacity};
				Color c;
				double[] result = {1.0, 0.0, 0.0, 1.0};
				
				
				if (testImage != null) {
					double width = testImage.getWidth();
					double height = testImage.getHeight();
					
					
					double z = getZ(x, y);
					
					
					Point p = r.toXYZ(new Point(x, y, 0), z);
					
					double relX = Math.max(p.x - a.x, 0);
					double relY = Math.max(p.y - a.y, 0);
					double relZ = Math.max(p.z - a.z, 0);
					
					// Hoping to keep the image rendered properly even when the triangle gets rotated
					double xComponent = Math.sqrt(Math.pow(relX, 2) + Math.pow(relY, 2));
					double yComponent = Math.sqrt(Math.pow(relY, 2) + Math.pow(relY, 2));
					
					
					double hShift = 150;
					double vShift = 150;
					
					
					c = new Color(testImage.getRGB((int) ((hShift + xComponent) % (width - 1)), (int) ((vShift + yComponent) % (height - 1))));
					
					double[] newResult = {c.getRed() / (double) 255, c.getBlue() / (double) 255, c.getGreen() / (double) 255, c.getAlpha() / (double) 255};
					result = newResult;
				}
				
				result[0] -= 0.5;
				return result;
			}
		};
		
		
		
		// x2.x += 0.7;
		// y2.x += 0.7;
		// z2.x += 0.7;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public static void makeImage() {
		img = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
		
		
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
	}
	
	public static void clear() {
		r.clear();
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