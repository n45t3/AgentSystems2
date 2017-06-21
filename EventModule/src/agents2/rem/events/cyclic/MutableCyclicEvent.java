package agents2.rem.events.cyclic;

import java.util.Map;

import agents2.rem.events.Event;
import agents2.rem.events.EventClass;

public class MutableCyclicEvent extends CyclicEvent implements EventClass {

	public void setInterval(int interval) {
		if (interval < 0)
			throw new IllegalArgumentException("must be a positive integer");
		this.interval = interval;
		if (interval == 0)
			return;
		this.tick = this.tick % this.interval;
	}

	public MutableCyclicEvent(Class<?> e, int interval) {
		super(e, interval);
	}

	public MutableCyclicEvent(Class<?> e, int interval, int tick) {
		super(e, interval, tick);
	}

	public MutableCyclicEvent(Event e, int interval) {
		super(e, interval);
	}

	public MutableCyclicEvent(Event e, int interval, int tick) {
		super(e, interval, tick);
	}

	public MutableCyclicEvent(Class<?> e, Map<String, Object> args, int interval) {
		super(e, args, interval);
	}

	public MutableCyclicEvent(Class<?> e, Map<String, Object> args, int interval, int tick) {
		super(e, args, interval, tick);
	}

	public MutableCyclicEvent(Event e, Map<String, Object> args, int interval) {
		super(e, args, interval);
	}

	public MutableCyclicEvent(Event e, Map<String, Object> args, int interval, int tick) {
		super(e, args, interval, tick);
	}

}
