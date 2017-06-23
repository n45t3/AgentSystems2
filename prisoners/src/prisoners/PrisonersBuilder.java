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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.*;

import prison.map.map2d.Prison2DMap;
import prison.map.map2d.RepastMap2DTranslator;

public class PrisonersBuilder implements ContextBuilder<Object> {

	@Override
	public Context build(Context<Object> context) {
		context.setId("prisoners");

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new StrictBorders(), new SimpleGridAdder<Object>(), true, 100, 100));

		RepastMap2DTranslator rmp = new RepastMap2DTranslator(context);
		String file = "example.json";
		try (FileReader fr = new FileReader(new File(file))) {
			rmp.translateAndAssign(Prison2DMap.parseMap(new JSONObject(new JSONTokener(fr))), "prisonmap");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// try{
		// rmp.translateAndAssign(Prison2DMap.parseMap(
		// new JSONObject(
		// new JSONTokener(
		// ))));

		int prisonerCount = 5; // (Integer)params.getValue("prisoner_count");
		for (int i = 0; i < prisonerCount; i++) {
			Prisoner p = new Prisoner(grid);
			context.add(p);
			grid.moveTo(p, 10, 10 + i * 10);
		}

		return context;
	}
}
