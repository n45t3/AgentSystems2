package prisoners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.BouncyBorders;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.GridDimensions;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;

public class Prisoner {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private boolean moved;
	private ArrayList<NdPoint> destinations;
	private NdPoint currDest;
	public boolean master = false;
	
	public Prisoner(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
		this.destinations = new ArrayList<>(Arrays.asList(
				new NdPoint(5, 5), new NdPoint(45, 5), new NdPoint(45, 45), new NdPoint(5, 45)
		));
		this.currDest = getDestination();
	}
	
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		NdPoint pt = space.getLocation(this);
		moveTowards(currDest);
	}
	
	private NdPoint getDestination() {
		Random r = new Random();
		return new NdPoint(r.nextInt(50), r.nextInt(50));
	}
	
	private boolean isClose(NdPoint pt, NdPoint mypt) {
		return Math.abs(pt.getX()-mypt.getX()) < 1 && Math.abs(pt.getY()-mypt.getY()) < 1; 
	}
	
	public void moveTowards(NdPoint pt) {
		NdPoint myPoint  = space.getLocation(this);
		if(!isClose(myPoint, pt)) {
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
			double angle = correctAngle(SpatialMath.calcAngleFor2DMovement(space, myPoint, otherPoint), myPoint);
			space.moveByVector(this, 0.04, angle, 0);
			myPoint = space.getLocation(this);
			moved = true;
		} else {
			
			this.currDest = getDestination();
		}
		if (master) {
			try {
	            Thread.sleep(5);
	        } catch (InterruptedException e) {
	        	
	        }
		}
	}
	
	double correctAngle(double angle, NdPoint myPoint) {
		Context<Object> context = ContextUtils.getContext(this);
		for (Object enemy : context) {
			if (enemy == this) {
				continue;
			}
			NdPoint enemyPoint = space.getLocation(enemy);
			space.moveByVector(this, 0.04, angle, 0);
			if (space.getDistance(myPoint, enemyPoint) < 10) {
				NdPoint newPoint  = space.getLocation(this);
				double newAngle = SpatialMath.calcAngleFor2DMovement(space, myPoint, newPoint) + (Math.PI / 2);
				newAngle = newAngle > 2*Math.PI ? newAngle - 2*Math.PI : newAngle;
				space.moveByVector(this, 0.04, angle < Math.PI ? angle + Math.PI : angle - Math.PI, 0);
				return newAngle;
			}
			space.moveByVector(this, 0.04, angle < Math.PI ? angle + Math.PI : angle - Math.PI, 0);
		}
		return angle;
	}
	
	
	private GridPoint pointWithMostHumans(List<GridCell<Prisoner>> gridCells) {
		return null;
	}

}
