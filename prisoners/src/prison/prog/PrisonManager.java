package prison.prog;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.goebl.david.Webb;

import agents.Agent;
import agents.prisoners.Prisoner;
import agents.prisoners.PrisonerGroupMap;
import prison.map.elements.elements2d.discrete.Spawner;
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

    private boolean post(JSONObject json) {
        Webb webb = Webb.create();
        webb.post("http://localhost:6000").param("message", json.toString()).ensureSuccess().asVoid();
        return true;
    }

    private void tick() throws InterruptedException {
        for (Agent a : agents)
            a.tick();
        // if (!Server.send(getJSON())) throw new RuntimeException();
        JSONObject json = getJSON();
        if (!this.post(json)) {
            System.err.println("couldn't send data to client, using stdout");
            System.out.println(json);
        }
        Thread.sleep(1000);
    }

    public void run() {
        if (RUNNING) return;
        // Server.register(this);
        // Server.init(DEF_PORT);
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
            for (int i = 0; i < 30;) {
                synchronized (lock) {
                    if (!RUNNING) break;
                    if (STOPPED) continue;
                }
                for (Spawner s : this.map.spawns) {
                    // Map<String, Object> args = new HashMap<>();
                    // args.put("x",s.getLocation().getX());
                    // args.put("y",s.getLocation().getY());
                    this.addAgent(
                            new Prisoner(
                                    s.getLocation().getX(), s.getLocation().getY(), PrisonerGroupMap.getRandomGroup()
                            )
                    );

                }
                tick();
                ++i;
            }
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
