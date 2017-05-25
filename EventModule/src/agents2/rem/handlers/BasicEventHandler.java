package agents2.rem.handlers;

import agents2.rem.events.Event;

public abstract class BasicEventHandler implements EventHandler {

	protected String id = null;

	protected final Class<?> event;

	public BasicEventHandler(Class<?> e) {
		if (e == null)
			throw new IllegalArgumentException("null event");
		if (!Event.class.isAssignableFrom(e))
			throw new IllegalArgumentException("must implement Event interface");
		this.event = e;
	}
	
	public BasicEventHandler(Event e) {
		if (e == null)
			throw new IllegalArgumentException("null event");
		this.event = e.getClass();
	}

	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public void setID(String id) {
		if (this.id == null)
			this.id = id;
	}

	@Override
	public Class<?> getEvent() {
		return this.event;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof EventHandler) {
			EventHandler ec = (EventHandler) o;
			if ((ec.getID() == null && this.getID() == null)
					|| (ec.getID() != null && this.getID() != null && this.getID().equals(ec.getID())))
				return true;
		}
		return false;
	}

}
