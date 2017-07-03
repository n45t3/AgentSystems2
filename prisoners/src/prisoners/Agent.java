package prisoners;

import prison.map.elements.elements2d.discrete.Direction;
import prison.map.map2d.Prison2DMap;
import prison.map.MapFieldType;



public class Agent {

    public int       x, y;
    public Direction dir;
    public Action    act;

    private Prison2DMap map;

    public void place(Prison2DMap map) {
        if (this.map != null) return;
        this.map = map;
    }

    public Agent() {
        this(0, 0, null);
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
        if (!this.map.acquireField(tmp[0], tmp[1])) return false;
        if (!this.map.releaseField(this.x, this.y)) return false;
        this.x = tmp[0];
        this.y = tmp[1];
        return true;
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
}

/*
 * N -1,-1 0,-1 1,-1 W -1, 0 0, 0 1, 0 E -1, 1 0, 1 1, 1 S
 */