package prison.prog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import agents.Agent;
import agents.prisoners.Prisoner;
import prison.map.map2d.Prison2DMap;



public class PrisonManager extends Thread {

    public final Prison2DMap getMap() {
        return this.map;
    }

    public final Object lock = new Object();

    private static final int    DEF_PORT = 8888;
    private static final String path     = "example.json";
    public boolean              RUNNING  = false, STOPPED = false;
    private Prison2DMap         map      = null;
    private PrisonGraph         graph    = null;

    private List<Agent> prisoners = new ArrayList<Agent>(), guards = new ArrayList<Agent>(),
            agents = new ArrayList<Agent>();

    public JSONObject getJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("type", "state_info");
            JSONArray jarr = new JSONArray();
            for (Agent a : agents)
                jarr.put(a.getJSON());
            json.put("content", jarr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private void tick() throws InterruptedException {
        for (Agent a : agents)
            a.tick();
        // if (!Server.send(getJSON())) throw new RuntimeException();
        JSONObject json = getJSON();
        if (!Server.send(json)) {
            System.err.println("couldn't send data to client, using stdout");
            System.out.println(json);
        }
        Thread.sleep(1000);
    }

    public void run() {
        if (RUNNING) return;
        Server.init(DEF_PORT);
        RUNNING = true;
        try (FileReader fr = new FileReader(new File(path))) {
            JSONObject map = new JSONObject(new JSONTokener(fr));
            this.map = Prison2DMap.parseMap(map);
            Server.send(map);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.graph = new PrisonGraph(this.map);
        try {
            this.STOPPED = true;
            while (true) {
                synchronized (lock) {
                    if (!RUNNING) break;
                    if (STOPPED) continue;
                }
                tick();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAgent(Agent a) {
        a.id = UUID.randomUUID();
        a.place(this.map);
        a.place(this.graph);
        agents.add(a);
        if (a instanceof Prisoner) prisoners.add(a);
        else guards.add(a);
    }

}
