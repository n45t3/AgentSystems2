package map.geometry.span.span2d;

import map.geometry.span.TypeIndifferentSpan;

public abstract class TypeIndifferent2DSpan<T> extends TypeIndifferentSpan<T> {

	@SuppressWarnings("unchecked")
	public TypeIndifferent2DSpan(T xspan, T yspan) {
		super(xspan, yspan);
	}

	public TypeIndifferent2DSpan(int dim) {
		super(dim);
	}

	public T getXspan() {
		return this.getParam(0);
	}

	public T getYspan() {
		return this.getParam(1);
	}

	public void setXspan(T val) {
		this.setParam(0, val);
	}

	public void setYspan(T val) {
		this.setParam(1, val);
	}
}
