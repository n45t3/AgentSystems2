package agents2.rem.events.random;

import java.util.Map;

import agents2.rem.events.BasicEvent;
import agents2.rem.events.Event;
import agents2.rem.events.EventClass;

public class RandomEvent extends BasicEvent implements EventClass {
	protected int prob;

	@Override
	public int getProb() {
		return this.prob;
	}

	public RandomEvent(Class<?> e, int prob) {
		super(e);
		if (prob < 0)
			throw new IllegalArgumentException("must be a positive integer");
		this.prob = prob;
	}

	public RandomEvent(Event e, int prob) {
		super(e);
		if (prob < 0)
			throw new IllegalArgumentException("must be a positive integer");
		this.prob = prob;
	}

	public RandomEvent(Class<?> e, Map<String, Object> args, int prob) {
		super(e, args);
		if (prob < 0)
			throw new IllegalArgumentException("must be a positive integer");
		this.prob = prob;
	}

	public RandomEvent(Event e, Map<String, Object> args, int prob) {
		super(e, args);
		if (prob < 0)
			throw new IllegalArgumentException("must be a positive integer");
		this.prob = prob;
	}
}
