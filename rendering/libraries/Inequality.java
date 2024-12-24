package rendering.libraries;

import rendering.libraries.*;

public class Inequality {
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