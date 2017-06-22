package prisoners;

import prisoners.Prisoner;
import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.StrictBorders;

public class PrisonersBuilder implements ContextBuilder<Object> {

	@Override
	public Context build(Context<Object> context) {
		context.setId("prisoners");
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid(
			"grid", context, new GridBuilderParameters<Object>(
				new StrictBorders(),
				new SimpleGridAdder<Object>(),
				true, 100, 100
			)
		);
		
		int prisonerCount = 5; //(Integer)params.getValue("prisoner_count");
		for(int i = 0; i < prisonerCount; i++) {
			Prisoner p = new Prisoner(grid);
			context.add(p);
			grid.moveTo(p, 10, 10 + i * 10);
		}
	
		return context;
	}
}
