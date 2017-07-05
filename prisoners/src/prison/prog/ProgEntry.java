package prison.prog;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import agents.Agent;
import agents.prisoners.Prisoner;
import agents.prisoners.PrisonerGroup;
import agents.prisoners.PrisonerGroupMap;
import prison.map.elements.elements2d.discrete.Spawner;



public class ProgEntry {

    public static void main(String[] a) {
        PrisonManager pm = new PrisonManager();
        pm.start();

        Random rng = new Random();

        PrisonerGroup cwks = new PrisonerGroup("CWKS hooligans", "POLONIA OFFICIAL", "Chłopakowie z Mokotowa");
        PrisonerGroup polonia = new PrisonerGroup(
                "POLONIA OFFICIAL", "CWKS hooligans", "Praga pany", "Praga chamy", "Chłopakowie z Mokotowa"
        );
        PrisonerGroup pp = new PrisonerGroup("Praga pany", "Praga chamy", "POLONIA OFFICIAL", "Chłopakowie z Mokotowa");
        PrisonerGroup pc = new PrisonerGroup("Praga chamy", "Praga pany", "POLONIA OFFICIAL", "Banda siwego");
        PrisonerGroup siwy = new PrisonerGroup("Banda siwego", "Praga chamy", "Chłopakowie z Mokotowa");
        PrisonerGroup mk = new PrisonerGroup(
                "Chłopakowie z Mokotowa", "POLONIA OFFICIAL", "Banda siwego", "Praga pany"
        );

        PrisonerGroupMap.mapping.put("CWKS hooligans", cwks);
        PrisonerGroupMap.mapping.put("POLONIA OFFICIAL", polonia);
        PrisonerGroupMap.mapping.put("Praga pany", pp);
        PrisonerGroupMap.mapping.put("Praga chamy", pc);
        PrisonerGroupMap.mapping.put("Banda siwego", siwy);
        PrisonerGroupMap.mapping.put("Chłopakowie z Mokotowa", mk);

        try {
            pm.join();
            //Server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
