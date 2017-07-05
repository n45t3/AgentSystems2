package prison.map.elements.elements2d.discrete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import agents.prisoners.Prisoner;
import prison.map.geometry.point.point2d.Int2DPoint;
import prison.map.geometry.span.span2d.Int2DSpan;



public class Cell extends Map2DElement {

    private Direction                   dir;
    private Map<UUID, Prisoner>         prisoners = new HashMap<>();
    private Map<String, List<Prisoner>> groups    = new HashMap<>();

    public String mainGroup;
    public int    targets = 0;

    public void register(Prisoner p) {
        if (this.prisoners.containsKey(p.id)) return;
        this.prisoners.put(p.id, p);
        List<Prisoner> group = groups.get(p.group);
        if (group == null) {
            group = new ArrayList<Prisoner>();
            groups.put(p.group, group);
        }
        group.add(p);
    }

    public void unregister(Prisoner p) {
        if (this.prisoners.containsKey(p.id)) {
            this.prisoners.remove(p.id);
            List<Prisoner> group = groups.get(p.group);
            if (group != null) {
                group.remove(p);
                if (group.isEmpty()) groups.remove(p.group);
            }
        }
    }

    public final Set<String> getGroups() {
        return groups.keySet();
    }

    public final Direction getDirection() {
        return this.dir;
    }

    public Cell(Int2DPoint loc, Int2DSpan span, Direction o) {
        super(loc, span);
        this.dir = o;
    }

    public Cell(int x, int y, Direction o) {
        super(x, y);
        this.dir = o;
    }

    public Cell(int x, int y, int len, int hgh, Direction o) {
        super(x, y, len, hgh);
        this.dir = o;
    }

    public Cell(Int2DPoint p, int len, int hgh, Direction o) {
        super(p, len, hgh);
        this.dir = o;
    }

    public Cell(int x, int y, Int2DSpan span, Direction o) {
        super(x, y, span);
        this.dir = o;
    }

    public String toString() {
        return this.getLocation().toString() + ' ' + this.getSpan().toString() + ' ' + this.getDirection().toString();
    }

}
