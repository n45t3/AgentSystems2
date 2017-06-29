package prison.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import prison.map.MapFieldType;
import repast.simphony.context.Context;
import repast.simphony.valueLayer.GridValueLayer;

public class RepastMap2DTranslator {

	private static final Map<MapFieldType, Double> fieldTypeTranslator = new HashMap<>();
	static {
		fieldTypeTranslator.put(MapFieldType.empty, (double) 0);
		fieldTypeTranslator.put(MapFieldType.occupied, (double) 1);
		fieldTypeTranslator.put(MapFieldType.cell, (double) 4);
		fieldTypeTranslator.put(MapFieldType.spawn, (double) 8);
		fieldTypeTranslator.put(MapFieldType.wall, (double) 16);
	}

	private final Map<MapFieldType, Double> overridingFieldTypeTranslator = new HashMap<>();
	private Context<?> target;

	private Double translate(MapFieldType word) {
		if (this.overridingFieldTypeTranslator.containsKey(word))
			return this.overridingFieldTypeTranslator.get(word);
		return fieldTypeTranslator.get(word);
	}

	public GridValueLayer translate(Prison2DMap map, String name) {
		GridValueLayer gvl = new GridValueLayer(name, this.translate(MapFieldType.empty), true, map.getXspan(),
				map.getYspan());
		for (int i = 0; i < map.getXspan(); ++i)
			for (int j = 0; j < map.getYspan(); ++j)
				gvl.set(this.translate(map.getField(i, j)), i, j);
		return gvl;
	}

	public void assign(GridValueLayer gvl) {
		if (this.target == null)
			return;
		this.target.addValueLayer(gvl);
	}

	public void translateAndAssign(Prison2DMap map, String name) {
		if (this.target == null)
			return;
		this.assign(this.translate(map, name));
	}

	public RepastMap2DTranslator() {
		this.target = null;
	}

	public RepastMap2DTranslator(Context<?> target) {
		this.target = target;
	}

	public RepastMap2DTranslator(Map<MapFieldType, Double> overridingDict) {
		this();
		for (Entry<MapFieldType, Double> t : overridingDict.entrySet())
			overridingFieldTypeTranslator.put(t.getKey(), t.getValue());
	}

	public RepastMap2DTranslator(Map<MapFieldType, Double> overridingDict, Context<?> target) {
		this(overridingDict);
		this.target = target;
	}

	public static void setTranslationBase(Map<MapFieldType, Double> dict) {
		for (Entry<MapFieldType, Double> t : dict.entrySet())
			fieldTypeTranslator.put(t.getKey(), t.getValue());
	}

}
