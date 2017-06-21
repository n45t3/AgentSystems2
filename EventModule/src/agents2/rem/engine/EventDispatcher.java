package agents2.rem.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import agents2.rem.events.EventClass;
import agents2.rem.handlers.EventHandler;
import agents2.rem.receivers.EventReceiver;

public class EventDispatcher {

	private static Map<Class<?>, String> mapping = new HashMap<>();
	private static Map<String, Map<String, EventReceiver>> receivers = new HashMap<>();
	private static Map<String, Map<String, EventHandler>> handlers = new HashMap<>();

	public static void pass(EventManagementMessage em) {
		for (EventReceiver er : receivers.get(em.id).values())
			er.send(em.event);
		for (EventHandler eh : handlers.get(em.id).values())
			eh.run(em.event, em.context.getHandlerArgs());
	}

	public static void register(EventClass e) throws EventManagementException {
		if (e == null)
			throw new IllegalArgumentException("must be a valid EventClass");
		if (e.getEvent() == null)
			throw new EventManagementException("null event");
		if (e.getID() == null)
			throw new EventManagementException("null id");
		mapping.put(e.getEvent(), e.getID());
		handlers.put(e.getID(), new HashMap<>());
		receivers.put(e.getID(), new HashMap<>());
	}

	public static void unregister(EventClass e) throws EventManagementException {
		if (e == null)
			throw new IllegalArgumentException("must be a valid EventClass");
		if (e.getID() == null)
			throw new EventManagementException("null id");
		unregister(e.getID());
	}

	public static void unregister(String id) throws EventManagementException {
		if (id == null)
			throw new IllegalArgumentException("null id");
		mapping.remove(id);
		handlers.remove(id);
		receivers.remove(id);
	}

	public static void register(EventReceiver e) throws EventManagementException {
		if (e == null)
			throw new IllegalArgumentException("must be a valid EventReceiver");
		if (e.getEvent() == null)
			throw new EventManagementException("null event");
		String id = mapping.get(e.getEvent());
		if (id == null)
			throw new EventManagementException("event not registered");
		Map<String, EventReceiver> l = receivers.get(id);
		do {
			id = UUID.randomUUID().toString();
		} while (l.containsKey(id));
		e.setID(id);
		l.put(e.getID(), e);
	}

	public static void unregister(EventReceiver e) throws EventManagementException {
		if (e == null)
			throw new IllegalArgumentException("must be a valid EventReceiver");
		if (e.getEvent() == null)
			throw new EventManagementException("null event");
		if (e.getID() == null)
			throw new EventManagementException("null id");
		String id = mapping.get(e.getEvent());
		if (id == null)
			throw new EventManagementException("event not registered");
		Map<String, EventReceiver> l = receivers.get(id);
		if (l != null)
			l.remove(e.getID());
	}

	public static void register(EventHandler e) throws EventManagementException {
		if (e == null)
			throw new IllegalArgumentException("must be a valid EventHandler");
		if (e.getEvent() == null)
			throw new EventManagementException("null event");
		String id = mapping.get(e.getEvent());
		if (id == null)
			throw new EventManagementException("event not registered");
		Map<String, EventHandler> l = handlers.get(id);
		do {
			id = UUID.randomUUID().toString();
		} while (l.containsKey(id));
		e.setID(id);
		l.put(e.getID(), e);
	}

	public static void unregister(EventHandler e) throws EventManagementException {
		if (e == null)
			throw new IllegalArgumentException("must be a valid EventHandler");
		if (e.getEvent() == null)
			throw new EventManagementException("null event");
		if (e.getID() == null)
			throw new EventManagementException("null id");
		String id = mapping.get(e.getEvent());
		if (id == null)
			throw new EventManagementException("event not registered");
		Map<String, EventHandler> l = handlers.get(id);
		if (l != null)
			l.remove(e.getID());
	}

}
