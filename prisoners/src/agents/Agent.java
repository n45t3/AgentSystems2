package agents;

import prison.map.elements.elements2d.discrete.Direction;
import prison.map.map2d.Prison2DMap;
import prison.prog.PrisonGraph;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import prison.map.MapFieldType;



public abstract class Agent {

    public int       x, y;
    public Direction dir;
    public Action    act;
    public UUID      id;

    protected Prison2DMap map;
    protected PrisonGraph graph;

    public boolean place(Prison2DMap map) {
        if (this.map != null) return false;
        this.map = map;
        this.goTo(this.x, this.y);
        this.searchDir();
        return true;
    }

    public boolean goTo(int x, int y) {
        synchronized (this.map) {
            if (!this.map.acquireField(x, y)) return false;
            this.map.releaseField(this.x, this.y);
        }
        this.x = x;
        this.y = y;
        return true;
    }

    public Agent() {
        this(-1, -1, null);
    }

    public Agent(int x, int y) {
        this(x, y, null);
    }

    public Agent(int x, int y, Prison2DMap map) {
        this.x = x;
        this.y = y;
        this.act = Action.ExistInConstantPainAndMisery;
        this.map = map;
        this.searchDir();
    }

    public boolean move() {
        if (this.map == null) {
            this.dir = Direction.UNDEF;
            return false;
        }
        int[] tmp = this.dir.move(this.x, this.y);
        if (tmp[0] == this.x && tmp[1] == this.y) return false;
        return this.goTo(tmp[0], tmp[1]);
    }

    public void searchDir() {
        if (this.map == null) {
            this.dir = Direction.UNDEF;
            return;
        }
        for (Direction d : Direction.values()) {
            if (d == Direction.UNDEF) continue;
            int[] tmp = d.move(this.x, this.y);
            if (this.map.getField(tmp[0], tmp[1]) != MapFieldType.wall) {
                this.dir = d;
                return;
            }
        }
        this.dir = Direction.UNDEF;
        return;
        // if (this.map.getField(x - 1, y - 1) != MapFieldType.wall) {
        // this.dir = Direction.NW;
        // return;
        // }
        // if (this.map.getField(x, y - 1) != MapFieldType.wall) {
        // this.dir = Direction.N;
        // return;
        // }
        // if (this.map.getField(x + 1, y - 1) != MapFieldType.wall) {
        // this.dir = Direction.NE;
        // return;
        // }
        // if (this.map.getField(x + 1, y) != MapFieldType.wall) {
        // this.dir = Direction.E;
        // return;
        // }
        // if (this.map.getField(x + 1, y + 1) != MapFieldType.wall) {
        // this.dir = Direction.SE;
        // return;
        // }
        // if (this.map.getField(x, y + 1) != MapFieldType.wall) {
        // this.dir = Direction.S;
        // return;
        // }
        // if (this.map.getField(x - 1, y + 1) != MapFieldType.wall) {
        // this.dir = Direction.SW;
        // return;
        // }
        // if (this.map.getField(x - 1, y) != MapFieldType.wall) {
        // this.dir = Direction.W;
        // return;
        // }
        // this.dir = Direction.UNDEF;
        // return;
    }

    public JSONObject getJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", this.id.toString());
            json.put("x", this.x);
            json.put("y", this.y);
            json.put("dir", this.dir);
            json.put("act", this.act);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public boolean equals(Object o) {
        if (o instanceof Agent) {
            Agent a = (Agent) o;
            return this.id.equals(a.id);
        }
        return false;
    }

    public abstract void tick();

    public abstract void die();

    public boolean place(PrisonGraph graph) {
        if (this.graph != null) return false;
        this.graph = graph;
        return true;
    }
}

/*
 * N -1,-1 0,-1 1,-1 W -1, 0 0, 0 1, 0 E -1, 1 0, 1 1, 1 S
 */