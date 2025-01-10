package rendering.libraries;

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
	
	// public void rotate(Point center, double radX, double radY, double radZ) {
		
	// }
	
	
	
	// Rotation algorithms
	public void rotateX(Point center, double radians) {
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		
		double[][] pointMatrix = {
			{x},
			{y},
			{z}
		};
		
		double[][] rotationMatrix = {
			{1, 0, 0},
			{0, cos, -sin},
			{0, sin, cos}
		};
		
		double[][] result = mult(pointMatrix, rotationMatrix);
		x = result[0][0];
		y = result[1][0];
		z = result[2][0];
	}
	
	
	
	public void rotateY(Point center, double radians) {
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		
		double[][] pointMatrix = {
			{x},
			{y},
			{z}
		};
		
		double[][] rotationMatrix = {
			{cos, 0, sin},
			{0, 1, 0},
			{-sin, 0, cos}
		};
		
		double[][] result = mult(pointMatrix, rotationMatrix);
		x = result[0][0];
		y = result[1][0];
		z = result[2][0];
	}
	
	
	
	public void rotateZ(Point center, double radians) {
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		
		double[][] pointMatrix = {
			{x},
			{y},
			{z}
		};
		
		double[][] rotationMatrix = {
			{cos, -sin, 0},
			{sin, cos, 0},
			{0, 0, 1}
		};
		
		double[][] result = mult(pointMatrix, rotationMatrix);
		x = result[0][0];
		y = result[1][0];
		z = result[2][0];
	}
	
	
	
	
	
	
	@Override
	public Point clone() {
		return new Point(x, y, z, color);
	}
	
	@Override
	public String toString() {
		return "" + x + ", " + y + ", " + z;
	}
	
	
	
	
	// For rotation
	public static double[][] mult(double[][] matrix1, double[][] matrix2) { //multiplies given matrices
        if (matrix1[0].length != matrix2.length) {
            if ((matrix1.length == 1 && matrix1[0].length == 1)) {
                double[][] result = new double[matrix2.length][matrix2[0].length];
                for (int i = 0; i < matrix2.length; i++) {
                    for (int j = 0; j < matrix2[0].length; j++) {
                        result[i][j] = matrix2[i][j] * matrix1[0][0];
                    }
                }
                return result;
            } else  if (matrix2.length == 1 && matrix2[0].length == 1) {
                double[][] result = new double[matrix1.length][matrix1[0].length];
                for (int i = 0; i < matrix1.length; i++) {
                    for (int j = 0; j < matrix1[0].length; j++) {
                        result[i][j] = matrix1[i][j] * matrix2[0][0];
                    }
                }
                return result;
            } else {
                System.out.println("Error: Matrices cannot be multiplied, their sizes are incorrect");
                return null;
            }
        }
        double[][] result = new double[matrix1.length][matrix2[0].length];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                for (int k = 0; k < matrix1[0].length; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return result;
    }
}