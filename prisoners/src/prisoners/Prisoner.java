package prisoners;

import prison.map.elements.elements2d.discrete.Direction;
import prison.map.geometry.point.point2d.Int2DPoint;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.SpatialException;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;



public class Prisoner extends Agent {

    public String group;

    private Grid<Object> grid;

    public int x, y;

    public Prisoner(Grid<Object> grid) {
        this.grid = grid;
    }

    @ScheduledMethod(start = 1, interval = 1)
    public void step() {
        moveTowards(new GridPoint(0, 0));
    }

    public void moveTowards(GridPoint pt) {
        try {
            grid.moveByDisplacement(this, x, y);
        } catch (SpatialException e) {
            x = -x;
        }

    }
}
