package prisoners;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.SpatialException;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Prisoner {
	private Grid<Object> grid;
	int directionX = 1;
	int directionY = 0;

	public Prisoner(Grid<Object> grid) {
		this.grid = grid;
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		moveTowards(new GridPoint(0, 0));
	}

	public void moveTowards(GridPoint pt) {
		try {
			grid.moveByDisplacement(this, directionX, directionY);
		} catch (SpatialException e) {
			directionX = -directionX;
		}

	}
}
