package prison.map.map2d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.json.*;

import prison.map.MapFieldType;
import prison.map.elements.elements2d.discrete.Cell;
import prison.map.elements.elements2d.discrete.Direction;
import prison.map.elements.elements2d.discrete.Spawner;
import prison.map.elements.elements2d.discrete.Wall;
import prison.map.geometry.point.point2d.Int2DPoint;
import prison.map.geometry.span.span2d.Int2DSpan;



public class Prison2DMap {

    public List<Spawner>     spawns = new ArrayList<Spawner>();
    public List<Cell>        cells  = new ArrayList<Cell>();
    private Int2DSpan        span;
    private MapFieldType[][] map;

    public int[] getFreeCellSpace(Cell c) {
        for (int i = 1; i < c.getSpan().getXspan() - 1; ++i)
            for (int j = 1; j < c.getSpan().getYspan() - 1; ++j)
                if (map[c.getLocation().getX() + i][c.getLocation().getY() + j] != MapFieldType.occupied)
                    return new int[] { c.getLocation().getX() + i, c.getLocation().getY() + j };
        return null;
    }

    public int getXspan() {
        return span.getXspan();
    }

    public int getYspan() {
        return span.getYspan();
    }

    public boolean releaseField(int x, int y) {
        if (this.map == null) return false;
        if (x < 0 || y < 0 || x >= this.getXspan() || y >= this.getYspan()) return false;
        this.map[x][y] = MapFieldType.empty;
        return true;
    }

    public boolean acquireField(int x, int y) {
        if (this.map == null) return false;
        if (x < 0 || y < 0 || x >= this.getXspan() || y >= this.getYspan()) return false;
        //if (getField(x, y) != MapFieldType.empty) return false;
        this.map[x][y] = MapFieldType.occupied;
        return true;
    }

    public MapFieldType getField(int x, int y) {
        if (this.map == null) throw new NullPointerException();
        if (this.span.getXspan() == 0)
            throw new IllegalArgumentException(Prison2DMap.class.getName() + ": zero x dimension");
        if (this.span.getYspan() == 0)
            throw new IllegalArgumentException(Prison2DMap.class.getName() + ": zero y dimension");
        if (x < 0 || y < 0 || x >= this.getXspan() || y >= this.getYspan())
            throw new IllegalArgumentException(Prison2DMap.class.getName() + ": index out of range");
        return this.map[x][y];
    }

    public boolean isFree(Int2DPoint beg, Int2DSpan span) {
        if (this.map == null) throw new NullPointerException();
        for (int i = 0; i < span.getXspan(); ++i)
            for (int j = 0; j < span.getYspan(); ++j)
                if (this.getField(beg.getX() + i, beg.getY() + j) != MapFieldType.empty) return false;
        return true;
    }

    public void addWall(Wall w) {
        Int2DPoint beg = w.getBeginning();
        for (int i = 0; i < w.getSpan().getXspan(); ++i)
            for (int j = 0; j < w.getSpan().getYspan(); ++j)
                if (this.getField(beg.getX() + i, beg.getY() + j) == MapFieldType.empty)
                    this.map[beg.getX() + i][beg.getY() + j] = MapFieldType.wall;
        // for (int i = 0; i < w.getSpan().getXspan(); ++i)
        // for (int j = 0; j < w.getSpan().getYspan(); ++j)
        // if (this.getField(w.getLocation().getX() + i, w.getLocation().getY()
        // + j) == MapFieldType.empty)
        // this.map[w.getBeginning().getX() + i][w.getBeginning().getY() + j] =
        // MapFieldType.wall;
    }

