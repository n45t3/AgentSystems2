package agents2.rem.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import agents2.rem.events.Event;
import agents2.rem.events.EventClass;

public class EventGenerator {
	private static final Random rng = new Random();
	private static final Map<String, EventClass> events = new HashMap<>();

	private static Event generateFrom(EventClass ec) {
		try {
			Event e = (Event) ec.getClass().newInstance();
			if (ec.getConstructorArgs() != null) {
				for (Map.Entry<String, Object> kvp : ec.getConstructorArgs().entrySet()) {
					try {
						e.getClass().getDeclaredField(kvp.getKey()).set(e, kvp.getValue());
					} catch (IllegalArgumentException | NoSuchFieldException | SecurityException E) {
						// logger.error(E)
					}
				}
				e.onCreate(ec.getConstructorArgs());
			}
			return e;
		} catch (InstantiationException | IllegalAccessException e) {
			// logger.error(E)
			return null;
		}
	}

	public static void runTick() {
		for (EventClass ec : events.values()) {
			if (ec.getProb() >= rng.nextInt(100)) {
				EventManagementMessage em = new EventManagementMessage();
				Event e = generateFrom(ec);
				if (e == null)
					continue;
				em.id = ec.getID();
				em.event = e;
				EventManager.pass(em);
			}
		}
	}

	public static void register(EventClass e) throws EventManagementException {
		if (e == null)
			throw new IllegalArgumentException("must be a valid EventClass");
		if (e.getID() == null)
			throw new EventManagementException("null id");
		events.put(e.getID(), e);
	}

	public static void unregister(EventClass e) throws EventManagementException {
		if (e == null)
			throw new IllegalArgumentException("must be a valid EventClass");
		if (e.getID() == null)
			throw new EventManagementException("null id");
		events.remove(e.getID());
	}

	public static void unregister(String id) {
		if (id == null)
			throw new IllegalArgumentException("null id");
		events.remove(id);
	}
}
