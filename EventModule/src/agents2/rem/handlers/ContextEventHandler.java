package agents2.rem.handlers;

import java.util.Map;

import agents2.rem.events.Event;

public abstract class ContextEventHandler extends BasicEventHandler implements EventHandler {

	protected final Map<String, Object> context;

	public final Map<String, Object> getContext() {
		return this.context;
	}

	public ContextEventHandler(Class<?> e) {
		super(e);
		this.context = null;
	}

	public ContextEventHandler(Event e) {
		super(e);
		this.context = null;
	}
	
	public ContextEventHandler(Class<?> e, Map<String, Object> context) {
		super(e);
		this.context = context;
	}

	public ContextEventHandler(Event e, Map<String, Object> context) {
		super(e);
		this.context = context;
	}

}
