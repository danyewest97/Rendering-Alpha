package RenderingAlpha.src.lib;

import RenderingAlpha.src.run.Panel;

import java.util.*;
import java.awt.*;

import javax.swing.*;

public class Point {
	public double x;
	public double y;
	public double z;
	public Color color;
	
	
	public Point() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.color = Color.white;
	}
	
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = Color.black;
	}
	
	public Point(double x, double y, double z, Color color) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = color;
	}
	
	public double dist(Point p) {
		return Math.sqrt(Math.pow(p.x - x, 2) + Math.pow(p.y - y, 2) + Math.pow(p.z - z, 2));
	}
	
	
	@Override
	public Point clone() {
		return new Point(x, y, z, color);
	}
	
	@Override
	public String toString() {
		return "" + x + ", " + y + ", " + z;
	}
}