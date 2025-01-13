package rendering.renderer;

import rendering.renderer.Cell;
import rendering.libraries.*;
import rendering.libraries.Tri;
import rendering.libraries.Point;

import java.util.*;
import java.util.Timer;
import java.awt.Color;



public class DoubleTri {
	Tri t;
	Tri tr;
	public DoubleTri(Tri t, Tri tr) {
		this.t = t.clone();
		this.tr = tr.clone();
	}
}