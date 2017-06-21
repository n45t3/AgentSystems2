package agents2.rem.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import agents2.rem.events.*;
import agents2.rem.handlers.EventHandler;

public final class EventManager {

	private static final Map<String, EventContext> contexts = new HashMap<>();

	public static void register(EventClass e) throws EventManagementException {
		register(e, null);
	}

	public static void register(EventClass E, EventContext EC) throws EventManagementException {
		if (E == null)
			throw new IllegalArgumentException("must be a valid EventClass");
		if (E.getEvent() == null)
			throw new EventManagementException("null event");
		String id = null;
		do {
			id = UUID.randomUUID().toString();
		} while (contexts.containsKey(id));
		E.setID(id);
		contexts.put(id, EC);
		EventGenerator.register(E);
		EventDispatcher.register(E);
	}

	public static void unregister(EventClass E)throws EventManagementException {
		if (E == null)
			throw new IllegalArgumentException("must be a valid EventClass");
		if (E.getID() == null)
			throw new EventManagementException("null id");
		contexts.remove(E.getID());
		EventGenerator.unregister(E.getID());
		EventDispatcher.unregister(E);
	}

	public static void register(EventHandler E) throws EventManagementException {
		if (E == null)
			throw new IllegalArgumentException("must be a valid EventClass");
		if (E.getEvent() == null)
			throw new EventManagementException("null event");
		EventDispatcher.register(E);
	}
	
	public static void unregister(EventHandler E)throws EventManagementException {
		if (E == null)
			throw new IllegalArgumentException("must be a valid EventClass");
		if (E.getEvent() == null)
			throw new EventManagementException("null event");
		EventDispatcher.unregister(E);
	}

	public static void pass(EventManagementMessage em) {
		em.context = contexts.get(em.id);
		if (em.context != null)
			em.event.beforeHandling(em.context.getPreContext());
		EventDispatcher.pass(em);
	}
}
