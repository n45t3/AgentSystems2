package agents2.rem.handlers.modifier;

import java.util.Map;

import agents2.rem.events.Event;
import agents2.rem.handlers.EventHandler;

public class ReentrantModifyingEventHandler extends ModifyingEventHandler implements EventHandler {

	public ReentrantModifyingEventHandler(Class<?> e) {
		super(e);
	}

	public ReentrantModifyingEventHandler(Event e) {
		super(e);
	}

	public ReentrantModifyingEventHandler(Class<?> e, Map<String, Object> context) {
		super(e, context);
	}

	public ReentrantModifyingEventHandler(Event e, Map<String, Object> context) {
		super(e, context);
	}

}
