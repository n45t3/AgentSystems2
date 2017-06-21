package agents2.rem.handlers.modifier;

import java.lang.reflect.Field;
import java.util.Map;

import agents2.rem.events.Event;
import agents2.rem.handlers.ContextEventHandler;
import agents2.rem.handlers.EventHandler;

public class ModifyingEventHandler extends ContextEventHandler implements EventHandler {

	public void getContextEvent(Event e) {
		try {
			if (this.context != null) {
				for (Map.Entry<String, Object> kvp : this.context.entrySet()) {
					try {
						e.getClass().getDeclaredField(kvp.getKey()).set(e, kvp.getValue());
					} catch (IllegalArgumentException | NoSuchFieldException | SecurityException E) {
						// logger.error(E)
					}
				}
			}
		} catch (IllegalAccessException E) {
			// logger.error(E)
		}
	}

	@Override
	public void run(Event e, Object... args) {
		getContextEvent(e);
		e.onHandling(args);
	}
	
	public ModifyingEventHandler(Class<?> e) {
		super(e);
	}

	public ModifyingEventHandler(Event e) {
		super(e);
	}

	public ModifyingEventHandler(Class<?> e, Map<String, Object> context) {
		super(e, context);
	}

	public ModifyingEventHandler(Event e, Map<String, Object> context) {
		super(e, context);
	}

}
