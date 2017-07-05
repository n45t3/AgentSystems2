package prison.prog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;
import org.jgrapht.alg.shortestpath.AStarShortestPath;
import org.jgrapht.graph.*;

import agents.prisoners.Prisoner;
import agents.prisoners.PrisonerGroupMap;
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

    private SimpleGraph<Integer, DefaultWeightedEdge>       graph       = new SimpleGraph<>(DefaultWeightedEdge.class);
    private Prison2DMap                                     map         = null;
    private AStarShortestPath<Integer, DefaultWeightedEdge> pathCounter = null;

    public PrisonGraph(Prison2DMap pm) {
        this.map = pm;
        for (int x = 0; x < this.map.getXspan(); ++x) {
            for (int y = 0; y < this.map.getYspan(); ++y) {
                if (this.map.getField(x, y) == MapFieldType.wall) continue;
                int v = this.getVertex(x, y);
                this.graph.addVertex(v);
                if (x > 0) this.addEdge(v, this.getVertex(x - 1, y));
                if (y > 0) this.addEdge(v, this.getVertex(x, y - 1));
            }
        }
        this.pathCounter = new AStarShortestPath<Integer, DefaultWeightedEdge>(this.graph, new ManhattanDistance(this));
    }

    public List<Integer> getPath(Integer from, Integer to) {
        GraphPath<Integer, DefaultWeightedEdge> gp = this.pathCounter.getPath(from, to);
        return gp.getVertexList();
    }

    public void setWeights(String group) {
        for (DefaultWeightedEdge e : this.graph.edgeSet())
            this.graph.setEdgeWeight(e, 1.0);
        List<String> set = PrisonerGroupMap.mapping.get(group).getScythe();
        for (String g : set) {
            for (Prisoner p : PrisonerGroupMap.mapping.get(g).members) {
                /*
                 * Map<Integer, List<DefaultWeightedEdge>> neigh =
                 * this.getNeighbourhood(this.getVertex(p.x, p.y), 3); for (int
                 * i = 1; i <= 3; ++i) { double wgh = 5.0 - i;
                 * List<DefaultWeightedEdge> li = neigh.get(i); for
                 * (DefaultWeightedEdge e : li) if (wgh >
                 * this.graph.getEdgeWeight(e)) this.graph.setEdgeWeight(e,
                 * wgh); }
                 */
                Integer v_N = this.getVertex(p.x, p.y - 1), v_E = this.getVertex(p.x + 1, p.y),
                        v_S = this.getVertex(p.x, p.y + 1), v_W = this.getVertex(p.x - 1, p.y);
                if (this.graph.containsVertex(v_N)) for (DefaultWeightedEdge e : this.graph.edgesOf(v_N))
                    this.graph.setEdgeWeight(e, 4.0);
                if (this.graph.containsVertex(v_E)) for (DefaultWeightedEdge e : this.graph.edgesOf(v_E))
                    this.graph.setEdgeWeight(e, 4.0);
                if (this.graph.containsVertex(v_S)) for (DefaultWeightedEdge e : this.graph.edgesOf(v_S))
                    this.graph.setEdgeWeight(e, 4.0);
                if (this.graph.containsVertex(v_W)) for (DefaultWeightedEdge e : this.graph.edgesOf(v_W))
                    this.graph.setEdgeWeight(e, 4.0);
            }
        }
    }

    public List<DefaultWeightedEdge> getNeighbourhood(Integer v) {
        return getNeighbourhood(v, 1).get(1);
    }

    public Map<Integer, List<DefaultWeightedEdge>> getNeighbourhood(Integer v, int row) {
        Map<Integer, List<DefaultWeightedEdge>> out = new HashMap<>();
        Map<Integer, Set<Integer>> visited = new HashMap<>();
        Set<Integer> l0 = new HashSet<>();
        l0.add(v);
        visited.put(0, l0);
        for (int i = 1; i <= row; ++i) {
            Set<Integer> li = new HashSet<>();
            List<DefaultWeightedEdge> neigh = new ArrayList<>();
            visited.put(i, li);
            for (Integer from : visited.get(i - 1)) {
                Set<Integer> prev = i >= 2 ? visited.get(i - 2) : new HashSet<>();
                for (DefaultWeightedEdge e : this.graph.edgesOf(from)) {
                    Integer to = this.graph.getEdgeTarget(e);
                    if (prev.contains(to)) continue;
                    li.add(to);
                    neigh.add(e);
                }
            }
            out.put(i, neigh);
        }
        return out;
    }

    public void addVertex(int x, int y) {
        Integer v = this.getVertex(x, y);
        if (this.graph.containsVertex(v)) return;
        this.graph.addVertex(v);
        for (Integer to : this.getEdges(x, y))
            this.graph.addEdge(v, to);
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
        return new int[] { v % this.map.getXspan(), v / this.map.getXspan() };
    }
}