    public void addCell(Cell c) {
        System.out.println("adding cell " + c.toString());
        // if (!isFree(c.getLocation(), c.getSpan()))
        // throw new IllegalArgumentException(Prison2DMap.class.getName() + ":
        // elements overlapping");
        System.out.println("entering loop");
        for (int i = 0; i < c.getSpan().getXspan(); ++i)
            for (int j = 0; j < c.getSpan().getYspan(); ++j)
                this.map[c.getLocation().getX() + i][c.getLocation().getY() + j] = MapFieldType.wall;
        for (int i = 1; i < c.getSpan().getXspan() - 1; ++i)
            for (int j = 1; j < c.getSpan().getYspan() - 1; ++j)
                this.map[c.getLocation().getX() + i][c.getLocation().getY() + j] = MapFieldType.cell;

        this.cells.add(c);

        System.out.println("building walls");
        switch (c.getDirection()) {
        case N:
            for (int i = 1; i < c.getSpan().getXspan() - 1; ++i)
                this.map[c.getLocation().getX() + i][c.getLocation().getY()] = MapFieldType.empty;
            break;
        case E:
            for (int i = 1; i < c.getSpan().getYspan() - 1; ++i)
                this.map[c.getLocation().getX() + c.getSpan().getXspan() - 1][c.getLocation().getY()
                        + i] = MapFieldType.empty;
            break;
        case S:
            for (int i = 1; i < c.getSpan().getXspan() - 1; ++i)
                this.map[c.getLocation().getX() + i][c.getLocation().getY() + c.getSpan().getYspan()
                        - 1] = MapFieldType.empty;
            break;
        case W:
            for (int i = 1; i < c.getSpan().getYspan() - 1; ++i)
                this.map[c.getLocation().getX()][c.getLocation().getY() + i] = MapFieldType.empty;
            break;
        }

        // Wall[] walls = new Wall[] { new Wall(c.getLocation(),
        // c.getSpan().getXspan(), Direction.W),
        // new Wall(c.getLocation(), c.getSpan().getXspan(), Direction.E),
        // new Wall(c.getLocation(), c.getSpan().getYspan(), Direction.N),
        // new Wall(c.getLocation(), c.getSpan().getYspan(), Direction.S) };
        // for (Wall w : walls)
        // if (w.getDirection() != c.getDirection()) this.addWall(w);
    }

    public void addSpawner(Spawner s) {
        if (!isFree(s.getLocation(), s.getSpan()))
            throw new IllegalArgumentException(Prison2DMap.class.getName() + ": elements overlapping");
        this.spawns.add(s);
        this.map[s.getLocation().getX()][s.getLocation().getY()] = MapFieldType.spawn;
    }

    private void initMap() {
        if (this.span == null) throw new NullPointerException();
        this.map = new MapFieldType[this.span.getXspan()][this.span.getYspan()];
        // Arrays.fill(this.map, MapFieldType.empty);
        for (int x = 0; x < this.getXspan(); ++x)
            for (int y = 0; y < this.getYspan(); ++y)
                this.map[x][y] = MapFieldType.empty;
    }

    public static Prison2DMap parseMap(JSONObject json) throws JSONException {
        if (json == null)
            throw new IllegalArgumentException(Prison2DMap.class.getName() + ": attempting to parse null JSON");
        Prison2DMap out = new Prison2DMap();

        JSONObject span = json.getJSONObject("span");
        System.out.println("span : " + span);
        JSONArray cells = json.getJSONArray("cells");
        System.out.println("cells : " + cells);
        JSONArray spawners = json.getJSONArray("spawners");
        System.out.println("spawners : " + spawners);

        out.span = new Int2DSpan(span.getInt("x"), span.getInt("y"));
        out.initMap();
        System.out.println("s = " + out.span);

        for (int i = 0; i < cells.length(); ++i) {
            JSONObject cell = cells.getJSONObject(i);
            try {
                out.addCell(
                        new Cell(
                                cell.getInt("x"), cell.getInt("y"), cell.getInt("length"), cell.getInt("height"),
                                Direction.valueOf(cell.getString("direction"))
                        )
                );
            } catch (Exception e) {
                System.err.println(Prison2DMap.class.getName() + ": error while parsing cell:");
                System.err.println(cell);
                System.err.println(e);
            }
        }

        for (int i = 0; i < spawners.length(); ++i) {
            JSONObject spawner = spawners.getJSONObject(i);
            try {
                out.addSpawner(
                        new Spawner(spawner.getInt("x"), spawner.getInt("y"), Class.forName(spawner.getString("spawn")))
                );
            } catch (Exception e) {
                System.err.println(Prison2DMap.class.getName() + ": error while parsing spawner:");
                System.err.println(spawner);
                System.err.println(e.toString());
            }
        }

        return out;
    }

}
