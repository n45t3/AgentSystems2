package agents2.rem.engine;

import agents2.rem.events.Event;
import agents2.rem.events.EventContext;

class EventManagementMessage {
	public String id;
	public Event event;
	public EventContext context;

	public EventManagementMessage() {
		id = null;
		event = null;
		context = null;
	};
}
