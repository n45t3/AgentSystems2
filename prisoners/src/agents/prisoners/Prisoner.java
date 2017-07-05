package agents.prisoners;

import org.json.JSONException;
import org.json.JSONObject;

import agents.Agent;
import prison.map.elements.elements2d.discrete.Cell;
import prison.map.elements.elements2d.discrete.Direction;
import prison.map.geometry.point.point2d.Int2DPoint;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.SpatialException;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;



public class Prisoner extends Agent {

    public String group;
    private Cell  targetCell = null;
    private int[] target     = new int[] { 0, 0 };

    // private Grid<Object> grid;

    public Prisoner(int x, int y, String group) {
        super(x, y);
        PrisonerGroupMap.add(group);
        this.group = group;
    }

    // public Prisoner(Grid<Object> grid) {
    // this.grid = grid;
    // }

    // @ScheduledMethod(start = 1, interval = 1)
    // public void step() {
    // moveTowards(new GridPoint(0, 0));
    // }

    // public void moveTowards(GridPoint pt) {
    // try {
    // grid.moveByDisplacement(this, x, y);
    // } catch (SpatialException e) {
    // x = -x;
    // }
    //
    // }

    public void chooseCell() {
        if (this.targetCell == null) {
            for (Cell c : this.map.cells) {
                boolean retry = false;
                for (String g : c.getGroups())
                    if (PrisonerGroupMap.mapping.get(this.group).hasScythe(g)) retry = true;
                if (retry) continue;
                int[] tmp = this.map.getFreeCellSpace(c);
                if (tmp == null) continue;
                this.targetCell = c;
                this.target = tmp;
                return;
            }
            this.targetCell = null;
            this.target = new int[] { 0, 0 };
            return;
        }
        int[] tmp = this.map.getFreeCellSpace(this.targetCell);
        if (tmp == null) {
            this.targetCell = null;
            chooseCell();
        }
    }

    public void scythe(Prisoner p) {

    }

    public void friend(Prisoner p) {

    }

    public void meet(Prisoner p) {
        if (this.group.equals(p.group)) friend(p);
        else if (PrisonerGroupMap.mapping.get(this.group).hasScythe(p.group)) scythe(p);
    }

    @Override
    public void tick() {
        chooseCell();
        this.graph.setWeights(this.group);
        this.graph.addVertex(this.x, this.y);
        int[] next = this.graph.getParams(
                this.graph.getPath(
                        this.graph.getVertex(this.x, this.y), this.graph.getVertex(this.target[0], this.target[1])
                ).get(0)
        );
        this.dir = Direction.get(next[0] - this.x, next[1] - this.y);
        this.goTo(next[0], next[1]);
        this.graph.removeVertex(this.graph.getVertex(this.x, this.y));
    }

    @Override
    public JSONObject getJSON() {
        JSONObject out = super.getJSON();
        try {
            out.put("group", this.group);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return out;
    }
}
