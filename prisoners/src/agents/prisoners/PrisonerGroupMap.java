package agents.prisoners;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;



public class PrisonerGroupMap {

    private static final Random rng = new Random();

    public static final Map<String, PrisonerGroup> mapping = new LinkedHashMap<>();

    public boolean exists(String g) {
        return mapping.containsKey(g);
    }

    public static PrisonerGroup add(String g) {
        if (mapping.containsKey(g)) return mapping.get(g);
        PrisonerGroup pg = new PrisonerGroup(g, getRandomScythe());
        mapping.put(g, pg);
        return pg;
    }

    public static String[] getRandomScythe() {
        if (mapping.isEmpty()) return new String[0];
        int ms = mapping.size();

        String[] out = new String[rng.nextInt(ms)];
        boolean[] added = new boolean[ms];
        for (int i = 0; i < added.length; ++i)
            added[i] = false;
        for (int i = 0; i < out.length; ++i) {
            int idx = rng.nextInt(ms);
            while (added[idx])
                ++idx;
            out[i] = (String) mapping.keySet().toArray()[idx];
        }
        return out;
    }

    public static String getRandomGroup() {
        return (String) mapping.keySet().toArray()[rng.nextInt(mapping.size())];
    }
}
