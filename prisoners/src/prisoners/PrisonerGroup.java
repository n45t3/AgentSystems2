package prisoners;

import java.util.ArrayList;
import java.util.List;



public class PrisonerGroup {

    public String        name;
    private List<String> scythe = new ArrayList<String>();

    public boolean hasScythe(String with) {
        return this.scythe.contains(with);
    }

    public PrisonerGroup(String name) {
        this.name = name;
    }

    public PrisonerGroup(String name, String... scythe) {
        this(name);
        for (String s : scythe)
            this.scythe.add(s);
    }

    public void addScythe(String... scythe) {
        for (String s : scythe)
            this.scythe.add(s);
    }
}
