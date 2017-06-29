package map.elements;

public abstract class MapElementBase<LocType, SpanType> {

	private LocType loc;
	private SpanType span;

	public MapElementBase(LocType loc, SpanType span) {
		if (loc == null)
			throw new IllegalArgumentException(this.getClass().getName() + ": null location");
		if (loc == span)
			throw new IllegalArgumentException(this.getClass().getName() + ": null span");
		this.loc = loc;
		this.span = span;
	}

	public LocType getLocation() {
		return loc;
	}

	public SpanType getSpan() {
		return span;
	}
}
