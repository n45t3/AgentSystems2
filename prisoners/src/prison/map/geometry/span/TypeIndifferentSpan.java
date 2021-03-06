package prison.map.geometry.span;

public abstract class TypeIndifferentSpan<T> {

    private int dim;
    private T[] coords;

    @SuppressWarnings("unchecked")
    public TypeIndifferentSpan(T... args) {
        if (args == null) throw new IllegalArgumentException(this.getClass().getName() + ": null arguments");
        if (args.length == 0) throw new IllegalArgumentException(this.getClass().getName() + ": dimension zero");
        this.dim = args.length;
        this.coords = (T[]) args;
    }

    @SuppressWarnings("unchecked")
    public TypeIndifferentSpan(int dim) {
        if (dim == 0) throw new IllegalArgumentException(this.getClass().getName() + ": dimension zero");
        this.dim = dim;
        this.coords = (T[]) new Object[dim];
        for (int i = 0; i < this.dim;)
            this.coords[i++] = null;
    }

    public T getParam(int idx, boolean one_based) {
        if (one_based) --idx;
        if (idx >= dim) throw new IllegalArgumentException(this.getClass().getName() + ": index out of bounds");
        return this.coords[idx];
    }

    public T getParam(int idx) {
        return getParam(idx, false);
    }

    public void setParam(int idx, T val, boolean one_based) {
        if (one_based) --idx;
        if (idx >= dim) throw new IllegalArgumentException(this.getClass().getName() + ": index out of bounds");
        this.coords[idx] = val;
    }

    public void setParam(int idx, T val) {
        setParam(idx, val, false);
    }

    public boolean equals(Object o) {
        if (o.getClass() == this.getClass()) return ((TypeIndifferentSpan<T>) o).coords.equals(this.coords);
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (T c : coords) {
            sb.append(c);
            sb.append(',');
        }
        sb.append(']');
        return sb.toString();
    }
}
