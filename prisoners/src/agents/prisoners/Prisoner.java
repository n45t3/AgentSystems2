package agents.prisoners;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.json.JSONException;
import org.json.JSONObject;

import agents.Action;
import agents.Agent;
import prison.map.elements.elements2d.discrete.Cell;
import prison.map.elements.elements2d.discrete.Direction;



public class Prisoner extends Agent {

    public String group;
    private Cell  targetCell = null;
    private int[] target     = new int[] { 0, 0 };

    // private Grid<Object> grid;

    public Prisoner(int x, int y, String group) {
        super(x, y);
        PrisonerGroupMap.add(group).members.add(this);
        this.group = group;
    }

    public void die() {
        PrisonerGroupMap.mapping.get(group).members.remove(this);
        this.map.releaseField(this.x, this.y);
        this.graph.addVertex(this.x, this.y);
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
        System.out.println("searching for cell");
        if (this.targetCell == null) {
            PriorityQueue<Cell> cells = new PriorityQueue<Cell>(
                    this.map.cells.size(), new Comparator<Cell>() {

                        @Override
                        public int compare(Cell o1, Cell o2) {
                            if (o1.targets == o2.targets) return 0;
                            return o1.targets < o2.targets ? -1 : 1;
                        }
                    }
            );

            for (Cell c : this.map.cells)
                cells.add(c);

            for (Cell c : cells) {
                if (c.mainGroup != null && c.mainGroup.equals(this.group)) {
                    int[] tmp = this.map.getFreeCellSpace(c);
                    if (tmp == null) {
                        System.out.println("no space at cell " + c.toString());
                        continue;
                    }
                    this.targetCell = c;
                    this.target = tmp;
                    c.targets++;
                    System.out.println("going to cell " + c);
                    System.out.println(this.x + ',' + this.y + " -> " + tmp[0] + ',' + tmp[1]);
                    return;
                }

                if (c.mainGroup != null && PrisonerGroupMap.mapping.get(this.group).hasScythe(c.mainGroup)) continue;

                boolean retry = false;
                for (String g : c.getGroups()) {
                    if (PrisonerGroupMap.mapping.get(this.group).hasScythe(g)) {
                        retry = true;
                        System.out.println("scythe with " + g);
                    }
                }
                if (retry) continue;

                int[] tmp = this.map.getFreeCellSpace(c);
                if (tmp == null) {
                    System.out.println("no space at cell " + c.toString());
                    continue;
                }
                this.targetCell = c;
                this.target = tmp;
                c.targets++;
                if (c.mainGroup == null) c.mainGroup = this.group;
                System.out.println("going to cell " + c);
                System.out.println(this.x + ',' + this.y + " -> " + tmp[0] + ',' + tmp[1]);
                return;
            }
            System.out.println("no suitable cell");
            this.targetCell = null;
            this.target = new int[] { 0, 0 };
            return;
        }

        int[] tmp = this.map.getFreeCellSpace(this.targetCell);
        if (tmp == null) {
            this.targetCell = null;
            chooseCell();
        } else this.target = tmp;
    }

    private boolean check() {
        if (this.act == Action.FinallyLayYourWorthlessBodyToRest) return true;
        if (this.target != null && this.target[0] == this.x && this.target[1] == this.y) {
            if (this.targetCell != null) this.targetCell.register(this);
            this.act = Action.FinallyLayYourWorthlessBodyToRest;
            System.out.println("destination reached");
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        System.out.println(this.id + " tick");
        if (this.check()) return;
        chooseCell();
        this.graph.addVertex(this.x, this.y);
        this.graph.setWeights(this.group);
        List<Integer> path = this.graph.getPath(
                this.graph.getVertex(this.x, this.y), this.graph.getVertex(this.target[0], this.target[1])
        );
        int[] next = this.graph.getParams(path.get(1));
        System.out.println("next step from " + this.x + ',' + this.y + ": " + next[0] + ',' + next[1]);
        if (next[0] == this.x && next[1] == this.y) {
            this.act = Action.ExistInConstantPainAndMisery;
            this.graph.removeVertex(this.graph.getVertex(this.x, this.y));
            return;
        }
        this.act = Action.TakeAnotherStepTowardsDeath;
        this.dir = Direction.get(next[0] - this.x, next[1] - this.y);
        if (this.goTo(next[0], next[1])) this.graph.removeVertex(this.graph.getVertex(this.x, this.y));
        else this.act = Action.ExistInConstantPainAndMisery;
    }

    @Override
    public JSONObject getJSON() {
        JSONObject out = super.getJSON();
        try {
            out.put("type", "prisoner");
            out.put("group", this.group);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return out;
    }
}
