package rendering.renderer;

import rendering.libraries.Point;

public class Renderer {
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// For projecting points onto the 2D plane of the camera (or reversing the projection)
	
	public static Point xy(Point point) {
		double rx = camX;
		double ry = camY;
		
		double x = point.x;
		double y = point.y;
		double z = point.z;
		
		double p = Math.exp(-(z * zSensitivity));
		if (z == 0) p = 1;
		
		rx += (x - camX) * p;
		ry += (y - camY) * p;
		
		Point result = new Point(rx, ry, 0, point.color);
		
		return result;
	}
	
	public static Point toXYZ(Point point, double z) {
		double x = point.x;
		double y = point.y;
		double z = point.z;
		
		double p = Math.exp(-(z * zSensitivity));
		
		double rx = x;
		double ry = y;
		
		
		rx += p * camX;
		ry += p * camY;

		
		rx -= (camX);
		ry -= (camY);
		
		rx /= p;
		ry /= p;
		
		
		Point result = new Point(rx, ry, z, point.color);
		
		return result;
	}
}