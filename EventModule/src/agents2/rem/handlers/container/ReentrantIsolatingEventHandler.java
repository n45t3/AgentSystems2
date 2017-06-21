package agents2.rem.handlers.container;

import java.util.HashMap;
import java.util.Map;

import agents2.rem.events.Event;
import agents2.rem.handlers.EventHandler;

public class ReentrantIsolatingEventHandler extends IsolatingEventHandler implements EventHandler {

	private final Map<Class<?>, Event> locals = new HashMap<>();

	@Override
	public void run(Event e, Object... args) {
		Event l = this.locals.get(e.getClass());
		if (l == null) {
			l = this.getContextEvent(e);
			this.locals.put(e.getClass(), l);
		}
		l.onHandling(args);
	}

	public ReentrantIsolatingEventHandler(Class<?> e) {
		super(e);
	}

	public ReentrantIsolatingEventHandler(Event e) {
		super(e);
	}

	public ReentrantIsolatingEventHandler(Class<?> e, Map<String, Object> context) {
		super(e, context);
	}

	public ReentrantIsolatingEventHandler(Event e, Map<String, Object> context) {
		super(e, context);
	}

}
