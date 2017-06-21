package agents2.rem.events.cyclic;

import java.util.Map;

import agents2.rem.events.BasicEvent;
import agents2.rem.events.Event;
import agents2.rem.events.EventClass;

public class CyclicEvent extends BasicEvent implements EventClass {
	protected int interval, tick;

	public void setTick(int val) {
		if (this.interval == 0)
			return;
		if (val < 0)
			val = Math.abs(this.interval + val);
		this.tick = val % this.interval;
	}

	public void addTick(int val) {
		if (this.interval == 0)
			return;
		val += this.tick;
		if (val < 0)
			val = Math.abs(this.interval + val);
		this.tick = val % this.interval;
	}

	@Override
	public int getProb() {
		if (this.interval == 0)
			return 100;
		return (this.tick % this.interval) == 0 ? 100 : 0;
	}

	public CyclicEvent(Class<?> e, int interval) {
		super(e);
		if (interval < 0)
			throw new IllegalArgumentException("must be a positive integer");
		this.interval = interval;
		this.tick = 1;
	}

	public CyclicEvent(Class<?> e, int interval, int tick) {
		this(e, interval);
		if (this.interval == 0)
			return;
		if (tick < 0)
			tick = Math.abs(interval + tick);
		this.tick = tick % interval;
	}

	public CyclicEvent(Event e, int interval) {
		super(e);
		if (interval < 0)
			throw new IllegalArgumentException("must be a positive integer");
		this.interval = interval;
		this.tick = 1;
	}

	public CyclicEvent(Event e, int interval, int tick) {
		this(e, interval);
		if (this.interval == 0)
			return;
		if (tick < 0)
			tick = Math.abs(interval + tick);
		this.tick = tick % interval;
	}

	public CyclicEvent(Class<?> e, Map<String, Object> args, int interval) {
		super(e, args);
		if (interval < 0)
			throw new IllegalArgumentException("must be a positive integer");
		this.interval = interval;
		this.tick = 1;
	}

	public CyclicEvent(Class<?> e, Map<String, Object> args, int interval, int tick) {
		this(e, args, interval);
		if (this.interval == 0)
			return;
		if (tick < 0)
			tick = Math.abs(interval + tick);
		this.tick = tick % interval;
	}

	public CyclicEvent(Event e, Map<String, Object> args, int interval) {
		super(e, args);
		if (interval < 0)
			throw new IllegalArgumentException("must be a positive integer");
		this.interval = interval;
		this.tick = 1;
	}

	public CyclicEvent(Event e, Map<String, Object> args, int interval, int tick) {
		this(e, args, interval);
		if (this.interval == 0)
			return;
		if (tick < 0)
			tick = Math.abs(interval + tick);
		this.tick = tick % interval;
	}
}
