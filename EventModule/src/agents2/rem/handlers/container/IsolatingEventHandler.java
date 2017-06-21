package agents2.rem.handlers.container;

import java.lang.reflect.Field;
import java.util.Map;

import agents2.rem.events.Event;
import agents2.rem.handlers.BasicEventHandler;
import agents2.rem.handlers.ContextEventHandler;
import agents2.rem.handlers.EventHandler;

public class IsolatingEventHandler extends ContextEventHandler implements EventHandler {

	public Event getContextEvent(Event e) {
		try {
			Event local = e.getClass().newInstance();
			for (Field f : e.getClass().getDeclaredFields()) {
				try {
					local.getClass().getDeclaredField(f.getName()).set(local, f.get(e));
				} catch (IllegalArgumentException | NoSuchFieldException | SecurityException E) {
					// logger.error(E)
				}
			}
			if (this.context != null) {
				for (Map.Entry<String, Object> kvp : this.context.entrySet()) {
					try {
						local.getClass().getDeclaredField(kvp.getKey()).set(local, kvp.getValue());
					} catch (IllegalArgumentException | NoSuchFieldException | SecurityException E) {
						// logger.error(E)
					}
				}
			}
			return local;
		} catch (InstantiationException | IllegalAccessException E) {
			// logger.error(E)
			return null;
		}
	}

	@Override
	public void run(Event e, Object... args) {
		Event local = getContextEvent(e);
		if (local != null)
			local.onHandling(args);
	}

	public IsolatingEventHandler(Class<?> e) {
		super(e);
	}

	public IsolatingEventHandler(Event e) {
		super(e);
	}

	public IsolatingEventHandler(Class<?> e, Map<String, Object> context) {
		super(e, context);
	}

	public IsolatingEventHandler(Event e, Map<String, Object> context) {
		super(e, context);
	}

}
