package prison.map.elements.elements2d.discrete;

import java.util.Map;
import java.util.Map.Entry;

import prison.map.geometry.point.point2d.Int2DPoint;

public class Spawner extends Map2DElement {

	private Class<?> spawn;

	public Object spawn() throws InstantiationException, IllegalAccessException {
		return spawn.newInstance();
	}

	public Object spawn(Map<String, Object> args) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, NoSuchFieldException, SecurityException {
		Object obj = spawn.newInstance();
		for (Entry<String, Object> kvp : args.entrySet())
			spawn.getField(kvp.getKey()).set(obj, kvp.getValue());
		return obj;
	}

	public Spawner(Int2DPoint loc, Class<?> spawn) {
		super(loc);
		if (spawn == null)
			throw new IllegalArgumentException(this.getClass().getName() + ": spawned class set to null");
		this.spawn = spawn;
	}

	public Spawner(int x, int y, Class<?> spawn) {
		super(x, y);
		if (spawn == null)
			throw new IllegalArgumentException(this.getClass().getName() + ": spawned class set to null");
		this.spawn = spawn;
	}

}
