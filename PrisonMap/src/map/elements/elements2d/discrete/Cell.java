package map.elements.elements2d.discrete;

import map.geometry.point.point2d.Int2DPoint;
import map.geometry.span.span2d.Int2DSpan;

public class Cell extends Map2DElement {

	private Direction dir;

	public final Direction getDirection() {
		return this.dir;
	}

	public Cell(Int2DPoint loc, Int2DSpan span, Direction o) {
		super(loc, span);
		this.dir = o;
	}

	public Cell(int x, int y, Direction o) {
		super(x, y);
		this.dir = o;
	}

	public Cell(int x, int y, int len, int hgh, Direction o) {
		super(x, y, len, hgh);
		this.dir = o;
	}

	public Cell(Int2DPoint p, int len, int hgh, Direction o) {
		super(p, len, hgh);
		this.dir = o;
	}

	public Cell(int x, int y, Int2DSpan span, Direction o) {
		super(x, y, span);
		this.dir = o;
	}

}
