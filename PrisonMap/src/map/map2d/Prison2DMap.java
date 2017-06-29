package map.map2d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.json.*;

import map.MapFieldType;
import map.elements.elements2d.discrete.Cell;
import map.elements.elements2d.discrete.Direction;
import map.elements.elements2d.discrete.Spawner;
import map.elements.elements2d.discrete.Wall;
import map.geometry.point.point2d.Int2DPoint;
import map.geometry.span.span2d.Int2DSpan;

public class Prison2DMap {

	public List<Spawner> spawns = new ArrayList<Spawner>();
	private Int2DSpan span;
	private MapFieldType[][] map;

	public int getXspan() {
		return span.getXspan();
	}

	public int getYspan() {
		return span.getYspan();
	}

	public boolean releaseField(int x, int y) {
		if (getField(x, y) != MapFieldType.empty)
			return false;
		this.map[x][y] = MapFieldType.empty;
		return true;
	}

	public boolean acquireField(int x, int y) {
		if (getField(x, y) != MapFieldType.empty)
			return false;
		this.map[x][y] = MapFieldType.occupied;
		return true;
	}

	public MapFieldType getField(int x, int y) {
		if (this.span.getXspan() == 0)
			throw new IllegalArgumentException(Prison2DMap.class.getName() + ": zero x dimension");
		if (this.span.getYspan() == 0)
			throw new IllegalArgumentException(Prison2DMap.class.getName() + ": zero y dimension");
		if (x >= map.length || y >= map[0].length)
			throw new IllegalArgumentException(Prison2DMap.class.getName() + ": index out of range");
		return this.map[x][y];
	}

	public boolean isFree(Int2DPoint beg, Int2DSpan span) {
		if (this.map == null)
			throw new NullPointerException();
		for (int i = 0; i < span.getXspan(); ++i)
			for (int j = 0; j < span.getYspan(); ++j)
				if (this.getField(beg.getX() + i, beg.getY() + j) != MapFieldType.empty)
					return false;
		return true;
	}

	public void addWall(Wall w) {
		for (int i = 0; i < w.getSpan().getXspan(); ++i)
			for (int j = 0; j < w.getSpan().getYspan(); ++j)
				if (this.getField(w.getLocation().getX() + i, w.getLocation().getY() + j) == MapFieldType.empty)
					this.map[w.getBeginning().getX() + i][w.getBeginning().getY() + j] = MapFieldType.wall;
	}

	public void addCell(Cell c) {
		if (!isFree(c.getLocation(), c.getSpan()))
			throw new IllegalArgumentException(Prison2DMap.class.getName() + ": elements overlapping");

		for (int i = c.getLocation().getX() + 1; i < c.getSpan().getXspan() - 1; ++i)
			for (int j = c.getLocation().getY() + 1; j < c.getSpan().getYspan() - 1; ++j)
				this.map[i][j] = MapFieldType.cell;

		Wall[] walls = new Wall[] { new Wall(c.getLocation(), c.getSpan().getXspan(), Direction.W),
				new Wall(c.getLocation(), c.getSpan().getXspan(), Direction.E),
				new Wall(c.getLocation(), c.getSpan().getYspan(), Direction.N),
				new Wall(c.getLocation(), c.getSpan().getYspan(), Direction.S) };
		for (Wall w : walls)
			if (w.getDirection() != c.getDirection())
				this.addWall(w);
	}

	public void addSpawner(Spawner s) {
		if (!isFree(s.getLocation(), s.getSpan()))
			throw new IllegalArgumentException(Prison2DMap.class.getName() + ": elements overlapping");
		this.spawns.add(s);
		this.map[s.getLocation().getX()][s.getLocation().getY()] = MapFieldType.spawn;
	}

	private void initMap() {
		if (this.span == null)
			throw new NullPointerException();
		this.map = new MapFieldType[this.span.getXspan()][this.span.getYspan()];
		Arrays.fill(this.map, MapFieldType.empty);
	}

	public static Prison2DMap parseMap(JSONObject json) {
		if (json == null)
			throw new IllegalArgumentException(Prison2DMap.class.getName() + ": attempting to parse null JSON");
		Prison2DMap out = new Prison2DMap();

		JSONObject span = json.getJSONObject("span");
		JSONArray cells = json.getJSONArray("cells");
		JSONArray spawners = json.getJSONArray("spawners");

		out.span = new Int2DSpan(span.getInt("x"), span.getInt("y"));
		out.initMap();
		cells.forEach(new Consumer<Object>() {
			@Override
			public void accept(Object t) {
				JSONObject cell = (JSONObject) t;
				try {
					out.addCell(new Cell(cell.getInt("x"), cell.getInt("y"), cell.getInt("length"),
							cell.getInt("height"), Direction.valueOf(cell.getString("direction"))));
				} catch (Exception e) {
					System.err.println(Prison2DMap.class.getName() + ": error while parsing cell:");
					System.err.println(e);
				}
			}
		});
		spawners.forEach(new Consumer<Object>() {
			@Override
			public void accept(Object t) {
				JSONObject spawner = (JSONObject) t;
				try {
					out.addSpawner(new Spawner(spawner.getInt("x"), spawner.getInt("y"),
							Class.forName(spawner.getString("spawn"))));
				} catch (Exception e) {
					System.err.println(Prison2DMap.class.getName() + ": error while parsing spawner:");
					System.err.println(e.toString());
				}
			}
		});

		return out;
	}

}
