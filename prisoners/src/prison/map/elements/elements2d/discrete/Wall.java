package prison.map.elements.elements2d.discrete;

import prison.map.geometry.point.point2d.Int2DPoint;
import prison.map.geometry.span.span2d.Int2DSpan;

public class Wall extends Map2DElement {

	private Direction dir;
	private Int2DPoint beg;

	private void setBeg(int len) {
		int x = this.getLocation().getY(), y = this.getLocation().getY();
		if (this.dir == Direction.left)
			x -= len - 1;
		else if (dir == Direction.down)
			y -= len - 1;
		this.beg = new Int2DPoint(x, y);
	}

	private static Int2DSpan getSpan(int len, Direction dir) {
		if (dir == Direction.left || dir == Direction.right)
			return new Int2DSpan(len, 1);
		else
			return new Int2DSpan(1, len);
	}

	public Wall(Int2DPoint loc, int len, Direction dir) {
		super(loc, getSpan(len, dir));
		this.dir = dir;
		this.setBeg(len);
	}

	public Int2DPoint getBeginning() {
		return this.beg;
	}

	public Direction getDirection() {
		return this.dir;
	}

	public int getLength() {
		return this.dir == Direction.down || this.dir == Direction.up ? this.getSpan().getYspan()
				: this.getSpan().getXspan();
	}

}
