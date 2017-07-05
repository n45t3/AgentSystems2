package agents.prisoners;

import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.DefaultEdge;

import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class GraphBuilder {
	public static SimpleGraph<GridPoint, DefaultEdge> createGraph(Grid<Object> grid) {
		SimpleGraph<GridPoint, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
		int[] dimensions = new int[2];
		grid.getDimensions().toIntArray(dimensions);
		for (int x = 0; x < dimensions[0]; x++) {
			for (int y = 0; y < dimensions[1]; y++) {
				GridPoint vertex = new GridPoint(x, y);
				graph.addVertex(vertex);
				if (x > 0) {
					graph.addEdge(vertex, new GridPoint(x - 1, y));
				}
				if (y > 0) {
					graph.addEdge(vertex, new GridPoint(x, y - 1));
				}
			}
		}
		return graph;
	}
}