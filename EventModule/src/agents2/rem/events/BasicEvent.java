package agents2.rem.events;

import java.util.Map;

public abstract class BasicEvent implements EventClass {

	protected String id = null;

	protected final Class<?> event;
	protected final Map<String, Object> args;

	public BasicEvent(Class<?> e) {
		if (e == null)
			throw new IllegalArgumentException("null event");
		if (!Event.class.isAssignableFrom(e))
			throw new IllegalArgumentException("must implement Event interface");
		this.event = e;
		this.args = null;
	}

	public BasicEvent(Event e) {
		if (e == null)
			throw new IllegalArgumentException("null event");
		this.event = e.getClass();
		this.args = null;
	}

	public BasicEvent(Class<?> e, Map<String, Object> args) {
		if (e == null)
			throw new IllegalArgumentException("null event");
		if (!Event.class.isAssignableFrom(e))
			throw new IllegalArgumentException("must implement Event interface");
		this.event = e;
		this.args = args;
	}

	public BasicEvent(Event e, Map<String, Object> args) {
		if (e == null)
			throw new IllegalArgumentException("null event");
		this.event = e.getClass();
		this.args = args;
	}

	@Override
	public final Class<?> getEvent() {
		return this.event;
	}

	@Override
	public final Map<String, Object> getConstructorArgs() {
		return this.args;
	}

	@Override
	public final String getID() {
		return this.id;
	}

	@Override
	public final void setID(String id) {
		if (this.id == null)
			this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof EventClass) {
			EventClass ec = (EventClass) o;
			if ((ec.getID() == null && this.getID() == null)
					|| (ec.getID() != null && this.getID() != null && this.getID().equals(ec.getID())))
				return true;
		}
		return false;
	}
}
