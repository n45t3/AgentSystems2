package prison.map.elements.elements2d.discrete;

import prison.map.elements.MapElementBase;
import prison.map.geometry.point.point2d.Int2DPoint;
import prison.map.geometry.span.span2d.Int2DSpan;

public abstract class Map2DElement extends MapElementBase<Int2DPoint, Int2DSpan> {

	public Map2DElement(Int2DPoint loc, Int2DSpan span) {
		super(loc, span);
	}
	
	public Map2DElement(Int2DPoint loc) {
		super(loc, new Int2DSpan());
	}

	public Map2DElement(int x, int y) {
		super(new Int2DPoint(x, y), new Int2DSpan());
	}

	public Map2DElement(int x, int y, int len, int hgh) {
		super(new Int2DPoint(x, y), new Int2DSpan(len, hgh));
	}

	public Map2DElement(Int2DPoint p, int len, int hgh) {
		super(p, new Int2DSpan(len, hgh));
	}

	public Map2DElement(int x, int y, Int2DSpan span) {
		super(new Int2DPoint(x, y), span);
	}

}
