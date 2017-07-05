package prison.prog;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;
import org.jgrapht.alg.shortestpath.AStarShortestPath;
import org.jgrapht.graph.*;

import prison.map.MapFieldType;
import prison.map.geometry.point.point2d.Int2DPoint;
import prison.map.map2d.Prison2DMap;



class ManhattanDistance implements AStarAdmissibleHeuristic<Integer> {

    private PrisonGraph pg;

    public ManhattanDistance(PrisonGraph pg) {
        this.pg = pg;
    }

    @Override
    public double getCostEstimate(Integer from, Integer to) {
        int[] f = this.pg.getParams(from), t = this.pg.getParams(to);
        return Math.abs(t[0] - f[0]) + Math.abs(t[1] - f[1]);
    }

}


public class PrisonGraph {

    private SimpleGraph<Integer, DefaultEdge>       graph       = new SimpleGraph<>(DefaultEdge.class);
    private Prison2DMap                             map         = null;
    private AStarShortestPath<Integer, DefaultEdge> pathCounter = null;

    public PrisonGraph(Prison2DMap pm) {
        this.map = pm;
        for (int x = 0; x < this.map.getXspan(); ++x) {
            for (int y = 0; y < this.map.getYspan(); ++y) {
                if (this.map.getField(x, y) == MapFieldType.wall) continue;
                int v = this.getVertex(x, y);
                this.graph.addVertex(v);
                if (x > 0) this.graph.addEdge(v, this.getVertex(x - 1, y));
                if (y > 0) this.graph.addEdge(v, this.getVertex(x, y - 1));
            }
        }
        this.pathCounter = new AStarShortestPath<Integer, DefaultEdge>(this.graph, new ManhattanDistance(this));
    }

    public List<Integer> getPath(Integer from, Integer to) {
        GraphPath<Integer, DefaultEdge> gp = this.pathCounter.getPath(from, to);
        return gp.getVertexList();
    }

    public void addVertex(Integer v) {
        if (this.graph.containsVertex(v)) return;
        this.graph.addVertex(v);
        int[] tmp = this.getParams(v);
        for (Integer to : this.getEdges(tmp[0], tmp[1]))
            this.graph.addEdge(v, to);
    }

    public void removeVertex(Integer v) {
        if (this.graph.containsVertex(v)) this.graph.removeVertex(v);
    }

    public void removeEdge(Integer from, Integer to) {
        if (this.graph.containsEdge(from, to)) this.graph.removeEdge(from, to);
    }

    public void addEdge(Integer from, Integer to) {
        if (this.graph.containsEdge(from, to)) return;
        if (this.graph.containsVertex(from) && this.graph.containsVertex(to)) this.graph.addEdge(from, to);
    }

    public List<Integer> getEdges(int x, int y) {
        if (this.map == null) return null;
        List<Integer> out = new ArrayList<>();
        int[][] coords = new int[][] { new int[] { x, y - 1 }, new int[] { x - 1, y }, new int[] { x + 1, y },
                new int[] { x, y + 1 } };
        for (int[] to : coords) {
            if (to[0] < 0 || to[1] < 0) continue;
            if (to[0] >= this.map.getXspan() || to[1] >= this.map.getYspan()) continue;
            int v_to = this.getVertex(to[0], to[1]);
            if (this.graph.containsVertex(v_to)) out.add(v_to);
        }
        return out;
    }

    public Integer getVertex(int x, int y) {
        if (this.map == null) return -1;
        return y * this.map.getXspan() + x;
    }

    public int[] getParams(int v) {
        if (this.map == null) return null;
        return new int[] { v / this.map.getXspan(), v % this.map.getXspan() };
    }
}
